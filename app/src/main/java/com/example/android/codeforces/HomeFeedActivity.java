package com.example.android.codeforces;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.example.android.codeforces.Constants.API_URL;
import static com.example.android.codeforces.Constants.preferredHandleKey;

public class HomeFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

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

        adapter = new ContestsAppearedAdapter(this, contestList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int positionView = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (positionView > 0) {
                    if (fab.getVisibility() != View.VISIBLE) {
                        fab.show();
                    }
                } else {
                    if (fab.getVisibility() == View.VISIBLE) {
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
            getSupportActionBar().setSubtitle(String.format("Handle : %s",preferredHandle));
        }

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final ProgressDialog progressDialog;

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            progressDialog = new ProgressDialog(HomeFeedActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("https://codeforces.com/api/user.info?handles=" + preferredHandle, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    startActivity(
                            new Intent(HomeFeedActivity.this, ProfileActivity.class)
                                    .putExtra("profile", new String(responseBody)));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(HomeFeedActivity.this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    progressDialog.dismiss();
                    super.onFinish();
                }
            });

        }
        return super.onOptionsItemSelected(item);
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
            for (int i = 0; i < res.length(); i++) {
                Contest contest = new Contest();
                contest.setContestName(res.getJSONObject(i).getString("contestName"));
                contest.setRank(Integer.parseInt(res.getJSONObject(i).getString("rank")));
                contest.setOldRating(Integer.parseInt(res.getJSONObject(i).getString("oldRating")));
                contest.setNewRating(Integer.parseInt(res.getJSONObject(i).getString("newRating")));
                contest.setChange(contest.getNewRating() - contest.getOldRating());
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

    static ArrayList<Contest> reverse(ArrayList<Contest> contestList) {
        ArrayList<Contest> newList = new ArrayList<>();
        for (int i = contestList.size() - 1; i >= 0; i--)
            newList.add(contestList.get(i));
        return newList;
    }
}

