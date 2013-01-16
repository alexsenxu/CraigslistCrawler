/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.addressfinder;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author sxu
 */
public class JsoupHTMLCleaner {
    
    public static void main(String[] args){
        File f=new File("testCraig.html");
        Document doc;
        try {
            doc = Jsoup.parse(f, "UTF-8");
            doc = new Cleaner(Whitelist.simpleText()).clean(doc);
            doc.outputSettings().escapeMode(EscapeMode.xhtml);
            System.out.println(doc.body().html());
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
