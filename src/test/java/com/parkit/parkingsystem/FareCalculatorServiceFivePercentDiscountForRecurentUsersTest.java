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

public class FareCalculatorServiceFivePercentDiscountForRecurentUsersTest {
    
    // Test the 5% discount feature
    
    private static FareCalculatorService fareCalculatorService;
    private static ParkingSpot parkingSpotCar;
    private static ParkingSpot parkingSpotBike;
    private static Ticket ticketCar;
    private static Ticket ticketBike;
    
    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }
    
    @BeforeEach
    private void ticketSetUp() {
        ticketCar = new Ticket();
        ticketBike = new Ticket();
        parkingSpotCar = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotBike = new ParkingSpot(1, ParkingType.BIKE, true);
        
        Date outTime = new Date();
        ticketCar.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticketCar.setVehicleRegNumber("ABCDEF");
        ticketCar.setOutTime(outTime);
        ticketCar.setParkingSpot(parkingSpotCar);
        
        ticketBike.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticketBike.setVehicleRegNumber("ABCDEF");
        ticketBike.setOutTime(outTime);
        ticketBike.setParkingSpot(parkingSpotBike);
    }
    
    @Test
    public void GivenARecurentUser_WhenCalculateFare_ThenThePriceShouldBe5PercentLessCarCase() {
        fareCalculatorService.calculateFare(ticketCar, ticketCar.getVehicleRegNumber());
        assertEquals(ticketCar.getPrice(), Fare.CAR_RATE_PER_HOUR * 0.95);
    }
    
    @Test
    public void GivenARecurentUser_WhenCalculateFare_ThenThePriceShouldBe5PercentLessBikeCase() {
        fareCalculatorService.calculateFare(ticketBike, ticketBike.getVehicleRegNumber());
        assertEquals(ticketBike.getPrice(), Fare.BIKE_RATE_PER_HOUR * 0.95);
    }
}
