package com.aat.application.data.entity;

/**
 * @author jtrinidad
 *
 */
public enum TripLegType {
	LR("Laden Running"), LS ("Laden Standing"), UR ("Unladen Running"), US("Unladen Standing"); 
	
    private final String name;

    TripLegType(String name) {
        this.name = name;
    }

   @Override
   public String toString() {
	   return name;
   }
   
}