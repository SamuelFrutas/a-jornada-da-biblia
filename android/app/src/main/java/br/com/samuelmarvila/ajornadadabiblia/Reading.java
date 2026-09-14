package br.com.samuelmarvila.ajornadadabiblia;

public final class Reading {
    public final int order, chapter, startVerse, endVerse, totalVerses;
    public final String book, abbreviation, description;
    public final String[] devotional;

    public Reading(int order,String book,String abbreviation,int chapter,int startVerse,int endVerse,int totalVerses,String description,String[] devotional){
        this.order=order;this.book=book;this.abbreviation=abbreviation;this.chapter=chapter;this.startVerse=startVerse;this.endVerse=endVerse;this.totalVerses=totalVerses;this.description=description;
        String[] reviewed=DevotionalCatalog.get(book,chapter);
        if(reviewed==null) reviewed=DevotionalCatalog2.get(book,chapter);
        String[] base=reviewed!=null?reviewed:devotional;
        if(isPartial(startVerse,endVerse,totalVerses)) base=DevotionalComposer.forRange(book,chapter,startVerse,endVerse,totalVerses,base);
        this.devotional=normalizeDevotional(base,book,chapter,startVerse,endVerse,totalVerses);
    }

    /** Constructor usado quando o chamador já preparou um conteúdo específico. */
    public Reading(int order,String book,String abbreviation,int chapter,int startVerse,int endVerse,int totalVerses,String description,String[] devotional,boolean useProvided){
        this.order=order;this.book=book;this.abbreviation=abbreviation;this.chapter=chapter;this.startVerse=startVerse;this.endVerse=endVerse;this.totalVerses=totalVerses;this.description=description;
        String[] base=devotional;
        if(!useProvided){
            base=DevotionalCatalog.get(book,chapter);
            if(base==null) base=DevotionalCatalog2.get(book,chapter);
            if(base==null) base=devotional;
        }
        if(isPartial(startVerse,endVerse,totalVerses)) base=DevotionalComposer.forRange(book,chapter,startVerse,endVerse,totalVerses,base);
        this.devotional=normalizeDevotional(base,book,chapter,startVerse,endVerse,totalVerses);
    }

    private static boolean isPartial(int start,int end,int total){return total>0&&(start>1||end<total);}

    private static String[] normalizeDevotional(String[] source,String book,int chapter,int start,int end,int total){
        String[] out=new String[5];
        for(int i=0;i<5;i++){
            if(source!=null&&i<source.length&&source[i]!=null&&!source[i].trim().isEmpty()) out[i]=source[i].trim();
            else out[i]=fallback(i,book,chapter,start,end,total);
        }
        return out;
    }

    private static String fallback(int part,String book,int chapter,int start,int end,int total){
        String scope=(start<=1&&end>=total)?"o capítulo inteiro":"os versículos "+start+"–"+end;
        if(part==0)return "Leia "+book+" "+chapter+" na NTLH, concentrando-se em "+scope+". Observe quem fala, quem age e qual é a ideia principal.";
        if(part==1)return "Leia este texto dentro do contexto histórico e literário do livro. Observe personagens, costumes, gênero e a ligação com os capítulos próximos.";
        if(part==2)return "Pergunte o que o texto revela sobre Deus, sobre o ser humano e sobre a vida de fé. A aplicação deve nascer do sentido da passagem.";
        if(part==3)return "Escolha uma atitude concreta baseada no que você aprendeu e pratique-a hoje.";
        return "Senhor Deus, ajuda-me a compreender tua Palavra com fidelidade e a colocá-la em prática. Corrige meu coração e guia minhas decisões. Em nome de Jesus, amém.";
    }

    public String reference(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return book+" "+chapter;return book+" "+chapter+":"+startVerse+"–"+endVerse;}
    public String sbbUrl(){return "https://www.sbb.org.br/biblia/NTLH/"+abbreviation+"."+chapter;}
    public String scope(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return "Capítulo inteiro";return "Versículos "+startVerse+"–"+endVerse+"  •  trecho do capítulo";}
}
