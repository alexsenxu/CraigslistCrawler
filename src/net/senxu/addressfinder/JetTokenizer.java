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
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author sxu
 */
public class JetTokenizer {
    
    
    static boolean initialized = false;
    static Set<String> stateSet= loadUSState();
    private static Set<String> loadUSState() {
        Set<String> ret =new HashSet<>();
        ret.add("");
        ret.add("ALABAMA");
        ret.add("AL");
        ret.add("ALASKA");
        ret.add("AK");
        ret.add("AMERICAN SAMOA");
        ret.add("AS");
        ret.add("ARIZONA");
        ret.add("AZ"); 
        ret.add("ARKANSAS");
        ret.add("AR");
        ret.add("CALIFORNIA");
        ret.add("CA");
        ret.add("COLORADO");
        ret.add("CO");
        ret.add("CONNECTICUT");
        ret.add("CT");
        ret.add("DELAWARE");
        ret.add("DE");
        ret.add("DISTRICT OF COLUMBIA");
        ret.add("DC");
        ret.add("FEDERATED STATES OF MICRONESIA");
        ret.add("FM");
        ret.add("FLORIDA");
        ret.add("FL");
        ret.add("GEORGIA");
        ret.add("GA");
        ret.add("GUAM");
        ret.add("GU");
        ret.add("HAWAII");
        ret.add("HI");
        ret.add("IDAHO");
        ret.add("ID");
        ret.add("ILLINOIS");
        ret.add("IL");
        ret.add("INDIANA");
        ret.add("IN");
        ret.add("IOWA");
        ret.add("IA");
        ret.add("KANSAS");
        ret.add("KS");
        ret.add("KENTUCKY");
        ret.add("KY");
        ret.add("LOUISIANA");
        ret.add("LA");
        ret.add("MAINE");
        ret.add("ME");
        ret.add("MARSHALL IS");
        ret.add("MH");
        ret.add("MARSHALL ISLANDS");
        ret.add("MH");
        ret.add("MARYLAND");
        ret.add("MD");
        ret.add("MASSACHUSETTS");
        ret.add("MA");
        ret.add("MICHIGAN");
        ret.add("MI");
        ret.add("MINNESOTA");
        ret.add("MN");
        ret.add("MISSISSIPPI");
        ret.add("MS");
        ret.add("MISSOURI");
        ret.add("MO");
        ret.add("MONTANA");
        ret.add("MT");
        ret.add("NEBRASKA");
        ret.add("NE");
        ret.add("NEVADA");
        ret.add("NV");
        ret.add("NEW HAMPSHIRE");
        ret.add("NH");
        ret.add("NEW JERSEY");
        ret.add("NJ");
        ret.add("NEW MEXICO");
        ret.add("NM");
        ret.add("NEWJERSEY");
        ret.add("NJ");
        ret.add("NEWMEXICO");
        ret.add("NM");
        ret.add("NEWYORK");
        ret.add("NY");
        ret.add("NEW YORK");
        ret.add("NY");
        ret.add("N CAROLINA");
        ret.add("NC");
        ret.add("N DAKOTA");
        ret.add("ND");
        ret.add("NORTHERN MARIANA ISLANDS");
        ret.add("MP");
        ret.add("N CAROLINA");
        ret.add("NC");
        ret.add("NORTH DAKOTA");
        ret.add("ND");
        ret.add("NORTHERN MARIANA ISLANDS");
        ret.add("MP");
        ret.add("OHIO");
        ret.add("OH");
        ret.add("OKLAHOMA");
        ret.add("OK");
        ret.add("OREGON");
        ret.add("OR");
        ret.add("PALAU");
        ret.add("PW");
        ret.add("PENNSYLVANIA");
        ret.add("PA");
        ret.add("PUERTO RICO");
        ret.add("PR");
        ret.add("PUERTORICO");
        ret.add("PR");
        ret.add("RHODE ISLAND");
        ret.add("RI");
        ret.add("SOUTH CAROLINA");
        ret.add("SC");
        ret.add("SOUTH DAKOTA");
        ret.add("SD");
        ret.add("S CAROLINA");
        ret.add("SC");
        ret.add("S DAKOTA");
        ret.add("SD");
        ret.add("TENNESSEE");
        ret.add("TN");
        ret.add("TEXAS");
        ret.add("TX");
        ret.add("UTAH");
        ret.add("UT");
        ret.add("VERMONT");
        ret.add("VT");
        ret.add("VIRGIN IS");
        ret.add("VI");
        ret.add("VIRGIN ISLANDS");
        ret.add("VI");
        ret.add("VIRGINIA");
        ret.add("VA");
        ret.add("WASHINGTON");
        ret.add("WA");
        ret.add("WEST VIRGINIA");
        ret.add("WV");
        ret.add("W VIRGINIA");
        ret.add("WV");
        ret.add("WISCONSIN");
        ret.add("WI");
        ret.add("WYOMING");
        ret.add("WY");
        return ret;
    }
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
                    if (stateSet.contains(doc.text(tokenAnn).toUpperCase())){
                        tokenAnn.put("GeoFeat", "USState");
                    }
                    if (doc.text(tokenAnn).toString().matches("\\d{5}\\s*")){
                        tokenAnn.put("GeoFeat", "Zip5");
                    }
                    
                    //if (tokenAnn.attributes().containsFeature("intvalue"))
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
