package com.aat.application.data.entity;

/**
 * H - Hour
 * K - KM
 * E - Each
 * @author jtrinidad
 *
 */
public enum ElementList {
	VH("Vehicle Hours"), OH("Over Head"), VK("Vehicle KM"), PM("Profit Margin"), DH("Driver Hours"), AE("A & E F");
	
    private final String name;

    ElementList(String name) {
        this.name = name;
    }

   @Override
   public String toString() {
	   return name;
   }
   
}