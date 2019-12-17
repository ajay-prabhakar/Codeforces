package com.example.android.codeforces.Activities;

import static com.example.android.codeforces.Constants.preferredHandleKey;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android.codeforces.R;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etPreferredHandle;
    private ImageView ivSubmit;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPreferredHandle = findViewById(R.id.etPreferredHandle);
        ivSubmit = findViewById(R.id.ivSubmit);
        drawerLayout = findViewById(R.id.dashboardNavigationDrawer);
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_nav_btn);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        etPreferredHandle.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (TextUtils.isEmpty(v.getText())) {
                                v.setError(getString(R.string.empty_field));
                            } else {
                                Intent myIntent = new Intent(MainActivity.this, HomeFeedActivity.class);
                                myIntent.putExtra(preferredHandleKey, v.getText().toString());
                                startActivity(myIntent);
                            }
                        }
                        return false;
                    }
                });

        ivSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etPreferredHandle.getText())) {
                            etPreferredHandle.requestFocus();
                            etPreferredHandle.setError(getString(R.string.empty_field));
                        } else {
                            Intent myIntent = new Intent(MainActivity.this, HomeFeedActivity.class);
                            myIntent.putExtra(preferredHandleKey, etPreferredHandle.getText().toString());
                            startActivity(myIntent);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
