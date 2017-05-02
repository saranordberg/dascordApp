package REST;

/**
 * Created by sara on 02/05/2017.
 */

public class User {
    private int id;
    private String username;
    private String image;

    public User(int id, String username, String image){
        this.id = id;
        this.username = username;
        this.image = image;
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getImage(){
        return image;
    }

    @Override
    public String toString() {
        return username + ", " + id + ", " + image;
    }
}

