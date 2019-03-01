package com.example.android.codeforces;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String CODEFORCES_URL = "https://codeforces.com/api/contest.list?gym=true";

    private CodeforcesAdapter mAdapter;
    List<Codeforces> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CodeforcesAsyncTask task = new CodeforcesAsyncTask();
        task.execute(CODEFORCES_URL);


        ListView codeForcesListView = (ListView) findViewById(R.id.rvList);


        mAdapter = new CodeforcesAdapter(this,new ArrayList<Codeforces>());


        codeForcesListView.setAdapter(mAdapter);


    }


    private class CodeforcesAsyncTask extends AsyncTask<String, Void, List<Codeforces>> {


        @Override
        protected List<Codeforces> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            result = QueryUtils.fetchCodeforcesData(CODEFORCES_URL);

            Log.i("array list ",result.toString());
            return result;

        }

        @Override
        protected void onPostExecute(List<Codeforces> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }


        }
    }

}
