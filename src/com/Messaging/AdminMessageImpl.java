package com.Messaging;

import java.sql.SQLException;

import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseInsertHelper;

public class AdminMessageImpl extends MessagingCentreImpl{
    
  public void changeViewStatus(boolean viewed, Message message) {
    boolean view = message.getViewed();
    if(view != viewed) {
      message.toggleViewed();
    }
  }



  
  






  @Override
  public Message viewMessage() {
    return null;

    // TODO Auto-generated method stub
  }


  @Override
  public void sendMessage(int id, Message message) throws DatabaseInsertException, SQLException {
    int messageId = DatabaseInsertHelper.insertMessage(id, message.getContent());
    // TODO Auto-generated method stub
    
  }

    @Override
    public void sendMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }












 
  
}
