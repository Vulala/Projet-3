package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

//@ExtendWith(MockitoExtension.class)
public class FivePercentDiscountForRecurentUsersTest {

	// Test the 5% discount feature

	// private static ParkingService parkingService;
	private static FareCalculatorService fareCalculatorService;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	/**
		@Mock
		private static InputReaderUtil inputReaderUtil;
		@Mock
		private static ParkingSpotDAO parkingSpotDAO;
		@Mock
		private static TicketDAO ticketDAO;
	
		@BeforeEach
		private void SetUpCarTest() {
		
			// BeforeEach used to put a record in the DB before the test. To test
			// the current user. Might be pointless for a Unit Test(?) edit: Yes it is.
		
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
		
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Failed to set up test mock objects");
		
			}
		}*/

	@Test
	public void GivenARecurentCarUser_WhenCalculateFare_ThenThePriceShouldBe5PercentLess() {
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
	public void GivenARecurentBikeUser_WhenCalculateFare_ThenThePriceShouldBe5PercentLess() {
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
