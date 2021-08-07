package com.Messaging;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bank.database.DatabaseInsertException;

public interface MessagingCentre {
  
  public ArrayList<Message> getInbox();

  public ArrayList<Message> getSent();

  public Message getCurrentMessage();

  public void setCurrentMessage(Message currentMessage);
  
  public abstract void sendMessage(int id, Message message) throws DatabaseInsertException, SQLException;
  
  public abstract Message viewMessage();
}