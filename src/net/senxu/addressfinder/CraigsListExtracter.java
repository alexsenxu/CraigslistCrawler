/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.addressfinder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 *
 * @author sxu
 */
public class CraigsListExtracter {
    /*
     * parse the city name from the URL (match to Craiglist list of cities) and the listing title (surrounded by ())
     */
    public static ArrayList<String> getCities(File f){
        ArrayList<String> ret=new ArrayList<>();
        
        
        return ret;
        
    }
    /*
     * extract the category of craigslist from this file
     */
    public static String getRegion(File f){
        String ret=f.getName().toString().split("_L_")[1].split("_")[0];
        return ret;
    }
    
    
    
    /*
     * extract the category of craigslist from this file
     */
    public static String getCategory(File f){
        String[] arr=f.getName().toString().split("_L_")[1].split("_");
        for (int i=0;i<arr.length;i++){
            if (arr[i].matches("\\d*")) return arr[i-1];
        }
        return arr[1];
    }
    /*
     * extract the phrase of proximity from this file
     */
    public static String getPhrase(File f){
        String ret=f.getName().toString().split("\\+[A-Z]")[0].replace("+", " ");
        return ret;
    }
    
    public static void main(String[] args){
        File dir=new File("./CrawledData/ResultDir/");
        File[] listFiles=dir.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File f, String name){
                return name.toLowerCase().endsWith(".html");
            }
        });
        for (File f:listFiles){
            System.out.println(f.getName());
            System.out.println(getPhrase(f));
            System.out.println(getCategory(f));
            System.out.println(getRegion(f));
            
        }
    }
    
}
