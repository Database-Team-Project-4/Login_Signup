package twitter.service;

import org.json.JSONObject;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
public class GeminiService {

    public static String callGeminiApi(String prompt, JTextArea responseArea) throws Exception 
    {
        String encodedPrompt = new URI(null, null, prompt, null).toASCIIString();
        String urlString = "http://58.121.110.129:8000/" + encodedPrompt;
        
        URL url = new URL(urlString);
        System.out.println("URL: " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        SwingUtilities.invokeLater(() -> {
            responseArea.setText("API Calling...\n"); 
        });

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                 // example: {"response": "I'm a Gemini AI model. I can generate text based on the input prompt."}
                return jsonResponse.getString("response");
            }
        } else if (responseCode == 401) {
            throw new RuntimeException("401 Unauthorized. API 키와 권한을 확인하세요.");
        } else if (responseCode == 400) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                StringBuilder errorResponse = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                throw new RuntimeException("400 Bad Request: " + errorResponse.toString());
            }
        } else {
            throw new RuntimeException("HTTP error code : " + responseCode);
        }
    }
}