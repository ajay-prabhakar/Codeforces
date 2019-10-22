package com.example.android.codeforces.Activities;

import static com.example.android.codeforces.Constants.API_URL;
import static com.example.android.codeforces.Constants.CHANGE;
import static com.example.android.codeforces.Constants.NEGATIVE_CHANGE;
import static com.example.android.codeforces.Constants.POSITIVE_CHANGE;
import static com.example.android.codeforces.Constants.contestUrlKey;
import static com.example.android.codeforces.Constants.preferredHandleKey;

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
import com.example.android.codeforces.Adapter.ContestsAppearedAdapter;
import com.example.android.codeforces.BottomSheet.SortBottomSheetView;
import com.example.android.codeforces.Listeners.ContestItemClickListener;
import com.example.android.codeforces.Listeners.SortClickListener;
import com.example.android.codeforces.Model.Contest;
import com.example.android.codeforces.R;
import com.example.android.codeforces.Utils.RatingLoader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFeedActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>, ContestItemClickListener, SortClickListener {

    private String preferredHandle = "";
    private ProgressBar pbProgressBar;
    private RecyclerView recyclerView;
    private ArrayList<Contest> contestList = new ArrayList<>();
    private FloatingActionButton fab;
    private ContestsAppearedAdapter adapter;
    private TextView tvEmptyList;
    private int currentSortState = CHANGE;
    private ArrayList<Contest> positiveChangeList = new ArrayList<>();
    private ArrayList<Contest> negativeChangeList = new ArrayList<>();
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

        recyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        int positionView =
                                ((LinearLayoutManager) recyclerView.getLayoutManager())
                                        .findFirstVisibleItemPosition();

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

        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView.scrollToPosition(0);
                    }
                });

        if (getIntent() != null) {
            preferredHandle = getIntent().getExtras().getString(preferredHandleKey);
            getSupportActionBar().setSubtitle(String.format("Handle : %s", preferredHandle));
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
            client.get(
                    "https://codeforces.com/api/user.info?handles=" + preferredHandle,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            startActivity(
                                    new Intent(HomeFeedActivity.this, ProfileActivity.class)
                                            .putExtra("profile", new String(responseBody)));
                        }

                        @Override
                        public void onFailure(
                                int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(
                                            HomeFeedActivity.this, "An unexpected error occurred.", Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onFinish() {
                            progressDialog.dismiss();
                            super.onFinish();
                        }
                    });
        }

        if (id == R.id.filter) {
            SortBottomSheetView bottomSheetView = new SortBottomSheetView();
            Bundle bundle = new Bundle();
            bundle.putInt("sortValue", currentSortState);
            bottomSheetView.setArguments(bundle);
            bottomSheetView.setListener(this);
            bottomSheetView.show(getSupportFragmentManager(), "SortBottomSheetView");
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

        if (contestList.size() == 0) {
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        positiveNegativeChangeList(contestList);
    }

    private void positiveNegativeChangeList(ArrayList<Contest> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getChange() > 0) {
                positiveChangeList.add(list.get(i));
            } else {
                negativeChangeList.add(list.get(i));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    static ArrayList<Contest> reverse(ArrayList<Contest> contestList) {
        ArrayList<Contest> newList = new ArrayList<>();
        for (int i = contestList.size() - 1; i >= 0; i--) newList.add(contestList.get(i));
        return newList;
    }

    @Override
    public void onClick(int contestId) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(contestUrlKey, "https://codeforces.com/contest/" + contestId);
        startActivity(intent);
    }

    @Override
    public void buttonClicked(int button) {
        switch (button) {
            case CHANGE: {
                currentSortState = CHANGE;
                adapter = new ContestsAppearedAdapter(this, contestList, this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            }
            case NEGATIVE_CHANGE: {
                currentSortState = NEGATIVE_CHANGE;
                adapter = new ContestsAppearedAdapter(this, negativeChangeList, this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            }
            case POSITIVE_CHANGE: {
                currentSortState = POSITIVE_CHANGE;
                adapter = new ContestsAppearedAdapter(this, positiveChangeList, this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            }
        }
    }
}
