/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.addressfinder;

import Jet.Tipster.Annotation;
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
    
    public static String cleanFile(File f){
        Document doc;
        try {
            doc = Jsoup.parse(f, "UTF-8");
            doc = new Cleaner(Whitelist.simpleText()).clean(doc);
            doc.outputSettings().escapeMode(EscapeMode.xhtml);
            return doc.body().html();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    
    public static void main(String[] args){
        File f=new File("testCraig.html");
        String s=cleanFile(f);
        Annotation[] tokens = JetTokenizer.getTokens(s);
        Jet.Tipster.Document doc = new Jet.Tipster.Document(s);
        for (Annotation token:tokens){
            System.out.println(doc.text(token) + "\t" + token.toSGMLString());
        }
    }
    
}
