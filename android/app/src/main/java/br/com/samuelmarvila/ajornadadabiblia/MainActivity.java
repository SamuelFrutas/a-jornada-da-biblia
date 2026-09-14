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
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String PREFS = "jornada";
    private SharedPreferences prefs;
    private TextToSpeech tts;
    private Button playPause;
    private TextView playerStatus;
    private boolean speaking = false;
    private boolean paused = false;
    private int speechPart = 0;

    private final String[] speechParts = {
        "Explicação do texto. Levítico capítulo um apresenta as orientações de Deus para a oferta queimada. O Senhor fala a Moisés da Tenda Sagrada e estabelece como o israelita deveria apresentar o animal, como o sangue seria tratado pelos sacerdotes e como a oferta seria totalmente queimada. São apresentadas três possibilidades: gado, rebanho de ovelhas ou cabras e aves.",
        "Contexto. Levítico vem logo depois da construção do Tabernáculo. Israel estava aprendendo a viver como povo da aliança na presença de um Deus santo. Sacrifícios faziam parte desse sistema de culto. A exigência de um animal sem defeito mostrava que não se oferecia a Deus algo tratado como sem valor. A expressão cheiro agradável descreve a oferta aceita por Deus; não significa que Deus precisasse de alimento.",
        "Aplicação. O texto nos lembra que aproximar-se de Deus não deve ser tratado de qualquer maneira. A adoração envolve reverência, entrega e reconhecimento da santidade de Deus. Para o cristão, não devemos transformar Levítico um numa simples fórmula dizendo que o animal era Jesus. O texto precisa primeiro ser entendido dentro da aliança de Israel. Depois, à luz do Novo Testamento, reconhecemos que a obra de Cristo é apresentada como definitiva.",
        "Prática. Pratique uma adoração que não seja apenas aparência. Separe um tempo real para Deus, confesse aquilo que precisa ser confessado e não trate a fé como uma negociação para conseguir benefícios. Entregue a Deus suas decisões, hábitos, tempo e prioridades. Faça sua leitura bíblica com atenção e procure obedecer ao que realmente o texto ensina.",
        "Oração. Senhor Deus, ensina-me a te tratar com reverência e sinceridade. Que eu não transforme minha fé em aparência nem tente negociar contigo. Ajuda-me a reconhecer a seriedade do pecado, valorizar a tua presença e viver uma vida de entrega. Dá-me entendimento para ler tua Palavra com fidelidade e coragem para praticá-la. Em nome de Jesus, amém."
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        buildScreen();
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "BR"));
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override public void onStart(String id) { }
                    @Override public void onDone(String id) {
                        runOnUiThread(() -> {
                            if (!speaking) return;
                            speechPart++;
                            if (speechPart < speechParts.length) {
                                playerStatus.setText("Reproduzindo parte " + (speechPart + 1) + " de 5");
                                speakCurrentPart();
                            } else finishSpeech();
                        });
                    }
                    @Override public void onError(String id) { runOnUiThread(() -> { if (speaking) finishSpeech(); }); }
                });
            }
        });
    }

    private int dp(int value) { return (int)(value * getResources().getDisplayMetrics().density + 0.5f); }
    private TextView text(String value, float size, boolean bold) {
        TextView v = new TextView(this); v.setText(value); v.setTextSize(size); v.setTextColor(Color.rgb(35,40,38));
        v.setPadding(0, dp(5), 0, dp(5)); if (bold) v.setTypeface(Typeface.DEFAULT, Typeface.BOLD); return v;
    }
    private GradientDrawable bg(int color, int radius) { GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d; }
    private Button button(String label) { Button b=new Button(this); b.setText(label); b.setAllCaps(false); b.setTextSize(15); b.setMinHeight(dp(48)); b.setPadding(dp(16),0,dp(16),0); return b; }

    private void addCard(LinearLayout parent,String heading,String body) {
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(dp(20),dp(16),dp(20),dp(16)); card.setBackground(bg(Color.WHITE,18));
        TextView h=text(heading,17,true); h.setTextColor(Color.rgb(25,86,68)); card.addView(h); card.addView(text(body,15.5f,false));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.setMargins(0,0,0,dp(12)); parent.addView(card,p);
    }

    private void buildScreen() {
        boolean done=prefs.getBoolean("lev1",false);
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(18),dp(16),dp(18),dp(18)); root.setBackgroundColor(Color.rgb(246,247,245));
        TextView title=text("A Jornada da Bíblia",28,true); title.setTextColor(Color.rgb(22,75,59)); root.addView(title);
        TextView subtitle=text("Sua caminhada pela Palavra, em sequência.",14,false); subtitle.setTextColor(Color.rgb(90,98,94)); root.addView(subtitle);
        ScrollView scroll=new ScrollView(this); LinearLayout content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(0,dp(18),0,dp(12));

        LinearLayout readingCard=new LinearLayout(this); readingCard.setOrientation(LinearLayout.VERTICAL); readingCard.setPadding(dp(22),dp(20),dp(22),dp(20)); readingCard.setBackground(bg(Color.rgb(221,237,229),22));
        TextView label=text("LEITURA DE HOJE",12,true); label.setTextColor(Color.rgb(53,105,86)); readingCard.addView(label);
        TextView ref=text("Levítico 1",30,true); ref.setTextColor(Color.rgb(20,78,60)); readingCard.addView(ref);
        readingCard.addView(text("Capítulo inteiro  •  17 versículos",14,false)); readingCard.addView(text("A oferta queimada e a adoração diante de Deus.",14,false));
        Button read=button("📖  Ler na NTLH"); read.setTextColor(Color.WHITE); read.setBackground(bg(Color.rgb(24,91,70),16));
        read.setOnClickListener(v->{Intent i=new Intent(this,WebViewActivity.class); i.putExtra("url","https://www.sbb.org.br/biblia/NTLH/LEV.1"); i.putExtra("title","Levítico 1 — NTLH"); startActivity(i);});
        LinearLayout.LayoutParams rb=new LinearLayout.LayoutParams(-1,dp(52)); rb.setMargins(0,dp(14),0,0); readingCard.addView(read,rb); content.addView(readingCard);

        LinearLayout progressCard=new LinearLayout(this); progressCard.setOrientation(LinearLayout.VERTICAL); progressCard.setPadding(dp(18),dp(15),dp(18),dp(15)); progressCard.setBackground(bg(Color.WHITE,18));
        LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,-2); pp.setMargins(0,0,0,dp(14)); content.addView(progressCard,pp);
        LinearLayout progressRow=new LinearLayout(this); progressRow.setGravity(Gravity.CENTER_VERTICAL); progressRow.addView(text("Seu progresso",17,true),new LinearLayout.LayoutParams(0,-2,1)); TextView percent=text(done?"100%":"0%",15,true); percent.setTextColor(Color.rgb(24,91,70)); progressRow.addView(percent); progressCard.addView(progressRow);
        ProgressBar progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal); progress.setMax(100); progress.setProgress(done?100:0); progressCard.addView(progress); progressCard.addView(text(done?"Leitura concluída. Muito bem!":"Você está começando sua jornada.",13.5f,false));

        TextView devLabel=text("DEVOCIONAL",12,true); devLabel.setTextColor(Color.rgb(80,88,84)); content.addView(devLabel);
        addCard(content,"🧠  1. Explicação do texto","Levítico 1 apresenta as orientações de Deus para a oferta queimada. O Senhor fala a Moisés da Tenda Sagrada e estabelece como o israelita deveria apresentar o animal, como o sangue seria tratado pelos sacerdotes e como a oferta seria totalmente queimada. São apresentadas três possibilidades: gado, rebanho de ovelhas ou cabras e aves. O ponto central é uma adoração ordenada por Deus, com um animal sem defeito e com participação do adorador e dos sacerdotes.");
        addCard(content,"🏺  2. Contexto — tempo e cultura","Levítico vem logo depois da construção do Tabernáculo. Israel estava aprendendo a viver como povo da aliança na presença de um Deus santo. Sacrifícios faziam parte desse sistema de culto. A exigência de um animal sem defeito mostrava que não se oferecia a Deus algo tratado como sem valor. A possibilidade de oferecer aves também mostra que o sistema não estava restrito aos que possuíam gado. A expressão “cheiro agradável” descreve a oferta aceita por Deus; não significa que Deus precisasse de alimento.");
        addCard(content,"❤️  3. Aplicação — reflexão","O texto nos lembra que aproximar-se de Deus não deve ser tratado de qualquer maneira. A adoração envolve reverência, entrega e reconhecimento da santidade de Deus. Também vemos que o pecado e a necessidade de reconciliação eram levados a sério. Para o cristão, não devemos transformar Levítico 1 numa simples fórmula dizendo que o animal era Jesus. O texto precisa primeiro ser entendido dentro da aliança de Israel. Depois, à luz do Novo Testamento, podemos reconhecer que os sacrifícios não eram a solução final e que a obra de Cristo é apresentada como definitiva.");
        addCard(content,"🚶  4. Prática — como viver isso hoje","Hoje, pratique uma adoração que não seja apenas aparência. Separe um tempo real para Deus, confesse aquilo que precisa ser confessado e não trate a fé como uma negociação para conseguir benefícios. Entregue a Deus não apenas palavras, mas decisões, hábitos, tempo e prioridades. Faça sua leitura bíblica com atenção e procure obedecer ao que realmente o texto ensina.");
        addCard(content,"🙏  5. Oração","Senhor Deus, ensina-me a te tratar com reverência e sinceridade. Que eu não transforme minha fé em aparência nem tente negociar contigo. Ajuda-me a reconhecer a seriedade do pecado, valorizar a tua presença e viver uma vida de entrega. Dá-me entendimento para ler tua Palavra com fidelidade e coragem para praticá-la. Em nome de Jesus, amém.");

        LinearLayout playerCard=new LinearLayout(this); playerCard.setOrientation(LinearLayout.VERTICAL); playerCard.setPadding(dp(18),dp(16),dp(18),dp(16)); playerCard.setBackground(bg(Color.rgb(232,238,234),18)); LinearLayout.LayoutParams pl=new LinearLayout.LayoutParams(-1,-2); pl.setMargins(0,dp(2),0,dp(12)); content.addView(playerCard,pl);
        playerCard.addView(text("🔊  Leitor do devocional",17,true)); playerStatus=text("Pronto para ouvir",13.5f,false); playerStatus.setTextColor(Color.rgb(85,94,89)); playerCard.addView(playerStatus);
        LinearLayout controls=new LinearLayout(this); controls.setGravity(Gravity.CENTER_VERTICAL); controls.setPadding(0,dp(8),0,0);
        playPause=button("▶  Ouvir"); playPause.setOnClickListener(v->toggleSpeech()); controls.addView(playPause,new LinearLayout.LayoutParams(0,dp(50),1));
        Button stop=button("■  Parar"); stop.setOnClickListener(v->stopSpeech()); controls.addView(stop,new LinearLayout.LayoutParams(dp(112),dp(50))); playerCard.addView(controls);

        Button complete=button(done?"✓  Leitura concluída":"✓  Marcar leitura como concluída"); complete.setTextColor(done?Color.rgb(24,91,70):Color.WHITE); complete.setBackground(bg(done?Color.rgb(224,235,228):Color.rgb(24,91,70),16)); complete.setOnClickListener(v->{prefs.edit().putBoolean("lev1",true).apply(); buildScreen();}); content.addView(complete,new LinearLayout.LayoutParams(-1,dp(52)));
        content.addView(text("Próxima etapa",17,true)); content.addView(text("Depois de concluir, a jornada avançará para a próxima leitura definida. A divisão dos capítulos será feita conforme o contexto do texto, e não por números aleatórios de versículos.",14,false));
        scroll.addView(content); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); setContentView(root);
    }

    private void toggleSpeech() {
        if(tts==null)return;
        if(speaking){ tts.stop(); speaking=false; paused=true; playPause.setText("▶  Continuar"); playerStatus.setText("Pausado na parte "+(speechPart+1)+" de 5"); }
        else { speaking=true; playPause.setText("Ⅱ  Pausar"); playerStatus.setText("Reproduzindo parte "+(speechPart+1)+" de 5"); speakCurrentPart(); }
    }
    private void speakCurrentPart(){ if(!speaking||tts==null||speechPart>=speechParts.length){finishSpeech();return;} tts.speak(speechParts[speechPart],TextToSpeech.QUEUE_FLUSH,null,"devocional-"+speechPart); }
    private void stopSpeech(){if(tts!=null)tts.stop(); speaking=false; paused=false; speechPart=0; if(playPause!=null)playPause.setText("▶  Ouvir"); if(playerStatus!=null)playerStatus.setText("Pronto para ouvir");}
    private void finishSpeech(){speaking=false;paused=false;speechPart=0;if(playPause!=null)playPause.setText("▶  Ouvir novamente");if(playerStatus!=null)playerStatus.setText("Devocional concluído");}
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
