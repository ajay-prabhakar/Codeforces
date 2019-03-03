package com.example.android.codeforces;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {


    private QueryUtils() {

    }

    private static List<Codeforces> extractFeatureFromJson(String codeforcesJSON) {
        if (TextUtils.isEmpty(codeforcesJSON)) {
            return null;
        }
        List<Codeforces> codeforcess = new ArrayList<>();


        try {
            JSONObject baseJsonResponse = new JSONObject(codeforcesJSON);

            JSONArray codeforcesArray = baseJsonResponse.getJSONArray("result");

            for (int i = 0; i < codeforcesArray.length(); i++) {
                JSONObject currentCodeforces = codeforcesArray.getJSONObject(i);




                String name = currentCodeforces.getString("name");
                Integer duration = currentCodeforces.getInt("durationSeconds");
                Integer startTimeSecond;
                if(currentCodeforces.isNull("startTimeSeconds")) {
                startTimeSecond= 0;

                }
                else{
                    startTimeSecond = currentCodeforces.getInt("startTimeSeconds");



                }

                Codeforces codeforces = new Codeforces(name, duration, startTimeSecond);
                codeforcess.add(codeforces);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("QueryUtils", "Problem parsing the codeforces JSON results", e);

        }
        return codeforcess;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {

            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }


    public static List<Codeforces> fetchEarthquakeData(String requestUrl) {
        
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

        }



        List<Codeforces> codeforcess = extractFeatureFromJson(jsonResponse);

        Log.i( "main thing ",codeforcess.toString());
        return codeforcess;
    }


}
