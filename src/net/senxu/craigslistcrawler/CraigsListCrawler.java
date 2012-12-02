/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.craigslistcrawler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sxu
 */
public class CraigsListCrawler {

    /**
     * @param args the command line arguments
     */
    public static final String fsep=System.getProperty("file.separator");
    
    public static final File listDir=new File("."+fsep+"CrawledData"+fsep+"ListDir");
    public static final File resultDir=new File("."+fsep+"CrawledData"+fsep+"ResultDir");
    public static final File urlFile=new File("."+fsep+"uniqueURL.txt");
    
    
    public static void CraigslistCrawl() {
        // TODO code application logic here
        
        ArrayList<String> uniqueurlList=new ArrayList<String>();
        ArrayList<String> queryList;
        //step0. prep queryList, load urlList
        queryList=DataPrep.ConstructQuery();
        
        
        //loop step 1-3 for all queryList.
        
        
            //step1. store search result list pages.
        String pageplus="&pstart=1&b=";
        boolean hitlistStored;
               for (String s:queryList){
               //read in the initial query, construct next page until their is no more pages.
                   int pagecount=1;
                   while (!IsHitListEmpty(s+pageplus+pagecount)){
                       hitlistStored=StoreHitList(s+pageplus+pagecount, listDir);
                       if (!hitlistStored) System.out.println("error Storing the returned search result");
                       pagecount=pagecount+10;
                   }
                    
                   
                   
               //step 1.1 check if the search result is using suggested query, if the query is without quotes, neglect this search result page, this step is done in the query generation step
           
               
           }
        

            //step2. parse URL from result list pages, store URL in resultDir if it's a uniqueURL, update the unique URLfile.
            
                //step 2.1 parse URL
                
                //step 2.2 check if the search result is using suggested query, if the query is without quotes, neglect this search result page
        
                //step 2.3 check if the 
        
        
            //step3. delete the search result list pages.
        
        
        
    }
    
    public static void main(String[] args){
        //RunScheduledCrawling();
        //System.out.println(GetCurrentDate());
        //System.out.println(ExtractPageFromURL("http://search.yahoo.com/search?p=site:craigslist.org+\"adjacent+to+Marshalls\"&b=41"));
        RunScheduledCrawling();
    }
    
    /*
     * schedule the crawler to run periodically
     */
    private static void RunScheduledCrawling() {
        CraigslistCrawl();
    }
    /*
    * return true if the url s returns a webpage that doesn't have any search 
    * result, false otherwise
    */
    private static boolean IsHitListEmpty(String s) {
        System.out.println(s);
        boolean resultIsEmpty=false;
        try {
                
            URL url = new URL(s);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.0");

            String contentType = urlConn.getContentType();

            //System.out.println("contentType:" + contentType);

            InputStream is = urlConn.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String temp;
            while ((temp=br.readLine())!=null){
                if (temp.contains("We did not find results for:")) {
                    resultIsEmpty=true;//determine if the webpage is a empty list
                    return resultIsEmpty;
                }
            }
            
            br.close();
            
            return resultIsEmpty;
        } catch (MalformedURLException ex) {
            Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        } catch (IOException ex1){
            Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex1);
            return true;
        }
    }
    /*
    * return true if url s webpage was stored sucessfully, false otherwise
    */
    private static boolean StoreHitList(String urlStr, File listDir) {
        String queryphrase=ExtractQueryFromURL(urlStr);
        String page=ExtractPageFromURL(urlStr);
        String date=GetCurrentDate();
        File destFile=new File(listDir+fsep+queryphrase+"_P"+page+"_D"+date+".html");
        System.out.println("storing "+urlStr+" to local file:"+destFile.toString());
        try {
            if (!destFile.exists()) destFile.createNewFile();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            URL url = new URL(urlStr);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.0");

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
            
            return true;
            } catch (IOException ex) {
                Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
    }
    
    private static String ExtractQueryFromURL(String url){
        String queryphrase="";
        if (url.split("\"").length>1) queryphrase=url.split("\"")[1];//get filename like near+McDonald's
        return queryphrase;
    }
    
    private static String GetCurrentDate(){
        SimpleDateFormat formatter=  new SimpleDateFormat("MM_dd_yyyy");
        Calendar currentDate = Calendar.getInstance(); 
        String dateNow = formatter.format(currentDate.getTime());
        return dateNow;
    }

    private static String ExtractPageFromURL(String urlStr) {
        String page="";
        if (urlStr.split("&b=").length>1) page=urlStr.split("&b=")[1];//get page start number
        //since every page is 10 items, the page number should be the starting number without the last digit, if start number is 41, the page is 4
        page=page.substring(0,page.length()-1);
        return page;
    }
}
