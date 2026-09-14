package br.com.samuelmarvila.ajornadadabiblia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
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

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        buildScreen();
        tts = new TextToSpeech(this, status -> { if (status == TextToSpeech.SUCCESS) tts.setLanguage(new Locale("pt", "BR")); });
    }

    private TextView text(String value, float size, boolean bold) {
        TextView v = new TextView(this); v.setText(value); v.setTextSize(size); v.setTextColor(Color.rgb(35,35,35));
        v.setPadding(0, 10, 0, 10); if (bold) v.setTypeface(null, 1); return v;
    }

    private Button button(String label) {
        Button b = new Button(this); b.setText(label); b.setAllCaps(false); return b;
    }

    private void buildScreen() {
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28, 24, 28, 28); root.setBackgroundColor(Color.rgb(247,244,238));
        TextView title = text("A Jornada da Bíblia", 28, true); title.setTextColor(Color.rgb(23,63,53)); root.addView(title);
        root.addView(text("Leitura sequencial • sem pular partes", 15, false));

        ScrollView scroll = new ScrollView(this); LinearLayout content = new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL);
        content.addView(text("LEITURA ATUAL", 13, true));
        content.addView(text("Levítico 1", 25, true));
        content.addView(text("Levítico 1:1–17", 17, false));
        content.addView(text("Capítulo inteiro: o texto forma uma unidade sobre a oferta queimada.", 15, false));

        Button read = button("📖 Ler NTLH dentro do aplicativo");
        read.setOnClickListener(v -> { Intent i = new Intent(this, WebViewActivity.class); i.putExtra("url", "https://www.sbb.org.br/biblia/NTLH/LEV.1"); i.putExtra("title", "Levítico 1 — NTLH"); startActivity(i); });
        content.addView(read);

        ProgressBar progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal); progress.setMax(100); progress.setProgress(prefs.getBoolean("lev1", false) ? 100 : 0); content.addView(progress);
        content.addView(text(prefs.getBoolean("lev1", false) ? "Leitura concluída." : "Você está começando em Levítico 1.", 14, false));

        content.addView(text("DEVOCIONAL", 13, true));
        addSection(content, "1. Explicação do texto", "Levítico 1 apresenta as orientações de Deus para a oferta queimada. O Senhor fala a Moisés da Tenda Sagrada e estabelece como o israelita deveria apresentar o animal, como o sangue seria tratado pelos sacerdotes e como a oferta seria totalmente queimada. São apresentadas três possibilidades: gado, rebanho de ovelhas ou cabras e aves. O ponto central é uma adoração ordenada por Deus, com um animal sem defeito e com participação do adorador e dos sacerdotes.");
        addSection(content, "2. Contexto — tempo e cultura", "Levítico vem logo depois da construção do Tabernáculo. Israel estava aprendendo a viver como povo da aliança na presença de um Deus santo. Sacrifícios faziam parte desse sistema de culto. A exigência de um animal sem defeito mostrava que não se oferecia a Deus algo tratado como sem valor. A possibilidade de oferecer aves também mostra que o sistema não estava restrito aos que possuíam gado. A expressão “cheiro agradável” descreve a oferta aceita por Deus; não significa que Deus precisasse de alimento.");
        addSection(content, "3. Aplicação — reflexão", "O texto nos lembra que aproximar-se de Deus não deve ser tratado de qualquer maneira. A adoração envolve reverência, entrega e reconhecimento da santidade de Deus. Também vemos que o pecado e a necessidade de reconciliação eram levados a sério. Para o cristão, não devemos transformar Levítico 1 numa simples fórmula dizendo que o animal era Jesus. O texto precisa primeiro ser entendido dentro da aliança de Israel. Depois, à luz do Novo Testamento, podemos reconhecer que os sacrifícios não eram a solução final e que a obra de Cristo é apresentada como definitiva.");
        addSection(content, "4. Prática — como viver isso hoje", "Hoje, pratique uma adoração que não seja apenas aparência. Separe um tempo real para Deus, confesse aquilo que precisa ser confessado e não trate a fé como uma negociação para conseguir benefícios. Entregue a Deus não apenas palavras, mas decisões, hábitos, tempo e prioridades. Faça sua leitura bíblica com atenção e procure obedecer ao que realmente o texto ensina.");
        addSection(content, "5. Oração", "Senhor Deus, ensina-me a te tratar com reverência e sinceridade. Que eu não transforme minha fé em aparência nem tente negociar contigo. Ajuda-me a reconhecer a seriedade do pecado, valorizar a tua presença e viver uma vida de entrega. Dá-me entendimento para ler tua Palavra com fidelidade e coragem para praticá-la. Em nome de Jesus, amém.");

        Button listen = button("🔊 Ouvir devocional"); listen.setOnClickListener(v -> speak()); content.addView(listen);
        Button complete = button(prefs.getBoolean("lev1", false) ? "✓ Leitura concluída" : "✓ Marcar leitura como concluída");
        complete.setOnClickListener(v -> { prefs.edit().putBoolean("lev1", true).apply(); buildScreen(); }); content.addView(complete);
        scroll.addView(content); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1)); setContentView(root);
    }

    private void addSection(LinearLayout box, String heading, String body) { box.addView(text(heading, 19, true)); box.addView(text(body, 16, false)); }
    private void speak() { if (tts != null) { String s = "Explicação do texto. Levítico capítulo um apresenta as orientações de Deus para a oferta queimada. Contexto. Israel estava aprendendo a viver como povo da aliança na presença de um Deus santo. Aplicação. A adoração envolve reverência, entrega e reconhecimento da santidade de Deus. Prática. Separe um tempo real para Deus, confesse o que precisa ser confessado e viva uma fé de entrega. Oração. Senhor Deus, ensina-me a te tratar com reverência e sinceridade. Em nome de Jesus, amém."; tts.speak(s, TextToSpeech.QUEUE_FLUSH, null, "devocional"); } }
    @Override protected void onDestroy() { if (tts != null) { tts.stop(); tts.shutdown(); } super.onDestroy(); }
}
