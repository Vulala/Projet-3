package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;

public class DataBasePrepareService {

	// Class used to clear the entries of the DataBase
	// Setting parking spot to available and clearing tickets.

	DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	public void clearDataBaseEntries() {
		Connection con = null;
		try {
			con = dataBaseTestConfig.getConnection();

			// set parking entries to available
			con.prepareStatement("update parking set available = true").execute();

			// clear ticket entries;
			con.prepareStatement("truncate table ticket").execute();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			dataBaseTestConfig.closeConnection(con);

		}
	}
}
