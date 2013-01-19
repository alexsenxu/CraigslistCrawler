/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.addressfinder;

import Jet.Tipster.Annotation;
import java.io.File;
import net.senxu.location.AddressComponents;

/**
 *
 * @author sxu
 */
public class AddressFinder {
    
    AddressFinder(){};
    
    public AddressComponents getCityState(String s){
        AddressComponents ac=new AddressComponents();
        
        return ac;
    }
    
    
    
    
    
    
    public static void main(String[] args){
        File f=new File("testCraig.html");
        String s=JsoupHTMLCleaner.cleanFile(f);
        Annotation[] tokens = JetTokenizer.getTokens(s);
        Jet.Tipster.Document doc = new Jet.Tipster.Document(s);
        for (Annotation token:tokens){
            System.out.println(doc.text(token) + "\t" + token.toSGMLString());
        }
        
    }
    
}
