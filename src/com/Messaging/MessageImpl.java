package com.Messaging;

public class MessageImpl implements Message {
  private boolean viewed;
  private String content;
  private int id; 

  public MessageImpl(int id, String content, boolean viewed) {
    this.content = content;
    this.viewed = viewed;
    this.id = id;
  }
  public boolean getViewed() {
    return viewed;
  }
  
  public void toggleViewed() {
    this.viewed = !(this.viewed);
  }
  
  public void setViewed() {
      this.viewed = true;
  }
  
   public void setUnviewed() {
      this.viewed = false;
  }


  public String getContent() {
    return content;
  }

  public int getId() {
    return this.id;
  }
  
  public String toString() {
    return "Viewed: " + this.viewed + " Id: " + this.getId();
  }
}
