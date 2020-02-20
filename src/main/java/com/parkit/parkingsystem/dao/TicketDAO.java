package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			// ps.setInt(1,ticket.getId());
			preparedStatement.setInt(1, ticket.getParkingSpot().getId());
			preparedStatement.setString(2, ticket.getVehicleRegNumber());
			preparedStatement.setDouble(3, ticket.getPrice());
			preparedStatement.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			preparedStatement.setTimestamp(5,
					(ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			return preparedStatement.execute();

		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);

		} finally {
			dataBaseConfig.closeConnection(con);
			return false;

		}
	}

	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(DBConstants.GET_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			preparedStatement.setString(1, vehicleRegNumber);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(resultSet.getInt(1),
						ParkingType.valueOf(resultSet.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(resultSet.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(resultSet.getDouble(3));
				ticket.setInTime(resultSet.getTimestamp(4));
				ticket.setOutTime(resultSet.getTimestamp(5));

			}
			dataBaseConfig.closeResultSet(resultSet);
			dataBaseConfig.closePreparedStatement(preparedStatement);

		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);

		} finally {
			dataBaseConfig.closeConnection(con);
			return ticket;

		}
	}

	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(DBConstants.UPDATE_TICKET);
			preparedStatement.setDouble(1, ticket.getPrice());
			preparedStatement.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			preparedStatement.setInt(3, ticket.getId());
			preparedStatement.execute();
			return true;

		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);

		} finally {
			dataBaseConfig.closeConnection(con);

		}
		return false;
	}
}
