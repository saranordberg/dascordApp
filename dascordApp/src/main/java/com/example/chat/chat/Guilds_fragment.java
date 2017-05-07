package com.example.chat.chat;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import REST.Guild;
import REST.RESTService;
import REST.Team;

import static com.example.chat.chat.MainActivity.download;

/**
 * Created by Rasmus on 02-05-2017.
 */

public class Guilds_fragment extends Fragment implements View.OnClickListener {
    private ListView guild_List, team_list, chat_list;
    private SharedPreferences pref;
    private GuildArrayAdapter adp;
    private TeamArrayAdapter team_adp;
    private ChatArrayAdapter chat_adp;
    private ArrayList<Guild> Guilds;
    private ArrayList<Team> Teams;
    private ImageButton send;
    private String username;
    private EditText chatText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guilds, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        guild_List = (ListView) view.findViewById(R.id.lv_guilds);
        team_list =  (ListView) view.findViewById(R.id.lv_teams);
        chat_list = (ListView) view.findViewById(R.id.chat_list);

        chatText = (EditText) view.findViewById(R.id.chat);
        send = (ImageButton) view.findViewById(R.id.btn_send);

        adp = new GuildArrayAdapter(view.getContext(), R.layout.guilds);

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
                guild_List.setVisibility(View.INVISIBLE);
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
                username = pref.getString("USERNAME", null);
                chat_adp = new ChatArrayAdapter(view.getContext(), R.layout.chat);
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

    @Override
    public void onClick(View view) {

    }

    private boolean sendChatMessage() {
        if (username == null) {

            return false;
        } else if (!chatText.getText().toString().isEmpty()) {
            chat_adp.add(new ChatMessage(chatText.getText().toString(), username));
            chatText.setText("");
        }

        return true;
    }

}
