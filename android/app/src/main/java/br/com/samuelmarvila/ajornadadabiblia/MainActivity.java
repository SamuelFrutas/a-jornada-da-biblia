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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {
    private static final String PREFS="jornada", CURRENT="current_reading", PLAN_VERSION="plan_version";
    private static final int CURRENT_PLAN_VERSION=2;
    private static final int BG=Color.rgb(247,248,246), GREEN=Color.rgb(20,91,69), DARK=Color.rgb(25,45,38), MUTED=Color.rgb(91,103,97), SOFT=Color.rgb(225,239,232), WHITE=Color.WHITE, BORDER=Color.rgb(226,232,228);
    private SharedPreferences prefs; private List<Reading> readings; private Reading current;
    private TextToSpeech tts; private Button playPause; private TextView playerStatus; private boolean speaking; private int speechPart;
    private CheckBox readCheck; private Button devotionalButton;
    private Calendar calendarCursor=Calendar.getInstance();
    private final SimpleDateFormat dateKeyFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
    private final SimpleDateFormat monthFormat=new SimpleDateFormat("MMMM yyyy",new Locale("pt","BR"));

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
    private Button secondary(String s){Button b=button(s);b.setTextColor(GREEN);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackground(outline(WHITE,1,BORDER,16));return b;}
    private LinearLayout card(){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(20),dp(18),dp(20),dp(18));c.setBackground(outline(WHITE,1,BORDER,20));c.setElevation(dp(1));return c;}
    private void base(LinearLayout root){root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(14),dp(18),dp(18));root.setBackgroundColor(BG);}
    private ScrollView scroll(LinearLayout content){ScrollView s=new ScrollView(this);s.setFillViewport(true);s.setClipToPadding(false);s.addView(content);return s;}
    private void header(LinearLayout root,String title,String subtitle){LinearLayout h=new LinearLayout(this);h.setOrientation(LinearLayout.VERTICAL);h.setPadding(dp(2),dp(4),dp(2),dp(4));TextView t=text(title,29,true);t.setTextColor(Color.rgb(17,72,55));h.addView(t);TextView s=text(subtitle,14,false);s.setTextColor(MUTED);h.addView(s);root.addView(h);}
    private void SpaceView(LinearLayout p,int h){View v=new View(this);p.addView(v,new LinearLayout.LayoutParams(1,dp(h)));}
    private void addCard(LinearLayout p,String h,String body){LinearLayout c=card();TextView ht=text(h,17,true);ht.setTextColor(GREEN);c.addView(ht);c.addView(text(body,15.5f,false));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,dp(12));p.addView(c,lp);}

    private void buildHome(){
        stopSpeech(); LinearLayout root=new LinearLayout(this);base(root);header(root,"A Jornada da Bíblia","Uma etapa de cada vez, do Gênesis ao Apocalipse.");
        LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(8));
        if(current==null){LinearLayout done=card();done.setBackground(bg(SOFT,22));done.addView(text("JORNADA CONCLUÍDA",12,true));done.addView(text("Você chegou ao fim!",29,true));done.addView(text("Toda a sequência bíblica foi concluída.",15,false));content.addView(done);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);return;}
        int i=index();int percent=Math.round(i*100f/readings.size());
        LinearLayout hero=card();hero.setBackground(bg(Color.rgb(28,91,70),24));TextView eyebrow=text("SUA CAMINHADA",12,true);eyebrow.setTextColor(Color.rgb(197,226,211));hero.addView(eyebrow);TextView ref=text(current.reference(),28,true);ref.setTextColor(WHITE);hero.addView(ref);TextView desc=text(current.scope(),14,false);desc.setTextColor(Color.rgb(229,242,235));hero.addView(desc);SpaceView(hero,6);LinearLayout progressLine=new LinearLayout(this);progressLine.setGravity(Gravity.CENTER_VERTICAL);TextView step=text("Etapa "+(i+1)+" de "+readings.size(),13.5f,true);step.setTextColor(Color.rgb(229,242,235));progressLine.addView(step,new LinearLayout.LayoutParams(0,-2,1));TextView pct=text(percent+"%",13.5f,true);pct.setTextColor(WHITE);progressLine.addView(pct);hero.addView(progressLine);ProgressBar heroBar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);heroBar.setMax(readings.size());heroBar.setProgress(i);hero.addView(heroBar,new LinearLayout.LayoutParams(-1,dp(6)));LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.setMargins(0,0,0,dp(14));content.addView(hero,hp);
        addStatsRow(content);
        LinearLayout phrase=card();phrase.setBackground(bg(SOFT,22));TextView label=text("FRASE PARA HOJE",12,true);label.setTextColor(GREEN);phrase.addView(label);phrase.addView(text(phraseOfDay(current),19,true));TextView hint=text("Leia, reflita e coloque em prática.",13,false);hint.setTextColor(MUTED);phrase.addView(hint);LinearLayout.LayoutParams fp=new LinearLayout.LayoutParams(-1,-2);fp.setMargins(0,dp(14),0,dp(14));content.addView(phrase,fp);
        LinearLayout next=card();TextView nextLabel=text("PRÓXIMA ETAPA",12,true);nextLabel.setTextColor(GREEN);next.addView(nextLabel);TextView nextRef=text(current.reference(),24,true);nextRef.setTextColor(DARK);next.addView(nextRef);next.addView(text(current.description,14,false));SpaceView(next,5);Button go=primary("📖  Ir para a leitura");go.setOnClickListener(v->buildReading());next.addView(go,new LinearLayout.LayoutParams(-1,dp(52)));content.addView(next);
        if(prefs.getBoolean("reading_"+i,false)){Button dev=secondary("❤️  Ver devocional");dev.setOnClickListener(v->buildDevotional());LinearLayout.LayoutParams dlp=new LinearLayout.LayoutParams(-1,dp(50));dlp.setMargins(0,dp(12),0,0);content.addView(dev,dlp);}
        Button journey=secondary("▦  Minha jornada e calendário");journey.setOnClickListener(v->buildJourney());LinearLayout.LayoutParams jlp=new LinearLayout.LayoutParams(-1,dp(50));jlp.setMargins(0,dp(12),0,0);content.addView(journey,jlp);
        root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void addStatsRow(LinearLayout content){
        LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);row.setGravity(Gravity.CENTER);String[] values={String.valueOf(completedBooks()),String.valueOf(registeredDays()),String.valueOf(streak())};String[] labels={"livros concluídos","dias registrados","dias consecutivos"};
        for(int i=0;i<3;i++){LinearLayout c=card();c.setPadding(dp(8),dp(12),dp(8),dp(10));TextView v=text(values[i],22,true);v.setGravity(Gravity.CENTER);v.setTextColor(GREEN);c.addView(v);TextView l=text(labels[i],10.5f,false);l.setGravity(Gravity.CENTER);l.setTextColor(MUTED);c.addView(l);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,-2,1);lp.setMargins(i==0?0:dp(4),0,i==2?0:dp(4),dp(14));row.addView(c,lp);}content.addView(row);
    }

    private String phraseOfDay(Reading r){String b=r.book;if(b.equals("Levítico"))return "Aproxime-se de Deus com reverência e um coração inteiro.";if(b.equals("Gênesis"))return "Deus está presente e age mesmo quando a história ainda parece estar começando.";if(b.equals("Êxodo"))return "Deus liberta, conduz e ensina seu povo a caminhar com Ele.";if(b.equals("Números")||b.equals("Deuteronômio"))return "Fidelidade a Deus também se aprende no caminho.";if(b.equals("Salmos")||b.equals("Provérbios"))return "A Palavra de Deus merece entrar não apenas na mente, mas também na vida.";if(b.equals("Mateus")||b.equals("Marcos")||b.equals("Lucas")||b.equals("João"))return "Conhecer Jesus transforma a maneira de viver, servir e confiar.";if(b.equals("Atos"))return "O evangelho continua avançando quando pessoas comuns obedecem a Deus.";return "Leia com atenção, reflita com sinceridade e coloque em prática o que Deus está ensinando.";}

    private void buildReading(){
        stopSpeech();LinearLayout root=new LinearLayout(this);base(root);header(root,"Leitura","Leia primeiro o texto bíblico. Depois, siga para o devocional.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(8));
        LinearLayout c=card();c.setBackground(bg(SOFT,22));TextView lab=text("LEITURA DE HOJE",12,true);lab.setTextColor(GREEN);c.addView(lab);c.addView(text(current.reference(),30,true));c.addView(text(current.scope(),14,false));c.addView(text(current.description,15,false));SpaceView(c,7);Button open=primary("📖  Abrir NTLH");open.setOnClickListener(v->{Intent in=new Intent(this,WebViewActivity.class);in.putExtra("url",current.sbbUrl());in.putExtra("title",current.reference()+" — NTLH");startActivity(in);});c.addView(open,new LinearLayout.LayoutParams(-1,dp(52)));content.addView(c);
        LinearLayout mark=card();LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(-1,-2);mp.setMargins(0,dp(14),0,dp(14));content.addView(mark,mp);readCheck=new CheckBox(this);readCheck.setText("Eu li esta leitura na NTLH");readCheck.setTextSize(16);readCheck.setTextColor(DARK);readCheck.setChecked(prefs.getBoolean("reading_"+index(),false));readCheck.setOnCheckedChangeListener((b,checked)->{prefs.edit().putBoolean("reading_"+index(),checked).apply();refreshReadingButtons();});mark.addView(readCheck);TextView explain=text("Marque somente depois de terminar a leitura. Isso libera o próximo passo.",13.5f,false);explain.setTextColor(MUTED);mark.addView(explain);
        devotionalButton=primary("❤️  Ir para o devocional");devotionalButton.setOnClickListener(v->buildDevotional());content.addView(devotionalButton,new LinearLayout.LayoutParams(-1,dp(52)));Button back=secondary("←  Voltar ao início");back.setOnClickListener(v->buildHome());content.addView(back,new LinearLayout.LayoutParams(-1,dp(50)));root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);refreshReadingButtons();
    }
    private void refreshReadingButtons(){if(devotionalButton==null)return;boolean enabled=readCheck!=null&&readCheck.isChecked();devotionalButton.setEnabled(enabled);devotionalButton.setAlpha(enabled?1f:.45f);}

    private void buildDevotional(){
        stopSpeech();if(!prefs.getBoolean("reading_"+index(),false)){buildReading();return;}LinearLayout root=new LinearLayout(this);base(root);header(root,"Devocional","Pare, reflita e deixe a Palavra falar com você.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(8));LinearLayout refCard=card();refCard.setBackground(bg(SOFT,20));refCard.addView(text("REFLEXÃO DE HOJE",12,true));refCard.addView(text(current.reference(),25,true));refCard.addView(text("5 momentos para ler, refletir, praticar e orar.",13,false));content.addView(refCard,new LinearLayout.LayoutParams(-1,-2));SpaceView(content,14);
        String[] heads={"🧠  1. Explicação do texto","🏺  2. Contexto — tempo e cultura","❤️  3. Aplicação — reflexão","🚶  4. Prática — como viver isso hoje","🙏  5. Oração"};for(int n=0;n<current.devotional.length;n++)addCard(content,heads[n],current.devotional[n]);
        LinearLayout player=card();player.setBackground(bg(Color.rgb(235,241,237),20));player.addView(text("🔊  Ouvir devocional",17,true));playerStatus=text("Pronto para ouvir",13.5f,false);playerStatus.setTextColor(MUTED);player.addView(playerStatus);SpaceView(player,4);LinearLayout controls=new LinearLayout(this);controls.setGravity(Gravity.CENTER_VERTICAL);playPause=primary("▶  Ouvir");playPause.setOnClickListener(v->toggleSpeech());controls.addView(playPause,new LinearLayout.LayoutParams(0,dp(50),1));Button stop=secondary("■  Parar");stop.setOnClickListener(v->stopSpeech());LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(dp(112),dp(50));sp.setMargins(dp(8),0,0,0);controls.addView(stop,sp);player.addView(controls);LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,-2);pp.setMargins(0,0,0,dp(12));content.addView(player,pp);
        Button finish=primary("✓  Concluir e continuar a jornada");finish.setOnClickListener(v->completeReading());content.addView(finish,new LinearLayout.LayoutParams(-1,dp(52)));Button back=secondary("←  Voltar para a leitura");back.setOnClickListener(v->buildReading());content.addView(back,new LinearLayout.LayoutParams(-1,dp(50)));root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void completeReading(){
        stopSpeech();int old=index();String today=dateKeyFormat.format(Calendar.getInstance().getTime());int count=prefs.getInt("day_count_"+today,0)+1;
        prefs.edit().putInt(CURRENT,old+1).putBoolean("reading_"+old,true).putBoolean("day_"+today,true).putInt("day_count_"+today,count).apply();
        current=getCurrentReading(); buildCompletion();
    }
    private void buildCompletion(){
        LinearLayout root=new LinearLayout(this);base(root);header(root,"Muito bem!","Mais uma etapa concluída na sua jornada.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(18),0,dp(8));
        LinearLayout c=card();c.setBackground(bg(SOFT,24));TextView icon=text("✓",42,true);icon.setTextColor(GREEN);icon.setGravity(Gravity.CENTER);c.addView(icon);TextView title=text("Leitura concluída",24,true);title.setGravity(Gravity.CENTER);c.addView(title);TextView msg=text("Você pode continuar agora. Não existe limite de um devocional por dia.",14.5f,false);msg.setGravity(Gravity.CENTER);msg.setTextColor(MUTED);c.addView(msg);content.addView(c);SpaceView(content,14);
        if(current!=null){Button next=primary("📖  Continuar para o próximo devocional");next.setOnClickListener(v->buildReading());content.addView(next,new LinearLayout.LayoutParams(-1,dp(54)));SpaceView(content,8);}
        Button journey=secondary("▦  Ver minha jornada");journey.setOnClickListener(v->buildJourney());content.addView(journey,new LinearLayout.LayoutParams(-1,dp(50)));Button home=secondary("←  Voltar ao início");home.setOnClickListener(v->buildHome());LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,dp(50));hp.setMargins(0,dp(8),0,0);content.addView(home,hp);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void buildJourney(){
        stopSpeech();LinearLayout root=new LinearLayout(this);base(root);header(root,"Minha jornada","Veja seu ritmo de leitura, seus dias e seu calendário.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(8));
        LinearLayout overview=card();overview.setBackground(bg(SOFT,22));overview.addView(text("SEU PROGRESSO",12,true));overview.addView(text(Math.round(index()*100f/readings.size())+"%",31,true));overview.addView(text(index()+" etapas concluídas de "+readings.size(),14,false));ProgressBar bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);bar.setMax(readings.size());bar.setProgress(index());overview.addView(bar,new LinearLayout.LayoutParams(-1,dp(6)));content.addView(overview);SpaceView(content,12);
        addStatsRow(content);
        LinearLayout cal=card();TextView calTitle=text("CALENDÁRIO DE LEITURA",18,true);cal.addView(calTitle);TextView calHint=text("Cada dia é registrado quando pelo menos um devocional é concluído. Você pode avançar quantas etapas quiser no mesmo dia.",12.5f,false);calHint.setTextColor(MUTED);cal.addView(calHint);SpaceView(cal,8);
        LinearLayout nav=new LinearLayout(this);nav.setGravity(Gravity.CENTER_VERTICAL);Button prev=secondary("‹");prev.setTextSize(24);prev.setOnClickListener(v->{calendarCursor.add(Calendar.MONTH,-1);renderCalendar(cal);});nav.addView(prev,new LinearLayout.LayoutParams(dp(52),dp(46)));TextView month=text(monthFormat.format(calendarCursor.getTime()),18,true);month.setGravity(Gravity.CENTER);month.setTextColor(GREEN);nav.addView(month,new LinearLayout.LayoutParams(0,dp(46),1));Button next=secondary("›");next.setTextSize(24);next.setOnClickListener(v->{calendarCursor.add(Calendar.MONTH,1);renderCalendar(cal);});nav.addView(next,new LinearLayout.LayoutParams(dp(52),dp(46)));cal.addView(nav);
        GridLayout grid=new GridLayout(this);grid.setColumnCount(7);grid.setUseDefaultMargins(false);cal.addView(grid,new LinearLayout.LayoutParams(-1,-2));renderCalendarInto(cal,grid,month);
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.setMargins(0,0,0,dp(12));content.addView(cal,cp);
        Button back=secondary("←  Voltar ao início");back.setOnClickListener(v->buildHome());content.addView(back,new LinearLayout.LayoutParams(-1,dp(50)));root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }
    private void renderCalendar(LinearLayout cal){
        TextView month=null;GridLayout grid=null;for(int i=0;i<cal.getChildCount();i++){View v=cal.getChildAt(i);if(v instanceof LinearLayout){LinearLayout nav=(LinearLayout)v;if(nav.getChildCount()==3&&nav.getChildAt(1) instanceof TextView){month=(TextView)nav.getChildAt(1);}}if(v instanceof GridLayout)grid=(GridLayout)v;}if(month!=null)month.setText(monthFormat.format(calendarCursor.getTime()));if(grid!=null)renderCalendarInto(cal,grid,month);
    }
    private void renderCalendarInto(LinearLayout cal,GridLayout grid,TextView month){
        grid.removeAllViews();String[] names={"D","S","T","Q","Q","S","S"};for(String n:names){TextView d=text(n,11,true);d.setGravity(Gravity.CENTER);d.setTextColor(MUTED);grid.addView(d,new GridLayout.LayoutParams());}
        Calendar first=(Calendar)calendarCursor.clone();first.set(Calendar.DAY_OF_MONTH,1);int offset=first.get(Calendar.DAY_OF_WEEK)-1;int max=first.getActualMaximum(Calendar.DAY_OF_MONTH);Set<String> done=completedDates();String today=dateKeyFormat.format(Calendar.getInstance().getTime());
        for(int i=0;i<offset;i++)addDayCell(grid,"",false,false,false,0);
        for(int day=1;day<=max;day++){Calendar c=(Calendar)calendarCursor.clone();c.set(Calendar.DAY_OF_MONTH,day);String key=dateKeyFormat.format(c.getTime());boolean isDone=done.contains(key);boolean isToday=key.equals(today);addDayCell(grid,String.valueOf(day),isDone,isToday,false,prefs.getInt("day_count_"+key,0));}
        int cells=offset+max;for(int i=cells;i<42;i++)addDayCell(grid,"",false,false,false,0);
    }
    private void addDayCell(GridLayout grid,String label,boolean done,boolean today,boolean ignored,int count){
        TextView d=text(label,13.5f,done||today);d.setGravity(Gravity.CENTER);d.setTextColor(done?GREEN:(today?DARK:MUTED));d.setBackground(done?bg(Color.rgb(210,234,221),24):today?outline(WHITE,1,GREEN,24):bg(Color.TRANSPARENT,24));String info=done?(count>1?count+" etapas concluídas":"Dia concluído"):"Sem conclusão";d.setContentDescription(label.isEmpty()?"":label+" — "+info);GridLayout.LayoutParams lp=new GridLayout.LayoutParams();lp.width=0;lp.height=dp(42);lp.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);lp.setMargins(dp(2),dp(2),dp(2),dp(2));grid.addView(d,lp);
    }

    private Set<String> completedDates(){Set<String> out=new HashSet<>();Map<String,?> all=prefs.getAll();for(String k:all.keySet())if(k.startsWith("day_")&&k.length()>4&&!k.startsWith("day_count_"))out.add(k.substring(4));return out;}
    private int registeredDays(){return completedDates().size();}
    private int streak(){Set<String> done=completedDates();Calendar c=Calendar.getInstance();String today=dateKeyFormat.format(c.getTime());if(!done.contains(today))c.add(Calendar.DAY_OF_YEAR,-1);int n=0;while(done.contains(dateKeyFormat.format(c.getTime()))){n++;c.add(Calendar.DAY_OF_YEAR,-1);}return n;}
    private int completedBooks(){if(index()>=readings.size())return distinctBooks(readings.size());Set<String> books=new HashSet<>();for(int i=0;i<index()&&i<readings.size();i++)books.add(readings.get(i).book);return books.size();}
    private int distinctBooks(int end){Set<String> books=new HashSet<>();for(int i=0;i<end&&i<readings.size();i++)books.add(readings.get(i).book);return books.size();}

    private void toggleSpeech(){if(tts==null)return;if(speaking){tts.stop();speaking=false;playPause.setText("▶  Continuar");playerStatus.setText("Pausado na parte "+(speechPart+1)+" de 5");}else{speaking=true;playPause.setText("Ⅱ  Pausar");speakCurrent();}}
    private void speakCurrent(){if(!speaking||current==null)return;playerStatus.setText("Reproduzindo parte "+(speechPart+1)+" de 5");tts.speak(current.devotional[speechPart],TextToSpeech.QUEUE_FLUSH,null,"devotional-"+speechPart);}
    private void nextSpeechPart(){if(!speaking)return;speechPart++;if(current!=null&&speechPart<current.devotional.length)speakCurrent();else finishSpeech();}
    private void stopSpeech(){if(tts!=null)tts.stop();speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir");if(playerStatus!=null)playerStatus.setText("Pronto para ouvir");}
    private void finishSpeech(){speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir novamente");if(playerStatus!=null)playerStatus.setText("Devocional concluído");}
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}