package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());

		}

		long inHour = ticket.getInTime().getTime();
		long outHour = ticket.getOutTime().getTime();

		// TODO: Some tests are failing here. Need to check if this logic is
		// correct

		long duration = outHour - inHour;

		if (duration <= (30 * 60 * 1000)) { // Don't pay when less than 30 mins
			System.out.println("Your vehicle have been parked for " + duration / (60 * 1000)
					+ " minutes which benefit from the 30 mins free parking time.");
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice(Fare.CAR_RATE_PER_HOUR * 0);
				break;

			}

			case BIKE: {
				ticket.setPrice(Fare.BIKE_RATE_PER_HOUR * 0);
				break;

			}

			}

		} else {
			if (duration > (30 * 60 * 1000)) {
				System.out.println("Your vehicle have been parked for " + duration / (60 * 1000) + " minutes.");
				switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice(Fare.CAR_RATE_PER_HOUR * duration / (60 * 60 * 1000));
					break;

				}
				case BIKE: {
					ticket.setPrice(Fare.BIKE_RATE_PER_HOUR * duration / (60 * 60 * 1000));
					break;

				}
				default:
					throw new IllegalArgumentException("Unknown Parking Type");

				}

			}

		}
	}
}
// 5% decrease if already been there
