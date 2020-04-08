package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    
    /*
     * FareCalculatorService is the class that will calculate the price that the user have
     * to pay when he leave the parking. The two features requested have been added: One
     * allow the user to not pay anything when he park for less than 30 minutes . (It is
     * not 30 first free minutes) The second features reduce the price by 5% if the user
     * is a recurrent user. (if he already been there before.)
     */
    
    public void calculateFare(Ticket ticket, String vehicleRegNumber) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
            
        } else {
            // 5% decrease if recurrent user -> ticket.getVehicleRegNumber
            long inHour = ticket.getInTime().getTime();
            long outHour = ticket.getOutTime().getTime();
            
            long duration = (outHour - inHour) / (60 * 1000);
            if (duration > 30 && vehicleRegNumber != null) {
                System.out.println(
                        "Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
                System.out.println("Your vehicle have been parked for " + duration + " minutes.");
                switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(Fare.CAR_RATE_PER_HOUR * duration / 60 * 0.95);
                    break;
                    
                }
                case BIKE: {
                    ticket.setPrice(Fare.BIKE_RATE_PER_HOUR * duration / 60 * 0.95);
                    break;
                    
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
                
                }
                
            }
            
        }
        
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        
// TODO: Some tests are failing here. Need to check if this logic is correct
//-> change int getHour() to long getTime().
        
        long duration = (outHour - inHour) / (60 * 1000);
        
        if (duration <= 30) {
            // Free when parked for less than 30 minutes
            System.out.println("Your vehicle have been parked for " + duration
                    + " minutes which benefit from the 30 mins free parking time.");
            switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(Fare.CAR_RATE_PER_HOUR * 0);
                break;
                
            }
            
            case BIKE: {
                ticket.setPrice(Fare.BIKE_RATE_PER_HOUR * 0);
                break;
                
            }
            
            }
            
        } else {
            if (duration > 30 && vehicleRegNumber == null) {
                System.out.println("Your vehicle have been parked for " + duration + " minutes.");
                switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(Fare.CAR_RATE_PER_HOUR * duration / 60);
                    break;
                    
                }
                case BIKE: {
                    ticket.setPrice(Fare.BIKE_RATE_PER_HOUR * duration / 60);
                    break;
                    
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
                
                }
                
            }
            
        }
    }
}
