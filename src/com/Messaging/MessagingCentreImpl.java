package com.Messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MessagingCentreImpl implements MessagingCentre {
  private List<Message> inbox = new ArrayList<>();
  //dictionary {id:message}
  private List<Message> sent = new ArrayList<>();
  private Message currentMessage;

  public ArrayList<Message> getInbox() {
    return (ArrayList<Message>) inbox;
  }

  public ArrayList<Message> getSent() {
    return (ArrayList<Message>) sent;
  }

  public Message getCurrentMessage() {
    return currentMessage;
  }

  public void setCurrentMessage(Message currentMessage) {
    this.currentMessage = currentMessage;
  }
  
  public void addMessage(Message message) {
    this.inbox.add(message);
  }

  public abstract void sendMessage(Message message);
  
  public abstract Message viewMessage();
}