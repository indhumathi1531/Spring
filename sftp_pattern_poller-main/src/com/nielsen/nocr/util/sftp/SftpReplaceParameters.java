package com.nielsen.nocr.util.sftp;

public class SftpReplaceParameters {
  String paramName;
  
  int minPos;
  
  int maxPos;
  
  boolean isInteger;
  
  public String getParamName() {
    return this.paramName;
  }
  
  public void setParamName(String paramName) {
    this.paramName = paramName;
  }
  
  public int getMinPos() {
    return this.minPos;
  }
  
  public void setMinPos(int minPos) {
    this.minPos = minPos;
  }
  
  public int getMaxPos() {
    return this.maxPos;
  }
  
  public void setMaxPos(int maxPos) {
    this.maxPos = maxPos;
  }
  
  public boolean isInteger() {
    return this.isInteger;
  }
  
  public void setInteger(boolean isInteger) {
    this.isInteger = isInteger;
  }
}
