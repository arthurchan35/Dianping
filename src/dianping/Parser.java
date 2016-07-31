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

/*
 * Parser class takes file name as argument and parse the content data
 * into UserRecord object, and insert userRecord into oracle database.
 * 
 * */
public class Parser {
	/* the input stream for data file */
	private FileInputStream in;
	/* sqlconnector takes care of connecting to oracle database */
	private SQLConnector connector;
	/* parser constructor, initializing class members */
	public Parser(String fileDir) throws FileNotFoundException {
		in = new FileInputStream(fileDir);	
		connector = SQLConnector.getInstance();
	}
	/* takes one line from data file and parse the string into userRecord */	
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
	/* takes userRecord object and insert into database */
	private void insertToSQL(UserRecord ur) throws SQLException {
		if (ur == null) return;
		Connection con = connector.getConnection();
		PreparedStatement updateUR = null;
		PreparedStatement updateUO = null;
		
		String urs = "insert into lnd values(?, ?, ?, ?) "; 
		String uos = "insert into other values(?, ?)";		
		try {
			con.setAutoCommit(false);
			updateUR = con.prepareStatement(urs);			
			updateUO = con.prepareStatement(uos);
			for (int i = 0; i < ur.getDLPairsSize(); ++i) {

				UserRecord.DLPair dlp = ur.getDLPair(i);
				java.util.Date utilDate = dlp.getDate();
				java.sql.Timestamp ts = new java.sql.Timestamp(utilDate.getTime());
				updateUR.setInt(1, ur.getUserID());
				updateUR.setTimestamp(2, ts);
				updateUR.setDouble(3, dlp.getLatitude());
				updateUR.setDouble(4, dlp.getLongitude());
				updateUR.executeUpdate();
				con.commit();
			}
			
			for (int i = 0; i < ur.getOtherInfosSize(); ++i) {

				String otherinfo = ur.getOtherInfo(i);
				if (otherinfo == null || otherinfo.equals(""))  continue;
				updateUO.setInt(1, ur.getUserID());
				updateUO.setString(2, otherinfo);
				updateUO.executeUpdate();
				con.commit();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			if (updateUR != null) updateUR.close();
			if (updateUO != null) updateUO.close();
			con.setAutoCommit(true);
		}
	}
	/* public API of this program, which starts the parsing process */
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
