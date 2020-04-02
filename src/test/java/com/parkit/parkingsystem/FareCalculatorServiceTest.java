package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

	// Test the {@link FareCalculatorService}

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
	public void GivenACarParkedFor60Minutes_WhenCalculateFare_ThenThePriceFollowTheRateFare() {
		// Car parked for 60 minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	public void GivenABikeParkedFor60Minutes_WhenCalculateFare_ThenThePriceFollowTheRateFare() {
		// Bike parked for 60 minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	public void GivenAWrongInput_WhenCalculateFare_ThenItReturnANullPointerException() {
		// Wrong vehicle input
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber()));
	}

	@Test()
	public void GivenAFutureTimeForCar_WhenCalculateFare_ThenItReturnAnIllegalArgumentException() {
		// Future time return IllegalArgumentException
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber()));
	}

	@Test
	public void GivenAFutureTimeForBike_WhenCalculateFare_ThenItReturnAnIllegalArgumentException() {
		// Future time return IllegalArgumentException
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber()));
	}

	@Test
	public void GivenACarParkedForLessThan60Minutes_WhenCalculateFare_ThenThePriceIsProportional() {
		// Less than one hour in the parking give less than the price for one
		// hour, in a proportional way
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void GivenABikeParkedForLessThan60Minutes_WhenCalculateFare_ThenThePriceIsProportional() {
		// Parked for less than one hour in the parking give less than the price
		// for one hour, in a proportional way
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void GivenACarParkedForMoreThan1Day_WhenCalculateFare_ThenThePriceIsProportional() {
		// Parked for more than one day in the parking give a proportional
		// increase of the price
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void GivenABikeParkedForMoreThan1Day_WhenCalculateFare_ThenThePriceIsProportional() {
		// Parked for more than one day in the parking give a proportional
		// increase of the price
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber());
		assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void GivenANullInput_WhenCalculateFare_ThenItReturnANullPointerException() {
		// Return NullPointerException when a null vehicle type is set as input
		ParkingType vehicle = null;
		ParkingSpot parkingSpot = new ParkingSpot(1, vehicle, false);

		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber()));
	}

	@Test
	public void GivenAnIncorectOutTime_WhenCalculateFare_ThenItReturnAnIllegalArgumentException() {
		// Return IllegalArgumentException when the outTime is not correct
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 1000));
		Date outTime = new Date();
		outTime.setTime(0);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateFare(ticket, ticket.getVehicleRegNumber()));
	}
}
