/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.file.analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author sxu
 */
public class LocatedCount {
    
    public static void main(String[] args) throws IOException{
        File dir=new File("./CrawledData/ResultDir/");
        DateFormat df=new SimpleDateFormat("MMMdd_yyyy");
        Date today=new Date();
        File output=new File("./Summary"+df.format(today)+".csv");
        if (!output.exists()){
            output.createNewFile();
        }
        File[] listFile=dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".html");
            }
        });
        int count=0;
        Map<String, Integer> storeCount=new HashMap<>();
        Map<String, Integer> phraseCount=new HashMap<>();
        BufferedWriter bw=new BufferedWriter(new FileWriter(output));
        bw.append("FileName"+"\t");
        bw.append("NearPhrase"+"\t");
        bw.append("ReferenceObject"+"\t");
        bw.append("MapLink"+"\t");
        bw.append("GoogleNearLink"+"\t");
        bw.newLine();
        String store;
        String phrase;
        String temp;
        String nearLink;
        int tmp;
        for (File f:listFile){
            String link=getLocatedLink(f);
            if (link.length()!=0) {
                count++;
//                System.out.println(f.getName().toString());
                bw.append(f.getName()+"\t");
                temp=f.getName().split("_L_")[0];
                
                phrase=temp.split("\\+[A-Z]")[0];
                bw.append(phrase.replaceAll("\\+", " ") +"\t");
                store=temp.split("\\+")[temp.split("\\+").length-1];
                bw.append(store +"\t");
                bw.append(link+"\t");
                nearLink="http://maps.google.com/?q="+store+"+near+"+link.split("q=loc")[1];
                bw.append(nearLink+"\t");
                bw.newLine();
                bw.flush();
                if (storeCount.containsKey(store)){
                    tmp=storeCount.get(store);
                    tmp++;
                    storeCount.put(store, tmp);
                }else{
                    storeCount.put(store, 1);
                }
                if (phraseCount.containsKey(phrase)){
                    tmp=phraseCount.get(phrase);
                    tmp++;
                    phraseCount.put(phrase, tmp);
                }else{
                    phraseCount.put(phrase, 1);
                }
            }
            
        }
        bw.close();
        
        System.out.println(count +" out of "+ listFile.length+" contains located addresses");
        print(phraseCount);
        print(storeCount);
        
        
    }

    private static String getLocatedLink(File f) {
        try {
            BufferedReader br=new BufferedReader(new FileReader(f));
            String google="google map</a>)";
            String yahoo="yahoo map</a>)";
            String temp;
            String link;
            while ((temp=br.readLine())!=null){
                if (temp.contains(google)||temp.contains(yahoo)){
                    link=temp.split("href=")[1].substring(1).split("\"")[0];
//                    System.out.println(link);
                    return link;
                }
            }
            return "";
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    private static void print(Map<String, Integer> m) {
        Iterator it=m.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry kv=(Map.Entry) it.next();
            System.out.println(kv.getKey()+"\t"+kv.getValue());
        }
    }
    
}
