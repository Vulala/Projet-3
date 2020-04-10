package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {
    
    // Integration Test used to test that tickets are correctly saved in the
    // DataBase
    
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static ParkingService parkingService;
    
    @Mock
    private static InputReaderUtil inputReaderUtil;
    
    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }
    
    @BeforeEach
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }
    
    @AfterAll
    private static void tearDown() {
        
    }
    
    @Test
    public void GivenAnUserComingInTheParking_WhenProcessIncomingVehicle_ThenItSaveATicketInTheDBAndTheAvailabilityIsWellSet()
            throws ClassNotFoundException, SQLException {
        
        // ARANGE
        Connection con = null;
        ResultSet resultSet = null;
        String request = "SELECT AVAILABLE FROM parking";
        
        // ACT
        parkingService.processIncomingVehicle(); // availability: true -> false
        con = dataBaseTestConfig.getConnection();
        System.out.println("Connexion à la base de données");
        
        System.out.println("Execution de la requête");
        try {
            Statement stmt = con.createStatement();
            resultSet = stmt.executeQuery(request);
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'execution de la requête");
            
        }
        
        // ASSERT
        System.out.println("Parcours des données retournées par la requête");
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int numberColumn = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= numberColumn; i++) {
                    assertEquals("0", resultSet.getString("AVAILABLE"));
                    System.out.println(resultSet.getString("AVAILABLE"));
                    // 0 mean that the parking slot is not available
                    
                }
                while (resultSet.next()) {
                    for (int i = 1; i <= numberColumn; i++);
                    System.out.print(resultSet.getString("AVAILABLE"));
                    
                    // => 0;1;1;1;1
                }
                
            }
            resultSet.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }
        
    }
    
    @DisplayName("The ID is saved in the DB, Car case")
    @Test
    public void GivenACarComingInTheParking_WhenProcessIncomingVehicle_ThenTheIDIsSavedInTheDB() {
        parkingService.processIncomingVehicle();
        assertEquals(ticketDAO.getTicket("ABCDEF").getId(), 1);
    }
    
    @DisplayName("The ID is saved in the DB, Bike case")
    @Test
    public void GivenABikeComingInTheParking_WhenProcessIncomingVehicle_ThenTheIDIsSavedInTheDB() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService.processIncomingVehicle();
        assertEquals(ticketDAO.getTicket("ABCDEF").getId(), 1);
    }
    
    @DisplayName("The ParkingType is saved in the DB, Car case")
    @Test
    public void GivenACarComingInTheParking_WhenProcessIncomingVehicle_ThenTheParkingTypeSavedInTheDBIsCorrect() {
        parkingService.processIncomingVehicle();
        assertEquals(ticketDAO.getTicket("ABCDEF").getParkingSpot().getParkingType(), ParkingType.CAR);
    }
    
    @DisplayName("The ParkingType is saved in the DB, Bike case")
    @Test
    public void GivenABikeComingInTheParking_WhenProcessIncomingVehicle_ThenTheParkingTypeSavedInTheDBIsCorrect() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService.processIncomingVehicle();
        assertEquals(ticketDAO.getTicket("ABCDEF").getParkingSpot().getParkingType(), ParkingType.BIKE);
    }
    
    @DisplayName("The Availability is updated in the DB, Car case")
    @Test
    public void GivenACarComingInTheParking_WhenProcessIncomingInTheParking_ThenTheParkingAvailabilityIsSetInTheDB() {
        parkingService.processIncomingVehicle();
        assertEquals(ticketDAO.getTicket("ABCDEF").getParkingSpot().isAvailable(), false);
    }
    
    @DisplayName("The Availability is updated in the DB, Bike case")
    @Test
    public void GivenABikeComingInTheParking_WhenProcessIncomingInTheParking_ThenTheParkingAvailabilityIsSetInTheDB() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService.processIncomingVehicle();
        assertEquals(ticketDAO.getTicket("ABCDEF").getParkingSpot().isAvailable(), false);
    }
    
    @DisplayName("The Price is updated in the DB, Car case")
    @Test
    public void GivenACarLeavingTheParking_WhenProcessExitingVehicle_ThenThePriceIsSetCorrectly()
            throws InterruptedException {
        
        parkingService.processIncomingVehicle();
        TimeUnit.SECONDS.sleep(1);
        parkingService.processExitingVehicle();
        assertEquals(ticketDAO.getTicket("ABCDEF").getPrice(), 0);
        // Check that the price of the Ticket is equal to 0 (30 minutes feature)
    }
    
    @DisplayName("The Price is updated in the DB, Bike case")
    @Test
    public void GivenABikeLeavingTheParking_WhenProcessExitingVehicle_ThenThePriceIsSetCorrectly()
            throws InterruptedException {
        
        when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService.processIncomingVehicle();
        TimeUnit.SECONDS.sleep(1);
        parkingService.processExitingVehicle();
        
        assertEquals(ticketDAO.getTicket("ABCDEF").getPrice(), 0);
        // Check that the price of the Ticket is equal to 0 (30 minutes feature)
    }
    
}
