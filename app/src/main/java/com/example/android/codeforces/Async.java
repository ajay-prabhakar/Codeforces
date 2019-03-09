package com.example.android.codeforces;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import static com.example.android.codeforces.Loader.CodeforcesURL;
import static com.example.android.codeforces.Loader.makeHttpRequest;
import static java.util.Collections.reverse;

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


}
