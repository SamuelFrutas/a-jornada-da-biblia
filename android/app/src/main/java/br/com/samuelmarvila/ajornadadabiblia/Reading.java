package br.com.samuelmarvila.ajornadadabiblia;

public final class Reading {
    public final int order, chapter, startVerse, endVerse, totalVerses;
    public final String book, abbreviation, description;
    public final String[] devotional;

    public Reading(int order,String book,String abbreviation,int chapter,int startVerse,int endVerse,int totalVerses,String description,String[] devotional){
        this.order=order;this.book=book;this.abbreviation=abbreviation;this.chapter=chapter;this.startVerse=startVerse;this.endVerse=endVerse;this.totalVerses=totalVerses;this.description=description;
        String[] reviewed=DevotionalCatalog.get(book,chapter);
        if(reviewed==null) reviewed=DevotionalCatalog2.get(book,chapter);
        this.devotional=reviewed!=null?reviewed:devotional;
    }
    public String reference(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return book+" "+chapter;return book+" "+chapter+":"+startVerse+"–"+endVerse;}
    public String sbbUrl(){return "https://www.sbb.org.br/biblia/NTLH/"+abbreviation+"."+chapter;}
    public String scope(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return "Capítulo inteiro";return "Versículos "+startVerse+"–"+endVerse+"  •  trecho do capítulo";}
}
