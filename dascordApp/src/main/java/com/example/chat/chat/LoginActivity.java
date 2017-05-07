package com.example.chat.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import REST.RESTService;
import REST.User;

/**
 * Created by sara on 18/04/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password;
    private Button loginBtn;
    private int returnCode;
    private SharedPreferences pref;
    public LoginActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.et_username);
        username.setText("s153980");
        password = (EditText) findViewById(R.id.pw_password);
        password.setText("123");
        loginBtn = (Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       final String usernameSt = username.getText().toString();

        if(!usernameSt.isEmpty()){
            (new AsyncTask<String, String, String>(){
                @Override
                    protected String doInBackground(String... params) {
                        try {
                            String token = RESTService.instance().Login(params[0], params[1]);
                            pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("USERNAME", usernameSt);
                            editor.putString("TOKEN", token);
                            User user = null;
                            try {
                               user = RESTService.instance().Userinfo();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (user != null) {
                                editor.putString("USERNAME", user.getUsername());
                                editor.putString("IMAGE", user.getImage());
                            }



                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).execute(username.getText().toString(), password.getText().toString());
        } else Toast.makeText(LoginActivity.this, "Username cannot be empty", Toast.LENGTH_LONG).show();
    }
}
