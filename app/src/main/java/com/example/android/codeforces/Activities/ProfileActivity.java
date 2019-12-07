package com.example.android.codeforces.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.codeforces.R;
import com.example.android.codeforces.Utils.StringUtils;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("profile"));
            JSONObject data = jsonObject.getJSONArray("result").getJSONObject(0);
            getSupportActionBar().setTitle(data.getString("handle"));

            ImageView photo = findViewById(R.id.photo);
            Picasso.get()
                    .load("https:" + data.getString("titlePhoto"))
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(photo);

            TextView handle, rating, maxrating, rank, maxrank, friendof, lastseen, country, org;
            handle = findViewById(R.id.handle);
            rating = findViewById(R.id.rating);
            maxrating = findViewById(R.id.maxrating);
            rank = findViewById(R.id.rank);
            maxrank = findViewById(R.id.maxrank);
            friendof = findViewById(R.id.friendsof);
            country = findViewById(R.id.country);
            org = findViewById(R.id.organization);

            handle.setText(data.getString("handle"));
            rating.setText(data.getString("rating"));
            maxrating.setText(data.getString("maxRating"));
            rank.setText(data.has("rank") ? StringUtils.toTitleCase(data.getString("rank")) : "N/A");
            maxrank.setText(
                    data.has("maxRank") ? StringUtils.toTitleCase(data.getString("maxRank")) : "N/A");
            friendof.setText(data.has("friendOfCount") ? data.getString("friendOfCount") : "N/A");
            country.setText(data.has("country") ? data.getString("country") : "N/A");
            org.setText(data.has("organization") ? data.getString("organization") : "N/A");

        } catch (JSONException e) {
            Toast.makeText(this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
    }
}
