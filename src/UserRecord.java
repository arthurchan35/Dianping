package dianping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserRecord {
	
	class DLPair {
		Date date;
		double latitude;
		double longitude;
		public DLPair(Date d, double la, double lo) {
			date = d;
			latitude = la;
			longitude = lo;
		}
	}
	
	int userid;
	ArrayList<DLPair> DLPairs;
	ArrayList<String> otherInfos;
	
	public UserRecord(int id) {
		userid = id;
	}
	
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
	    } catch (ParseException e) {
	    	otherInfos.add(linePart);
	    }
	}
	
}
