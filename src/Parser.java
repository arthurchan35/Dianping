package dianping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

	private FileInputStream in;
	private Scanner scanner;
	
	
	public Parser(String fileDir) throws FileNotFoundException {
		in = new FileInputStream(fileDir);	
		scanner = new Scanner(in);
	}
		
	private UserRecord parserUser(String line) {
		int id = line[0];
		UserRecord ur = new UserRecord[id];
		
		while (line.hasNextPart()) {
			ur.addPair(line.nextPart());
		}
		return ur;
	}
	
	private void insertToSQL(UserRecord ur) {
		
	}
	
	public void start() {
		while (scanner.hasNextLine()) {
			UserRecord ur = parserUser(scanner.nextLine());
			insertToSQL(ur);
		}
	}
	
	public static void main(String[] args) {
		try {
			Parser parser = new Parser(args[1]);
			parser.start();
		} catch (FileNotFoundException e) {
			System.out.println("unable to open file:"+ args[1]);
			e.printStackTrace();
		}
	}

}
