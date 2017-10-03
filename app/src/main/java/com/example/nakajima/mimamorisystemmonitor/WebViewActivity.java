package com.example.nakajima.mimamorisystemmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    public static final String EXTRA_NOTIFY_IMG = "img";
    public static final String EXTRA_NOTIFY_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = (WebView)findViewById(R.id.web_view);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + "MIMAMORI-SYSTEM-MONITOR");

        Intent intent = getIntent();
        String ip = intent.getStringExtra(EXTRA_NOTIFY_URL);
        mWebView.loadUrl("http://"+ ip +"/");
    }
}
