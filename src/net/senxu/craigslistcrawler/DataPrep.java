/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.craigslistcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sxu
 */
public class DataPrep {
    
    public static final String fsep=System.getProperty("file.separator");
    
    public static final File POIListFile=new File("."+fsep+"Dicts"+fsep+"POIList.txt");
    public static final File NearnessTermFile=new File("."+fsep+"Dicts"+fsep+"NearPhrases.txt");
    
    public static ArrayList<String> LoadFile2ArrayList(File f){
        
        ArrayList<String> ret=new ArrayList<String>();
        try {
            BufferedReader br=new BufferedReader(new FileReader(f));
            String temp;
            while ((temp=br.readLine())!=null){
                if (temp.startsWith("#") || temp.isEmpty()) continue;
                else{
                    ret.add(temp);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DataPrep.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    
    public static ArrayList<String> LoadPOIList(){
        return LoadFile2ArrayList(POIListFile);
    }
    
    public static ArrayList<String> LoadNearnessTerms(){
        return LoadFile2ArrayList(NearnessTermFile);
    }
    
    public static ArrayList<String> ConstructQuery(){
        String prefix="http://search.yahoo.com/search?p=site%3Acraigslist.org+%22";//http://search.yahoo.com/search?p=site%3Acraigslist.org+%22near+mcdonald%27s%22
        String suffix="%22";
        ArrayList<String> ret=new ArrayList<String>();
        ArrayList<String> nearnessTerms=LoadFile2ArrayList(NearnessTermFile);
        ArrayList<String> POIList=LoadFile2ArrayList(POIListFile);
        for (String s:POIList){
            for (String st:nearnessTerms){
                String temp=st+"+"+s;
                temp=temp.replace(" ", "+");
                temp=temp.replace("'", "%27");
                ret.add(prefix+temp+suffix);
            }
        }
        return ret;
    }
    
    public static void main(String[] args){
        ArrayList<String> ret=ConstructQuery();
        for (String s:ret){
            System.out.println(s);
        }
    }
    
}
