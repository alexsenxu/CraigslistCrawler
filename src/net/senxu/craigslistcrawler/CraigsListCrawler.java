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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final File urlFile=new File("."+fsep+"CrawledData"+fsep+"uniqueURLs.txt");
    public static final String targetURL="//[a-z]{2,20}.craigslist.(org|ca|co.uk)(/[a-z]{2,20}){1,3}/\\d{3,15}.html";//http%3a//sanantonio.craigslist.org/rvs/3397989229.html, 
    
    
    public static void CraigslistCrawl() {
        // TODO code application logic here
        
        Set<String> uniqueurls=loadSet(urlFile);
        
        ArrayList<String> queryList;
        //step0. prep queryList, load urlList
        queryList=DataPrep.ConstructQuery();
        //loop step 1-3 for all queryList.
        String queryphrase;
        String date;
        File destFile;
            //step1. store search result list pages.
        String pageplus="&pstart=1&b=";
        boolean hitlistStored;
               for (String s:queryList){
               //read in the initial query, construct next page until their is no more pages.
                   int pagecount=1;
                   
                    queryphrase=ExtractQueryFromURL(s);
                    date=GetCurrentDate();
                    destFile=new File(listDir+fsep+queryphrase+"_P"+pagecount%10+"_D"+date+".html");
                    hitlistStored=StoreHitList(s+pageplus+pagecount, destFile);
                    //first store the file, then check if the file is an empty list
                    while (!IsStoredListEmpty(destFile)){//if it's not an empty list, keep on crawling
                        
                       if (!hitlistStored) {
                            System.out.println("error Storing the returned search result");
                        }
                       pagecount=pagecount+10;
                       destFile=new File(listDir+fsep+queryphrase+"_P"+pagecount%10+"_D"+date+".html");
                       hitlistStored=StoreHitList(s+pageplus+pagecount, destFile);
                       
                   }
                    //if it is an empty list, delete the file and move on to the next query.
                   destFile.delete();
               //step 1.1 check if the search result is using suggested query, if the query is without quotes, neglect this search result page, this step is done in the query generation step
               }
      //step2. parse URL from result list pages, store URL in resultDir if it's a uniqueURL, update the unique URLfile.
            File[] resultList = listDir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                                return name.endsWith(".html");
                        }
                    });
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(urlFile, true));
            String foundurl;
            String url2filename;
            boolean urlStored;
            for (File result:resultList){
                //step 2.1 parse URL
                BufferedReader br=new BufferedReader(new FileReader(result));
                String temp;
                while ((temp=br.readLine())!=null){
                    Pattern p=Pattern.compile(targetURL);
                    Matcher m=p.matcher(temp);
                    if (m.find()){//if a craigslist url is found, 
                        //step 2.2 check if the URL is unique, if it is, update the uniqueURL hashset
                        foundurl="http:"+m.group(0);
                        if (!uniqueurls.contains(foundurl)){//if it hasn't been stored before
                            System.out.println("found new URL:"+foundurl);
                   //step 2.3 crawl the url and store it in resultDir
                            queryphrase=ExtractQueryFromFile(result);
                            date=GetCurrentDate();
                            url2filename=URL2FileName(foundurl);
                            destFile=new File(resultDir+fsep+queryphrase+"_L_"+url2filename+"_D"+date+".html");
                            urlStored=StoreHitList(foundurl, destFile);
                            if (!urlStored) {
                                System.out.println("!!!!!!!!!!!!! URL not stored successfully for: "+foundurl + " to local: "+ destFile);
                            }
                            uniqueurls.add(foundurl);
                            bw.append(foundurl);
                            bw.newLine();
                            if (IsPostEmpty(destFile)) destFile.delete();
                        }
                        bw.flush();
                    }
                    
                }
                br.close();
                //step3. move the search result list pages to archieve
                MoveToArchieve(result);
            }
            
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        CraigslistCrawl();
        RunScheduledCrawling();
        //System.out.println(GetCurrentDate());
        //System.out.println(ExtractPageFromURL("http://search.yahoo.com/search?p=site:craigslist.org+\"adjacent+to+Marshalls\"&b=41"));
//        Pattern p=Pattern.compile(targetURL);
//        Matcher m=p.matcher("<li><a href=\"http://belleville.craigslist.ca/rvs/3397989229.html\">belleville</a></li>");
//        if (m.find()){
//            System.out.println(m.group(0));
//        }
        
//        File[] resultList = listDir.listFiles(new FilenameFilter() {
//                        @Override
//                        public boolean accept(File dir, String name) {
//                                return name.endsWith(".html");
//                        }
//                    });
//        for (File f:resultList){
//            MoveToArchieve(f);
//        }
        //System.out.println(ExtractQueryFromFile(new File(".\\CrawledData\\ListDir\\adjacent+to+KFC_P_D12_01_2012.html")));
    }
    
    /*
     * schedule the crawler to run periodically
     */
    private static void RunScheduledCrawling() {
        Timer timer = new Timer();
        System.out.println("started scheduling daily crawling task");
        long delay =72000000;//20 hours
        long period =86400000;//24 hours
        timer.schedule(new TimerTask(){
        public void run(){
            System.out.println("Task Running on Date:"+GetCurrentDate());
            CraigslistCrawl();
        }
        },delay);
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
    private static boolean StoreHitList(String urlStr, File destFile) {
        System.out.println("storing "+urlStr+" to local file:"+destFile.toString());
        try {
            if (!destFile.exists()) destFile.createNewFile();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            URL url = new URL(urlStr);

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

    private static boolean IsStoredListEmpty(File destFile) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(destFile));
            String temp;
            while ((temp=br.readLine())!=null){
                if (temp.contains("We did not find results for:")) {//determine if the webpage is a empty list
                    br.close();
                    return true;
                }
            }
            br.close();
            return false;
            
        } catch (IOException ex) {
            Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    private static Set<String> loadSet(File urlFile) {
        Set<String> ret=new HashSet<String>();
        try {
            BufferedReader br=new BufferedReader(new FileReader(urlFile));
            String temp;
            while ((temp=br.readLine())!=null){
                if (temp.startsWith("#") || temp.isEmpty()) continue;
                else{
                    ret.add(temp);
                }
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    private static String ExtractQueryFromFile(File result) {
        String queryphrase="";
        String filename=result.getName();
        if (filename.split("_").length>1) queryphrase=filename.split("_")[0];//get filename like near+McDonald's
        return queryphrase;
    }

    private static String URL2FileName(String foundurl) {
        String ret;
        ret=foundurl.replace("http://", "");
        ret=ret.replace(".craigslist.org", "");
        ret=ret.replace(".html", "");
        ret=ret.replace('.', '_');
        ret=ret.replace('/', '_');
        return ret;
    }

    private static void MoveToArchieve(File result) {
        String dest=result.getName();
        System.out.println(dest);
        File destFile=new File("."+fsep+"CrawledData"+fsep+"ArchievedListDir"+fsep+dest);
        
        boolean success=result.renameTo(destFile);
        if (!success){
            System.out.println(result.toString()+" move uncessful to"+destFile.toString());
        }
        
//        Path source=Paths.get(result.getAbsolutePath());
//        Path target=Paths.get(destFile.getAbsolutePath());
//        try {
//            Files.move(source, target);
//        } catch (IOException ex) {
//            Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }

    private static boolean IsPostEmpty(File destFile) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(destFile));
            String temp;
            while ((temp=br.readLine())!=null){
                if (temp.contains("This posting has been deleted by its author.") || temp.contains("This posting has expired.")) {//determine if the webpage is a empty list
                    br.close();
                    return true;
                }
            }
            br.close();
            return false;
            
        } catch (IOException ex) {
            Logger.getLogger(CraigsListCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
}
