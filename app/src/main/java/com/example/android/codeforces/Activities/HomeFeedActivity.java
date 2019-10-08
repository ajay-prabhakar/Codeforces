package com.example.android.codeforces.Activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.codeforces.Adapter.ContestsAppearedAdapter;
import com.example.android.codeforces.Listeners.ContestItemClickListener;
import com.example.android.codeforces.Model.Contest;
import com.example.android.codeforces.R;
import com.example.android.codeforces.Utils.RatingLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.example.android.codeforces.Constants.API_URL;
import static com.example.android.codeforces.Constants.contestUrlKey;
import static com.example.android.codeforces.Constants.preferredHandleKey;

public class HomeFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, ContestItemClickListener {

    private String preferredHandle = "";
    private ProgressBar pbProgressBar;
    private RecyclerView recyclerView;
    private ArrayList<Contest> contestList = new ArrayList<>();
    private FloatingActionButton fab;
    private ContestsAppearedAdapter adapter;
    private TextView tvEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);

        pbProgressBar = findViewById(R.id.pbProgressBar);
        recyclerView = findViewById(R.id.contestsAppeared);
        fab = findViewById(R.id.fab);
        tvEmptyList = findViewById(R.id.tvEmptyList);

        adapter = new ContestsAppearedAdapter(this, contestList, this);
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

        if (getIntent() != null) {
            preferredHandle = getIntent().getExtras().getString(preferredHandleKey);
        }

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new RatingLoader(this, API_URL + preferredHandle);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        pbProgressBar.setVisibility(View.GONE);

        try {
            JSONObject baseJsonResponse = new JSONObject(data);
            JSONArray res = baseJsonResponse.getJSONArray("result");
            for(int i=0; i<res.length(); i++){
                JSONObject currentContest = res.getJSONObject(i);
                int change = currentContest.getInt("newRating") - currentContest.getInt("oldRating");
                Contest contest = new Contest(
                        currentContest.getInt("contestId"),
                        currentContest.getString("contestName"),
                        currentContest.getInt("rank"),
                        currentContest.getInt("oldRating"),
                        change,
                        currentContest.getInt("newRating")
                );
                contestList.add(contest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        contestList = reverse(contestList);

        if (contestList.size() == 0) {
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
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

    @Override
    public void onClick(int contestId) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(contestUrlKey, "https://codeforces.com/contest/" + contestId);
        startActivity(intent);
    }
}

