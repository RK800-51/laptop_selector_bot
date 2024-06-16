package org.example.laptopselectorbot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestSenderByID {
    private static final String API_URL = "https://noteb.com/api/webservice.php";
    private static final String API_KEY = "112233aabbcc";
    private static final String METHOD = "get_model_info";

    public static JSONObject getCandidate(Integer modelID) throws IOException {
        StringBuilder postData = new StringBuilder();
        postData.append("apikey=").append(API_KEY);
        postData.append("&method=").append(METHOD);

        if (modelID != null) {
            postData.append("&param[model_id]=").append(modelID);
        }

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        // System.out.println(postData);
        os.write(postData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        JSONObject jsonObj = new JSONObject(response.toString());
        conn.disconnect();
        JSONObject oneLaptopJSON = jsonObj.getJSONObject("result");

        return oneLaptopJSON;
    }
}
