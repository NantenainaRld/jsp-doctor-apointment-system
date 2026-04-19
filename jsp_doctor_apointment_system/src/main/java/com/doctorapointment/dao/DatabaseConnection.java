package com.doctorapointment.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.util.EnvLoader;

public class DatabaseConnection {
	// logger
	private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);

	// read mysql driver
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			log.info("Mysql JDBC Driver registered successfully");
		}
		catch(ClassNotFoundException e) {
			log.error("MySQL JDBC driver not found");
		}
	}

	// get connection
	public static Connection getConnection() throws SQLException{
		// mysql config
		String url = EnvLoader.get("DB_URL");
		String user = EnvLoader.get("DB_USER");
		String password  = EnvLoader.get("DB_PASSWORD");

		// credentials check
		if(url == null || user == null || password == null) {
			log.error("Database credentials missing in .env file");
			throw new SQLException("DB_URL or DB_USER or DB_PASSWORD not configured");
		}

		log.debug("Connection to database : '{}'", url);
		return DriverManager.getConnection(url, user, password);
	}
}
