package com.Messaging;

import java.util.ArrayList;

public class CustomerMessagingCentre extends MessagingCentreImpl {
  
  @Override
  public void sendMessage(Message message) {
    System.out.println("I don't do anything");
    
  }

  @Override
  public Message viewMessage() {
    super.getCurrentMessage().toggleViewed();
    return super.getCurrentMessage();
  }

  @Override
  public void sendMessage(int id, Message message) {
    // TODO Auto-generated method stub
    
  }

}