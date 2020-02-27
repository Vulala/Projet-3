package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class FivePercentsDiscountForRecurentUsersTest {

	// Class used to test the 5% discount feature. It test for car and
	// for bike

	private static ParkingService parkingService;
	private static FareCalculatorService fareCalculatorService;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void SetUpCarTest() {

		// BeforeEach used to put a record in the DB before the test. To test
		// the current user. Might be pointless for a Unit Test(?)

		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket();
			ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");

			when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
			when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

			parkingService.processExitingVehicle();
// verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");

		}
	}

	@Test
	public void FivePercentsDiscountForRecurentUsersCarTest() {
		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Date outTime = new Date();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR * 0.95);
	}

	@Test
	public void FivePercentsDiscountForRecurentUsersBikeTest() {
		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		Date outTime = new Date();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR * 0.95);
	}
}