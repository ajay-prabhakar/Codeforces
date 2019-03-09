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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;



public class Async extends AsyncTask<String,Void,ArrayList<Codeforces>> {

    private Activity activity;

    public Async(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Codeforces> doInBackground(String... strings) {
        String CODEFORCE_API=strings[0],jsonResponse =null;

        URL url=CodeforcesURL(CODEFORCE_API);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extract(jsonResponse);

    }

    private static ArrayList<Codeforces> extract(String jsonResponse){
        ArrayList<Codeforces> CodeforcesList = new ArrayList<Codeforces>();
        try {
            JSONObject basejsonResponse = new JSONObject(jsonResponse);

            JSONArray res = basejsonResponse.getJSONArray("result");
            for(int i=0; i<res.length(); i++){
                Codeforces codeforces = new Codeforces();
                codeforces.setContestName(res.getJSONObject(i).getString("contestName"));
                codeforces.setRank(Integer.parseInt(res.getJSONObject(i).getString("rank")));
                codeforces.setOldRating(Integer.parseInt(res.getJSONObject(i).getString("oldRating")));
                codeforces.setNewRating(Integer.parseInt(res.getJSONObject(i).getString("newRating")));
                codeforces.setChange(codeforces.getNewRating()-codeforces.getOldRating());
                CodeforcesList.add(codeforces);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CodeforcesList=reverse(CodeforcesList);
        return CodeforcesList;
    }


    static ArrayList<Codeforces> reverse(ArrayList<Codeforces> CodeforcesList){
        ArrayList<Codeforces> newList = new ArrayList<>();
        for(int i=CodeforcesList.size()-1; i>=0; i--)
            newList.add(CodeforcesList.get(i));
        return newList;
    }

    @Override
    protected void onPostExecute(ArrayList<Codeforces> codeforces) {
        RecyclerView recyclerView = activity.findViewById(R.id.contestsAppeared);
        CodeforcesAdapter adapter = new CodeforcesAdapter(activity, codeforces);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private static URL CodeforcesURL(String stringUrl) {
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
