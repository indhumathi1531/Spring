package com.nielsen.nocr.util.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.nielsenmedia.foundations.nce.connectionpool.SQLProcessorConnectionManager;
import com.nielsenmedia.foundations.util.configuration.ConfigurationMgr;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;
import org.apache.log4j.Logger;

public class SftpUtilProcess {
  ArrayList<SftpParameters> sftpParams;
  
  SftpUtil sftpUtil;
  
  public static final Logger log = Logger.getLogger(SftpUtilProcess.class);
  
  public void getFiles(int patternId) throws Exception {
    log.info("SFTP process started for patternId : " + patternId);
    this.sftpUtil = new SftpUtil();
    this.sftpParams = this.sftpUtil.getSftpParams(patternId);
    HashMap<String, Object> triggerFiles = getFilesToLocal(patternId);
    moveFilesToDestination(triggerFiles);
    writeLocalFileList(triggerFiles);
    log.info("SFTP process completed for patternId : " + patternId);
  }
  
  private HashMap<String, Object> getFilesToLocal(int patternId) throws Exception {
    Session session = null;
    ChannelSftp channel = null;
    ChannelSftp sftpChannel = null;
    HashMap<String, Object> triggerFiles = null;
    int timeout = 0;
    try {
      session = this.sftpUtil.getFTPSession();
      channel = (ChannelSftp)session.openChannel("sftp");
      try {
        timeout = Integer.parseInt(ConfigurationMgr.getProperty("SFTP.SOCKET.TIMEOUT"));
      } catch (Exception e) {
        timeout = 0;
      } 
      channel.connect(timeout);
      sftpChannel = channel;
      Object[] diffFiles = this.sftpUtil.getDifferenceFileList(sftpChannel, this.sftpParams);
      triggerFiles = this.sftpUtil.getFilesToLocal(diffFiles, this.sftpParams, sftpChannel);
    } catch (Exception e) {
      throw e;
    } finally {
      if (sftpChannel != null && sftpChannel.isConnected())
        sftpChannel.exit(); 
      if (session != null && session.isConnected())
        session.disconnect(); 
    } 
    return triggerFiles;
  }
  
  private void moveFilesToDestination(HashMap<String, Object> triggerFiles) throws Exception {
    ArrayList<String> countsTriggerFiles = (ArrayList<String>)triggerFiles.get("countsTriggerFiles");
    ArrayList<String> dataTriggerFiles = (ArrayList<String>)triggerFiles.get("dataTriggerFiles");
    FileInputStream fsotream = null;
    DataInputStream in = null;
    BufferedReader br = null;
    File file = null;
    String strLine = null;
    String dataFilename = null;
    String destFilename = "";
    try {
      for (String currentCountsFile : countsTriggerFiles) {
        file = new File(currentCountsFile);
        fsotream = new FileInputStream(currentCountsFile);
        in = new DataInputStream(fsotream);
        br = new BufferedReader(new InputStreamReader(in));
        while ((strLine = br.readLine()) != null) {
          String[] strArray = strLine.trim().split("\\" + this.sftpUtil.triggerFileDelimiter);
          dataFilename = file.getParent() + File.separator + strArray[0];
          destFilename = this.sftpUtil.destLocation + strArray[0];
          if (this.sftpUtil.zipDataFile) {
            dataFilename = zipFile(dataFilename);
            destFilename = destFilename + ".gz";
          } 
          moveFiles(dataFilename, destFilename);
        } 
        br.close();
        in.close();
        fsotream.close();
        moveFiles(currentCountsFile, this.sftpUtil.destLocation + file.getName());
      } 
      for (String currentdataFile : dataTriggerFiles) {
        file = new File(currentdataFile);
        moveFiles(currentdataFile, this.sftpUtil.destLocation + file.getName());
      } 
    } catch (Exception e) {
      log.error("Error in moving files to destination within local : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } 
  }
  
  private void moveFiles(String fromFile, String toFile) throws Exception {
    File _fromFile = new File(fromFile);
    File _toFile = new File(toFile);
    log.info("Moving " + fromFile + " to " + toFile);
    boolean success = _fromFile.renameTo(_toFile);
    if (!success)
      throw new Exception("Issues moving the file " + fromFile + " to " + toFile); 
  }
  
  private void writeLocalFileList(HashMap<String, Object> triggerFiles) throws Exception {
    ArrayList<String> remoteFiles = (ArrayList<String>)triggerFiles.get("remoteFiles");
    String localFileName = (String)triggerFiles.get("localListFile");
    BufferedWriter out = null;
    FileWriter fstream = null;
    try {
      fstream = new FileWriter(localFileName, true);
      out = new BufferedWriter(fstream);
      for (int i = 0; i < remoteFiles.size(); i++) {
        out.write((String)remoteFiles.get(i) + "\n");
        out.flush();
      } 
    } catch (Exception e) {
      log.error("Error in Writing local file list for the nwe files : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (out != null)
        out.close(); 
      if (fstream != null)
        fstream.close(); 
    } 
  }
  
  private String zipFile(String fileName) throws Exception {
    log.info("Zipping the file : " + fileName);
    String outputFile = fileName + ".gz";
    GZIPOutputStream out = null;
    FileInputStream in = null;
    byte[] buf = new byte[1024];
    int len = 0;
    try {
      out = new GZIPOutputStream(new FileOutputStream(outputFile));
      in = new FileInputStream(fileName);
      while ((len = in.read(buf)) > 0)
        out.write(buf, 0, len); 
    } catch (Exception e) {
      log.error("Error in Zipping the file : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (out != null)
        out.close(); 
      if (in != null)
        in.close(); 
    } 
    try {
      File f = new File(fileName);
      f.delete();
    } catch (Exception e) {
      log.error("Error occured while deleting original unzipped data file : " + SftpConstants.stack2string(e));
      throw e;
    } 
    return outputFile;
  }
  
  public static void main(String[] args) throws Exception {
    ConfigurationMgr.loadConfigurationFrom("./config/sftpUtil/sftpConfig.xml");
    SQLProcessorConnectionManager.createConnectionPoolManager();
    SftpUtilProcess sftp = new SftpUtilProcess();
    sftp.getFiles(1);
  }
}
