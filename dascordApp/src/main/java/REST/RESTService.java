package REST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sara on 18/04/2017.
 */

public class RESTService {
        private static URL baseUrl;
        private static URLConnection con;
        private static HttpURLConnection http;

    public RESTService(String url) throws IOException {
        baseUrl = new URL(url);
        con = baseUrl.openConnection();
        http = (HttpURLConnection) con;

    }


    public static String Get(String field) throws IOException {
        String result = "";
        baseUrl = new URL(baseUrl + "/" + field);
        // optional default is GET
        http.setRequestMethod("GET");

        //add request header
//        http.setRequestProperty(field, result);

        int responseCode = http.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + baseUrl);
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
        return response.toString();


    }


    public static void Post(String endpoint, Map<String, String> postBody) throws IOException {
        http.setDoOutput(true);
        http.setRequestMethod("POST");

        StringBuilder sj = new StringBuilder();
        for(Map.Entry<String,String> entry : postBody.entrySet()) {
            sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
        }
        byte[] out = sj.toString().getBytes();

        http.setFixedLengthStreamingMode(out.length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.connect();
        try
        {
            OutputStream os = http.getOutputStream();
            os.write(out);

        }
        catch (Exception e)
        {

        }
        System.out.println(http.getResponseCode());
        System.out.println(http.getResponseMessage());
        http.disconnect();
    }



    public static int Login(String username, String password) throws IOException {
        Map<String, String> arguments = new HashMap<>();
        arguments.put(username, password);
        try {
            Post("login", arguments);
            return 200;
        } catch (IOException e) {
            e.printStackTrace();
            return http.getResponseCode();
        }



    }
}

