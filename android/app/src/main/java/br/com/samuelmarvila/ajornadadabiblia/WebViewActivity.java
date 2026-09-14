package br.com.samuelmarvila.ajornadadabiblia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewActivity extends Activity {
    private WebView web;
    private ProgressBar progress;
    private String url;

    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
    private GradientDrawable bg(int color, int radius) { GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d; }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        url = getIntent().getStringExtra("url");
        if (url == null) url = "https://www.sbb.org.br/biblia/NTLH/LEV.1";

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(12), dp(10), dp(12), 0);
        root.setBackgroundColor(Color.rgb(246,247,245));

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = new TextView(this);
        title.setText("📖  NTLH"); title.setTextSize(20); title.setTypeface(Typeface.DEFAULT, Typeface.BOLD); title.setTextColor(Color.rgb(22,75,59));
        top.addView(title, new LinearLayout.LayoutParams(0, dp(48), 1));
        Button browser = new Button(this); browser.setText("Abrir fora"); browser.setAllCaps(false); browser.setTextSize(13); browser.setMinHeight(dp(44));
        browser.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))));
        top.addView(browser, new LinearLayout.LayoutParams(dp(110), dp(48)));
        root.addView(top);

        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100); progress.setVisibility(ProgressBar.INVISIBLE);
        root.addView(progress, new LinearLayout.LayoutParams(-1, dp(3)));

        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setBuiltInZoomControls(false); s.setDisplayZoomControls(false); s.setLoadWithOverviewMode(true); s.setUseWideViewPort(true);
        web.setBackgroundColor(Color.WHITE);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri target = request.getUrl();
                if (target.getHost() != null && target.getHost().endsWith("sbb.org.br")) return false;
                startActivity(new Intent(Intent.ACTION_VIEW, target)); return true;
            }
            @Override public void onPageStarted(WebView view, String u, android.graphics.Bitmap favicon) { progress.setVisibility(ProgressBar.VISIBLE); }
            @Override public void onPageFinished(WebView view, String u) { progress.setProgress(100); progress.setVisibility(ProgressBar.INVISIBLE); }
        });
        root.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
        web.loadUrl(url);
    }

    @Override public void onBackPressed() { if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed(); }
    @Override protected void onDestroy() { if (web != null) web.destroy(); super.onDestroy(); }
}
