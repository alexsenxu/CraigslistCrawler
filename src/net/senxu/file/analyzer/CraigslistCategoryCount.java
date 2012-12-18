/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.file.analyzer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author sxu
 */
public class CraigslistCategoryCount {
    
    public static File dataDir=new File("./CrawledData/ResultDir");
    
    
    public static Map<String, Integer> getCraigslistCount(File dir){
        FilenameFilter htmlFilter=new FilenameFilter(){
            @Override
            public boolean accept(File d, String name){
                return name.endsWith(".html");
            }
        };
        Map<String, Integer> catCount=new HashMap<>();
        File[] listFiles=dir.listFiles(htmlFilter);
        for (File f:listFiles){
            String name=f.getName();

            String cat=name.split("_L_")[1].split("_D")[0];
            cat=cat.split("_")[cat.split("_").length-2];
//            System.out.println(cat);
            if (catCount.containsKey(cat)){
                int x=catCount.get(cat);
                x++;
                catCount.put(cat, x);
            }else{
                catCount.put(cat, 1);
            }
        }
        
        return catCount;
        
    }
    
    public static void main(String[] args){
        Map<String, Integer> t=getCraigslistCount(dataDir);
        System.out.println(t);
        Iterator it=t.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry e=(Map.Entry)it.next();
            System.out.println(e.getKey()+"\t"+e.getValue());
        }
    }
    
}
