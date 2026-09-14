package br.com.samuelmarvila.ajornadadabiblia;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

/** Ajustes da interface moderna: calendário real, fluxo claro e botões sempre visíveis. */
public class ModernActivityV2 extends ModernActivity {

    @Override void reading(){
        super.reading();
        if(current==null) return;
        Button finish = button(pref.getBoolean("reading_"+index,false) ? "Leitura concluída" : "Marcar leitura como concluída", true, R.drawable.ic_check);
        finish.setEnabled(!pref.getBoolean("reading_"+index,false));
        finish.setAlpha(finish.isEnabled()?1f:.65f);
        finish.setOnClickListener(v -> {
            pref.edit().putBoolean("reading_"+index,true).apply();
            Toast.makeText(this,"Leitura concluída. O devocional está liberado.",Toast.LENGTH_SHORT).show();
            open("devhub",true);
        });
        body.addView(finish,m(12,0));
    }

    @Override void devHub(){
        base("devhub");
        hero("SEU MOMENTO COM DEUS","Devocional","Primeiro a leitura. Depois, a reflexão.",R.drawable.ic_pray);
        boolean r=pref.getBoolean("reading_"+index,false);
        LinearLayout a=step("01","Leitura do dia",r?"Leitura concluída. Você pode revisar a passagem.":"Comece lendo a passagem bíblica.",r?"Revisar leitura":"Ler a leitura",true,R.drawable.ic_book,()->open("read",true));
        body.addView(a,m(0,0));
        LinearLayout b=step("02","Devocional",r?"Agora pare, reflita, pratique e ore.":"Bloqueado até a leitura ser concluída.","Abrir devocional",r,R.drawable.ic_pray,()->open("dev",true));
        body.addView(b,m(0,10));
        LinearLayout flow=card(CREAM);
        flow.addView(text("A ORDEM DA JORNADA",10,GREEN,true));
        flow.addView(text("1  Leia a Bíblia\n2  Marque a leitura como concluída\n3  Faça o devocional\n4  Conclua o devocional para liberar a próxima etapa",14,TEXT,false),m(7,0));
        body.addView(flow,m(0,12));
    }

    @Override void journey(){
        base("journey");
        body.addView(text("MINHA JORNADA",10,GREEN,true));
        body.addView(title("Seu caminho pela Bíblia",29,NAVY),m(6,4));
        body.addView(text("Acompanhe seu progresso e veja seus dias concluídos.",14,MUTED,false),m(0,14));

        LinearLayout p=card(NAVY);
        int pct=plan.size()==0?0:index*100/plan.size();
        p.addView(text("SEU PROGRESSO",10,GOLD,true));
        p.addView(title(pct+"%",34,WHITE),m(7,2));
        p.addView(text(index+" etapas concluídas de "+plan.size(),13,Color.rgb(215,225,234),false));
        ProgressBar bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        bar.setMax(Math.max(1,plan.size())); bar.setProgress(index); p.addView(bar,m(10,0));
        body.addView(p,m(0,12));

        body.addView(calendarCard(),m(0,12));

        LinearLayout s=card(WHITE);
        s.addView(text("ETAPA ATUAL",10,GREEN,true));
        s.addView(text(current==null?"Jornada concluída":current.reference(),21,NAVY,true),m(6,0));
        body.addView(s,m(0,10));
    }

    LinearLayout calendarCard(){
        Calendar now=Calendar.getInstance();
        int year=now.get(Calendar.YEAR), month=now.get(Calendar.MONTH), today=now.get(Calendar.DAY_OF_MONTH);
        String monthName=new SimpleDateFormat("MMMM 'de' yyyy",new Locale("pt","BR")).format(now.getTime());
        monthName=Character.toUpperCase(monthName.charAt(0))+monthName.substring(1);

        LinearLayout c=card(WHITE);
        LinearLayout head=new LinearLayout(this); head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(icon(R.drawable.ic_journey,GOLD,30));
        head.addView(text("CALENDÁRIO DA JORNADA",12,NAVY,true),marg(8));
        c.addView(head);
        TextView mt=title(monthName,19,NAVY); mt.setGravity(Gravity.CENTER); c.addView(mt,m(10,8));

        GridLayout grid=new GridLayout(this); grid.setColumnCount(7); grid.setUseDefaultMargins(false);
        String[] names={"D","S","T","Q","Q","S","S"};
        for(String name:names){TextView h=text(name,11,MUTED,true);h.setGravity(Gravity.CENTER);GridLayout.LayoutParams gp=new GridLayout.LayoutParams();gp.width=0;gp.height=dp(30);gp.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);grid.addView(h,gp);}
        Calendar first=new GregorianCalendar(year,month,1);
        int offset=first.get(Calendar.DAY_OF_WEEK)-1;
        int max=first.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=0;i<offset;i++) addDay(grid,"",false,false,false);
        for(int d=1;d<=max;d++){
            Calendar date=new GregorianCalendar(year,month,d);
            String key=new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(date.getTime());
            boolean done=pref.getBoolean("day_"+key,false);
            boolean isToday=d==today;
            addDay(grid,String.valueOf(d),done,isToday,!date.after(now));
        }
        c.addView(grid);
        LinearLayout legend=new LinearLayout(this); legend.setGravity(Gravity.CENTER_VERTICAL); legend.setPadding(0,dp(10),0,0);
        legend.addView(text("Concluído",11,GREEN,true));
        legend.addView(text("   Hoje",11,NAVY,true));
        legend.addView(text("   Pendente",11,MUTED,false));
        c.addView(legend);
        return c;
    }

    void addDay(GridLayout grid,String value,boolean done,boolean today,boolean past){
        TextView d=text(value,13,done?GREEN:(today?NAVY:MUTED),done||today);
        d.setGravity(Gravity.CENTER);
        if(today){d.setBackground(stroke(CREAM,GOLD,14));}
        if(done){d.setText("✓"+value);}
        GridLayout.LayoutParams gp=new GridLayout.LayoutParams();gp.width=0;gp.height=dp(38);gp.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);gp.setMargins(dp(2),dp(2),dp(2),dp(2));
        grid.addView(d,gp);
    }
}
