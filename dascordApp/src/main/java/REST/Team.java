package REST;

/**
 * Created by Gundel on 07-05-2017.
 */

public class Team {
    private String team_name, topic;
    private int team_id;

    public Team (int team_id, String team_name, String topic){
        this.team_id = team_id;
        this.team_name = team_name;
        this.topic = topic;

    }

    public int getTeam_id() {
        return team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public String getTopic() {
        return topic;
    }
}
