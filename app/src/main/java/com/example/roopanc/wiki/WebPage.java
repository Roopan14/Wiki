package com.example.roopanc.wiki;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.roopanc.wiki.data.DataItem;

public class WebPage extends Fragment implements View.OnClickListener {

    private DataItem dataItem;
    private WebView webView;
    private ImageView imageView;
    private TextView titleTv, descriptionTv;
    private ProgressBar progressBar;
    String title, description, weburl, urltitle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webpage, container, false);
        getData();
        progressBar = view.findViewById(R.id.progressbarwv);
        progressBar.setVisibility(View.VISIBLE);
        webView = view.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(weburl);
        imageView = view.findViewById(R.id.backweb);
        imageView.setOnClickListener(this);
        titleTv = view.findViewById(R.id.titleweb);
        titleTv.setText(title);
        descriptionTv = view.findViewById(R.id.descriptionweb);
        descriptionTv.setText(description);
        return view;
    }

    private void getData() {
        dataItem = getArguments().getParcelable("data");
        if (dataItem != null)
        {
            title = dataItem.getTitle();
            urltitle = title.replaceAll(" ", "_");
            description = dataItem.getDescription();
            weburl = "https://en.wikipedia.org/wiki/"+urltitle;
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.backweb){
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        }
    }
}
