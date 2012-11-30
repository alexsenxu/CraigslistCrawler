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
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        ArrayList<String> uniqueurlList=new ArrayList<String>();
        ArrayList<String> queryList=new ArrayList<String>();
        //step0. prep queryList, load urlList
        queryList=DataPrep.ConstructQuery();
        
        
        //loop step 1-3 for all queryList.
        
        
            //step1. store search result list pages.
                //step 1.1 check if the search result is using suggested query, if the query is without quotes, neglect this search result page
        

            //step2. parse URL from result list pages, store URL in resultDir if it's a uniqueURL, update the unique URLfile.
            
                //step 2.1 parse URL
                
                //step 2.2 check if the search result is using suggested query, if the query is without quotes, neglect this search result page
        
                //step 2.3 check if the 
        
        
            //step3. delete the search result list pages.
        
        
        
    }
}
