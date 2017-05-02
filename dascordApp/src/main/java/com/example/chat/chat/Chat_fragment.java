package com.example.chat.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by Rasmus on 01-05-2017.
 */

public class Chat_fragment extends Fragment implements View.OnClickListener {


    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private ImageButton send;
    private String username;
    private SharedPreferences pref;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // pref.getString("username", username);
        //  username = i.getStringExtra("USERNAME");
        send = (ImageButton) view.findViewById(R.id.btn_send);
        list = (ListView) view.findViewById(R.id.listView);
        adp = new ChatArrayAdapter(view.getContext(), R.layout.chat);
        chatText = (EditText) view.findViewById(R.id.chat);


        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

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

                list.setSelection(adp.getCount() - 1);
            }
        });
    }
    @Override
    public void onClick(View view) {

    }

    private boolean sendChatMessage() {
        if (username == null) {

            return false;
        } else if (!chatText.getText().toString().isEmpty()) {
            adp.add(new ChatMessage(chatText.getText().toString(), "Rasmus"));
            chatText.setText("");
        }

        return true;
    }



}
