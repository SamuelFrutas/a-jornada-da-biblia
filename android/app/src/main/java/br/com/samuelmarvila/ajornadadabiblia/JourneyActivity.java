package br.com.samuelmarvila.ajornadadabiblia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.widget.*;
import java.util.List;
import java.util.Locale;

public class JourneyActivity extends Activity {
    private SharedPreferences prefs; private List<Reading> readings; private Reading current;
    private TextToSpeech tts; private Button play; private TextView status; private boolean speaking; private int part;
    private static final String PREFS="jornada", CURRENT="current_reading";
    private int dp(int x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
    private GradientDrawable bg(int c,int r){GradientDrawable d=new GradientDrawable();d.setColor(c);d.setCornerRadius(dp(r));return d;}
    private TextView tv(String s,float z,boolean b){TextView t=new TextView(this);t.setText(s);t.setTextSize(z);t.setTextColor(Color.rgb(35,40,38));t.setPadding(0,dp(5),0,dp(5));if(b)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    private Button btn(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(15);b.setMinHeight(dp(48));return b;}
    @Override public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences(PREFS,MODE_PRIVATE);readings=ReadingCatalog.all();if(!prefs.contains(CURRENT)&&prefs.getBoolean("lev1",false))prefs.edit().putInt(CURRENT,1).apply();current=getCurrent();render();tts=new TextToSpeech(this,x->{if(x==TextToSpeech.SUCCESS)tts.setLanguage(new Locale("pt","BR"));});}
    private Reading getCurrent(){int i=prefs.getInt(CURRENT,0);return i<readings.size()?readings.get(i):null;}
    private int index(){return prefs.getInt(CURRENT,0);}
    private void card(LinearLayout p,String h,String body){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(20),dp(16),dp(20),dp(16));c.setBackground(bg(Color.WHITE,18));TextView x=tv(h,17,true);x.setTextColor(Color.rgb(25,86,68));c.addView(x);c.addView(tv(body,15.5f,false));LinearLayout.LayoutParams q=new LinearLayout.LayoutParams(-1,-2);q.setMargins(0,0,0,dp(12));p.addView(c,q);}
    private void render(){LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(16),dp(18),dp(18));root.setBackgroundColor(Color.rgb(246,247,245));root.addView(tv("A Jornada da Bíblia",28,true));TextView sub=tv("Sua caminhada pela Palavra, em sequência.",14,false);sub.setTextColor(Color.rgb(90,98,94));root.addView(sub);ScrollView sc=new ScrollView(this);LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(0,dp(18),0,dp(12));
        if(current==null){LinearLayout done=new LinearLayout(this);done.setOrientation(LinearLayout.VERTICAL);done.setPadding(dp(22),dp(22),dp(22),dp(22));done.setBackground(bg(Color.rgb(221,237,229),22));done.addView(tv("JORNADA CONCLUÍDA",12,true));done.addView(tv("Parabéns!",30,true));done.addView(tv("Você chegou ao final da jornada preparada no aplicativo.",15,false));c.addView(done);sc.addView(c);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);return;}
        int i=index();LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.VERTICAL);r.setPadding(dp(22),dp(20),dp(22),dp(20));r.setBackground(bg(Color.rgb(221,237,229),22));TextView l=tv("LEITURA DE HOJE",12,true);l.setTextColor(Color.rgb(53,105,86));r.addView(l);TextView ref=tv(current.reference(),30,true);ref.setTextColor(Color.rgb(20,78,60));r.addView(ref);r.addView(tv(current.scope(),14,false));r.addView(tv(current.description,14,false));Button read=btn("📖  Ler na NTLH");read.setTextColor(Color.WHITE);read.setBackground(bg(Color.rgb(24,91,70),16));read.setOnClickListener(v->{Intent x=new Intent(this,WebViewActivity.class);x.putExtra("url",current.sbbUrl());x.putExtra("title",current.reference()+" — NTLH");startActivity(x);});r.addView(read,new LinearLayout.LayoutParams(-1,dp(52)));c.addView(r);
        LinearLayout pc=new LinearLayout(this);pc.setOrientation(LinearLayout.VERTICAL);pc.setPadding(dp(18),dp(15),dp(18),dp(15));pc.setBackground(bg(Color.WHITE,18));LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.addView(tv("Progresso da jornada",17,true),new LinearLayout.LayoutParams(0,-2,1));row.addView(tv(Math.round(i*100f/readings.size())+"%",15,true));pc.addView(row);ProgressBar pb=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);pb.setMax(readings.size());pb.setProgress(i);pc.addView(pb);pc.addView(tv("Leitura "+(i+1)+" de "+readings.size()+" nesta versão.",13.5f,false));LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,-2);pp.setMargins(0,0,0,dp(14));c.addView(pc,pp);
        c.addView(tv("DEVOCIONAL",12,true));String[] heads={"🧠  1. Explicação do texto","🏺  2. Contexto — tempo e cultura","❤️  3. Aplicação — reflexão","🚶  4. Prática — como viver isso hoje","🙏  5. Oração"};for(int n=0;n<current.devotional.length;n++)card(c,heads[n],current.devotional[n]);
        LinearLayout player=new LinearLayout(this);player.setOrientation(LinearLayout.VERTICAL);player.setPadding(dp(18),dp(16),dp(18),dp(16));player.setBackground(bg(Color.rgb(232,238,234),18));player.addView(tv("🔊  Leitor do devocional",17,true));status=tv("Pronto para ouvir",13.5f,false);player.addView(status);LinearLayout controls=new LinearLayout(this);play=btn("▶  Ouvir");play.setOnClickListener(v->toggle());controls.addView(play,new LinearLayout.LayoutParams(0,dp(50),1));Button stop=btn("■  Parar");stop.setOnClickListener(v->stop());controls.addView(stop,new LinearLayout.LayoutParams(dp(112),dp(50)));player.addView(controls);c.addView(player);
        Button done=btn(i+1<readings.size()?"✓  Concluir e avançar":"✓  Concluir leitura");done.setTextColor(Color.WHITE);done.setBackground(bg(Color.rgb(24,91,70),16));done.setOnClickListener(v->complete());LinearLayout.LayoutParams dd=new LinearLayout.LayoutParams(-1,dp(52));dd.setMargins(0,dp(12),0,dp(10));c.addView(done,dd);if(i+1<readings.size())c.addView(tv("Próxima leitura: "+readings.get(i+1).reference(),15,true));sc.addView(c);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);}
    private void toggle(){if(tts==null)return;if(speaking){tts.stop();speaking=false;play.setText("▶  Continuar");status.setText("Pausado");}else{speaking=true;play.setText("Ⅱ  Pausar");speak();}}
    private void speak(){if(!speaking)return;status.setText("Reproduzindo parte "+(part+1)+" de 5");tts.speak(current.devotional[part],TextToSpeech.QUEUE_FLUSH,null,"p"+part);}
    private void stop(){if(tts!=null)tts.stop();speaking=false;part=0;if(play!=null)play.setText("▶  Ouvir");if(status!=null)status.setText("Pronto para ouvir");}
    private void complete(){stop();prefs.edit().putInt(CURRENT,index()+1).apply();current=getCurrent();render();}
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
