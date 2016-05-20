package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.user.treesinthecloud.R;


public class GoogleSearch extends AppCompatActivity{

    private WebView web;
    private String specie, url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_google_search_tree);

        web = (WebView)findViewById(R.id.webView_google_tree);
        specie = getIntent().getExtras().getString("Specie");
        web.setWebViewClient(new MyBrowser());

        if(specie.startsWith("http")){
            url = specie;
            specie = null;
        }

        if (specie != null) {
            specie.replace(" ", "+");
            url = "https://www.google.com/#q=" + specie;
        }

        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web.loadUrl(url);
    }
}

class MyBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
