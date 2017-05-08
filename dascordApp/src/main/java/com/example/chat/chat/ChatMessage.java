package com.example.chat.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Rasmus on 19-04-2017.
 */

public class ChatMessage {
    private String message, owner,timestamp;


    public ChatMessage(String message, String owner) {

        super();
        this.message = message;
        this.owner = owner;
        this.timestamp = new SimpleDateFormat("dd.MM.yyyyy : HH.mm").format(new Date());
    }

    public ChatMessage(String message, String owner, Long stamp){
        super();
        this.message = message;
        this.owner = owner;
        this.timestamp = toDate(stamp);
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

    public String toDate(Long stamp) {
        Date date = new Date(stamp*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyyy : HH.mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
        return sdf.format(date);
    }
}


