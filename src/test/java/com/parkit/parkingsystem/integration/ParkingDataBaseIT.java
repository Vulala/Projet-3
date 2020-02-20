package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	// TODO: check that a ticket is actually saved in DB and Parking table
	// is updated with availability

	@Test
	public void testParkingACar() throws ClassNotFoundException, SQLException {
		// ARANGE
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		// ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		// System.out.println("*** 1 *** :" + parkingSpot.isAvailable());

		Connection con = null;
		ResultSet resultSet = null;
		String request = "SELECT AVAILABLE FROM parking";

		// ACT// ASSERT
		parkingService.processIncomingVehicle(); // true -> false
		con = dataBaseTestConfig.getConnection();
		System.out.println("Connexion à la base de données");

		System.out.println("Execution de la requête");
		try {
			Statement stmt = con.createStatement();
			resultSet = stmt.executeQuery(request);

		} catch (SQLException e) {
			System.out.println("Erreur lors de l'execution de la requête");

		}

		System.out.println("Parcours des données retournées par la requête");
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int numberColumn = rsmd.getColumnCount();
			boolean again = resultSet.next();
			while (again) {
				for (int i = 1; i <= numberColumn; i++) {
					System.out.print(resultSet.getInt(i));

				}
				while (again) {
					for (int i = 1; i <= numberColumn; i++)
						assertEquals("0", resultSet.getString("AVAILABLE"));
					again = resultSet.next();

					// expected 0 but was 1 -> expected 1 but was 0...
				}
				resultSet.close();

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

	}

	@Disabled
	@Test // TODO: check that the fare generated AND the out time are populated
			// correctly in the database
	public void testParkingLotExit() {
		// ARRANGE
		// testParkingACar();
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

		// ACT
		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();

		// ASSERT
		assertEquals(parkingSpotDAO.updateParking(parkingSpot), true); // Exiting
		// assertEquals(ticketDAO.updateTicket(any(Ticket.class)), 1.5); // Fare
	}

}
