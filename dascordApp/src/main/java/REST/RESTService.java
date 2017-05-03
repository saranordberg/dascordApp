package REST;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sara on 18/04/2017.
 */

public class RESTService {
    private static String baseAPI = "http://ubuntu4.javabog.dk:43232/dascord/api/";
    private static URLConnection con;
    private static HttpURLConnection http;
    private static CookieManager cookMan;
    private static RESTService instance;

    private RESTService() {
        cookMan = new CookieManager();
        cookMan.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookMan);

        // TODO(Sara, Rasmus): Need to check cookie handling.
        // The connection between calls need to use the same session cookie otherwise
        // each call on the connection is on a new session.
    }
    public static RESTService instance(){
        if(instance == null) instance = new RESTService();
        return instance;
    }

    public static JSONObject Get(String endpoint) throws IOException, JSONException {
        con = new URL(baseAPI + endpoint).openConnection();
        http = (HttpURLConnection) con;

        http.setRequestMethod("GET");

        //add request header
//        http.setRequestProperty(field, result);

        int responseCode = http.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + baseAPI);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(http.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        int status = http.getResponseCode();
        String content = response.toString();
        http.disconnect();
        JSONObject response2 = new JSONObject(content);
        response2.put("status", status);
        return response2;

    }

    public static JSONObject Post(String endpoint, Map<String, String> postBody) throws IOException, JSONException {
        con = new URL(baseAPI + endpoint).openConnection();
        http = (HttpURLConnection) con;

        http.setDoOutput(true);
        http.setRequestMethod("POST");

        StringBuilder sj = new StringBuilder();
        for (Map.Entry<String, String> entry : postBody.entrySet()) {
            sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
        }
        byte[] out = sj.toString().getBytes();

        http.setFixedLengthStreamingMode(out.length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.connect();
        try {
            OutputStream os = http.getOutputStream();
            os.write(out);

        } catch (Exception e) {

        }
        int status = http.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(status == 200 ? http.getInputStream() : http.getErrorStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String content = response.toString();
        http.disconnect();
        if (status != 200){
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", status);
            if (content.length() > 0) {
                errorResponse.put("Errormessage", content);
            } else{
                errorResponse.put("Errormessage", "");
            }
            return errorResponse;
        }
        JSONObject response2 = new JSONObject(content);
        response2.put("status", status);
        return response2;
    }

    public String Login(String username, String password) throws IOException {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);
        try {
            JSONObject response = Post("login", arguments);
            if(response.getInt("status") != 200) {
                throw new IOException((response.getInt("status") + ", error: " +
                        response.getString("Errormessage")));
            }
            return response.getString("token");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User Userinfo() throws IOException {
        try {
            JSONObject response = Get("userinfo");
            if(response.getInt("status") != 200) {
                throw new IOException((response.getInt("status") + ", error: " +
                        response.getString("Errormessage")));
            }
            return new User(response.getInt("id"), response.getString("displayname"),
                    response.optString("image"));
        } catch (IOException | JSONException  e) {
            e.printStackTrace();
        }
        return null;
    }
}

