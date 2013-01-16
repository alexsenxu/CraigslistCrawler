/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.location;

/**
 *
 * @author sxu
 */
public class StreetIntersection {
    
    Street street1;
    Street street2;
    String city;
    String state;
    
    public StreetIntersection(){
        
    }
    public StreetIntersection(Street s1, Street s2){
        street1=s1;
        street2=s2;
    }
    
}
