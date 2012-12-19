/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.addressfinder;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author sxu
 */
public class JsoupHTMLCleaner {
    
    public static void main(String[] args){
        String s="test<head>with html tag</head><body>hello world</body>";
        System.out.println(Jsoup.clean(s,Whitelist.basicWithImages()));
    }
    
}
