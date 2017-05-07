package com.example.chat.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gundel on 07-05-2017.
 */

public class TeamArrayAdapter extends ArrayAdapter<list_element> {
    private TextView team_name, team_topic;
    private List<list_element> team_list = new ArrayList<list_element>();


    public TeamArrayAdapter(Context context, int textViewResourceId, list_element[] objects) {
        super(context, textViewResourceId, objects);
    }

    public TeamArrayAdapter(Context applicationContext, int chat) {
        super(applicationContext, chat);
    }

    public void add(list_element team){

        team_list.add(team);
        super.add(team);
    }

    public int getCount() {
        return this.team_list.size();
    }

    public list_element getItem(int index) {

        return this.team_list.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup parent){

        View v = ConvertView;
        if(v==null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.teams, parent, false);


        }
        list_element item = getItem(position);
        team_name = (TextView) v.findViewById(R.id.team_name);
        team_name.setText(item.getTeam_name());
        team_topic = (TextView) v.findViewById(R.id.topic);
        team_topic.setText("Topic: " + item.getTopic());
        notifyDataSetChanged();
        return v;
    }
}