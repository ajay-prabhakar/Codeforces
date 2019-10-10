package com.example.android.codeforces.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.example.android.codeforces.R;
import static com.example.android.codeforces.Constants.contestUrlKey;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String url = "";
        if (getIntent() != null) {
            url = getIntent().getExtras().getString(contestUrlKey);
        }

        webView = findViewById(R.id.webView);
        webView.loadUrl(url);
    }
}