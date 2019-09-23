package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;


       //Creating WebView to display website where user can control printer.

public class WebViewActivity extends AppCompatActivity {

    public WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //Referencing WebView layout

        mWebView = findViewById(R.id.activity_main_webview);

        //Enable JavasScript for webView

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Loading website using LoadUrl
        mWebView.loadUrl("http://10.0.0.115/");


    }
}
