/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.craigslistcrawler;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author sxu
 */
public class CraigsListCrawler {

    /**
     * @param args the command line arguments
     */
    
    public static final File listDir=new File("./ListDir/");
    public static final File resultDir=new File("./ResultDir/");
    public static final File urlFile=new File("./uniqueURL.txt");
    
    
    public static void CraigslistCrawl(String[] args) {
        // TODO code application logic here
        
        ArrayList<String> uniqueurlList=new ArrayList<String>();
        ArrayList<String> queryList=new ArrayList<String>();
        //step0. prep queryList, load urlList
        queryList=DataPrep.ConstructQuery();
        
        
        //loop step 1-3 for all queryList.
        
        
            //step1. store search result list pages.
        String pageplus="&pstart=1&b=";
        boolean hitlistStored;
               for (String s:queryList){
               //read in the initial query, construct next page until their is no more pages.
                   int pagecount=0;
                   while (!IsHitListEmpty(s)){
                       if (pagecount>10) s=s+pageplus+pagecount;
                       hitlistStored=StoreHitList(s, listDir);
                       pageplus=pageplus+10;
                   }
                   
                   
                   
               //step 1.1 check if the search result is using suggested query, if the query is without quotes, neglect this search result page
           
               
           }
        

            //step2. parse URL from result list pages, store URL in resultDir if it's a uniqueURL, update the unique URLfile.
            
                //step 2.1 parse URL
                
                //step 2.2 check if the search result is using suggested query, if the query is without quotes, neglect this search result page
        
                //step 2.3 check if the 
        
        
            //step3. delete the search result list pages.
        
        
        
    }
    
    public static void main(String[] args){
        RunScheduledCrawling();
    }

    private static void RunScheduledCrawling() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    /*
    * return true if the url s returns a webpage that doesn't have any search 
    * result, false otherwise
    */
    private static boolean IsHitListEmpty(String s) {
        return false;
    }
    /*
    * return true if url s webpage was stored sucessfully, false otherwise
    */
    private static boolean StoreHitList(String s, File listDir) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
