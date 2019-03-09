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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Uri uri=Uri.parse(CODEFORCES_URL);
        getLoaderManager().initLoader(0,null,  this);


    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new Loader(this);
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        ArrayList<Codeforces> CodeforcesList = new ArrayList<Codeforces>();
        try {
            JSONObject basejsonResponse = new JSONObject(data);
            JSONArray res = basejsonResponse.getJSONArray("result");
            for(int i=0; i<res.length(); i++){
                Codeforces Codeforces = new Codeforces();
                Codeforces.setContestName(res.getJSONObject(i).getString("contestName"));
                Codeforces.setRank(Integer.parseInt(res.getJSONObject(i).getString("rank")));
                Codeforces.setOldRating(Integer.parseInt(res.getJSONObject(i).getString("oldRating")));
                Codeforces.setNewRating(Integer.parseInt(res.getJSONObject(i).getString("newRating")));
                Codeforces.setChange(Codeforces.getNewRating()-Codeforces.getOldRating());
                CodeforcesList.add(Codeforces);
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


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    static ArrayList<Codeforces> reverse(ArrayList<Codeforces> CodeforcesList){
        ArrayList<Codeforces> newList = new ArrayList<>();
        for(int i=CodeforcesList.size()-1; i>=0; i--)
            newList.add(CodeforcesList.get(i));

        Log.i( "reverse:hello ",newList.toString());
        return newList;
    }
}
