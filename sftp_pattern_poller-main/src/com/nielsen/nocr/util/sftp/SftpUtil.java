package com.nielsen.nocr.util.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.nielsenmedia.foundations.nce.connectionpool.SQLProcessorConnectionManager;
import com.nielsenmedia.foundations.nce.connectionpool.SQLProcessorConnectionPoolManager;
import com.nielsenmedia.foundations.util.configuration.ConfigurationMgr;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;

public class SftpUtil {
  private static SQLProcessorConnectionPoolManager poolMgr;
  
  public static final Logger log = Logger.getLogger(SftpUtil.class);
  
  String interLocation;
  
  String destLocation;
  
  String serverName;
  
  String userName;
  
  String passWord;
  
  String localFileListFile;
  
  String triggerFileDelimiter;
  
  boolean zipDataFile = false;
  
  public SftpUtil() throws Exception {
    getConnectionPoolManager();
  }
  
  private void getConnectionPoolManager() throws Exception {
    poolMgr = SQLProcessorConnectionManager.getConnectionPoolManager();
  }
  
  public ArrayList<SftpParameters> getSftpParams(int patternId) throws Exception {
    ArrayList<SftpParameters> sftpParams = new ArrayList<>();
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = SftpConstants.getPatternDetails(patternId);
    log.info("SQL to get sftp details for pattern id : " + patternId + " is : " + sql);
    try {
      conn = poolMgr.getConnection("FNDTNS");
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        SftpParameters newParams = new SftpParameters();
        newParams.filepattern = rs.getString("FILE_PATTERN");
        newParams.srcLocation = rs.getString("SOURCE_FILE_LOCATION");
        newParams.outputFileName = rs.getString("OUTPUT_FILENAME");
        newParams.triggerFileDelimiter = rs.getString("TRIGGER_FILE_DELIMITER");
        if ("Y".equals(rs.getString("ZIP_FILE")))
          this.zipDataFile = true; 
        if ("Y".equals(rs.getString("IS_TRIGGER_FILE"))) {
          newParams.isTriggerFile = true;
          this.interLocation = rs.getString("INTERIM_LOCATION");
          this.destLocation = rs.getString("DESTINATION_LOCATION");
          this.serverName = rs.getString("SERVER_NAME");
          this.userName = rs.getString("USERNAME");
          this.passWord = rs.getString("PASSWORD");
          this.localFileListFile = this.interLocation + "localfilelist_" + patternId + ".txt";
          this.triggerFileDelimiter = newParams.triggerFileDelimiter;
        } else {
          newParams.isTriggerFile = false;
        } 
        if ("Y".equals(rs.getString("UNZIP_REQ"))) {
          newParams.unzipReq = true;
        } else {
          newParams.unzipReq = false;
        } 
        newParams.fileTypeCode = rs.getInt("FILE_TYPE_CODE");
        sftpParams.add(newParams);
      } 
      rs.close();
      for (SftpParameters sftpParam : sftpParams) {
        sql = SftpConstants.getParameterDetails(patternId, sftpParam.fileTypeCode);
        log.info("Sql to get parameters for patternID : " + patternId + " fileTypeCode : " + sftpParam.fileTypeCode + " is : " + sql);
        rs = stmt.executeQuery(sql);
        ArrayList<SftpReplaceParameters> replaceParams = new ArrayList<>();
        while (rs.next()) {
          SftpReplaceParameters _replaceParam = new SftpReplaceParameters();
          _replaceParam.paramName = rs.getString("PARAMETER_NAME");
          _replaceParam.minPos = rs.getInt("LOWER_POSITION");
          _replaceParam.maxPos = rs.getInt("UPPER_POSITION");
          if ("Y".equals(rs.getString("IS_INTEGER"))) {
            _replaceParam.isInteger = true;
          } else {
            _replaceParam.isInteger = false;
          } 
          replaceParams.add(_replaceParam);
        } 
        sftpParam.parameters = replaceParams;
      } 
    } catch (Exception e) {
      log.error("Error in getting parameters for patternId = " + patternId + " : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (rs != null)
        rs.close(); 
      if (stmt != null)
        stmt.close(); 
      if (conn != null)
        conn.close(); 
    } 
    return sftpParams;
  }
  
  public Object[] getDifferenceFileList(ChannelSftp sftpChannel, ArrayList<SftpParameters> sftpParams) throws Exception {
    Vector v = new Vector();
    for (SftpParameters sftpParam : sftpParams) {
      String lsPattern = sftpParam.filepattern;
      Vector v1 = sftpChannel.ls(sftpParam.srcLocation + lsPattern);
      v.addAll(v1);
    } 
    String[] remoteFileList = new String[v.size()];
    Object[] Files = new Object[v.size()];
    v.copyInto(Files);
    for (int i = 0; i < Files.length; i++)
      remoteFileList[i] = Files[i].toString().substring(Files[i].toString().lastIndexOf(" ") + 1); 
    String[] localFileList = getLocalFileList(remoteFileList, this.localFileListFile);
    List diff = ListUtils.subtract(Arrays.asList(remoteFileList), Arrays.asList(localFileList));
    log.info("New files generated in the server : " + diff);
    return diff.toArray();
  }
  
  public Session getFTPSession() throws Exception {
    Session session;
    log.info("Gettting the sftp connection using hostname : " + this.serverName + " username : " + this.userName);
    int timeout = 0;
    try {
      JSch jsch = new JSch();
      session = jsch.getSession(this.userName, this.serverName, 22);
      session.setConfig("StrictHostKeyChecking", "no");
      session.setPassword(this.passWord);
      try {
        timeout = Integer.parseInt(ConfigurationMgr.getProperty("SFTP.SOCKET.TIMEOUT"));
      } catch (Exception e) {
        timeout = 0;
      } 
      session.connect(timeout);
      log.info("Succesfully created sftp connection using hostname : " + this.serverName + " username : " + this.userName);
    } catch (JSchException e) {
      log.error("Error in getting FTP session : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string((Exception)e));
      throw e;
    } catch (Exception e) {
      log.error("Error in getting FTP session : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } 
    return session;
  }
  
  private String[] getLocalFileList(String[] remoteFileList, String localFileListFile) throws Exception {
    String[] localFileList = new String[remoteFileList.length];
    BufferedWriter out = null;
    FileWriter fstream = null;
    try {
      File localListFile = new File(localFileListFile);
      if (!localListFile.exists()) {
        log.info("Creating new file as the local list file " + localFileListFile + " does not exist");
        fstream = new FileWriter(localFileListFile);
        out = new BufferedWriter(fstream);
        for (int i = 0; i < remoteFileList.length; i++) {
          out.write(remoteFileList[i] + "\n");
          out.flush();
        } 
      } 
    } catch (Exception e) {
      log.error("Error in Writing local file list to a file : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (out != null)
        out.close(); 
      if (fstream != null)
        fstream.close(); 
    } 
    FileInputStream fsotream = null;
    DataInputStream in = null;
    BufferedReader br = null;
    ArrayList<String> localArray = new ArrayList<>();
    try {
      fsotream = new FileInputStream(localFileListFile);
      in = new DataInputStream(fsotream);
      br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      while ((strLine = br.readLine()) != null)
        localArray.add(strLine.trim()); 
      localFileList = localArray.<String>toArray(localFileList);
    } catch (Exception e) {
      log.error("Error in getting the local file list : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (br != null)
        br.close(); 
      if (in != null)
        in.close(); 
      if (fsotream != null)
        fsotream.close(); 
    } 
    return localFileList;
  }
  
  public HashMap<String, Object> getFilesToLocal(Object[] filesToBeProcessed, ArrayList<SftpParameters> sftpParams, ChannelSftp sftpChannel) throws Exception {
    String outputFileName = null;
    String remotePath = null;
    String currentFile = null;
    HashMap<String, Object> triggerFiles = new HashMap<>();
    ArrayList<String> dataTriggerFiles = new ArrayList<>();
    ArrayList<String> countsTriggerFiles = new ArrayList<>();
    ArrayList<String> remoteFiles = new ArrayList<>();
    for (int i = 0; i < filesToBeProcessed.length; i++) {
      currentFile = (String)filesToBeProcessed[i];
      for (SftpParameters currentSftpEntry : sftpParams) {
        String filePattern = currentSftpEntry.filepattern;
        filePattern = filePattern.replaceAll("\\*", ".*");
        Pattern pattern = Pattern.compile(filePattern.trim());
        Matcher matcher = pattern.matcher(currentFile);
        if (matcher != null && matcher.find()) {
          outputFileName = currentSftpEntry.outputFileName;
          ArrayList<SftpReplaceParameters> parameters = currentSftpEntry.parameters;
          for (SftpReplaceParameters sftpReplaceParameters : parameters) {
            String replaceValue = currentFile.substring(sftpReplaceParameters.minPos, sftpReplaceParameters.maxPos);
            if (sftpReplaceParameters.isInteger)
              replaceValue = replaceValue.replaceAll("\\D", ""); 
            outputFileName = outputFileName.replaceAll("%" + sftpReplaceParameters.paramName + "%", replaceValue);
          } 
          remotePath = currentSftpEntry.srcLocation;
          String absoluteLocalFileName = this.interLocation + outputFileName;
          String absoluteRemoteFileName = remotePath + currentFile;
          checkFileTransferComplete(absoluteRemoteFileName, sftpChannel);
          if (currentSftpEntry.isTriggerFile && 1 == currentSftpEntry.fileTypeCode) {
            countsTriggerFiles.add(absoluteLocalFileName);
          } else if (currentSftpEntry.isTriggerFile) {
            dataTriggerFiles.add(absoluteLocalFileName);
          } 
          remoteFiles.add(currentFile);
          log.info("Copying " + absoluteRemoteFileName + " from server to " + this.interLocation + outputFileName + " in local");
          try {
            if (1 == currentSftpEntry.fileTypeCode) {
              String interimFileUnzipped = this.interLocation + "original_" + outputFileName;
              if (currentSftpEntry.unzipReq) {
                String interimFile = this.interLocation + "original_" + outputFileName + ".gz";
                sftpChannel.get(absoluteRemoteFileName, interimFile);
                Unzip(interimFile, interimFileUnzipped);
              } else {
                sftpChannel.get(absoluteRemoteFileName, interimFileUnzipped);
              } 
              rewriteCountsFile(interimFileUnzipped, absoluteLocalFileName, sftpParams, currentSftpEntry.triggerFileDelimiter);
              break;
            } 
            if (currentSftpEntry.unzipReq) {
              String interimFile = this.interLocation + outputFileName + ".gz";
              sftpChannel.get(absoluteRemoteFileName, interimFile);
              Unzip(interimFile, absoluteLocalFileName);
              break;
            } 
            sftpChannel.get(absoluteRemoteFileName, absoluteLocalFileName);
          } catch (SftpException e) {
            log.error("Error in copying files from server to local : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string((Exception)e));
            throw e;
          } catch (Exception e) {
            log.error("Error in copying files from server to local : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
            throw e;
          } 
          break;
        } 
      } 
    } 
    triggerFiles.put("dataTriggerFiles", dataTriggerFiles);
    triggerFiles.put("countsTriggerFiles", countsTriggerFiles);
    triggerFiles.put("remoteFiles", remoteFiles);
    triggerFiles.put("localListFile", this.localFileListFile);
    return triggerFiles;
  }
  
  private boolean checkFileTransferComplete(String absoluteRemoteFileName, ChannelSftp sftpChannel) throws Exception {
    log.info("Checking file transfer completion for " + absoluteRemoteFileName);
    SftpATTRS attrs = null;
    int waitTime = Integer.parseInt(ConfigurationMgr.getProperty("FILE.SIZE.CHECK.WAIT.TIME")) * 1000;
    int checkTimes = Integer.parseInt(ConfigurationMgr.getProperty("FILE.SIZE.CHECK.RETRY.COUNT"));
    Long[] fileSize = null;
    boolean isFileTransferred = false;
    int currentIndex = 0;
    try {
      if (checkTimes < 2)
        return true; 
      fileSize = new Long[checkTimes];
      int i;
      for (i = 0; i < checkTimes; i++) {
        attrs = sftpChannel.lstat(absoluteRemoteFileName);
        fileSize[i] = Long.valueOf(attrs.getSize());
        log.info("File Size which is transferred to source server for " + absoluteRemoteFileName + " : " + fileSize[i]);
        if (checkTimes - 1 != i)
          Thread.sleep(waitTime); 
      } 
      do {
        isFileTransferred = true;
        for (i = 1; i < checkTimes; i++) {
          if (!fileSize[i].equals(fileSize[i - 1])) {
            isFileTransferred = false;
            break;
          } 
        } 
        if (isFileTransferred)
          continue; 
        Thread.sleep(waitTime);
        attrs = sftpChannel.lstat(absoluteRemoteFileName);
        fileSize[currentIndex] = Long.valueOf(attrs.getSize());
        log.info("File Size which is transferred to source server for " + absoluteRemoteFileName + " : " + fileSize[currentIndex]);
        if (currentIndex < checkTimes - 1) {
          currentIndex++;
        } else {
          currentIndex = 0;
        } 
      } while (!isFileTransferred);
    } catch (Exception e) {
      log.error("[SftpUtil-checkFileTransferComplete] For file = " + absoluteRemoteFileName + " Exception occurred while trying to monitor transferred file size in source server " + e.getMessage());
      throw e;
    } 
    log.info("File transfer to source server complete for " + absoluteRemoteFileName);
    return true;
  }
  
  private String Unzip(String gzFileName, String outputFile) throws Exception {
    log.info("Unzipping the file : " + gzFileName);
    GZIPInputStream gzipInputStream = null;
    OutputStream out = null;
    byte[] buf = new byte[1024];
    int len = 0;
    try {
      gzipInputStream = new GZIPInputStream(new FileInputStream(new File(gzFileName)));
      out = new FileOutputStream(new File(outputFile));
      while ((len = gzipInputStream.read(buf)) > 0)
        out.write(buf, 0, len); 
    } catch (Exception e) {
      log.error("Error in Unzip file : " + e.getMessage() + " Stack Trace : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (out != null)
        out.close(); 
      if (gzipInputStream != null)
        gzipInputStream.close(); 
    } 
    try {
      File f = new File(gzFileName);
      f.delete();
    } catch (Exception e) {
      log.error("Error occured while deleting original gz data file : " + SftpConstants.stack2string(e));
      throw e;
    } 
    return outputFile;
  }
  
  public void rewriteCountsFile(String srcFilename, String destFileName, ArrayList<SftpParameters> sftpParams, String delimiter) throws Exception {
    log.info("Rewriting counts file :" + srcFilename);
    FileInputStream fsotream = null;
    DataInputStream in = null;
    BufferedReader br = null;
    String outputContent = "";
    String outputFileName = "";
    boolean matchFound = false;
    try {
      fsotream = new FileInputStream(srcFilename);
      in = new DataInputStream(fsotream);
      br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      while ((strLine = br.readLine()) != null) {
        String[] strArray = strLine.trim().split("\\" + delimiter);
        for (int i = 0; i < strArray.length; i++) {
          String currentvalue = strArray[i];
          outputFileName = "";
          matchFound = false;
          for (SftpParameters currentSftpEntry : sftpParams) {
            String filePattern = currentSftpEntry.filepattern;
            filePattern = filePattern.replaceAll("\\*", ".*");
            Pattern pattern = Pattern.compile(filePattern.trim());
            Matcher matcher = pattern.matcher(currentvalue);
            if (matcher != null && matcher.find()) {
              outputFileName = currentSftpEntry.outputFileName;
              ArrayList<SftpReplaceParameters> parameters = currentSftpEntry.parameters;
              for (SftpReplaceParameters sftpReplaceParameters : parameters) {
                String replaceValue = currentvalue.substring(sftpReplaceParameters.minPos, sftpReplaceParameters.maxPos);
                if (sftpReplaceParameters.isInteger)
                  replaceValue = replaceValue.replaceAll("\\D", ""); 
                outputFileName = outputFileName.replaceAll("%" + sftpReplaceParameters.paramName + "%", replaceValue);
              } 
              matchFound = true;
            } 
          } 
          if (matchFound)
            currentvalue = outputFileName; 
          if (i == 0) {
            outputContent = outputContent + currentvalue;
          } else if (delimiter.equals("t")) {
            outputContent = outputContent + "\t" + currentvalue;
          } else {
            outputContent = outputContent + delimiter + currentvalue;
          } 
        } 
        outputContent = outputContent + "\n";
      } 
    } catch (Exception e) {
      log.error("Error in framing new counts file content : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (br != null)
        br.close(); 
      if (in != null)
        in.close(); 
      if (fsotream != null)
        fsotream.close(); 
    } 
    BufferedWriter out = null;
    FileWriter fstream = null;
    try {
      fstream = new FileWriter(destFileName);
      out = new BufferedWriter(fstream);
      out.write(outputContent);
      out.flush();
    } catch (Exception e) {
      log.error("Error in rewriting new counts file : " + SftpConstants.stack2string(e));
      throw e;
    } finally {
      if (out != null)
        out.close(); 
      if (fstream != null)
        fstream.close(); 
    } 
    try {
      File f = new File(srcFilename);
      f.delete();
    } catch (Exception e) {
      log.error("Error occured while deleting original counts file : " + SftpConstants.stack2string(e));
      throw e;
    } 
  }
}
