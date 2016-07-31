/*
 * Author: Zheng(Arthur) Chen arthurchan35@gmail.com
 * Testing: Liming Sun
 * */

package dianping;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/* a wrapper class for connection of database, singleton object provided */
public class SQLConnector {
	/* singleton object */
	private static SQLConnector connector;
	private Connection connection;
	/* read database configuration from property file */
	private Properties readProperties() throws IOException {
		Properties props = new Properties();
		
		FileInputStream in = new FileInputStream("Config.properties");
		props.load(in);
		in.close();
		return props;
	}
	/* private constructor, initializing class members */
	private SQLConnector() throws IOException, SQLException {
		Properties props = readProperties();
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null) System.setProperty("jdbc.drivers", drivers);
		String url = props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");

		connection = DriverManager.getConnection( url, username, password);
	}
	/* get singleton */
	public static SQLConnector getInstance() {
		if (connector == null)
			try {
				connector = new SQLConnector();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return connector;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
