package com.example.android.codeforces;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String CODEFORCES_URL = "https://codeforces.com/api/user.rating?handle=sandeshghanta";

    private CodeforcesAdapter mAdapter;
    List<Codeforces> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Uri uri=Uri.parse(CODEFORCES_URL);
        getLoaderManager().initLoader(0,null,  this);


    }



    static ArrayList<Codeforces> reverse(ArrayList<Codeforces> CodeforcesList){
        ArrayList<Codeforces> newList = new ArrayList<>();
        for(int i=CodeforcesList.size()-1; i>=0; i--)
            newList.add(CodeforcesList.get(i));
        return newList;
    }


    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new Loader(this);


    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {

        ArrayList<Codeforces> CodeforcesList = new ArrayList<Codeforces>();
        try {
            JSONObject basejsonResponse = new JSONObject(s);
            JSONArray res = basejsonResponse.getJSONArray("result");
            for(int i=0; i<res.length(); i++){
                Codeforces contest = new Codeforces();
                contest.setContestName(res.getJSONObject(i).getString("contestName"));
                contest.setRank(Integer.parseInt(res.getJSONObject(i).getString("rank")));
                contest.setOldRating(Integer.parseInt(res.getJSONObject(i).getString("oldRating")));
                contest.setNewRating(Integer.parseInt(res.getJSONObject(i).getString("newRating")));
                contest.setChange(contest.getNewRating()-contest.getOldRating());
                CodeforcesList.add(contest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CodeforcesList = reverse(CodeforcesList);
        RecyclerView recyclerView = findViewById(R.id.contestsAppeared);
        CodeforcesAdapter adapter = new CodeforcesAdapter(this, CodeforcesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


//    @Override
//    public void onLoadFinished(Loader<Object> loader,String data) {
//        ArrayList<Codeforces> CodeforcesList = new ArrayList<Codeforces>();
//        try {
//            JSONObject basejsonResponse = new JSONObject(data);
//            JSONArray res = basejsonResponse.getJSONArray("result");
//            for(int i=0; i<res.length(); i++){
//                Codeforces contest = new Codeforces();
//                contest.setContestName(res.getJSONObject(i).getString("contestName"));
//                contest.setRank(Integer.parseInt(res.getJSONObject(i).getString("rank")));
//                contest.setOldRating(Integer.parseInt(res.getJSONObject(i).getString("oldRating")));
//                contest.setNewRating(Integer.parseInt(res.getJSONObject(i).getString("newRating")));
//                contest.setChange(contest.getNewRating()-contest.getOldRating());
//                CodeforcesList.add(contest);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        CodeforcesList = reverse(CodeforcesList);
//        RecyclerView recyclerView = findViewById(R.id.contestsAppeared);
//        CodeforcesAdapter adapter = new CodeforcesAdapter(this, CodeforcesList);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
