package com.example.chat.chat;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static com.example.chat.chat.MainActivity.download;

public class Profile_fragment extends Fragment {

    private ImageView profilePic;
    private TextView username;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = pref.edit();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = (TextView) getView().findViewById(R.id.tv_username);
        profilePic = (ImageView) getView().findViewById(R.id.iv_profilepic);

        username.setText(pref.getString("USERNAME", null));

        if(pref.getString("IMAGE", null) != null){
            profilePic.setImageBitmap(download(pref.getString("IMAGE", null)));
        }

    }
}
