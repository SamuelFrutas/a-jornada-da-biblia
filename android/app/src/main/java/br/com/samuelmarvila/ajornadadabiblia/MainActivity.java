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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String PREFS="jornada", CURRENT="current_reading";
    private static final int START_LEVITICUS_1=90;
    private SharedPreferences prefs; private List<Reading> readings; private Reading current;
    private TextToSpeech tts; private Button playPause; private TextView playerStatus; private boolean speaking; private int speechPart;
    private CheckBox readCheck;

    @Override public void onCreate(Bundle state){
        super.onCreate(state); prefs=getSharedPreferences(PREFS,MODE_PRIVATE); readings=ReadingCatalog.all(); migrateOldProgress(); current=getCurrentReading(); buildHome();
        tts=new TextToSpeech(this,status->{if(status==TextToSpeech.SUCCESS){tts.setLanguage(new Locale("pt","BR"));tts.setOnUtteranceProgressListener(new UtteranceProgressListener(){public void onStart(String id){}public void onDone(String id){runOnUiThread(()->nextSpeechPart());}public void onError(String id){runOnUiThread(()->stopSpeech());}});}});
    }
    private void migrateOldProgress(){if(!prefs.contains(CURRENT)){int initial=prefs.getBoolean("lev1",false)?START_LEVITICUS_1+1:START_LEVITICUS_1;prefs.edit().putInt(CURRENT,initial).apply();}}
    private Reading getCurrentReading(){int i=prefs.getInt(CURRENT,START_LEVITICUS_1);if(i<0)i=0;return i<readings.size()?readings.get(i):null;}
    private int index(){return prefs.getInt(CURRENT,START_LEVITICUS_1);}
    private int dp(int v){return(int)(v*getResources().getDisplayMetrics().density+.5f);}
    private TextView text(String s,float size,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(Color.rgb(35,40,38));t.setPadding(0,dp(5),0,dp(5));if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    private GradientDrawable bg(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(15);b.setMinHeight(dp(48));return b;}
    private void base(LinearLayout root){root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(16),dp(18),dp(18));root.setBackgroundColor(Color.rgb(246,247,245));}
    private ScrollView scroll(LinearLayout content){ScrollView s=new ScrollView(this);s.setFillViewport(true);s.addView(content);return s;}
    private void addCard(LinearLayout p,String h,String body){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(20),dp(16),dp(20),dp(16));c.setBackground(bg(Color.WHITE,18));TextView ht=text(h,17,true);ht.setTextColor(Color.rgb(25,86,68));c.addView(ht);c.addView(text(body,15.5f,false));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,dp(12));p.addView(c,lp);}
    private void header(LinearLayout root,String title,String subtitle){TextView t=text(title,28,true);t.setTextColor(Color.rgb(22,75,59));root.addView(t);TextView s=text(subtitle,14,false);s.setTextColor(Color.rgb(90,98,94));root.addView(s);}

    private void buildHome(){
        stopSpeech(); LinearLayout root=new LinearLayout(this);base(root);header(root,"A Jornada da Bíblia","Uma etapa de cada vez, do Gênesis ao Apocalipse.");
        LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(18),0,dp(12));
        if(current==null){LinearLayout done=new LinearLayout(this);done.setOrientation(LinearLayout.VERTICAL);done.setPadding(dp(22),dp(22),dp(22),dp(22));done.setBackground(bg(Color.rgb(221,237,229),22));done.addView(text("JORNADA CONCLUÍDA",12,true));done.addView(text("Você chegou ao fim!",27,true));done.addView(text("Toda a sequência bíblica foi concluída.",15,false));content.addView(done);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);return;}
        int i=index(); int percent=Math.round(i*100f/readings.size());
        LinearLayout progress=new LinearLayout(this);progress.setOrientation(LinearLayout.VERTICAL);progress.setPadding(dp(20),dp(18),dp(20),dp(18));progress.setBackground(bg(Color.WHITE,20));
        LinearLayout pr=new LinearLayout(this);pr.setGravity(Gravity.CENTER_VERTICAL);pr.addView(text("Seu progresso",18,true),new LinearLayout.LayoutParams(0,-2,1));TextView pct=text(percent+"%",17,true);pct.setTextColor(Color.rgb(24,91,70));pr.addView(pct);progress.addView(pr);
        ProgressBar bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);bar.setMax(readings.size());bar.setProgress(i);progress.addView(bar);progress.addView(text("Leitura "+(i+1)+" de "+readings.size(),13.5f,false));content.addView(progress,new LinearLayout.LayoutParams(-1,-2));
        LinearLayout phrase=new LinearLayout(this);phrase.setOrientation(LinearLayout.VERTICAL);phrase.setPadding(dp(20),dp(18),dp(20),dp(18));phrase.setBackground(bg(Color.rgb(221,237,229),20));LinearLayout.LayoutParams fp=new LinearLayout.LayoutParams(-1,-2);fp.setMargins(0,dp(14),0,dp(14));content.addView(phrase,fp);phrase.addView(text("FRASE PARA HOJE",12,true));phrase.addView(text(phraseOfDay(current),19,true));phrase.addView(text("Baseada no tema da leitura de hoje.",13,false));
        LinearLayout today=new LinearLayout(this);today.setOrientation(LinearLayout.VERTICAL);today.setPadding(dp(20),dp(18),dp(20),dp(18));today.setBackground(bg(Color.WHITE,20));today.addView(text("PRÓXIMA ETAPA",12,true));today.addView(text(current.reference(),25,true));today.addView(text(current.description,14,false));Button go=button("📖  Ir para a leitura");go.setTextColor(Color.WHITE);go.setBackground(bg(Color.rgb(24,91,70),16));go.setOnClickListener(v->buildReading());today.addView(go,new LinearLayout.LayoutParams(-1,dp(52)));content.addView(today);
        if(prefs.getBoolean("reading_"+i,false)){Button dev=button("❤️  Ver devocional");dev.setOnClickListener(v->buildDevotional());content.addView(dev,new LinearLayout.LayoutParams(-1,dp(52)));}
        root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private String phraseOfDay(Reading r){
        String b=r.book; if(b.equals("Levítico")) return "Aproxime-se de Deus com reverência e um coração inteiro.";
        if(b.equals("Gênesis")) return "Deus está presente e age mesmo quando a história ainda parece estar começando.";
        if(b.equals("Êxodo")) return "Deus liberta, conduz e ensina seu povo a caminhar com Ele.";
        if(b.equals("Números")||b.equals("Deuteronômio")) return "Fidelidade a Deus também se aprende no caminho.";
        if(b.equals("Salmos")||b.equals("Provérbios")) return "A Palavra de Deus merece entrar não apenas na mente, mas também na vida.";
        if(b.equals("Evangelho segundo Mateus")||b.equals("Evangelho segundo Marcos")||b.equals("Evangelho segundo Lucas")||b.equals("Evangelho segundo João")) return "Conhecer Jesus transforma a maneira de viver, servir e confiar.";
        if(b.equals("Atos")) return "O evangelho continua avançando quando pessoas comuns obedecem a Deus.";
        return "Leia com atenção, reflita com sinceridade e coloque em prática o que Deus está ensinando.";
    }

    private void buildReading(){
        stopSpeech(); LinearLayout root=new LinearLayout(this);base(root);header(root,"Leitura","Agora leia o texto bíblico com atenção.");
        LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(18),0,dp(12));
        LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setPadding(dp(20),dp(20),dp(20),dp(20));card.setBackground(bg(Color.rgb(221,237,229),22));card.addView(text("LEITURA DE HOJE",12,true));card.addView(text(current.reference(),30,true));card.addView(text(current.scope(),14,false));card.addView(text(current.description,15,false));Button open=button("📖  Abrir NTLH");open.setTextColor(Color.WHITE);open.setBackground(bg(Color.rgb(24,91,70),16));open.setOnClickListener(v->{Intent in=new Intent(this,WebViewActivity.class);in.putExtra("url",current.sbbUrl());in.putExtra("title",current.reference()+" — NTLH");startActivity(in);});card.addView(open,new LinearLayout.LayoutParams(-1,dp(52)));content.addView(card);
        LinearLayout mark=new LinearLayout(this);mark.setOrientation(LinearLayout.VERTICAL);mark.setPadding(dp(18),dp(16),dp(18),dp(16));mark.setBackground(bg(Color.WHITE,18));LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(-1,-2);mp.setMargins(0,dp(14),0,dp(14));content.addView(mark,mp);readCheck=new CheckBox(this);readCheck.setText("  Eu li esta leitura na NTLH");readCheck.setTextSize(16);readCheck.setChecked(prefs.getBoolean("reading_"+index(),false));readCheck.setOnCheckedChangeListener((b,checked)->{if(checked){prefs.edit().putBoolean("reading_"+index(),true).apply();}else{prefs.edit().putBoolean("reading_"+index(),false).apply();}refreshReadingButtons();});mark.addView(readCheck);mark.addView(text("Depois de marcar a leitura, o devocional fica disponível para esta etapa.",13.5f,false));
        Button dev=button("❤️  Ir para o devocional");dev.setTextColor(Color.WHITE);dev.setBackground(bg(Color.rgb(24,91,70),16));dev.setTag("dev");dev.setOnClickListener(v->buildDevotional());content.addView(dev,new LinearLayout.LayoutParams(-1,dp(52)));Button back=button("←  Voltar ao início");back.setOnClickListener(v->buildHome());content.addView(back,new LinearLayout.LayoutParams(-1,dp(50)));root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);refreshReadingButtons();
    }
    private void refreshReadingButtons(){if(readCheck==null)return;View parent=readCheck.getParent(); if(parent==null)return;LinearLayout root=(LinearLayout)parent.getParent(); if(root==null)return;}

    private void buildDevotional(){
        stopSpeech(); if(!prefs.getBoolean("reading_"+index(),false)){buildReading();return;} LinearLayout root=new LinearLayout(this);base(root);header(root,"Devocional","Agora pare, reflita e deixe a Palavra falar com você.");
        LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(18),0,dp(12));content.addView(text(current.reference(),25,true));
        String[] heads={"🧠  1. Explicação do texto","🏺  2. Contexto — tempo e cultura","❤️  3. Aplicação — reflexão","🚶  4. Prática — como viver isso hoje","🙏  5. Oração"};for(int n=0;n<current.devotional.length;n++)addCard(content,heads[n],current.devotional[n]);
        LinearLayout player=new LinearLayout(this);player.setOrientation(LinearLayout.VERTICAL);player.setPadding(dp(18),dp(16),dp(18),dp(16));player.setBackground(bg(Color.rgb(232,238,234),18));player.addView(text("🔊  Ouvir devocional",17,true));playerStatus=text("Pronto para ouvir",13.5f,false);player.addView(playerStatus);LinearLayout controls=new LinearLayout(this);controls.setGravity(Gravity.CENTER_VERTICAL);playPause=button("▶  Ouvir");playPause.setOnClickListener(v->toggleSpeech());controls.addView(playPause,new LinearLayout.LayoutParams(0,dp(50),1));Button stop=button("■  Parar");stop.setOnClickListener(v->stopSpeech());controls.addView(stop,new LinearLayout.LayoutParams(dp(112),dp(50)));player.addView(controls);content.addView(player);
        Button finish=button("✓  Concluir devocional e avançar");finish.setTextColor(Color.WHITE);finish.setBackground(bg(Color.rgb(24,91,70),16));finish.setOnClickListener(v->completeReading());content.addView(finish,new LinearLayout.LayoutParams(-1,dp(52)));Button back=button("←  Voltar para a leitura");back.setOnClickListener(v->buildReading());content.addView(back,new LinearLayout.LayoutParams(-1,dp(50)));root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void completeReading(){stopSpeech();int next=index()+1;prefs.edit().putInt(CURRENT,next).putBoolean("reading_"+index(),true).apply();current=getCurrentReading();buildHome();}
    private void toggleSpeech(){if(tts==null)return;if(speaking){tts.stop();speaking=false;playPause.setText("▶  Continuar");playerStatus.setText("Pausado na parte "+(speechPart+1)+" de 5");}else{speaking=true;playPause.setText("Ⅱ  Pausar");speakCurrent();}}
    private void speakCurrent(){if(!speaking||current==null)return;playerStatus.setText("Reproduzindo parte "+(speechPart+1)+" de 5");tts.speak(current.devotional[speechPart],TextToSpeech.QUEUE_FLUSH,null,"devotional-"+speechPart);}
    private void nextSpeechPart(){if(!speaking)return;speechPart++;if(current!=null&&speechPart<current.devotional.length)speakCurrent();else finishSpeech();}
    private void stopSpeech(){if(tts!=null)tts.stop();speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir");if(playerStatus!=null)playerStatus.setText("Pronto para ouvir");}
    private void finishSpeech(){speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir novamente");if(playerStatus!=null)playerStatus.setText("Devocional concluído");}
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
