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

public class SQLConnector {
	private static SQLConnector connector;
	private Connection connection;
	
	private Properties readProperties() throws IOException {
		Properties props = new Properties();
		
		FileInputStream in = new FileInputStream("Config.properties");
		props.load(in);
		in.close();
		return props;
	}
	
	private SQLConnector() throws IOException, SQLException {
		Properties props = readProperties();
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null) System.setProperty("jdbc.drivers", drivers);
/*		try {
			Class.forName (drivers);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String url = props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");

		connection = DriverManager.getConnection( url, username, password);
	}
	
	public static SQLConnector getInstance() {
		if (connector == null)
			try {
				connector = new SQLConnector();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return connector;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
