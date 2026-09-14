package br.com.samuelmarvila.ajornadadabiblia;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;

public class WebViewActivity extends Activity {
    private WebView web;
    private String url;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        url = getIntent().getStringExtra("url");
        if (url == null) url = "https://www.sbb.org.br/biblia/NTLH/LEV.1";

        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.WHITE);
        Button browser = new Button(this); browser.setText("Abrir no navegador"); browser.setAllCaps(false);
        browser.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))); root.addView(browser, new LinearLayout.LayoutParams(-1, -2));

        web = new WebView(this); WebSettings s = web.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setBuiltInZoomControls(false); s.setDisplayZoomControls(false); s.setLoadWithOverviewMode(true); s.setUseWideViewPort(true);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri target = request.getUrl();
                if (target.getHost() != null && target.getHost().endsWith("sbb.org.br")) return false;
                startActivity(new Intent(Intent.ACTION_VIEW, target)); return true;
            }
        });
        root.addView(web, new LinearLayout.LayoutParams(-1, 0, 1)); setContentView(root); web.loadUrl(url);
    }

    @Override public void onBackPressed() { if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
