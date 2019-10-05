package com.example.android.codeforces;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new RatingLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        ArrayList<Contest> contestList = new ArrayList<Contest>();
        try {
            JSONObject basejsonResponse = new JSONObject(data);
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
        final FloatingActionButton fab = findViewById(R.id.fab);

        final RecyclerView recyclerView = findViewById(R.id.contestsAppeared);
        ContestsAppearedAdapter adapter = new ContestsAppearedAdapter(this, contestList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int positionView = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (positionView > 0) {
                    if(fab.getVisibility() != View.VISIBLE) {
                        fab.show();
                    }
                } else  {
                    if(fab.getVisibility() == View.VISIBLE) {
                        fab.hide();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    static ArrayList<Contest> reverse(ArrayList<Contest> contestList){
        ArrayList<Contest> newList = new ArrayList<>();
        for(int i=contestList.size()-1; i>=0; i--)
            newList.add(contestList.get(i));
        return newList;
    }
}
