package com.example.chat.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rasmus on 19-04-2017.
 */

class ChatArrayAdapter extends ArrayAdapter<ChatMessage>{
    private TextView chatText, messageOwner, messageTimestamp;
    private List<ChatMessage> MessageList = new ArrayList<ChatMessage>();


    public ChatArrayAdapter(Context context, int textViewResourceId, ChatMessage[] objects) {
        super(context, textViewResourceId, objects);
    }

    public ChatArrayAdapter(Context applicationContext, int chat) {
        super(applicationContext, chat);
    }

    public void add(ChatMessage object){

        MessageList.add(object);
        super.add(object);

    }

    public int getCount() {
        return this.MessageList.size();
    }

    public ChatMessage getItem(int index) {

      return this.MessageList.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup parent){

        View v = ConvertView;
        if(v==null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat, parent, false);


        }
        ChatMessage messageobj = getItem(position);
        chatText = (TextView) v.findViewById(R.id.singlemessage);
        messageOwner = (TextView) v.findViewById(R.id.chat_user);
        messageTimestamp = (TextView) v.findViewById(R.id.chat_time);

        chatText.setText(messageobj.getMessage());
        messageOwner.setText(messageobj.getOwner());
        messageTimestamp.setText(messageobj.getTimestamp());


        return v;
    }

    public Bitmap decodeToBitmap(byte[] decodeByte) {
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }

}
