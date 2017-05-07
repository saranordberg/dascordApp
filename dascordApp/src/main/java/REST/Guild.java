package REST;

/**
 * Created by Gundel on 07-05-2017.
 */

public class Guild {
    private int id, ownerId;
    private String guildName;
    private String image;

    public Guild(int id, int ownerId, String guildName, String image) {
        this.id = id;
        this.ownerId = ownerId;
        this.guildName = guildName;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getGuildName() {
        return guildName;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return guildName + ", " + id + ", " + image;
    }
}
