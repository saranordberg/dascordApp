package com.example.chat.chat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Rasmus on 19-04-2017.
 */

public class MainActivity extends AppCompatActivity {

    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private ImageButton send;
    private Intent in;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent i = getIntent();

        username = i.getStringExtra("USERNAME");

        send = (ImageButton) findViewById(R.id.btn_send);

        list = (ListView) findViewById(R.id.listView);

        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.chat);

        chatText = (EditText) findViewById(R.id.chat);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)){


                    return sendChatMessage();
                }
                return false;
            }


        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatMessage();

            }
        });

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);

        adp.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                super.onChanged();

                list.setSelection(adp.getCount() -1);
            }
        });


    }
    private boolean sendChatMessage() {
        if(username == null){
            Toast.makeText(this, "You need to be logged in to send messages", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!chatText.getText().toString().isEmpty()) {
            adp.add(new ChatMessage(chatText.getText().toString(), username));
            chatText.setText("");
        }



        return true;
    }

}
