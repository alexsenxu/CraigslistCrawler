/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.location;

/**
 *
 * @author sxu
 */
public class AddressComponents {
    
    String houseNumber;
    String Street;
    String line2;
    String city;
    String State;
    String zipcode;
    String fullAddress;
    public AddressComponents(){}
    public AddressComponents(String f){
        fullAddress=f;
    }
    
}
