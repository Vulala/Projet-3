package com.parkit.parkingsystem.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

public class ParkingService {
    
    /*
     * Class used to Write a ticket for every incoming vehicle with the method
     * processIncomingVehicle() This method also save the ticket in the DataBase. Class
     * also used to get the next parking spot available with the method
     * getNextParkingNumberIfAvailable(). Used too to get the vehicle type with the method
     * getVehicleType() This class also proceed to the exit of an user with the method
     * processExitingVehicle()
     */
    
    private static final Logger logger = LogManager.getLogger("ParkingService");
    
    private static FareCalculatorService fareCalculatorService = new FareCalculatorService();
    
    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;
    private TicketDAO ticketDAO;
    
    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }
    
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                String vehicleRegNumber = getVehicleRegNumber();
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);
                // allot this parking space and mark it's availability as false
                Date inTime = new Date();
                Ticket ticket = new Ticket();
                // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE,
                // IN_TIME, OUT_TIME,
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket); // Save the updated ticket in DB
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
                
            }
            
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
            
        }
    }
    
    private String getVehicleRegNumber() throws Exception {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }
    
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
                
            } else {
                System.out.println(
                        "***The parking for your type of vehicle is actually full. Sorry for the inconvenience.***");
                throw new Exception("Error fetching parking number from DB. Parking slots are full");
                
            }
            
        } catch (IllegalArgumentException ie) {
            logger.error("Error parsing user input for type of vehicle", ie);
            
        } catch (Exception e) {
            logger.error("Error fetching next available parking slot", e);
            
        }
        return parkingSpot;
    }
    
    private ParkingType getVehicleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
        case 1: {
            return ParkingType.CAR;
            
        }
        case 2: {
            return ParkingType.BIKE;
            
        }
        default: {
            System.out.println("Incorrect input provided");
            throw new IllegalArgumentException("Entered input is invalid");
            
        }
        
        }
    }
    
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            // Set the outTime and then call the FareCalculatorService class
            // to calculate the price
            fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true); // Set the spot as available
                parkingSpotDAO.updateParking(parkingSpot);
                // update the availability for that parking slot in the DB
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println(
                        "Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
                
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
                
            }
            
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
            
        }
    }
}
