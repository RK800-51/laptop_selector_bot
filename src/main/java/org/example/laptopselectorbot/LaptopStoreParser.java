package org.example.laptopselectorbot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import org.json.*;

public class LaptopStoreParser {
    private static final String API_URL = "https://noteb.com/api/webservice.php";
    private static final String API_KEY = "112233aabbcc";
    private static final String METHOD = "list_models";

    // String modelId = "5509"; // Lenovo Legion Pro 6766 (AMD), 6770 (Intel)

    public ArrayList<Integer> getLaptopsIDByCPU(Map<String, String> inputMap, Laptop laptopObj) throws IOException {
        // creating laptop object
        String modelName = inputMap.get("model_name");
        // Laptop laptopObj = new Laptop();
        laptopObj.setModel(modelName);
        laptopObj.setBudget(Integer.parseInt(inputMap.get("budget")));

        if (inputMap.get("cpu_name") != null) {
            laptopObj.setCpu(inputMap.get("cpu_name").toLowerCase());
        }

        if (inputMap.get("gpu_name") != null) {
            laptopObj.setGpu(inputMap.get("gpu_name").toLowerCase());
        }

        if (inputMap.get("memory_type") != null) {
            laptopObj.setMemoryType(inputMap.get("memory_type").toLowerCase());
        }

        // creating request object and sending POST request
        StringBuilder postData = new StringBuilder();
        postData.append("apikey=").append(API_KEY);
        postData.append("&method=").append(METHOD);

        if ((modelName != null) && (!modelName.isEmpty())) {
            postData.append("&param[model_name]=").append(modelName);
        }

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(postData.toString().getBytes(StandardCharsets.UTF_8));
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        // getting response
        JSONObject jsonObj = new JSONObject(response.toString());
        conn.disconnect();
        JSONObject laptopList = jsonObj.getJSONObject("result");

        if (laptopObj.getCpu() != null) {
            ArrayList <Integer> laptopListIDByCpu = new ArrayList<>();
            for (String key : laptopList.keySet()) { // iterate over a laptop in "result" json
                JSONObject modelInfo = laptopList.getJSONObject(key).getJSONArray("model_info").
                        getJSONObject(0);
                String cpuResponse = ((String) modelInfo.get("extra_name")).toLowerCase().replaceAll("[^a-z]", "");
                if (cpuResponse.equals(laptopObj.getCpu())) {
                    Integer id = (Integer) modelInfo.get("id");
                    laptopListIDByCpu.add(id);
                }
                else if (cpuResponse.isEmpty() && laptopObj.getCpu().equals("amd")) {
                    Integer id = (Integer) modelInfo.get("id");
                    laptopListIDByCpu.add(id);
                }
            }
            return laptopListIDByCpu; // returns laptop IDs filtered by user's cpu
        }
        else {
            ArrayList <Integer> laptopListID = new ArrayList<>();
            for (String key : laptopList.keySet()) { // iterate over a laptop in "result" json
                JSONObject modelInfo = laptopList.getJSONObject(key).getJSONArray("model_info").
                        getJSONObject(0);
                laptopListID.add((Integer) modelInfo.get("id"));

            }
            return laptopListID; // returns all laptop IDs

        }
    }
}


