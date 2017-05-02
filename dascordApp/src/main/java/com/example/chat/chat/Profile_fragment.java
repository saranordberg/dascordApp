package com.example.chat.chat;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile_fragment extends Fragment {

    private ImageView profilePic;
    private TextView username;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = (TextView) getView().findViewById(R.id.tv_username);
        profilePic = (ImageView) getView().findViewById(R.id.iv_profilepic);

        username.setText(pref.getString("USERNAME", null));
    }
}
