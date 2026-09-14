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
    private static final int BG=Color.rgb(246,248,246), GREEN=Color.rgb(18,91,69), GREEN_DARK=Color.rgb(10,64,49), DARK=Color.rgb(27,42,36), MUTED=Color.rgb(96,108,102), SOFT=Color.rgb(228,241,234), WHITE=Color.WHITE, BORDER=Color.rgb(220,228,223);
    private SharedPreferences prefs; private List<Reading> readings; private Reading current;
    private TextToSpeech tts; private Button playPause; private TextView playerStatus; private boolean speaking; private int speechPart;
    private CheckBox readCheck; private Button devotionalButton;
    private Calendar calendarCursor=Calendar.getInstance();
    private final SimpleDateFormat dateKeyFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
    private final SimpleDateFormat monthFormat=new SimpleDateFormat("MMMM yyyy",new Locale("pt","BR"));

    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        getWindow().setStatusBarColor(GREEN_DARK); getWindow().setNavigationBarColor(BG);
        prefs=getSharedPreferences(PREFS,MODE_PRIVATE); readings=ReadingPlan.build(); migrateOldProgress(); current=getCurrentReading(); buildHome();
        tts=new TextToSpeech(this,status->{if(status==TextToSpeech.SUCCESS){tts.setLanguage(new Locale("pt","BR"));tts.setOnUtteranceProgressListener(new UtteranceProgressListener(){public void onStart(String id){}public void onDone(String id){runOnUiThread(()->nextSpeechPart());}public void onError(String id){runOnUiThread(()->stopSpeech());}});}});
    }

    private void migrateOldProgress(){
        int version=prefs.getInt(PLAN_VERSION,0);
        if(version<CURRENT_PLAN_VERSION){int old=prefs.getInt(CURRENT,-1);int lev=findReadingIndex("Levítico",1,1);if(old<0)old=prefs.getBoolean("lev1",false)?lev+1:lev;else if(old==90||old==91)old=prefs.getBoolean("reading_90",false)?lev+1:lev;prefs.edit().putInt(CURRENT,old).putInt(PLAN_VERSION,CURRENT_PLAN_VERSION).apply();}
        else if(!prefs.contains(CURRENT))prefs.edit().putInt(CURRENT,findReadingIndex("Levítico",1,1)).apply();
    }
    private int findReadingIndex(String book,int chapter,int verse){for(int i=0;i<readings.size();i++){Reading r=readings.get(i);if(r.book.equals(book)&&r.chapter==chapter&&r.startVerse==verse)return i;}return 0;}
    private Reading getCurrentReading(){int i=index();if(i<0)i=0;return i<readings.size()?readings.get(i):null;}
    private int index(){return prefs.getInt(CURRENT,findReadingIndex("Levítico",1,1));}
    private int dp(int v){return(int)(v*getResources().getDisplayMetrics().density+.5f);}

    private TextView text(String s,float size,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(DARK);t.setIncludeFontPadding(false);t.setPadding(0,dp(3),0,dp(3));if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    private GradientDrawable bg(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private GradientDrawable outline(int color,int stroke,int strokeColor,int radius){GradientDrawable d=bg(color,radius);d.setStroke(dp(stroke),strokeColor);return d;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(15);b.setGravity(Gravity.CENTER);b.setMinHeight(dp(52));b.setMinimumHeight(dp(52));b.setPadding(dp(14),dp(10),dp(14),dp(10));b.setStateListAnimator(null);b.setIncludeFontPadding(false);return b;}
    private Button primary(String s){Button b=button(s);b.setTextColor(WHITE);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackground(bg(GREEN,15));b.setElevation(dp(1));return b;}
    private Button secondary(String s){Button b=button(s);b.setTextColor(GREEN);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackground(outline(WHITE,1,BORDER,15));return b;}
    private LinearLayout card(){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(18),dp(17),dp(18),dp(17));c.setBackground(outline(WHITE,1,BORDER,22));c.setElevation(dp(1));return c;}
    private void base(LinearLayout root){root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(16),dp(12),dp(16),dp(12));root.setBackgroundColor(BG);}
    private ScrollView scroll(LinearLayout content){ScrollView s=new ScrollView(this);s.setFillViewport(true);s.setClipToPadding(false);s.setPadding(0,0,0,dp(6));s.addView(content);return s;}
    private void header(LinearLayout root,String title,String subtitle){LinearLayout h=new LinearLayout(this);h.setOrientation(LinearLayout.VERTICAL);h.setPadding(dp(3),dp(5),dp(3),0);TextView eyebrow=text("A JORNADA DA BÍBLIA",11,true);eyebrow.setTextColor(GREEN);h.addView(eyebrow);TextView t=text(title,28,true);t.setTextColor(GREEN_DARK);h.addView(t);TextView s=text(subtitle,14,false);s.setTextColor(MUTED);s.setPadding(0,dp(5),0,dp(2));h.addView(s);root.addView(h);}
    private void SpaceView(LinearLayout p,int h){View v=new View(this);p.addView(v,new LinearLayout.LayoutParams(1,dp(h)));}
    private void addCard(LinearLayout p,String h,String body){LinearLayout c=card();TextView ht=text(h,17,true);ht.setTextColor(GREEN);c.addView(ht);TextView bt=text(body,15.5f,false);bt.setLineSpacing(1.05f,1f);c.addView(bt);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,dp(10));p.addView(c,lp);}
    private LinearLayout.LayoutParams fullWrap(){return new LinearLayout.LayoutParams(-1,-2);}
    private LinearLayout.LayoutParams gap(int bottom){LinearLayout.LayoutParams lp=fullWrap();lp.setMargins(0,0,0,dp(bottom));return lp;}

    private void buildHome(){
        stopSpeech();LinearLayout root=new LinearLayout(this);base(root);header(root,"Sua jornada","Uma etapa por vez, do Gênesis ao Apocalipse.");
        LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(28));
        if(current==null){LinearLayout done=card();done.setBackground(bg(SOFT,24));done.addView(text("JORNADA CONCLUÍDA",11,true));done.addView(text("Você chegou ao fim.",28,true));done.addView(text("Toda a sequência bíblica foi concluída.",15,false));content.addView(done);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);return;}
        int i=index();int percent=Math.round(i*100f/readings.size());
        LinearLayout hero=card();hero.setBackground(bg(GREEN,25));hero.setPadding(dp(20),dp(20),dp(20),dp(20));
        TextView eyebrow=text("AGORA",11,true);eyebrow.setTextColor(Color.rgb(188,222,207));hero.addView(eyebrow);
        TextView ref=text(current.reference(),28,true);ref.setTextColor(WHITE);ref.setPadding(0,dp(6),0,dp(5));hero.addView(ref);
        TextView desc=text(current.scope(),14,false);desc.setTextColor(Color.rgb(229,242,235));desc.setLineSpacing(1.05f,1f);hero.addView(desc);SpaceView(hero,10);
        LinearLayout progressLine=new LinearLayout(this);progressLine.setGravity(Gravity.CENTER_VERTICAL);TextView step=text("Etapa "+(i+1)+" de "+readings.size(),13,true);step.setTextColor(Color.rgb(229,242,235));progressLine.addView(step,new LinearLayout.LayoutParams(0,-2,1));TextView pct=text(percent+"%",13,true);pct.setTextColor(WHITE);progressLine.addView(pct);hero.addView(progressLine);
        ProgressBar bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);bar.setMax(readings.size());bar.setProgress(i);LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-1,dp(6));bp.setMargins(0,dp(9),0,0);hero.addView(bar,bp);content.addView(hero,gap(12));
        addStatsRow(content);
        LinearLayout phrase=card();phrase.setBackground(bg(SOFT,22));TextView label=text("FRASE PARA HOJE",11,true);label.setTextColor(GREEN);phrase.addView(label);TextView ph=text(phraseOfDay(current),18,true);ph.setLineSpacing(1.04f,1f);ph.setPadding(0,dp(7),0,dp(5));phrase.addView(ph);TextView hint=text("Leia. Reflita. Viva.",13,false);hint.setTextColor(MUTED);phrase.addView(hint);content.addView(phrase,gap(12));
        LinearLayout next=card();TextView nextLabel=text("PRÓXIMA ETAPA",11,true);nextLabel.setTextColor(GREEN);next.addView(nextLabel);TextView nextRef=text(current.reference(),23,true);nextRef.setPadding(0,dp(6),0,dp(4));next.addView(nextRef);TextView nd=text(current.description,14,false);nd.setLineSpacing(1.04f,1f);next.addView(nd);SpaceView(next,10);Button go=primary("Abrir leitura  →");go.setOnClickListener(v->buildReading());next.addView(go,fullWrap());content.addView(next,gap(12));
        if(prefs.getBoolean("reading_"+i,false)){Button dev=secondary("Ver devocional  ♥");dev.setOnClickListener(v->buildDevotional());content.addView(dev,gap(10));}
        Button journey=secondary("Minha jornada  ·  Calendário");journey.setOnClickListener(v->buildJourney());content.addView(journey,fullWrap());
        root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void addStatsRow(LinearLayout content){
        LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);row.setGravity(Gravity.CENTER);String[] values={String.valueOf(completedBooks()),String.valueOf(registeredDays()),String.valueOf(streak())};String[] labels={"livros","dias","sequência"};
        for(int i=0;i<3;i++){LinearLayout c=card();c.setPadding(dp(7),dp(11),dp(7),dp(10));TextView v=text(values[i],21,true);v.setGravity(Gravity.CENTER);v.setTextColor(GREEN);c.addView(v);TextView l=text(labels[i],10.5f,false);l.setGravity(Gravity.CENTER);l.setTextColor(MUTED);c.addView(l);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,-2,1);lp.setMargins(i==0?0:dp(4),0,i==2?0:dp(4),0);row.addView(c,lp);}content.addView(row,gap(12));
    }

    private String phraseOfDay(Reading r){String b=r.book;if(b.equals("Levítico"))return "Aproxime-se de Deus com reverência e um coração inteiro.";if(b.equals("Gênesis"))return "Deus está presente e age mesmo quando a história ainda parece estar começando.";if(b.equals("Êxodo"))return "Deus liberta, conduz e ensina seu povo a caminhar com Ele.";if(b.equals("Números")||b.equals("Deuteronômio"))return "Fidelidade a Deus também se aprende no caminho.";if(b.equals("Salmos")||b.equals("Provérbios"))return "A Palavra de Deus merece entrar não apenas na mente, mas também na vida.";if(b.equals("Mateus")||b.equals("Marcos")||b.equals("Lucas")||b.equals("João"))return "Conhecer Jesus transforma a maneira de viver, servir e confiar.";if(b.equals("Atos"))return "O evangelho continua avançando quando pessoas comuns obedecem a Deus.";return "Leia com atenção, reflita com sinceridade e coloque em prática o que Deus está ensinando.";}

    private void buildReading(){
        stopSpeech();LinearLayout root=new LinearLayout(this);base(root);header(root,"Leitura","Leia primeiro o texto bíblico. Depois, siga para a reflexão.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(28));
        LinearLayout c=card();c.setBackground(bg(SOFT,23));c.addView(text("LEITURA DE HOJE",11,true));TextView r=text(current.reference(),29,true);r.setPadding(0,dp(7),0,dp(5));c.addView(r);c.addView(text(current.scope(),14,false));TextView d=text(current.description,15,false);d.setLineSpacing(1.05f,1f);c.addView(d);SpaceView(c,10);Button open=primary("Abrir NTLH  →");open.setOnClickListener(v->{Intent in=new Intent(this,WebViewActivity.class);in.putExtra("url",current.sbbUrl());in.putExtra("title",current.reference()+" — NTLH");startActivity(in);});c.addView(open,fullWrap());content.addView(c,gap(12));
        LinearLayout mark=card();readCheck=new CheckBox(this);readCheck.setText("Eu li esta leitura na NTLH");readCheck.setTextSize(16);readCheck.setTextColor(DARK);readCheck.setPadding(0,0,0,0);readCheck.setChecked(prefs.getBoolean("reading_"+index(),false));readCheck.setOnCheckedChangeListener((b,checked)->{prefs.edit().putBoolean("reading_"+index(),checked).apply();refreshReadingButtons();});mark.addView(readCheck);TextView explain=text("Marque depois de terminar a leitura. Isso libera a reflexão.",13.5f,false);explain.setTextColor(MUTED);explain.setPadding(0,dp(7),0,0);mark.addView(explain);content.addView(mark,gap(12));
        devotionalButton=primary("Ir para o devocional  →");devotionalButton.setOnClickListener(v->buildDevotional());content.addView(devotionalButton,gap(10));Button back=secondary("Voltar ao início");back.setOnClickListener(v->buildHome());content.addView(back,fullWrap());root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);refreshReadingButtons();
    }
    private void refreshReadingButtons(){if(devotionalButton==null)return;boolean enabled=readCheck!=null&&readCheck.isChecked();devotionalButton.setEnabled(enabled);devotionalButton.setAlpha(enabled?1f:.42f);}

    private void buildDevotional(){
        stopSpeech();if(!prefs.getBoolean("reading_"+index(),false)){buildReading();return;}LinearLayout root=new LinearLayout(this);base(root);header(root,"Devocional","Pare, reflita e deixe a Palavra falar com você.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(28));LinearLayout refCard=card();refCard.setBackground(bg(SOFT,22));refCard.addView(text("REFLEXÃO DE HOJE",11,true));refCard.addView(text(current.reference(),25,true));refCard.addView(text("5 momentos para ler, refletir, praticar e orar.",13,false));content.addView(refCard);SpaceView(content,13);
        String[] heads={"1  ·  Explicação do texto","2  ·  Contexto — tempo e cultura","3  ·  Aplicação — reflexão","4  ·  Prática — como viver hoje","5  ·  Oração"};for(int n=0;n<current.devotional.length;n++)addCard(content,heads[n],current.devotional[n]);
        LinearLayout player=card();player.setBackground(bg(Color.rgb(235,242,237),20));player.addView(text("Ouvir devocional",17,true));playerStatus=text("Pronto para ouvir",13.5f,false);playerStatus.setTextColor(MUTED);player.addView(playerStatus);SpaceView(player,5);LinearLayout controls=new LinearLayout(this);controls.setGravity(Gravity.CENTER_VERTICAL);playPause=primary("▶  Ouvir");playPause.setOnClickListener(v->toggleSpeech());controls.addView(playPause,new LinearLayout.LayoutParams(0,dp(52),1));Button stop=secondary("Parar");stop.setOnClickListener(v->stopSpeech());LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(dp(92),dp(52));sp.setMargins(dp(8),0,0,0);controls.addView(stop,sp);player.addView(controls);content.addView(player,gap(12));
        Button finish=primary("Concluir e continuar  →");finish.setOnClickListener(v->completeReading());content.addView(finish,gap(10));Button back=secondary("Voltar para a leitura");back.setOnClickListener(v->buildReading());content.addView(back);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void completeReading(){stopSpeech();int old=index();String today=dateKeyFormat.format(Calendar.getInstance().getTime());int count=prefs.getInt("day_count_"+today,0)+1;prefs.edit().putInt(CURRENT,old+1).putBoolean("reading_"+old,true).putBoolean("day_"+today,true).putInt("day_count_"+today,count).apply();current=getCurrentReading();buildCompletion();}
    private void buildCompletion(){
        LinearLayout root=new LinearLayout(this);base(root);header(root,"Concluído","Mais uma etapa registrada na sua jornada.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(18),0,dp(28));LinearLayout c=card();c.setBackground(bg(SOFT,24));TextView icon=text("✓",44,true);icon.setTextColor(GREEN);icon.setGravity(Gravity.CENTER);c.addView(icon);TextView title=text("Leitura concluída",24,true);title.setGravity(Gravity.CENTER);c.addView(title);TextView msg=text("Você pode continuar agora. Não existe limite de um devocional por dia.",14.5f,false);msg.setGravity(Gravity.CENTER);msg.setTextColor(MUTED);c.addView(msg);content.addView(c,gap(14));if(current!=null){Button next=primary("Próximo devocional  →");next.setOnClickListener(v->buildReading());content.addView(next,gap(10));}Button journey=secondary("Minha jornada");journey.setOnClickListener(v->buildJourney());content.addView(journey,gap(10));Button home=secondary("Voltar ao início");home.setOnClickListener(v->buildHome());content.addView(home);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void buildJourney(){
        stopSpeech();LinearLayout root=new LinearLayout(this);base(root);header(root,"Minha jornada","Seu ritmo de leitura, seus dias e seu calendário.");LinearLayout content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,dp(16),0,dp(28));
        LinearLayout overview=card();overview.setBackground(bg(SOFT,22));overview.addView(text("SEU PROGRESSO",11,true));overview.addView(text(Math.round(index()*100f/readings.size())+"%",31,true));overview.addView(text(index()+" etapas concluídas de "+readings.size(),14,false));ProgressBar bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);bar.setMax(readings.size());bar.setProgress(index());overview.addView(bar,new LinearLayout.LayoutParams(-1,dp(6)));content.addView(overview,gap(12));addStatsRow(content);
        LinearLayout cal=card();TextView calTitle=text("Calendário de leitura",19,true);cal.addView(calTitle);TextView calHint=text("Cada dia aparece quando pelo menos uma etapa é concluída.",12.5f,false);calHint.setTextColor(MUTED);calHint.setPadding(0,dp(5),0,dp(2));cal.addView(calHint);SpaceView(cal,10);
        LinearLayout nav=new LinearLayout(this);nav.setGravity(Gravity.CENTER_VERTICAL);Button prev=secondary("‹");prev.setTextSize(25);prev.setPadding(0,0,0,0);prev.setOnClickListener(v->{calendarCursor.add(Calendar.MONTH,-1);renderCalendar(cal);});nav.addView(prev,new LinearLayout.LayoutParams(dp(48),dp(48)));TextView month=text(monthFormat.format(calendarCursor.getTime()),17,true);month.setGravity(Gravity.CENTER);month.setTextColor(GREEN);nav.addView(month,new LinearLayout.LayoutParams(0,dp(48),1));Button next=secondary("›");next.setTextSize(25);next.setPadding(0,0,0,0);next.setOnClickListener(v->{calendarCursor.add(Calendar.MONTH,1);renderCalendar(cal);});nav.addView(next,new LinearLayout.LayoutParams(dp(48),dp(48)));cal.addView(nav);renderCalendar(cal);content.addView(cal,gap(12));
        Button back=secondary("Voltar ao início");back.setOnClickListener(v->buildHome());content.addView(back);root.addView(scroll(content),new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }

    private void renderCalendar(LinearLayout cal){
        TextView month=null;LinearLayout grid=null;
        for(int i=0;i<cal.getChildCount();i++){View v=cal.getChildAt(i);if(v instanceof LinearLayout){LinearLayout l=(LinearLayout)v;if(l.getTag()!=null&&l.getTag().equals("calendar_grid"))grid=l;else if(l.getChildCount()==3&&l.getChildAt(1) instanceof TextView)month=(TextView)l.getChildAt(1);}}
        if(grid==null){grid=new LinearLayout(this);grid.setOrientation(LinearLayout.VERTICAL);grid.setTag("calendar_grid");cal.addView(grid,fullWrap());}
        if(month!=null)month.setText(monthFormat.format(calendarCursor.getTime()));
        renderCalendarInto(grid);
    }
    private void renderCalendarInto(LinearLayout grid){
        grid.removeAllViews();String[] names={"D","S","T","Q","Q","S","S"};LinearLayout headerRow=new LinearLayout(this);headerRow.setOrientation(LinearLayout.HORIZONTAL);
        for(String n:names){TextView d=text(n,11,true);d.setGravity(Gravity.CENTER);d.setTextColor(MUTED);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(26),1);headerRow.addView(d,lp);}grid.addView(headerRow);
        Calendar first=(Calendar)calendarCursor.clone();first.set(Calendar.DAY_OF_MONTH,1);int offset=first.get(Calendar.DAY_OF_WEEK)-1;int max=first.getActualMaximum(Calendar.DAY_OF_MONTH);Set<String> done=completedDates();String today=dateKeyFormat.format(Calendar.getInstance().getTime());
        LinearLayout row=null;int col=0;for(int i=0;i<offset;i++){if(col==0){row=calendarRow();grid.addView(row);}addDayCell(row,"",false,false,0);col++;}
        for(int day=1;day<=max;day++){if(col==0){row=calendarRow();grid.addView(row);}Calendar c=(Calendar)calendarCursor.clone();c.set(Calendar.DAY_OF_MONTH,day);String key=dateKeyFormat.format(c.getTime());boolean isDone=done.contains(key);boolean isToday=key.equals(today);addDayCell(row,String.valueOf(day),isDone,isToday,prefs.getInt("day_count_"+key,0));col++;if(col==7)col=0;}
        if(col>0)while(col<7){addDayCell(row,"",false,false,0);col++;}
    }
    private LinearLayout calendarRow(){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);r.setGravity(Gravity.CENTER_VERTICAL);return r;}
    private void addDayCell(LinearLayout row,String label,boolean done,boolean today,int count){TextView d=text(label,13.5f,done||today);d.setGravity(Gravity.CENTER);d.setTextColor(done?GREEN:(today?DARK:MUTED));d.setBackground(done?bg(Color.rgb(209,235,221),20):today?outline(WHITE,1,GREEN,20):bg(Color.TRANSPARENT,20));String info=done?(count>1?count+" etapas concluídas":"Dia concluído"):"Sem conclusão";d.setContentDescription(label.isEmpty()?"":label+" — "+info);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(40),1);lp.setMargins(dp(2),dp(2),dp(2),dp(2));row.addView(d,lp);}

    private Set<String> completedDates(){Set<String> out=new HashSet<>();Map<String,?> all=prefs.getAll();for(String k:all.keySet())if(k.startsWith("day_")&&k.length()>4&&!k.startsWith("day_count_"))out.add(k.substring(4));return out;}
    private int registeredDays(){return completedDates().size();}
    private int streak(){Set<String> done=completedDates();Calendar c=Calendar.getInstance();String today=dateKeyFormat.format(c.getTime());if(!done.contains(today))c.add(Calendar.DAY_OF_YEAR,-1);int n=0;while(done.contains(dateKeyFormat.format(c.getTime()))){n++;c.add(Calendar.DAY_OF_YEAR,-1);}return n;}
    private int completedBooks(){if(index()>=readings.size())return distinctBooks(readings.size());return distinctBooks(index());}
    private int distinctBooks(int end){Set<String> books=new HashSet<>();for(int i=0;i<end&&i<readings.size();i++)books.add(readings.get(i).book);return books.size();}

    private void toggleSpeech(){if(tts==null)return;if(speaking){tts.stop();speaking=false;playPause.setText("▶  Continuar");playerStatus.setText("Pausado na parte "+(speechPart+1)+" de 5");}else{speaking=true;playPause.setText("Ⅱ  Pausar");speakCurrent();}}
    private void speakCurrent(){if(!speaking||current==null)return;playerStatus.setText("Reproduzindo parte "+(speechPart+1)+" de 5");tts.speak(current.devotional[speechPart],TextToSpeech.QUEUE_FLUSH,null,"devotional-"+speechPart);}
    private void nextSpeechPart(){if(!speaking)return;speechPart++;if(current!=null&&speechPart<current.devotional.length)speakCurrent();else finishSpeech();}
    private void stopSpeech(){if(tts!=null)tts.stop();speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir");if(playerStatus!=null)playerStatus.setText("Pronto para ouvir");}
    private void finishSpeech(){speaking=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir novamente");if(playerStatus!=null)playerStatus.setText("Devocional concluído");}
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}