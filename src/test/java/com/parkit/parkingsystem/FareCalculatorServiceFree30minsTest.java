package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceFree30minsTest {
    
    // Test the 30 minutes free fare when the user is parked for 30 minutes or less.
    
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    
    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }
    
    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }
    
    @Test
    public void GivenACarParkedFor30MinutesOrLess_WhenCalculateFare_ThenThePriceShouldBe0() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        // 30 minutes or less parking time should give free parking fare
        
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, null);
        assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }
    
    @Test
    public void GivenABikeParkedFor30MinutesOrLess_WhenCalculateFare_ThenThePriceShouldBe0() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        // 30 minutes or less parking time should give free parking fare
        
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, null);
        assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }
}
