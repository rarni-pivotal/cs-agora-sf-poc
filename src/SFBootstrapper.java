import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;

import com.vmware.sqlfire.FabricLocator;
import com.vmware.sqlfire.FabricServer;
import com.vmware.sqlfire.FabricServiceManager;
import com.vmware.sqlfire.jdbc.EmbeddedDriver;

import asg.cliche.Command;
import asg.cliche.ShellFactory;


public class SFBootstrapper {
	
	boolean embeded = true;
	int nThreads = 1;
	int numRecords = 100000;
	String host = "pivhdsne";
	String locator = "pivhdsne";
	String type="sample";
	int multiplier = 1;
	int port = 1528;
	int locatorPort = 56788;
	static String version="3.4";
	static int init = 1;
	Properties sprops = new Properties();
	Properties props = new Properties();
	//Version 2: sample insert, insert into Fillallocations
	//Version 3: Added multiplier, insert into all 3 tables. Customize locator and port
	//Version 3.1: Added props file, increased thread count.
	//version 3.2: Preliminary update support
	//version 3.3: Multiple update support
	//version 3.4: Embeded and client connections + update count  check
	//Version 4 (planned): Standard deviation for inserts
	
	
	@SuppressWarnings("serial")
	@Command(name="startserver", abbrev="s", description="Start SQLFire Server")
	public  void createServer()  {	
		
		
		System.out.println("Starting server on "+this.host+":"+this.port);

		FabricServer server = FabricServiceManager.getFabricServerInstance();
		try {
			server.start(sprops);
			server.startNetworkServer(host, this.port, sprops);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Command(name="createsampletables", abbrev="cst")
	public void createTables() throws SQLException {
		
		Properties connProps = new Properties();

		Connection conn = DriverManager.getConnection(getConnURL(), connProps);
		conn.setAutoCommit(true);

		// create a table using this connection
		Statement st = conn.createStatement();
		st.executeUpdate("create table store (id int, city varchar(80), state char(2))");
		
	}
	
	@Command(name="loadprops", abbrev="lp")
	public void loadProps() throws SQLException {
		
		try {
			sprops.load(new FileInputStream("sf.properties"));
			props.load(new FileInputStream("cs.properties"));
		} catch (FileNotFoundException e1) {
			System.out.println("Did not find properties file: "+e1.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		if(props!=null) {
			if(props.getProperty("host")!=null) this.host = props.getProperty("host");
			if(props.getProperty("locator")!=null) this.locator = props.getProperty("locator");
			if(props.getProperty("port")!=null) this.port = Integer.parseInt(props.getProperty("port"));
			if(props.getProperty("locatorPort")!=null) this.locatorPort = Integer.parseInt(props.getProperty("locatorPort"));
			if(props.getProperty("embeded")!=null) this.embeded = Boolean.parseBoolean(props.getProperty("port"));
		}
		
		sprops.setProperty("locators", this.locator+"[56788]");
		sprops.setProperty("mcast-port", "0");
		
	}
	
	
	@Command(name="getmetadata", abbrev="getmeta")
	public void getMetadata() throws SQLException, IOException {
		
		Properties connProps = new Properties();
		
		String s;
		Connection con;
		Statement stmt;
		ResultSet rs;
		ResultSetMetaData rsmtadta;
		int colCount;
		int mtadtaint;
		int i;
		String colName;
		String colType;
		StringBuilder insertO_sb = new StringBuilder(); 
		File file = new File("code.txt");
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		insertO_sb.append("insert into Orders (");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		

		Connection conn = DriverManager.getConnection(getConnURL(), connProps);
		conn.setAutoCommit(true);

		stmt = conn.createStatement();    
		rs = stmt.executeQuery("SELECT * FROM orders where 0=1"); 
		                                  
		rsmtadta = rs.getMetaData();      
		colCount = rsmtadta.getColumnCount(); 
		
		                                  
		for (i=1; i<= colCount; i++) {                                 
			 colName = rsmtadta.getColumnName(i);   
			 colType = rsmtadta.getColumnTypeName(i);
			                                       
			 insertO_sb.append(colName+", ");
			 if(colType.equalsIgnoreCase("VARCHAR")){
				 bw.write("//"+colName+" VARCHAR");
				 bw.newLine();
				 bw.write("psO.setString("+i+", \"I\");");
				 bw.newLine();
			 
			} else if (colType.equalsIgnoreCase("INTEGER")) {
				bw.write("//"+colName+" INTEGER");
				 bw.newLine();
				 bw.write("psO.setInt("+i+", 1);");
				 bw.newLine();
				
			} else if (colType.equalsIgnoreCase("NUMERIC")) {
				bw.write("//"+colName+" NUMERIC");
				 bw.newLine();
				 bw.write("psO.setBigDecimal("+i+", new BigDecimal(10.1));");
				 bw.newLine();
				
			}  else if (colType.equalsIgnoreCase("DATE") || (colType.equalsIgnoreCase("TIMESTAMP"))) {
				bw.write("//"+colName+" DATE/TIMESTAMP");
				 bw.newLine();
				 bw.write("psO.setTimestamp("+i+", myDate);");
				 bw.newLine();
				
			} else {
				bw.write("This line will not compile. Unupported type = "+colName+" Name = "+colType);
				bw.newLine();
			}
			 
			 
		}
		 
		insertO_sb.append(") VALUES (");
		 
		 for(int j = 0; j<colCount; j++) {
			 insertO_sb.append("?, ");
		 }
		 insertO_sb.append(")");
		 bw.write(insertO_sb.toString());
		 bw.newLine();
		
		bw.close();
		 
		
		
	}
	
	@Command(name="getmetadata", abbrev="getmetau")
	public void getMetadataU(String table) throws SQLException, IOException {
		Properties connProps = new Properties();
		
		String s;
		Connection con;
		Statement stmt;
		ResultSet rs;
		ResultSetMetaData rsmtadta;
		int colCount;
		int mtadtaint;
		int i;
		String colName;
		String colType;
		StringBuilder updateO_sb = new StringBuilder(); 
		File file = new File("codeu.txt");
		
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		updateO_sb.append("update "+table+" set ");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		

		Connection conn = DriverManager.getConnection(getConnURL(), connProps);
		conn.setAutoCommit(true);

		stmt = conn.createStatement();    
		rs = stmt.executeQuery("SELECT * FROM "+table+" where 0=1"); 
		                                  
		rsmtadta = rs.getMetaData();      
		colCount = rsmtadta.getColumnCount(); 
		
		int j=0;                                 
		for (i=1; i<= colCount; i++) {                                 
			 colName = rsmtadta.getColumnName(i);   
			 colType = rsmtadta.getColumnTypeName(i);
			 if(rsmtadta.isNullable(i) == 0) {continue;}
			                                       	
			 j++;
			 if(colType.equalsIgnoreCase("VARCHAR")){
				 bw.write("//"+colName+" VARCHAR");
				 bw.newLine();
				 bw.write("psO.setString("+j+", \"U\");");
				 bw.newLine();
				 
			} else if (colType.equalsIgnoreCase("INTEGER")) {
				bw.write("//"+colName+" INTEGER");
				 bw.newLine();
				 bw.write("psO.setInt("+j+", 2);");
				 bw.newLine();
				
			} else if (colType.equalsIgnoreCase("NUMERIC")) {
				bw.write("//"+colName+" NUMERIC");
				 bw.newLine();
				 bw.write("psO.setBigDecimal("+j+", new BigDecimal(11.1));");
				 bw.newLine();
				
			}  else if (colType.equalsIgnoreCase("DATE") || (colType.equalsIgnoreCase("TIMESTAMP"))) {
				bw.write("//"+colName+" DATE/TIMESTAMP");
			
				bw.newLine();
				 bw.write("psO.setTimestamp("+j+", myDate);");
				 bw.newLine();
				
			} else {
				bw.write("This line will not compile. Unupported type = "+colName+" Name = "+colType);
				bw.newLine();
			}
			 
			 updateO_sb.append(colName+"=?, ");	 
		}
		
		bw.write(updateO_sb.toString());
		 bw.newLine();
		
		bw.close();
		 
		
		
	}
	
	@Command(name="createcstables", abbrev="ccst")
	public void createCSTables() throws SQLException, IOException {
		
		Properties connProps = new Properties();

		Connection conn = DriverManager.getConnection(getConnURL(), connProps);
		ScriptRunner sr = new ScriptRunner(conn, true, true);		
		FileReader file = new FileReader("sf_create_works.sql");
		Reader reader = new BufferedReader(file);		
		sr.runScript(reader);
		
	}
	
	@Command(name="getCount", abbrev="gc")
	public void getCount() {
		
		Properties connProps = new Properties();

		try {
		Connection conn = DriverManager.getConnection(getConnURL(), connProps);

		// create a table using this connection
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select count(*) from store");
		while(rs.next()){
	         //Retrieve by column name
	         System.out.println("Rows in Store: "+rs.getInt(1));
		}
		
		rs = st.executeQuery("select count(*) from orders");
		while(rs.next()){
	         //Retrieve by column name
	         System.out.println("Rows in Orders: "+rs.getInt(1));
		}
		
		rs = st.executeQuery("select count(*) from FillDetails");
		while(rs.next()){
	         //Retrieve by column name
	         System.out.println("Rows in FillDetails: "+rs.getInt(1));
		}
		
		rs = st.executeQuery("select count(*) from FillAllocations");
		while(rs.next()){
	         //Retrieve by column name
	         System.out.println("Rows in FillAllocations: "+rs.getInt(1));
		}
		
		} catch (SQLException se) {
			//Ignore
		}
	}
	
	@Command(name="getInfo", abbrev="gi")
	public void getInfo() {
		
		
		System.out.println("Avail Procs = "+Runtime.getRuntime().availableProcessors());
		System.out.println("Threads "+this.nThreads);
		System.out.println("Records per Thread "+this.numRecords);
		System.out.println("Type of insert "+this.type);
		System.out.println("Multiplier set to "+this.multiplier);
		System.out.println("Locator set to "+this.locator);
		System.out.println("Host set to "+this.host);
		String conType = embeded?"embeded":"client server";
		System.out.println("Connection type set to " + conType);
		System.out.println("Runcount is currently " + (init - 1));
		
	}
	
	@Command(name="setThreads", abbrev="st")
	public void setThreads(int t) {
		
		this.nThreads = t;		
		
	}
	
	@Command(name="setHost", abbrev="sh")
	public void setHost(String t) {
		
		this.host = t;		
		
	}
	
	@Command(name="setPort", abbrev="sp")
	public void setPort(int p) {
		
		this.port = p;		
		
	}
	
	@Command(name="setLocator", abbrev="sl")
	public void setLocator(String t) {
		
		this.locator = t;		
		
	}
	
	@Command(name="setMultiplier", abbrev="sm")
	public void setMultipler(int m) {
		
		this.multiplier = m;		
		
	}
	
	@Command(name="setRecods", abbrev="sr", description="Set number of records to insert per thread")
	public void setRecods(int t) {
		
		this.numRecords = t;		
		
	}

	@Command(name="insertdata", abbrev="i")
	public void insertData(String type) throws SQLException, InterruptedException {
		
		String[] tPrefix = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
				"AA","BB","CC", "DD","EE", "FF", "GG", "HH", "II", "JJ", "KK", "LL", "MM", "NN", "OO", "PP", "QQ", "RR", "SS", "TT", "UU", "VV", "WW", "XX", "YY", "ZZ",
				"AAA","BBB","CCC", "DDD","EEE", "FFF", "GGG", "HHH", "III", "JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR", "SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZZZ",
				"AAAA","BBBB","CCCC", "DDDD","EEEE", "FFFF", "GGGG", "HHHH", "IIII", "JJJJ", "KKKK", "LLLL", "MMMM", "NNNN", "OOOO", "PPPP", "QQQQ", "RRRR", "SSSS", "TTTT", "UUUU", "VVVV", "WWWW", "XXXX", "YYYY", "ZZZZ"
		};
		
		Thread [] theThreads = new Thread[nThreads];
		double startTime = System.currentTimeMillis();
		
		for (int i=0;i<theThreads.length;i++) {
			  if(type.equalsIgnoreCase("sample")) {
			  theThreads[i] = new Thread(new SampleDataInserter(init, numRecords, tPrefix[i], getConnURL()));
			  } else if (type.equalsIgnoreCase("cs2")) {
				  theThreads[i] = new Thread(new CSDataInserter2(init, numRecords, multiplier,tPrefix[i], getConnURL() ));	  
			  
			  } else if (type.equalsIgnoreCase("cs1")) {
				  theThreads[i] = new Thread(new CSDataInserter(init, numRecords, multiplier,tPrefix[i], getConnURL() ));	  
			  } else {
				  
				  System.out.println("Choose either sample or cs1 ");
			  }
			  theThreads[i].setName("CS-CF-"+i);
			  theThreads[i].start();
			}
		
		for (int i=0;i<theThreads.length;i++) {
			  theThreads[i].join();
			}
		
		init++;
	
		double endTime   = System.currentTimeMillis();
		double timeLapsed = endTime - startTime;
		NumberFormat nf = new DecimalFormat("###.###");
		double finalNumrecords = nThreads*(numRecords+2*multiplier*numRecords);
		double tput = (finalNumrecords/timeLapsed)*1000.0;
		
		System.out.println("Inserted total "+nf.format(finalNumrecords)+" records across 3 tables using multiplier "+ multiplier+", over "+nThreads+" threads in "+ nf.format(timeLapsed)+ " milliseconds with throughput = "+nf.format(tput)+" per second");
		
		
	}
	
	
	
	@Command(name="insertupdatedata", abbrev="iu")
	public void insertupdateData() throws SQLException, InterruptedException {
		String[] tPrefix = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
				"AA","BB","CC", "DD","EE", "FF", "GG", "HH", "II", "JJ", "KK", "LL", "MM", "NN", "OO", "PP", "QQ", "RR", "SS", "TT", "UU", "VV", "WW", "XX", "YY", "ZZ",
				"AAA","BBB","CCC", "DDD","EEE", "FFF", "GGG", "HHH", "III", "JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR", "SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZZZ",
				"AAAA","BBBB","CCCC", "DDDD","EEEE", "FFFF", "GGGG", "HHHH", "IIII", "JJJJ", "KKKK", "LLLL", "MMMM", "NNNN", "OOOO", "PPPP", "QQQQ", "RRRR", "SSSS", "TTTT", "UUUU", "VVVV", "WWWW", "XXXX", "YYYY", "ZZZZ"
		};
		
		Thread [][] theThreads = new Thread[nThreads][2];
		double startTime = System.currentTimeMillis();
		
		for (int i=0;i<theThreads.length;i++) {
			
			  theThreads[i][0] = new Thread(new CSDataInserter2(init, numRecords, multiplier,tPrefix[i], getConnURL() ));
			  theThreads[i][0].setName("CS-I-"+i);
			  theThreads[i][0].start();
			  // updates are done against the previous run's records, starting with the second run
			  if(init > 1){

				  theThreads[i][1] = new Thread(new CSDataUpdater(init -1, numRecords, multiplier,tPrefix[i], getConnURL() ));
				  theThreads[i][1].setName("CS-U-"+i);
				  theThreads[i][1].start();
				
			  }
			}
		
		for (int i=0;i<theThreads.length;i++) {
			  theThreads[i][0].join();
			  if(init > 1)
				  theThreads[i][1].join();
			}
		
		
	
		double endTime   = System.currentTimeMillis();
		double timeLapsed = endTime - startTime;
		NumberFormat nf = new DecimalFormat("###.###");
		double finalNumrecords = nThreads*(numRecords+2*multiplier*numRecords);
		double tput = (finalNumrecords/timeLapsed)*1000;
		
		System.out.println("Inserted total "+nf.format(finalNumrecords)+" records across 3 tables using multiplier "+ multiplier+", over "+nThreads+" threads in "+ nf.format(timeLapsed)+ " milliseconds with throughput = "+nf.format(tput)+" per second");
		finalNumrecords = getUpdateCount(init - 1);
		tput = (finalNumrecords/timeLapsed)*1000.0;
		System.out.println("Updated total "+nf.format(finalNumrecords)+" records across 3 tables using multiplier "+ multiplier+", over "+nThreads+" threads in "+ nf.format(timeLapsed)+ " milliseconds with throughput = "+nf.format(tput)+" per second");
		
		init++;
		
		
	}
	

	
	@Command(name="updatedata", abbrev="u")
	public void updateDatain(int init) throws SQLException, InterruptedException {
		String[] tPrefix = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
				"AA","BB","CC", "DD","EE", "FF", "GG", "HH", "II", "JJ", "KK", "LL", "MM", "NN", "OO", "PP", "QQ", "RR", "SS", "TT", "UU", "VV", "WW", "XX", "YY", "ZZ",
				"AAA","BBB","CCC", "DDD","EEE", "FFF", "GGG", "HHH", "III", "JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR", "SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZZZ",
				"AAAA","BBBB","CCCC", "DDDD","EEEE", "FFFF", "GGGG", "HHHH", "IIII", "JJJJ", "KKKK", "LLLL", "MMMM", "NNNN", "OOOO", "PPPP", "QQQQ", "RRRR", "SSSS", "TTTT", "UUUU", "VVVV", "WWWW", "XXXX", "YYYY", "ZZZZ"
		};
		
		Thread [] theThreads = new Thread[nThreads];
	
		
		double startTime = System.currentTimeMillis();
		
		for (int i=0;i<theThreads.length;i++) {
			
			theThreads[i] = new Thread(new CSDataUpdater(init, numRecords, multiplier,tPrefix[i], getConnURL() ));
			  theThreads[i].setName("CS-U-"+i);
			  theThreads[i].start();
			}
		
		for (int i=0;i<theThreads.length;i++) {
			  theThreads[i].join();
			}
		
		double endTime   = System.currentTimeMillis();
		
		//updated number of records = 
		
		double timeLapsed = endTime - startTime;
		//long finalNumrecords = nThreads*(numRecords+2*multiplier*numRecords);
		double finalNumrecords = getUpdateCount(init);
		NumberFormat nf = new DecimalFormat("###.###");;
		double tput = (finalNumrecords/timeLapsed)*1000.0;
		System.out.println("Updated total "+nf.format(finalNumrecords)+" records across 3 tables using multiplier "+ multiplier+", over "+nThreads+" threads in "+ nf.format(timeLapsed)+ " milliseconds with throughput = "+nf.format(tput)+" per second");
		
		
		
	}
	public String getConnURL() {
		// TODO Auto-generated method stub
		return embeded?"jdbc:sqlfire:":"jdbc:sqlfire://"+this.host+":"+this.locatorPort+"/";
		
	}
	public int getUpdateCount(int init){
		Connection conn;
		int count = 0;
		try {
			conn = DriverManager.getConnection(getConnURL(), new Properties());
			conn.setAutoCommit(true);
			Statement stmt = conn.createStatement();    
			ResultSet rs;
			//rs = stmt.executeQuery("select count(FILLDETAILS.ACTREPORT), count(ORDERS.ACTREPORTINFO2), count(FILLALLOCATIONS.ACTREPORTINFO1) from FILLDETAILS, ORDERS, FILLALLOCATIONS where ORDERS.OrderID=FILLDETAILS.EXECUTIONID and ORDERS.ORDERID=FILLALLOCATIONS.FILLID and FILLDETAILS.ACTREPORT='U"+init+"' and ORDERS.ACTREPORTINFO2='U"+init+"' and FILLALLOCATIONS.ACTREPORTINFO1='U"+init+"' ;");

			rs = stmt.executeQuery("select count(FILLALLOCATIONS.ACTREPORTINFO1) from FILLALLOCATIONS where FILLALLOCATIONS.ACTREPORTINFO1='U"+init+"' ;");
			while(rs.next()){
		         //Retrieve by column name
				count = count + rs.getInt(1);
			}
			rs = stmt.executeQuery("select count(ORDERS.ACTREPORTINFO2) from ORDERS where ORDERS.ACTREPORTINFO2='U"+init+"' ;");
			while(rs.next()){
		         //Retrieve by column name
				count = count + rs.getInt(1);
			}
			rs = stmt.executeQuery("select count(FILLDETAILS.ACTREPORT) from FILLDETAILS where FILLDETAILS.ACTREPORT='U"+init+"'  ;");
			while(rs.next()){
		         //Retrieve by column name
				count = count + rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Credit Suisse SQLFire POC Test Harness Version "+version);
		System.out.println("Run sh and sl or lp to set host & listener first before starting server (s). Use ?list to see available commands");
		ShellFactory.createConsoleShell("Agora-SFBootStrap-POC", "", new SFBootstrapper())
        .commandLoop(); // and three.

	}
}

class SampleDataInserter implements Runnable{
	
	int start=0;
	int numRecords = 100000;
	String type = null;
	String connURL;
	public SampleDataInserter(Object init, int nRecords, String prefix, String connURL) {
	       start = ((Integer)init).intValue();
	       numRecords = ((Integer)nRecords).intValue();
	       this.connURL = connURL;
	   }
	
	
	@Override
	 public void run() {//thread starts from here

			Properties connProps = new Properties();
			Connection conn;
			try {
				conn = DriverManager.getConnection(connURL, connProps);
				conn.setAutoCommit(true);
				String insertSQL = "insert into store values (?, ?, ?)";
				PreparedStatement ps;
				ps = conn.prepareStatement(insertSQL);
				
				ps.setString(2, "Marlboro");
				ps.setString(3, "NJ");
				for (int i=0;i<numRecords;i++){
					ps.setInt(1, i);	
					ps.executeUpdate();
				}
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	 }
	}
