package com.example.android.codeforces.Utils;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.codeforces.Adapter.ContestsAppearedAdapter;
import com.example.android.codeforces.Listeners.ContestItemClickListener;
import com.example.android.codeforces.Model.Contest;
import com.example.android.codeforces.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RatingAsync extends AsyncTask<String, Void, ArrayList<Contest>>
        implements ContestItemClickListener {

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

    private static ArrayList<Contest> extract(String jsonResponse) {
        ArrayList<Contest> contestList = new ArrayList<Contest>();
        try {
            JSONObject basejsonResponse = new JSONObject(jsonResponse);
            JSONArray res = basejsonResponse.getJSONArray("result");
            for (int i = 0; i < res.length(); i++) {
                JSONObject currentContest = res.getJSONObject(i);
                int change = currentContest.getInt("newRating") - currentContest.getInt("oldRating");
                Contest contest =
                        new Contest(
                                currentContest.getInt("contestId"),
                                currentContest.getString("contestName"),
                                currentContest.getInt("rank"),
                                currentContest.getInt("oldRating"),
                                change,
                                currentContest.getInt("newRating"));
                contestList.add(contest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        contestList = reverse(contestList);
        return contestList;
    }

    static ArrayList<Contest> reverse(ArrayList<Contest> contestList) {
        ArrayList<Contest> newList = new ArrayList<>();
        for (int i = contestList.size() - 1; i >= 0; i--) newList.add(contestList.get(i));
        return newList;
    }

    @Override
    protected void onPostExecute(ArrayList<Contest> contests) {
        RecyclerView recyclerView = activity.findViewById(R.id.contestsAppeared);
        ContestsAppearedAdapter adapter = new ContestsAppearedAdapter(activity, contests, this);
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

        if (url == null) return jsonResponse;

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
            if (inputStream != null) inputStream.close();
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

    @Override
    public void onClick(int contestId) {}
}
