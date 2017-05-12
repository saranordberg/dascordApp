package WebSocket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import REST.RESTService;

/**
 * Created by Rasmus on 12-05-2017.
 */

public class WebSocketService {

    private static SharedPreferences pref;
    private static WebSocketService instance;
    private WebSocketClient mWebSocketClient;
    private HashMap<String, WebsocketCallback> callbacks = new HashMap<>();

    private WebSocketService() {
    }

    public static WebSocketService getInstance(Context context) {
        if (instance == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
            instance = new WebSocketService();
            instance.connectWebSocket();
        }
        return instance;
    }

    public boolean closeConnection() throws JSONException {
        if (mWebSocketClient != null && mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            mWebSocketClient.close();
            return true;
        }
        return false;
    }

    public WebSocket.READYSTATE getReadyState() {
        if (mWebSocketClient == null) {
            return WebSocket.READYSTATE.NOT_YET_CONNECTED;
        }
        return mWebSocketClient.getReadyState();
    }

    public boolean identify(String token) throws JSONException {
        if (mWebSocketClient != null && mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            mWebSocketClient.send(new JSONObject().put("op", 2).put("token", token).toString());
            return true;
        }
        return false;
    }

    public boolean getGuildMembers(int guildId) throws JSONException {
        if (mWebSocketClient != null && mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            mWebSocketClient.send(new JSONObject().put("op", 3).put("guildid", guildId).toString());
            return true;
        }
        return false;
    }

    public boolean setTyping(int teamId, boolean isTyping) throws JSONException {
        if (mWebSocketClient != null && mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            mWebSocketClient.send(new JSONObject().put("op", 4).put("team_id", teamId).put("is_typing", isTyping).toString());
            return true;
        }
        return false;
    }

    public void listenCallback(String tag, WebsocketCallback callback) {
        callbacks.put(tag.toUpperCase().replaceAll(" ", "_"), callback);
    }

    public void connectWebSocket() {
        new AsyncTask<URI, Integer, URI>() {
            @Override
            protected URI doInBackground(URI... uri) {

                URI tmp = null;

                try {
                    JSONObject gateway = (JSONObject) (RESTService.Get("gateway"));
                    tmp = new URI(gateway.getString("url"));
                    System.out.println("Websocket Connecting to: " + tmp.toString());
                } catch (IOException | JSONException | URISyntaxException e) {
                    e.printStackTrace();
                }

                System.out.println("Websocket onPostExecute: " + tmp.toString());
                mWebSocketClient = new WebSocketClient(tmp, new Draft_17()) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        System.out.println("Websocket URI: " + this.getURI());
                        Log.i("Websocket", "Opened");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("op", 2);
                            json.put("token", pref.getString("TOKEN", null));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        this.send(json.toString());

                        new AsyncTask<Integer, Integer, Integer>() {

                            @Override
                            protected Integer doInBackground(Integer... integers) {
                                try {
                                    while (true) {
                                        mWebSocketClient.send("{\"op\":1}");
                                        Thread.sleep(100);
                                        //    System.out.println(this.getReadyState());
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return 0;
                            }
                        }.execute();
                    }

                    @Override
                    public void onMessage(String s) {

                        System.out.println("Websocket : onMessage: " + s);
                        try {
                            JSONObject message = new JSONObject(s);
                            if (callbacks.containsKey(message.getString("t"))) {
                                callbacks.get(message.getString("t")).run(message.getJSONObject("d"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                        /*final String message = s;
                          runOnUiThread(new Runnable() {
                              @Override
                             public void run() {
                        JSONObject payload = null;
                        try {
                            payload = new JSONObject(message);
                            JSONObject data = payload.getJSONObject("d");
                            System.out.println("Websocket: " + message);

                            switch (payload.getString("t")) {
                                case "READY":
                                    (new AsyncTask<WebSocketClient, Integer, String>() {
                                        @Override
                                        protected String doInBackground(WebSocketClient[] sockets) {


                                            try {
                                                while (true) {
                                                    sockets[0].send("{\"op\":1}");
                                                    Thread.sleep(1000);
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            return null;
                                        }
                                    }).execute(this);
                                    break;
                                case "MESSAGE_CREATE":
                                    ChatMessage chatMessage = new ChatMessage(data.getString("content"), data.getJSONObject("author").getString("displayname"), data.getLong("timestamp"));
                                    System.out.println(chatMessage.getMessage());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //  }
                        //  });*/
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.i("Websocket", "Closed , code = " + Integer.toString(i) + ", reason '" + s + "', was clean = " + Boolean.toString(b));
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Websocket", "Error " + e.getMessage());
                    }
                };
                try {
                    mWebSocketClient.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}