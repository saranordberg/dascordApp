package com.example.chat.chat;

import android.graphics.Bitmap;

/**
 * Created by Gundel on 07-05-2017.
 */

public class list_element {
    private String guildName, team_name, topic;
    private Bitmap image;
    private int guild_id, team_id;
    public list_element(String guildName, Bitmap image, int guild_id){
        super();
        this.guildName = guildName;
        this.image = image;
        this.guild_id = guild_id;
    }

    public list_element(int team_id, String team_name, String topic){
        super();
        this.team_id = team_id;
        this.team_name = team_name;
        this.topic = topic;
    }

    public String getGuildName(){
        return guildName;
    }
    public Bitmap getImage(){
        return image;
    }

    public int getGuild_id(){
        return guild_id;
    }

    public String getTopic() {
        return topic;
    }

    public String getTeam_name() {
        return team_name;
    }

    public int getTeam_id() {
        return team_id;
    }
}
