/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.file.analyzer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.senxu.craigslistcrawler.CraigsListCrawler;

/**
 *
 * @author sxu
 */
public class ExtractDistanceFromGoogle {
    
    static final String prefix="http://maps.google.com/?q=";
    static final String distTag="<span class=\"pp-distance\" dir=\"ltr\">";
    static final String sep=System.getProperty("file.separator");
    public static String getDistance(String googleLink, File tempDir){
        //<span class="pp-distance" dir="ltr">102 ft NW</span>
        String dist="";
        String filename=tempDir.getAbsolutePath()+sep+googleLink.substring(prefix.length())+".html";
        File destFile=new File(filename);
        
        System.out.println("storing "+googleLink+" to local file:"+destFile.toString());
        try {
            if (!destFile.exists()) destFile.createNewFile();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            URL url = new URL(googleLink);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.0");
            int code=urlConn.getResponseCode();
            System.out.println(code);
            if (code==999){
                try {
                    Thread.sleep(7200000);//pause for 2 hours then start crawling again.
                } catch (InterruptedException ex) {
                    Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
            InputStream is = urlConn.getInputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buf = new byte[256];  
            int n = 0;  
            while ((n=bis.read(buf))>=0){
                bos.write(buf, 0, n);  
            }
            bos.flush();
            bos.close();
            bis.close();
            BufferedReader bw=new BufferedReader(new FileReader(destFile));
            String temp;
            while ((temp=bw.readLine())!=null){
                if (temp.contains(distTag)){
                    dist=temp.split(distTag)[1].split("<")[0];
                    break;
                }
            }
            bw.close();
            return dist;

            } catch (IOException ex) {
                Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
                return "crawling error";
            }
    }
    
    public static void main(String[] args){
        File googleDir=new File("CrawledData/GoogleDir");
        DateFormat df=new SimpleDateFormat("MMMdd_yyyy");
        Date today=new Date();
        File output=new File("./SummaryWithGoogleOutput"+df.format(today)+".csv");
        File input=new File("SummaryJan25_2013.csv");
        if (!input.exists()){
             throw new IllegalArgumentException("cannot file input: "+input.getName().toString());
        }
        if (!googleDir.exists()){
            googleDir.mkdir();
            System.out.println(googleDir+" is created");
        }else{
            if (googleDir.isFile()){
                throw new IllegalArgumentException("googleDir is a file");
            }
        }
        try {
            BufferedReader br=new BufferedReader(new FileReader(input));
            BufferedWriter bw=new BufferedWriter(new FileWriter(output,true));
            String temp=br.readLine();//skip headline
            System.out.println("starts to read from"+input);
            System.out.println(temp);
            System.out.println("starts to write to "+output);
            
            bw.append(temp+"\t"+"Distance");
            bw.newLine();
            
            String googleURL;
            String dist;
            while ((temp=br.readLine())!=null){
                googleURL=temp.split("\t")[4];
                
                Random r=new Random();
                //sleep for a random interval before crawling the next one;
                int t=r.nextInt(600000);
                System.out.println("Sleeping for: "+ t/60000+" minutes");

                Thread.sleep(t);//from 0 second to half an hour
                System.out.println("starts to crawl "+googleURL);

                dist=getDistance(googleURL, googleDir);
                System.out.println("obtained distance "+dist);
                bw.append(temp+"\t"+dist);
                bw.newLine();
                bw.flush();
            }
            br.close();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ExtractDistanceFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExtractDistanceFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
