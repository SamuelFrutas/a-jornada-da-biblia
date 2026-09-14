package br.com.samuelmarvila.ajornadadabiblia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String PREFS = "jornada";
    private static final String CURRENT = "current_reading";
    private SharedPreferences prefs;
    private List<Reading> readings;
    private Reading current;
    private TextToSpeech tts;
    private Button playPause;
    private TextView playerStatus;
    private boolean speaking;
    private int speechPart;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        readings = ReadingCatalog.all();
        migrateOldProgress();
        current = getCurrentReading();
        buildScreen();
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "BR"));
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override public void onStart(String id) {}
                    @Override public void onDone(String id) { runOnUiThread(() -> nextSpeechPart()); }
                    @Override public void onError(String id) { runOnUiThread(this::stopFromError); }
                    private void stopFromError() { stopSpeech(); }
                });
            }
        });
    }

    private void migrateOldProgress() {
        if (!prefs.contains(CURRENT) && prefs.getBoolean("lev1", false))
            prefs.edit().putInt(CURRENT, 1).apply();
    }
    private Reading getCurrentReading() {
        int i = prefs.getInt(CURRENT, 0);
        if (i < 0) i = 0;
        return i < readings.size() ? readings.get(i) : null;
    }
    private int index() { return prefs.getInt(CURRENT, 0); }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private TextView text(String s,float size,boolean bold) {
        TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(Color.rgb(35,40,38)); t.setPadding(0,dp(5),0,dp(5));
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); return t;
    }
    private GradientDrawable bg(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(15);b.setMinHeight(dp(48));return b;}
    private void addCard(LinearLayout p,String h,String body){
        LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(20),dp(16),dp(20),dp(16));c.setBackground(bg(Color.WHITE,18));
        TextView ht=text(h,17,true);ht.setTextColor(Color.rgb(25,86,68));c.addView(ht);c.addView(text(body,15.5f,false));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,dp(12));p.addView(c,lp);
    }

    private void buildScreen(){
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(16),dp(18),dp(18));root.setBackgroundColor(Color.rgb(246,247,245));
        TextView title=text("A Jornada da Bíblia",28,true);title.setTextColor(Color.rgb(22,75,59));root.addView(title);
        TextView sub=text("Sua caminhada pela Palavra, em sequência.",14,false);sub.setTextColor(Color.rgb(90,98,94));root.addView(sub);
        ScrollView scroll=new ScrollView(this);LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(18),0,dp(12));
        if(current==null){
            LinearLayout done=new LinearLayout(this);done.setOrientation(LinearLayout.VERTICAL);done.setPadding(dp(22),dp(22),dp(22),dp(22));done.setBackground(bg(Color.rgb(221,237,229),22));
            done.addView(text("JORNADA CONCLUÍDA",12,true));done.addView(text("Você chegou ao fim!",27,true));done.addView(text("Toda a sequência preparada para o aplicativo foi concluída.",15,false));content.addView(done);
            scroll.addView(content);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);return;
        }
        int i=index();
        LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setPadding(dp(22),dp(20),dp(22),dp(20));card.setBackground(bg(Color.rgb(221,237,229),22));
        TextView lab=text("LEITURA DE HOJE",12,true);lab.setTextColor(Color.rgb(53,105,86));card.addView(lab);
        TextView ref=text(current.reference(),30,true);ref.setTextColor(Color.rgb(20,78,60));card.addView(ref);card.addView(text(current.scope(),14,false));card.addView(text(current.description,14,false));
        Button read=button("📖  Ler na NTLH");read.setTextColor(Color.WHITE);read.setBackground(bg(Color.rgb(24,91,70),16));
        read.setOnClickListener(v->{Intent in=new Intent(this,WebViewActivity.class);in.putExtra("url",current.sbbUrl());in.putExtra("title",current.reference()+" — NTLH");startActivity(in);});
        LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,dp(52));rp.setMargins(0,dp(14),0,0);card.addView(read,rp);content.addView(card);

        LinearLayout pc=new LinearLayout(this);pc.setOrientation(LinearLayout.VERTICAL);pc.setPadding(dp(18),dp(15),dp(18),dp(15));pc.setBackground(bg(Color.WHITE,18));
        LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,-2);pp.setMargins(0,0,0,dp(14));content.addView(pc,pp);
        LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.addView(text("Progresso da jornada",17,true),new LinearLayout.LayoutParams(0,-2,1));
        int percent=Math.round(i*100f/readings.size());TextView pct=text(percent+"%",15,true);pct.setTextColor(Color.rgb(24,91,70));row.addView(pct);pc.addView(row);
        ProgressBar bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);bar.setMax(readings.size());bar.setProgress(i);pc.addView(bar);pc.addView(text("Leitura "+(i+1)+" de "+readings.size(),13.5f,false));

        content.addView(text("DEVOCIONAL",12,true));
        String[] heads={"🧠  1. Explicação do texto","🏺  2. Contexto — tempo e cultura","❤️  3. Aplicação — reflexão","🚶  4. Prática — como viver isso hoje","🙏  5. Oração"};
        for(int n=0;n<current.devotional.length;n++)addCard(content,heads[n],current.devotional[n]);

        LinearLayout player=new LinearLayout(this);player.setOrientation(LinearLayout.VERTICAL);player.setPadding(dp(18),dp(16),dp(18),dp(16));player.setBackground(bg(Color.rgb(232,238,234),18));
        player.addView(text("🔊  Leitor do devocional",17,true));playerStatus=text("Pronto para ouvir",13.5f,false);player.addView(playerStatus);
        LinearLayout controls=new LinearLayout(this);controls.setGravity(Gravity.CENTER_VERTICAL);playPause=button("▶  Ouvir");playPause.setOnClickListener(v->toggleSpeech());controls.addView(playPause,new LinearLayout.LayoutParams(0,dp(50),1));
        Button stop=button("■  Parar");stop.setOnClickListener(v->stopSpeech());controls.addView(stop,new LinearLayout.LayoutParams(dp(112),dp(50)));player.addView(controls);content.addView(player);

        Button complete=button(i+1<readings.size()?"✓  Concluir e avançar":"✓  Concluir leitura");complete.setTextColor(Color.WHITE);complete.setBackground(bg(Color.rgb(24,91,70),16));complete.setOnClickListener(v->completeReading());
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,dp(52));cp.setMargins(0,dp(12),0,dp(10));content.addView(complete,cp);
        if(i+1<readings.size())content.addView(text("Próxima leitura: "+readings.get(i+1).reference(),15,true));
        scroll.addView(content);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void completeReading(){stopSpeech();int next=index()+1;prefs.edit().putInt(CURRENT,next).putBoolean("reading_"+index(),true).apply();current=getCurrentReading();buildScreen();}
    private void toggleSpeech(){if(tts==null)return;if(speaking){tts.stop();speaking=false;playPause.setText("▶  Continuar");playerStatus.setText("Pausado na parte "+(speechPart+1)+" de 5");}else{speaking=true;playPause.setText("Ⅱ  Pausar");speakCurrent();}}
    private void speakCurrent(){if(!speaking||current==null)return;playerStatus.setText("Reproduzindo parte "+(speechPart+1)+" de 5");tts.speak(current.devotional[speechPart],TextToSpeech.QUEUE_FLUSH,null,"devotional-"+speechPart);}
    private void nextSpeechPart(){if(!speaking)return;speechPart++;if(current!=null&&speechPart<current.devotional.length)speakCurrent();else finishSpeech();}
    private void stopSpeech(){if(tts!=null)tts.stop();speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir");if(playerStatus!=null)playerStatus.setText("Pronto para ouvir");}
    private void finishSpeech(){speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir novamente");if(playerStatus!=null)playerStatus.setText("Devocional concluído");}
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
