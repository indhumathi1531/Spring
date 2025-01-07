package com.nielsen.nocr.util.sftp;

public class UtilProcessor {
	public static void getFiles(int patternId) throws Exception {
	    SftpUtilProcess sftpProcess = new SftpUtilProcess();
	    sftpProcess.getFiles(patternId);
	  }
}
