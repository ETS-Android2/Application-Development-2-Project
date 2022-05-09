package com.example.moodplanet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPHandler {
    public HTTPHandler() {
    }

    public String makeServiceCall(String reqURL) {
        String response = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());  // this in will get all the data
            response = convertStreamtoString(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Build the string
     * @param in integer data
     * @return String
     */
    private String convertStreamtoString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;

        try {
            while((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString(); // build string and sent it back
    }
}
