package br.com.samuelmarvila.ajornadadabiblia;

import android.app.*;import android.content.*;import android.graphics.*;import android.graphics.drawable.*;import android.net.Uri;import android.os.*;import android.view.*;import android.webkit.*;import android.widget.*;

public class WebViewActivity extends Activity{
 private WebView web;private ProgressBar progress;private String url;
 private static final int NAVY=Color.rgb(25,48,67),GOLD=Color.rgb(205,157,58),BG=Color.rgb(247,244,237),WHITE=Color.WHITE;
 private int dp(int v){return(int)(v*getResources().getDisplayMetrics().density+.5f);}
 private GradientDrawable bg(int c,int r){GradientDrawable d=new GradientDrawable();d.setColor(c);d.setCornerRadius(dp(r));return d;}
 @Override public void onCreate(Bundle state){super.onCreate(state);url=getIntent().getStringExtra("url");if(url==null)url="https://www.sbb.org.br/biblia/NTLH/LEV.1";
  LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG);
  LinearLayout top=new LinearLayout(this);top.setGravity(Gravity.CENTER_VERTICAL);top.setPadding(dp(10),dp(7),dp(10),dp(7));top.setBackground(bg(NAVY,0));
  ImageButton back=new ImageButton(this);back.setImageDrawable(new IconDrawable(GOLD,22,"back"));back.setBackgroundColor(Color.TRANSPARENT);back.setContentDescription("Voltar");back.setOnClickListener(v->goBack());top.addView(back,new LinearLayout.LayoutParams(dp(48),dp(48)));
  TextView title=new TextView(this);title.setText("Leitura NTLH");title.setTextSize(18);title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);title.setTextColor(WHITE);title.setGravity(Gravity.CENTER_VERTICAL);top.addView(title,new LinearLayout.LayoutParams(0,dp(48),1));
  ImageButton browser=new ImageButton(this);browser.setImageDrawable(new IconDrawable(GOLD,22,"external"));browser.setBackgroundColor(Color.TRANSPARENT);browser.setContentDescription("Abrir no navegador");browser.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url))));top.addView(browser,new LinearLayout.LayoutParams(dp(48),dp(48)));
  root.addView(top);
  progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);progress.setMax(100);progress.setVisibility(ProgressBar.INVISIBLE);root.addView(progress,new LinearLayout.LayoutParams(-1,dp(3)));
  web=new WebView(this);WebSettings settings=web.getSettings();settings.setJavaScriptEnabled(true);settings.setDomStorageEnabled(true);settings.setBuiltInZoomControls(false);settings.setDisplayZoomControls(false);settings.setLoadWithOverviewMode(true);settings.setUseWideViewPort(true);web.setBackgroundColor(WHITE);
  web.setWebViewClient(new WebViewClient(){@Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){Uri target=r.getUrl();if(target.getHost()!=null&&target.getHost().endsWith("sbb.org.br"))return false;startActivity(new Intent(Intent.ACTION_VIEW,target));return true;}@Override public void onPageStarted(WebView v,String u,Bitmap f){progress.setVisibility(ProgressBar.VISIBLE);}@Override public void onPageFinished(WebView v,String u){progress.setProgress(100);progress.setVisibility(ProgressBar.INVISIBLE);}});
  root.addView(web,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);web.loadUrl(url);
 }
 private void goBack(){if(web!=null&&web.canGoBack())web.goBack();else finish();}
 @Override public void onBackPressed(){goBack();}
 @Override protected void onDestroy(){if(web!=null)web.destroy();super.onDestroy();}
 private static class IconDrawable extends Drawable{Paint p=new Paint(3);int color,size;String id;IconDrawable(int c,int s,String i){color=c;size=s;id=i;setBounds(0,0,s,s);}public void draw(Canvas c){p.setColor(color);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(Math.max(2,size*.09f));p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);float x=size/2f,y=size/2f,s=size*.30f;if(id.equals("back")){c.drawLine(x+s*1.4f,y,x-s*.9f,y,p);c.drawLine(x-s*.9f,y,x-s*.1f,y-s*.8f,p);c.drawLine(x-s*.9f,y,x-s*.1f,y+s*.8f,p);}else{c.drawRect(x-s,y-s,x+s*.8f,y+s,p);c.drawLine(x-s*.1f,y-s*.15f,x+s*1.2f,y-s*1.15f,p);c.drawLine(x+s*1.2f,y-s*1.15f,x+s*.55f,y-s*1.15f,p);c.drawLine(x+s*1.2f,y-s*1.15f,x+s*1.2f,y-s*.5f,p);}}
 public void setAlpha(int a){p.setAlpha(a);}public void setColorFilter(ColorFilter f){p.setColorFilter(f);}public int getOpacity(){return PixelFormat.TRANSLUCENT;}public int getIntrinsicWidth(){return size;}public int getIntrinsicHeight(){return size;}}
}
