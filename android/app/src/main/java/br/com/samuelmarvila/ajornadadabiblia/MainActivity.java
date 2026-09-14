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
    private static final String PREFS="jornada", CURRENT="current_reading", PLAN_VERSION="plan_version";
    private static final int CURRENT_PLAN_VERSION=2;
    private static final int BG=Color.rgb(247,248,246), GREEN=Color.rgb(20,91,69), DARK=Color.rgb(25,45,38), MUTED=Color.rgb(91,103,97), SOFT=Color.rgb(225,239,232), WHITE=Color.WHITE;
    private SharedPreferences prefs; private List<Reading> readings; private Reading current;
    private TextToSpeech tts; private Button playPause; private TextView playerStatus; private boolean speaking; private int speechPart;
    private CheckBox readCheck; private Button devotionalButton;

    @Override public void onCreate(Bundle state){
        super.onCreate(state); getWindow().setStatusBarColor(Color.rgb(12,67,51)); getWindow().setNavigationBarColor(BG);
        prefs=getSharedPreferences(PREFS,MODE_PRIVATE); readings=ReadingPlan.build(); migrateOldProgress(); current=getCurrentReading(); buildHome();
        tts=new TextToSpeech(this,status->{if(status==TextToSpeech.SUCCESS){tts.setLanguage(new Locale("pt","BR"));tts.setOnUtteranceProgressListener(new UtteranceProgressListener(){public void onStart(String id){}public void onDone(String id){runOnUiThread(()->nextSpeechPart());}public void onError(String id){runOnUiThread(()->stopSpeech());}});}});
    }
    private void migrateOldProgress(){
        int version=prefs.getInt(PLAN_VERSION,0); if(version<CURRENT_PLAN_VERSION){int old=prefs.getInt(CURRENT,-1);int lev=findReadingIndex("Levítico",1,1);if(old<0)old=prefs.getBoolean("lev1",false)?lev+1:lev;else if(old==90||old==91)old=prefs.getBoolean("reading_90",false)?lev+1:lev;prefs.edit().putInt(CURRENT,old).putInt(PLAN_VERSION,CURRENT_PLAN_VERSION).apply();}else if(!prefs.contains(CURRENT))prefs.edit().putInt(CURRENT,findReadingIndex("Levítico",1,1)).apply();
    }
    private int findReadingIndex(String book,int chapter,int verse){for(int i=0;i<readings.size();i++){Reading r=readings.get(i);if(r.book.equals(book)&&r.chapter==chapter&&r.startVerse==verse)return i;}return 0;}
    private Reading getCurrentReading(){int i=index();if(i<0)i=0;return i<readings.size()?readings.get(i):null;}
    private int index(){return prefs.getInt(CURRENT,findReadingIndex("Levítico",1,1));}
    private int dp(int v){return(int)(v*getResources().getDisplayMetrics().density+.5f);}
    private TextView text(String s,float size,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(DARK);t.setPadding(0,dp(3),0,dp(3));if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    private GradientDrawable bg(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private GradientDrawable outline(int color,int stroke,int strokeColor,int radius){GradientDrawable d=bg(color,radius);d.setStroke(dp(stroke),strokeColor);return d;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(15);b.setMinHeight(dp(50));b.setPadding(dp(12),0,dp(12),0);return b;}
    private Button primary(String s){Button b=button(s);b.setTextColor(WHITE);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackground(bg(GREEN,16));b.setElevation(dp(2));return b;}
    private LinearLayout card(){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(20),dp(18),dp(20),dp(18));c.setBackground(outline(WHITE,1,Color.rgb(232,235,232),20));c.setElevation(dp(1));return c;}
    private void base(LinearLayout root){root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(14),dp(18),dp(18));root.setBackgroundColor(BG);}
    private ScrollView scroll(LinearLayout content){ScrollView s=new ScrollView(this);s.setFillViewport(true);s.setClipToPadding(false);s.addView(content);return s;}
    private void header(LinearLayout root,String title,String subtitle){LinearLayout h=new LinearLayout(this);h.setOrientation(LinearLayout.VERTICAL);h.setPadding(dp(2),dp(4),dp(2),dp(4));TextView t=text(title,29,true);t.setTextColor(Color.rgb(17,72,55));h.addView(t);TextView s=text(subtitle,14,false);s.setTextColor(MUTED);h.addView(s);root.addView(h);}
    private void addGap(LinearLayout p,int h){SpaceView(p,h);}
    private void SpaceView(LinearLayout p,int h){View v=new View(this);p.addView(v,new LinearLayout.LayoutParams(1,dp(h)));}
    private void addCard(LinearLayout p,String h,String body){LinearLayout c=card();TextView ht=text(h,17,true);ht.setTextColor(GREEN);c.addView(ht);c.addView(text(body,15.5f,false));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,dp(12));p.addView(c,lp);}

    private void buildHome(){
        stopSpeech(); LinearLayout root=new LinearLayout(this);base(root);header(root,"A Jornada da Bíblia","Uma etapa de cada vez, do Gênesis ao Apocalipse.");
        LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(8));
        if(current==null){LinearLayout done=card();done.setBackground(bg(SOFT,22));done.addView(text("JORNADA CONCLUÍDA",12,true));done.addView(text("Você chegou ao fim!",29,true));done.addView(text("Toda a sequência bíblica foi concluída.",15,false));content.addView(done);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);return;}
        int i=index();int percent=Math.round(i*100f/readings.size());
        LinearLayout hero=card();hero.setBackground(bg(Color.rgb(28,91,70),24));TextView eyebrow=text("SUA CAMINHADA",12,true);eyebrow.setTextColor(Color.rgb(197,226,211));hero.addView(eyebrow);TextView ref=text(current.reference(),28,true);ref.setTextColor(WHITE);hero.addView(ref);TextView desc=text(current.scope(),14,false);desc.setTextColor(Color.rgb(229,242,235));hero.addView(desc);SpaceView(hero,6);TextView step=text("Etapa "+(i+1)+" de "+readings.size(),13.5f,true);step.setTextColor(Color.rgb(229,242,235));hero.addView(step);ProgressBar heroBar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);heroBar.setMax(readings.size());heroBar.setProgress(i);hero.addView(heroBar,new LinearLayout.LayoutParams(-1,dp(6)));LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.setMargins(0,0,0,dp(14));content.addView(hero,hp);
        LinearLayout phrase=card();phrase.setBackground(bg(SOFT,22));TextView label=text("FRASE PARA HOJE",12,true);label.setTextColor(GREEN);phrase.addView(label);phrase.addView(text(phraseOfDay(current),19,true));TextView hint=text("Leia, reflita e coloque em prática.",13,false);hint.setTextColor(MUTED);phrase.addView(hint);LinearLayout.LayoutParams fp=new LinearLayout.LayoutParams(-1,-2);fp.setMargins(0,0,0,dp(14));content.addView(phrase,fp);
        LinearLayout next=card();TextView nextLabel=text("PRÓXIMA ETAPA",12,true);nextLabel.setTextColor(GREEN);next.addView(nextLabel);TextView nextRef=text(current.reference(),24,true);nextRef.setTextColor(DARK);next.addView(nextRef);next.addView(text(current.description,14,false));SpaceView(next,5);Button go=primary("📖  Ir para a leitura");go.setOnClickListener(v->buildReading());next.addView(go,new LinearLayout.LayoutParams(-1,dp(52)));content.addView(next);
        if(prefs.getBoolean("reading_"+i,false)){Button dev=button("❤️  Ver devocional");dev.setTextColor(GREEN);dev.setTypeface(Typeface.DEFAULT,Typeface.BOLD);dev.setBackground(outline(WHITE,1,Color.rgb(205,222,214),16));dev.setOnClickListener(v->buildDevotional());LinearLayout.LayoutParams dp=new LinearLayout.LayoutParams(-1,dp(50));dp.setMargins(0,dp(12),0,0);content.addView(dev,dp);}
        root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private String phraseOfDay(Reading r){String b=r.book;if(b.equals("Levítico"))return "Aproxime-se de Deus com reverência e um coração inteiro.";if(b.equals("Gênesis"))return "Deus está presente e age mesmo quando a história ainda parece estar começando.";if(b.equals("Êxodo"))return "Deus liberta, conduz e ensina seu povo a caminhar com Ele.";if(b.equals("Números")||b.equals("Deuteronômio"))return "Fidelidade a Deus também se aprende no caminho.";if(b.equals("Salmos")||b.equals("Provérbios"))return "A Palavra de Deus merece entrar não apenas na mente, mas também na vida.";if(b.equals("Mateus")||b.equals("Marcos")||b.equals("Lucas")||b.equals("João"))return "Conhecer Jesus transforma a maneira de viver, servir e confiar.";if(b.equals("Atos"))return "O evangelho continua avançando quando pessoas comuns obedecem a Deus.";return "Leia com atenção, reflita com sinceridade e coloque em prática o que Deus está ensinando.";}

    private void buildReading(){
        stopSpeech();LinearLayout root=new LinearLayout(this);base(root);header(root,"Leitura","Leia primeiro o texto bíblico. Depois, siga para o devocional.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(8));
        LinearLayout card=card();card.setBackground(bg(SOFT,22));TextView lab=text("LEITURA DE HOJE",12,true);lab.setTextColor(GREEN);card.addView(lab);card.addView(text(current.reference(),30,true));card.addView(text(current.scope(),14,false));card.addView(text(current.description,15,false));SpaceView(card,7);Button open=primary("📖  Abrir NTLH");open.setOnClickListener(v->{Intent in=new Intent(this,WebViewActivity.class);in.putExtra("url",current.sbbUrl());in.putExtra("title",current.reference()+" — NTLH");startActivity(in);});card.addView(open,new LinearLayout.LayoutParams(-1,dp(52)));content.addView(card);
        LinearLayout mark=card();LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(-1,-2);mp.setMargins(0,dp(14),0,dp(14));content.addView(mark,mp);readCheck=new CheckBox(this);readCheck.setText("Eu li esta leitura na NTLH");readCheck.setTextSize(16);readCheck.setTextColor(DARK);readCheck.setChecked(prefs.getBoolean("reading_"+index(),false));readCheck.setOnCheckedChangeListener((b,checked)->{prefs.edit().putBoolean("reading_"+index(),checked).apply();refreshReadingButtons();});mark.addView(readCheck);TextView explain=text("Marque somente depois de terminar a leitura. Isso libera o próximo passo.",13.5f,false);explain.setTextColor(MUTED);mark.addView(explain);
        devotionalButton=primary("❤️  Ir para o devocional");devotionalButton.setOnClickListener(v->buildDevotional());content.addView(devotionalButton,new LinearLayout.LayoutParams(-1,dp(52)));Button back=button("←  Voltar ao início");back.setTextColor(GREEN);back.setBackground(outline(WHITE,1,Color.rgb(205,222,214),16));content.addView(back,new LinearLayout.LayoutParams(-1,dp(50)));root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);refreshReadingButtons();
    }
    private void refreshReadingButtons(){if(devotionalButton==null)return;boolean enabled=readCheck!=null&&readCheck.isChecked();devotionalButton.setEnabled(enabled);devotionalButton.setAlpha(enabled?1f:.45f);}

    private void buildDevotional(){
        stopSpeech();if(!prefs.getBoolean("reading_"+index(),false)){buildReading();return;}LinearLayout root=new LinearLayout(this);base(root);header(root,"Devocional","Agora pare, reflita e deixe a Palavra falar com você.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(8));LinearLayout refCard=card();refCard.setBackground(bg(SOFT,20));refCard.addView(text("REFLEXÃO DE HOJE",12,true));refCard.addView(text(current.reference(),25,true));content.addView(refCard,new LinearLayout.LayoutParams(-1,-2));SpaceView(content,14);
        String[] heads={"🧠  1. Explicação do texto","🏺  2. Contexto — tempo e cultura","❤️  3. Aplicação — reflexão","🚶  4. Prática — como viver isso hoje","🙏  5. Oração"};for(int n=0;n<current.devotional.length;n++)addCard(content,heads[n],current.devotional[n]);
        LinearLayout player=card();player.setBackground(bg(Color.rgb(235,241,237),20));player.addView(text("🔊  Ouvir devocional",17,true));playerStatus=text("Pronto para ouvir",13.5f,false);playerStatus.setTextColor(MUTED);player.addView(playerStatus);SpaceView(player,4);LinearLayout controls=new LinearLayout(this);controls.setGravity(Gravity.CENTER_VERTICAL);playPause=primary("▶  Ouvir");playPause.setOnClickListener(v->toggleSpeech());controls.addView(playPause,new LinearLayout.LayoutParams(0,dp(50),1));Button stop=button("■  Parar");stop.setTextColor(GREEN);stop.setBackground(outline(WHITE,1,Color.rgb(205,222,214),16));stop.setOnClickListener(v->stopSpeech());LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(dp(112),dp(50));sp.setMargins(dp(8),0,0,0);controls.addView(stop,sp);player.addView(controls);LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,-2);pp.setMargins(0,0,0,dp(12));content.addView(player,pp);
        Button finish=primary("✓  Concluir devocional e avançar");finish.setOnClickListener(v->completeReading());content.addView(finish,new LinearLayout.LayoutParams(-1,dp(52)));Button back=button("←  Voltar para a leitura");back.setTextColor(GREEN);back.setBackground(outline(WHITE,1,Color.rgb(205,222,214),16));content.addView(back,new LinearLayout.LayoutParams(-1,dp(50)));root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void completeReading(){stopSpeech();int old=index();int next=old+1;prefs.edit().putInt(CURRENT,next).putBoolean("reading_"+old,true).apply();current=getCurrentReading();buildHome();}
    private void toggleSpeech(){if(tts==null)return;if(speaking){tts.stop();speaking=false;playPause.setText("▶  Continuar");playerStatus.setText("Pausado na parte "+(speechPart+1)+" de 5");}else{speaking=true;playPause.setText("Ⅱ  Pausar");speakCurrent();}}
    private void speakCurrent(){if(!speaking||current==null)return;playerStatus.setText("Reproduzindo parte "+(speechPart+1)+" de 5");tts.speak(current.devotional[speechPart],TextToSpeech.QUEUE_FLUSH,null,"devotional-"+speechPart);}
    private void nextSpeechPart(){if(!speaking)return;speechPart++;if(current!=null&&speechPart<current.devotional.length)speakCurrent();else finishSpeech();}
    private void stopSpeech(){if(tts!=null)tts.stop();speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir");if(playerStatus!=null)playerStatus.setText("Pronto para ouvir");}
    private void finishSpeech(){speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir novamente");if(playerStatus!=null)playerStatus.setText("Devocional concluído");}
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
