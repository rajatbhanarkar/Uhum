package com.example.somebody;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.io.File;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    String url;

    Dialog LoaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView)findViewById(R.id.webview);

        url = getIntent().getStringExtra("URL");

        LoaderDialog = new Dialog(WebViewActivity.this);
        View vieww = getLayoutInflater().inflate(R.layout.loader_layout, null);
        ProgressBar progressBar = (ProgressBar) vieww.findViewById(R.id.spinKit);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        LoaderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LoaderDialog.setContentView(vieww);
        LoaderDialog.show();

        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);

        CountDownTimer countDownTimer = new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                LoaderDialog.dismiss();
            }
        }.start();

    }

    class MyBrowser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
