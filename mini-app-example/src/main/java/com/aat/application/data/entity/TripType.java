package com.aat.application.data.entity;

/**
 * @author jtrinidad
 *
 */
public enum TripType {
	TA("Trip Actual"), TL ("Trip Leg"), TI ("Trip Itinerary"), LI("Leg Itinerary"), TC("Trip Component"); 
	
    private final String name;

    TripType(String name) {
        this.name = name;
    }

   @Override
   public String toString() {
	   return name;
   }
   
}