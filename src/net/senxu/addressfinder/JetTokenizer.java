/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.addressfinder;

import Jet.JetTest;
import Jet.Lex.Tokenizer;
import Jet.Tipster.Annotation;
import Jet.Tipster.Document;
import Jet.Tipster.Span;
import java.util.Vector;

/**
 *
 * @author sxu
 */
public class JetTokenizer {
    
    
    static boolean initialized = false;

        public static void main (String args[]) {
                if (!initialized) {
                        JetTest.initializeFromConfig("./props");
                        initialized = true;
                }
                String txt = "He went to New York, NY, 10003 and then went to I.B.M.";
                Document doc = new Document(txt);
                Span span = doc.fullSpan();           
                Tokenizer.tokenize(doc, span);
                Annotation[] tokens = Tokenizer.gatherTokens(doc, span);
                for (Annotation tokenAnn : tokens) {

                    if (tokenAnn.attributes().containsFeature("intvalue"))
                    System.out.println(doc.text(tokenAnn) + "\t" + tokenAnn.toSGMLString());
                }
                JetTest.nameTagger.tag(doc, span);
                doc.shrink("ENAMEX");
                Vector<Annotation> names = doc.annotationsOfType("ENAMEX");
                if (names == null)
                        System.err.println ("No names.");
                else
                        for (Annotation name : names) {
                                Span nameSpan = name.span();
                                System.out.println("Name is " + doc.text(name));
                                String type = (String) name.get("TYPE");                                
                                System.out.println ("Name type is " + type + " from " +
                                        nameSpan.start() + " to " + (nameSpan.end()-1));
                        }

        }

    
}
