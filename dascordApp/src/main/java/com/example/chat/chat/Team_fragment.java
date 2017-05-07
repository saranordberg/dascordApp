package com.example.chat.chat;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

import REST.RESTService;
import REST.Team;

/**
 * Created by Gundel on 07-05-2017.
 */

public class Team_fragment extends Fragment {
    private TeamArrayAdapter team_adp;
    private ListView team_list;
    private ArrayList<Team> Teams;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        team_list =  (ListView) view.findViewById(R.id.lv_teams);
        team_adp = new TeamArrayAdapter(getContext(), R.layout.teams);
        team_list.setAdapter(team_adp);
        team_list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        final SharedPreferences.Editor editor = pref.edit();

        System.out.print("Team: " + pref.getInt("GUILD",0));

        try {
            Teams = new AsyncTask<ArrayList<Team>, Integer, ArrayList<Team>>() {
                @Override
                protected ArrayList<Team> doInBackground(ArrayList<Team>... arrayLists) {
                    try {

                        Teams = RESTService.instance().Teaminfo(pref.getInt("GUILD", 0));
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
                System.out.println(team.getTeam_name());
                team_adp.add(new list_element(team.getTeam_id(), team.getTeam_name(), team.getTopic()));
            }
        }



        team_adp.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                super.onChanged();
                System.out.println("Count: " + team_adp.getCount());
                team_list.setSelection(team_adp.getCount() - 1);
            }
        }
        );
        team_adp.notifyDataSetChanged();

        team_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putInt("TEAM", team_adp.getItem(i).getTeam_id());
                Fragment newFragment = new Chat_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }



}
