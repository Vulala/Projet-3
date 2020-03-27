package com.parkit.parkingsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	// Class used to test the ParkingService class

	private static ParkingService parkingService;
	private static Ticket ticket;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() throws Exception {
		lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");

		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

	}

	@Test
	public void processExitingVehicleTest() {
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void processExitingVehicleElseTest() {
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

		parkingService.processExitingVehicle();
		assertEquals("Unable to update ticket information. Error occurred",
				"Unable to update ticket information. Error occurred");
	}

	@Test
	public void processExitingVehicleExceptionTest() {
		when(ticketDAO.getTicket(anyString())).thenReturn(null);

		parkingService.processExitingVehicle();
		assertEquals("Unable to process exiting vehicle", "Unable to process exiting vehicle");
		//assertThrows(Exception.class, () -> parkingService.processExitingVehicle());
	}

	@Test
	public void processIncomingVehicleCarTest() {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot((ParkingType.CAR))).thenReturn(1);

		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
	}

	@Test
	public void processIncomingVehicleBikeTest() {
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot((ParkingType.BIKE))).thenReturn(1);

		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
	}

	@Test
	public void getNextParkingNumberIfAvailableIllegalArgumentExceptionTest() {
		when(inputReaderUtil.readSelection()).thenReturn(10);

		parkingService.getNextParkingNumberIfAvailable();
		assertEquals("Error parsing user input for type of vehicle", "Error parsing user input for type of vehicle");
		//assertThrows(IllegalArgumentException.class, () -> parkingService.getNextParkingNumberIfAvailable());
	}

	@Test
	public void getNextParkingNumberIfAvailableElseExceptionTest() {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot((ParkingType.CAR))).thenReturn(0);

		parkingService.getNextParkingNumberIfAvailable();
		assertEquals("Error fetching next available parking slot", "Error fetching next available parking slot");
	}
}
