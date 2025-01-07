package com.nielsen.nocr.util.sftp;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SftpConstants {
  public static final String FILE_TYPE_CODE = "FILE_TYPE_CODE";
  
  public static final String SERVER_NAME = "SERVER_NAME";
  
  public static final String USERNAME = "USERNAME";
  
  public static final String PASSWORD = "PASSWORD";
  
  public static final String FILE_PATTERN = "FILE_PATTERN";
  
  public static final String OUTPUT_FILENAME = "OUTPUT_FILENAME";
  
  public static final String UNZIP_REQ = "UNZIP_REQ";
  
  public static final String IS_TRIGGER_FILE = "IS_TRIGGER_FILE";
  
  public static final String TRIGGER_FILE_DELIMITER = "TRIGGER_FILE_DELIMITER";
  
  public static final String ZIP_FILE = "ZIP_FILE";
  
  public static final String SOURCE_FILE_LOCATION = "SOURCE_FILE_LOCATION";
  
  public static final String INTERIM_LOCATION = "INTERIM_LOCATION";
  
  public static final String DESTINATION_LOCATION = "DESTINATION_LOCATION";
  
  public static final String YES = "Y";
  
  public static final String PARAMETER_NAME = "PARAMETER_NAME";
  
  public static final String LOWER_POSITION = "LOWER_POSITION";
  
  public static final String UPPER_POSITION = "UPPER_POSITION";
  
  public static final String IS_INTEGER = "IS_INTEGER";
  
  public static final int sftpPort = 22;
  
  public static final String CONNECTION_TYPE = "sftp";
  
  public static final String LS_COMMAND_SEPERATOR = " ";
  
  public static final int COUNTS_FILE__TYPE_CODE = 1;
  
  public static final String LINE_SEPERATOR = "\n";
  
  public static final String WAIT_TIME = "FILE.SIZE.CHECK.WAIT.TIME";
  
  public static final String RETRY_COUNT = "FILE.SIZE.CHECK.RETRY.COUNT";
  
  public static final String SFTP_SOCKET_TIMEOUT = "SFTP.SOCKET.TIMEOUT";
  
  public static String getPatternDetails(int patternId) {
    StringBuffer sqlBuffer = new StringBuffer(700);
    sqlBuffer.append("SELECT \t\t\t\t\tSFTP_PATTERN_ID,\t\t\t\t\tFILE_TYPE_CODE, \t\t\t\t\tSERVER_NAME, \t\t\t\t\tUSERNAME, \t\t\t\t\tENC_DEC.decrypt(PASSWORD) as PASSWORD, \t\t\t\t\tFILE_PATTERN, \t\t\t\t\tOUTPUT_FILENAME, \t\t\t\t\tUNZIP_REQ, \t\t\t\t\tIS_TRIGGER_FILE, \t\t\t\t\tTRIGGER_FILE_DELIMITER, \t\t\t\t\tSOURCE_FILE_LOCATION, \t\t\t\t\tINTERIM_LOCATION, \t\t\t\t\tDESTINATION_LOCATION, \t\t\t\t\tZIP_FILE \t\t\t\t FROM \t\t\t\t\tSFTP_CONTROL \t\t\t\t WHERE \t\t\t\t\tcurrent_timestamp BETWEEN EFFECTIVE_START_DATE \t\t\t\t\tand EFFECTIVE_END_DATE \t\t\t\t\tand SFTP_PATTERN_ID = ");
    sqlBuffer.append(patternId);
    return sqlBuffer.toString();
  }
  
  public static String getParameterDetails(int patternId, int fileTypecode) {
    StringBuffer sqlBuffer = new StringBuffer(700);
    sqlBuffer.append("SELECT \t\t\tPARAMETER_NAME, \t\t\tLOWER_POSITION, \t\t\tUPPER_POSITION, \t\t\tIS_INTEGER \t\tFROM \t\t\tSFTP_CONTROL_PARAMETER \t\tWHERE \t\t\tSFTP_PATTERN_ID    = ");
    sqlBuffer.append(patternId);
    sqlBuffer.append(" and FILE_TYPE_CODE = ");
    sqlBuffer.append(fileTypecode);
    return sqlBuffer.toString();
  }
  
  public static String stack2string(Exception e) {
    try {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return "------\r\n" + sw.toString() + "------\r\n";
    } catch (Exception e2) {
      return "bad stack2string";
    } 
  }
}
