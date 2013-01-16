/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.file.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 * @author sxu
 */
public class LocatedCount {
    
    public static void main(String[] args){
        File dir=new File("./CrawledData/ResultDir/");
        File[] listFile=dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".html");
            }
        });
        int count=0;
        for (File f:listFile){
            if (CheckLocated(f)) count++;
        }
        System.out.println(count +" out of "+ listFile.length+" contains located addresses");
        
    }

    private static boolean CheckLocated(File f) {
        try {
            BufferedReader br=new BufferedReader(new FileReader(f));
            String google="google map</a>)";
            String yahoo="yahoo map</a>)";
            String temp;
            while ((temp=br.readLine())!=null){
                if (temp.contains(google)||temp.contains(yahoo)){
                    return true;
                }
            }
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
}
