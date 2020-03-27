package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class ParkingSpotDAOIT {

	private static DataBasePrepareService dataBasePrepareService;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
	private static Ticket ticket;

	@Mock
	private static TicketDAO ticketDAO;

	@BeforeAll
	private static void setUp() {
		dataBasePrepareService = new DataBasePrepareService();
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService.clearDataBaseEntries();
	}

	@BeforeEach
	private void setUpDB() {
		ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticketDAO.saveTicket(ticket);
	}

	@AfterAll
	private static void tearDown() {
	}

	@Test
	public void updateParkingTest() {
		parkingSpotDAO.updateParking(parkingSpot);
		ticketDAO.getTicket(ticket.getVehicleRegNumber());
		
		assertEquals(ticket.getVehicleRegNumber(), "ABCDEF");
		assertEquals(parkingSpot.getId(), 1);
		assertEquals(true, parkingSpot.isAvailable());

	}

	@Test
	public void getNextAvailableSlotTest() {
		// parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
	}
}
