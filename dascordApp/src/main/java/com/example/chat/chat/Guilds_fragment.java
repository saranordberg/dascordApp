package com.example.chat.chat;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
    private ListView guild_List, team_list;
    private SharedPreferences pref;
    private GuildArrayAdapter adp;
    private ArrayList<Guild> Guilds;
    private ArrayList<Team> Teams;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guilds, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        guild_List = (ListView) view.findViewById(R.id.lv_guilds);
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
                TeamArrayAdapter team_adp = new TeamArrayAdapter(view.getContext(), R.layout.teams);
                guild_List.setAdapter(team_adp);
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
                adp.registerDataSetObserver(new DataSetObserver() {
                    public void onChanged() {
                        super.onChanged();

                        guild_List.setSelection(adp.getCount() - 1);
                    }
                }
                );

               team_adp.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

}
