package com.example.android.codeforces;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import java.util.ArrayList;

public class RatingAsync extends AsyncTask<String, Void, ArrayList<Contest>> {

    private Activity activity;

    public RatingAsync(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Contest> doInBackground(String... strings) {
        String api = strings[0], jsonResponse = null;
        URL url = createUrl(api);

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extract(jsonResponse);

    }

    private static ArrayList<Contest> extract(String jsonResponse){
        ArrayList<Contest> contestList = new ArrayList<Contest>();
        try {
            JSONObject basejsonResponse = new JSONObject(jsonResponse);
            JSONArray res = basejsonResponse.getJSONArray("result");
            for(int i=0; i<res.length(); i++){
                Contest contest = new Contest();
                contest.setContestName(res.getJSONObject(i).getString("contestName"));
                contest.setRank(Integer.parseInt(res.getJSONObject(i).getString("rank")));
                contest.setOldRating(Integer.parseInt(res.getJSONObject(i).getString("oldRating")));
                contest.setNewRating(Integer.parseInt(res.getJSONObject(i).getString("newRating")));
                contest.setChange(contest.getNewRating()-contest.getOldRating());
                contestList.add(contest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        contestList = reverse(contestList);
        return contestList;
    }

    static ArrayList<Contest> reverse(ArrayList<Contest> contestList){
        ArrayList<Contest> newList = new ArrayList<>();
        for(int i=contestList.size()-1; i>=0; i--)
            newList.add(contestList.get(i));
        return newList;
    }

    @Override
    protected void onPostExecute(ArrayList<Contest> contests) {
        RecyclerView recyclerView = activity.findViewById(R.id.contestsAppeared);
        ContestsAppearedAdapter adapter = new ContestsAppearedAdapter(activity, contests);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
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

        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null)
                inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
