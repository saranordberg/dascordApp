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

public class GuildArrayAdapter extends ArrayAdapter<list_element>{
    private TextView guildName;
    private List<list_element> lstGuildList = new ArrayList<list_element>();
    private ImageView guildImage;


    public GuildArrayAdapter(Context context, int textViewResourceId, list_element[] objects) {
        super(context, textViewResourceId, objects);
    }

    public GuildArrayAdapter(Context applicationContext, int chat) {
        super(applicationContext, chat);
    }

    public void add(list_element lstGuild){

        lstGuildList.add(lstGuild);
        super.add(lstGuild);
    }

    public int getCount() {
        return this.lstGuildList.size();
    }

    public list_element getItem(int index) {

        return this.lstGuildList.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup parent){

        View v = ConvertView;
        if(v==null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.guilds, parent, false);


        }
        list_element lstGuildObj = getItem(position);
        guildName = (TextView) v.findViewById(R.id.guild_name);
        guildName.setText(lstGuildObj.getGuildName());
        guildImage = (ImageView) v.findViewById(R.id.guild_image);
        guildImage.setImageBitmap(lstGuildObj.getImage());
        return v;
    }
}
