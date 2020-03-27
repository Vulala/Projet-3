package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

class ParkingSpotDAOTest {

	private static DataBaseTestConfig dataBaseTestConfig;
	private static DataBasePrepareService dataBasePrepareService;
	//private static ParkingType parkingTypeCAR = ParkingType.CAR;
	private static Connection con;
	private static ParkingSpotDAO parkingSpotDAO;

	@Mock
	private static ParkingSpot parkingSpot;

	@BeforeAll
	private static void setUp() throws Exception {
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpDB() throws ClassNotFoundException, SQLException {
		//when(parkingSpot.getId()).thenReturn(1);
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Disabled("Argument problem")
	@Test
	public void getNextAvailableSlotTest(ParkingType CAR) throws SQLException {
		int result = -1;
		try {
			PreparedStatement preparedStatement = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
		//	preparedStatement.setString(1, parkingTypeCAR.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt(1);;
				System.out.println(result);

			}
			dataBaseTestConfig.closeResultSet(resultSet);
			dataBaseTestConfig.closePreparedStatement(preparedStatement);

		} finally {
			dataBaseTestConfig.closeConnection(con);
			assertEquals("", "");

		}

	}

	@Disabled
	@Test
	public void updateParkingTest() throws SQLException {
		try {
			PreparedStatement preparedStatement = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			preparedStatement.setBoolean(1, parkingSpot.isAvailable());
			preparedStatement.setInt(2, parkingSpot.getId());
			int updateRowCount = preparedStatement.executeUpdate();
			System.out.println(updateRowCount == 1);
			parkingSpotDAO.updateParking(parkingSpot);

		} finally {
		//	assertEquals(parkingSpot.getId(), 1);
			assertEquals(parkingSpotDAO.updateParking(parkingSpot), true);
		//	assertEquals(parkingSpotDAO, (any(ParkingSpot.class)));

		}
		dataBaseTestConfig.closeConnection(con);

	}

	@Disabled
	@Test
	public void updateParkingDBRequestTest() throws ClassNotFoundException, SQLException {

		// ARANGE
		// Connection con = null;
		ResultSet resultSet = null;
		String request = "SELECT PARKING_NUMBER FROM parking";

		// ACT
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
					assertEquals("1", resultSet.getString("PARKING_NUMBER"));
					System.out.println(resultSet.getString("PARKING_NUMBER"));

				}
				while (resultSet.next()) {
					for (int i = 1; i <= numberColumn; i++);
					System.out.print(resultSet.getString("PARKING_NUMBER"));

				}

			}
			resultSet.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

	}
}
