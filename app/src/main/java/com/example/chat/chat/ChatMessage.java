package com.example.chat.chat;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rasmus on 19-04-2017.
 */

class ChatMessage {
    private String message, owner,timestamp;



    public ChatMessage(String message, String owner) {

        super();
        this.message = message;
        this.owner = owner;
        this.timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    public String getMessage(){
        return message;
    }

    public  String getOwner(){
        return owner;
    }

    public String getTimestamp(){
        return timestamp;
    }
}
