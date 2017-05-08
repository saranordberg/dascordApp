package com.example.chat.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import REST.Guild;
import REST.RESTService;
import REST.Team;
import REST.User;

import static com.example.chat.chat.MainActivity.download;

/**
 * Created by Rasmus on 02-05-2017.
 */

public class Guilds_fragment extends Fragment{
    private ListView guild_List, team_list, chat_list;
    private SharedPreferences pref;
    private GuildArrayAdapter adp;
    private TeamArrayAdapter team_adp;
    private ChatArrayAdapter chat_adp;
    private ArrayList<Guild> Guilds;
    private ArrayList<Team> Teams;
    private ArrayList<ChatMessage> Messages;
    private ImageButton send;
    private String username;
    private EditText chatText;
    private Button back;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guilds, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        guild_List = (ListView) view.findViewById(R.id.lv_guilds);
        team_list = (ListView) view.findViewById(R.id.old_lv_teams);
        chat_list = (ListView) view.findViewById(R.id.chat_list);
        chatText = (EditText) view.findViewById(R.id.chat);
        send = (ImageButton) view.findViewById(R.id.btn_send);
        adp = new GuildArrayAdapter(view.getContext(), R.layout.guilds);
        back = (Button) view.findViewById(R.id.button);

        back.setVisibility(View.INVISIBLE);
        team_list.setVisibility(View.INVISIBLE);
        send.setVisibility(View.INVISIBLE);
        chatText.setVisibility(View.INVISIBLE);
        chat_list.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chat_list.getVisibility() == View.VISIBLE && team_list.getVisibility() == view.INVISIBLE){
                    chatText.setVisibility(View.INVISIBLE);
                    chat_list.setVisibility(View.INVISIBLE);
                    team_list.setVisibility(View.VISIBLE);
                } else if(team_list.getVisibility() == View.VISIBLE && guild_List.getVisibility() == View.INVISIBLE){
                    team_list.setVisibility(View.INVISIBLE);
                    guild_List.setVisibility(View.VISIBLE);
                    back.setVisibility(View.INVISIBLE);
                }
            }
        });


        final SharedPreferences.Editor editor = pref.edit();




        try {
            Guilds = new AsyncTask<ArrayList<Guild>, Integer, ArrayList<Guild>>() {
                @Override
                protected ArrayList<Guild> doInBackground(ArrayList<Guild>... arrayLists) {
                    try {
                        Guilds = RESTService.instance().Guildinfo();
                        return Guilds;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (Guilds != null) {
            for (Guild guild : Guilds) {
                System.out.println("Image string: " + guild.getImage());
                adp.add(new list_element(guild.getGuildName(), download(guild.getImage()), guild.getId()));
            }
        }


        guild_List.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        guild_List.setAdapter(adp);
        adp.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                super.onChanged();

                guild_List.setSelection(adp.getCount() - 1);
            }
        });

        guild_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                back.setVisibility(View.VISIBLE);
                guild_List.setVisibility(View.INVISIBLE);
                team_list.setVisibility(View.VISIBLE);
                team_adp = new TeamArrayAdapter(view.getContext(), R.layout.teams);
                team_list.setAdapter(team_adp);
                final int guild_id = adp.getItem(i).getGuild_id();

                try {
                    Teams = new AsyncTask<ArrayList<Team>, Integer, ArrayList<Team>>() {
                        @Override
                        protected ArrayList<Team> doInBackground(ArrayList<Team>... arrayLists) {
                            try {
                                Teams = RESTService.instance().Teaminfo(guild_id);
                                return Teams;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (Teams != null) {
                    for (Team team : Teams) {
                        team_adp.add(new list_element(team.getTeam_id(), team.getTeam_name(), team.getTopic()));
                    }
                }
                team_adp.registerDataSetObserver(new DataSetObserver() {
                                                     public void onChanged() {
                                                         super.onChanged();

                                                         team_list.setSelection(adp.getCount() - 1);
                                                     }
                                                 }
                );

                team_adp.notifyDataSetChanged();

            }
        });
        team_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                team_list.setVisibility(View.INVISIBLE);
                chatText.setVisibility(View.VISIBLE);
                chat_list.setVisibility(View.VISIBLE);
                username = pref.getString("USERNAME", null);
                chat_adp = new ChatArrayAdapter(view.getContext(), R.layout.chat);
                final int team_id = team_adp.getItem(i).getTeam_id();

                try {
                    Messages = new AsyncTask<ArrayList<ChatMessage>, Integer, ArrayList<ChatMessage>>() {
                        @Override
                        protected ArrayList<ChatMessage> doInBackground(ArrayList<ChatMessage>... arrayLists) {
                            try {
                                Messages = RESTService.instance().ChatInfo(team_id);
                                return Messages;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (Messages != null) {
                    Collections.reverse(Messages);
                    for (ChatMessage message : Messages) {
                        chat_adp.add(message);
                    }
                }

                chat_adp.registerDataSetObserver(new DataSetObserver() {
                                                     public void onChanged() {
                                                         super.onChanged();

                                                         chat_list.setSelection(chat_adp.getCount() - 1);

                                                     }
                                                 }
                );

                chat_adp.notifyDataSetChanged();

                chatText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            return sendChatMessage(team_id);
                        }
                        return false;
                    }


                });
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendChatMessage(team_id);

                    }
                });

                chat_list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                chat_list.setAdapter(chat_adp);

                chat_adp.registerDataSetObserver(new DataSetObserver() {
                    public void onChanged() {
                        super.onChanged();

                        chat_list.setSelection(chat_adp.getCount() - 1);


                    }
                });
            }
        });

    }

    private boolean sendChatMessage(final int team_id) {
        if (username == null) {

            return false;


        } else if (!chatText.getText().toString().isEmpty()) {
            final ChatMessage message = new ChatMessage(chatText.getText().toString(), username);

            (new AsyncTask<Integer, String, Integer>(){
                @Override
                protected Integer doInBackground(Integer... params) {

                    int msgID = RESTService.instance().SendMessage(message, team_id);
                    return msgID;
                }
            }).execute();

            chat_adp.add(message);
            chatText.setText("");
        }

        return true;
    }

}
