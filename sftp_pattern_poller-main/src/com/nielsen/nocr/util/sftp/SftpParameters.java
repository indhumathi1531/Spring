package com.nielsen.nocr.util.sftp;

import java.util.ArrayList;

public class SftpParameters {
  String filepattern;
  
  String srcLocation;
  
  String outputFileName;
  
  String triggerFileDelimiter;
  
  int fileTypeCode;
  
  boolean unzipReq;
  
  boolean isTriggerFile;
  
  ArrayList<SftpReplaceParameters> parameters;
  
  public ArrayList<SftpReplaceParameters> getParameters() {
    return this.parameters;
  }
  
  public void setParameters(ArrayList<SftpReplaceParameters> parameters) {
    this.parameters = parameters;
  }
  
  public String getFilepattern() {
    return this.filepattern;
  }
  
  public void setFilepattern(String filepattern) {
    this.filepattern = filepattern;
  }
  
  public String getSrcLocation() {
    return this.srcLocation;
  }
  
  public void setSrcLocation(String srcLocation) {
    this.srcLocation = srcLocation;
  }
  
  public String getOutputFileName() {
    return this.outputFileName;
  }
  
  public void setOutputFileName(String outputFileName) {
    this.outputFileName = outputFileName;
  }
  
  public boolean isUnzipReq() {
    return this.unzipReq;
  }
  
  public void setUnzipReq(boolean unzipReq) {
    this.unzipReq = unzipReq;
  }
  
  public boolean isTriggerFile() {
    return this.isTriggerFile;
  }
  
  public void setTriggerFile(boolean isTriggerFile) {
    this.isTriggerFile = isTriggerFile;
  }
  
  public String getTriggerFileDelimiter() {
    return this.triggerFileDelimiter;
  }
  
  public void setTriggerFileDelimiter(String triggerFileDelimiter) {
    this.triggerFileDelimiter = triggerFileDelimiter;
  }
  
  public int getFileTypeCode() {
    return this.fileTypeCode;
  }
  
  public void setFileTypeCode(int fileTypeCode) {
    this.fileTypeCode = fileTypeCode;
  }
}
