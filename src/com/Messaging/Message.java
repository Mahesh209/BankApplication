package com.Messaging;

public interface Message {
  public boolean getViewed();
  
  public void setViewed();
  
  public void setUnviewed();
  
  public void toggleViewed();
  
  public String getContent();
  
  public int getId();
}