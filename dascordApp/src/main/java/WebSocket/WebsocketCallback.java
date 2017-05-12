package WebSocket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rasmus on 12-05-2017.
 */

public abstract class WebsocketCallback implements Runnable {
    @Override
    public void run() {
    }

    abstract public void run(JSONObject payload) throws JSONException;
}
