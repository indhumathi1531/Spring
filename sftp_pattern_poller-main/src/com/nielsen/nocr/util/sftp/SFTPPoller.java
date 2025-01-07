package com.nielsen.nocr.util.sftp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.nielsenmedia.foundations.nce.connectionpool.SQLProcessorConnectionManager;
import com.nielsenmedia.foundations.util.configuration.ConfigurationMgr;

class SFTPPollerThread extends Thread {
	private static Logger log = Logger.getLogger(SFTPPollerThread.class);
	private int patternID;
	private long sleepInterval;

    public SFTPPollerThread(int patternID,int sleepInterval) {
        this.patternID = patternID;
        this.sleepInterval = sleepInterval;
    }
    
	public void run() 
    {
		while(true) {
        try {
        	System.out.println("PatternID "+patternID+" started polling");
        	log.info("PatternID "+patternID+" started polling");
        	UtilProcessor.getFiles(patternID);
			System.out.println("PatternID : "+patternID+" polling completed successfully");
			log.info("PatternID : "+patternID+" polling completed successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}7
        
    }
  }
}


public class SFTPPoller {
	private static Logger log = Logger.getLogger(SFTPPoller.class);
	
	public static Connection connect() throws Exception {
	    try{	     
	    	String url = ConfigurationMgr.getProperty("DATABASE.FNDTNS.URL");
			String user = ConfigurationMgr.getProperty("DATABASE.FNDTNS.USER");
			String password = ConfigurationMgr.getProperty("DATABASE.FNDTNS.PASSWORD");
			String driver = ConfigurationMgr.getProperty("DATABASE.FNDTNS.DRIVERNAME");
			Class.forName(driver).newInstance();
			return DriverManager.getConnection(url, user, password);
	     }catch (Exception e) {
			throw new Exception("DB Connection Failed! ", e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		String loadConfigFileName = args[0];
		String sleepIntervalTime=args[1];
		int sleepInterval=Integer.parseInt(sleepIntervalTime);
		connectionPoolSetup(loadConfigFileName);
		Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
		String patternIDSQL="SELECT distinct SFTP_PATTERN_ID FROM SFTP_CONTROL WHERE current_date BETWEEN EFFECTIVE_START_DATE and EFFECTIVE_END_DATE ORDER BY SFTP_PATTERN_ID ASC";
		log.info("SQL........" + patternIDSQL);
		try {	
			conn = connect();
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery(patternIDSQL);
	        while(rs.next()) { 
	        	int patternID = rs.getInt("SFTP_PATTERN_ID");
	        	SFTPPollerThread object= new SFTPPollerThread(patternID,sleepInterval);
	        	object.start();	
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;		
	  }
		finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }    
	}
	
	private static void connectionPoolSetup(String loadConfigFileName) throws Exception {
		String connectionPoolingClassName = "com.nielsenmedia.foundations.nce.connectionpool.SQLProcessorConnectionManager";
		String connectionPoolingMethodName = "createConnectionPoolManager";

		if (loadConfigFileName != null) {
			ConfigurationMgr.loadConfigurationFrom(loadConfigFileName);
			System.out.println("Loaded Config file as loadConfigFileName is " + loadConfigFileName);
		} else {
			System.out.println("No need to load Config file as loadConfigFileName is " + loadConfigFileName);
		}

		if ((connectionPoolingClassName != null) && (connectionPoolingMethodName != null)) {
			SQLProcessorConnectionManager.createConnectionPoolManager();
			System.out.println("Connection pool created using connectionPoolingClassName: " + connectionPoolingClassName + " connectionPoolingMethodName: " + connectionPoolingMethodName);
		} else {
			System.out.println("No need to invoke poolCreator as connectionPoolingClassName: " + connectionPoolingClassName + " connectionPoolingMethodName: " + connectionPoolingMethodName);
		}
  }

}