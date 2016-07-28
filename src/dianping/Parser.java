/*
 * Author: Zheng(Arthur) Chen arthurchan35@gmail.com
 * Testing: Liming Sun
 * */
package dianping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Parser {

	private FileInputStream in;

	private SQLConnector connector;
	
	public Parser(String fileDir) throws FileNotFoundException {
		in = new FileInputStream(fileDir);	
		connector = SQLConnector.getInstance();
	}
		
	private UserRecord parserUser(String line) {
		if (line.equals("")) return null;
		String parts[] = line.split("\\|");
		int id = -1;
		try {
			id = Integer.parseInt(parts[0].split("\t")[0]);
		} catch (NumberFormatException nfe) {
			return null;
		}
		UserRecord ur = new UserRecord(id);
		
		for (int i = 1; i < parts.length; ++i)
			ur.addPair(parts[i]);
		return ur;
	}
	
	private void insertToSQL(UserRecord ur) throws SQLException {
		if (ur == null) return;
		Connection con = connector.getConnection();
		PreparedStatement updateUR = null;
		PreparedStatement updateUO = null;
		
		String urs = "insert into lnd values(?, ?, ?, ?) "; 
		String uos = "insert into other values(?, ?)";		
		
		con.setAutoCommit(false);
		updateUR = con.prepareStatement(urs);
		updateUO = con.prepareStatement(uos);
		
		for (int i = 0; i < ur.getDLPairsSize(); ++i) {
			UserRecord.DLPair dlp = ur.getDLPair(i);
			java.util.Date utilDate = dlp.getDate();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			//java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime() - sqlDate.getTime());
		    //System.out.println("utilDate:" + utilDate);
		    //System.out.println("sqlDate:" + sqlDate);
			updateUR.setInt(1, ur.getUserID());
			updateUR.setDate(2, sqlDate);
			updateUR.setDouble(3, dlp.getLatitude());
			updateUR.setDouble(4, dlp.getLongitude());
			updateUR.executeUpdate();
			con.commit();
		}
		
		for (int i = 0; i < ur.getOtherInfosSize(); ++i) {
			String otherinfo = ur.getOtherInfo(i);
			updateUO.setInt(1, ur.getUserID());
			updateUO.setString(2, otherinfo);
			updateUO.executeUpdate();
			con.commit();
		}
	}
	
	public void start() {
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine()) {
			UserRecord ur = parserUser(scanner.nextLine());
			try {
				insertToSQL(ur);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		scanner.close();
	}
	
	public static void main(String[] args) {
		try {
			Parser parser = new Parser(args[0]);
			parser.start();
		} catch (FileNotFoundException e) {
			System.out.println("unable to open file:"+ args[0]);
			e.printStackTrace();
		}
	}

}
