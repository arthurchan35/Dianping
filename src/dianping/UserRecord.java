/*
 * Author: Zheng(Arthur) Chen arthurchan35@gmail.com
 * Testing: Liming Sun
 * */
package dianping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* storage class for user record */
public class UserRecord {
	/* inner class presenting a pair of date and location */
	class DLPair {
		Date date;
		double latitude;
		double longitude;
		public DLPair(Date d, double la, double lo) {
			date = d;
			latitude = la;
			longitude = lo;
		}
		public Date getDate() {
			return date;
		}
		public double getLatitude() {
			return latitude;
		}
		public double getLongitude() {
			return longitude;
		}
	}
	
	int userid; // user id is per user record
	ArrayList<DLPair> DLPairs; // user record can have multiple pairs of date and location
	ArrayList<String> otherInfos; // user record can have multiple other information
	
	public UserRecord(int id) {
		DLPairs = new ArrayList<DLPair>();
		otherInfos = new ArrayList<String>();
		userid = id;
	}
	/* linePart is separated by | , each representing a pair of date and location, or other information. 
	 * if parsing successfully, it will be inserted into DLPairs, else insert into otherInfos */
	public void addPair(String linePart) {
		String timeAndLoc[] = linePart.split("]");
		String pattern = "yyyy-MM-dd KK:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
	    try {
	        String ll[] = timeAndLoc[1].split(",");
	        Date date = format.parse(timeAndLoc[0]);
	        double latitude = Double.parseDouble(ll[0]);
	        double longitude = Double.parseDouble(ll[1]);
	        DLPair dlp = new DLPair(date, latitude, longitude);
	        DLPairs.add(dlp);
	    } catch (Exception pe) {
	    	otherInfos.add(linePart);
	    }
	}
	
	public int getUserID() {
		return userid;
	}
	
	public DLPair getDLPair(int index) {
		return DLPairs.get(index);
	}
	
	public int getDLPairsSize() {
		return DLPairs.size();
	}
	
	public String getOtherInfo(int index) {
		return otherInfos.get(index);
	}
	
	public int getOtherInfosSize() {
		return otherInfos.size();
	}
	
}
