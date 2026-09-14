package br.com.samuelmarvila.ajornadadabiblia;

public final class Reading {
    public final int order, chapter, startVerse, endVerse, totalVerses;
    public final String book, abbreviation, description;
    public final String[] devotional;

    public Reading(int order,String book,String abbreviation,int chapter,int startVerse,int endVerse,int totalVerses,String description,String[] devotional){
        this.order=order;this.book=book;this.abbreviation=abbreviation;this.chapter=chapter;this.startVerse=startVerse;this.endVerse=endVerse;this.totalVerses=totalVerses;this.description=description;
        String[] reviewed=DevotionalCatalog.get(book,chapter);
        if(reviewed==null) reviewed=DevotionalCatalog2.get(book,chapter);
        this.devotional=normalizeDevotional(reviewed!=null?reviewed:devotional,book,chapter);
    }

    private static String[] normalizeDevotional(String[] source,String book,int chapter){
        String[] out=new String[5];
        for(int i=0;i<5;i++){
            if(source!=null && i<source.length && source[i]!=null && !source[i].trim().isEmpty()) out[i]=source[i].trim();
            else out[i]=fallback(i,book,chapter);
        }
        return out;
    }

    private static String fallback(int part,String book,int chapter){
        if(part==0)return "Leia "+book+" "+chapter+" na NTLH com atenção. Observe o que acontece no texto, quem participa da cena e qual é a ideia principal do capítulo ou trecho.";
        if(part==1)return "Leia este capítulo dentro do contexto do livro e da época em que foi escrito. Observe personagens, costumes, gênero literário e a ligação com os capítulos próximos.";
        if(part==2)return "Pergunte o que o texto revela sobre Deus, sobre o ser humano e sobre a vida de fé. A aplicação deve nascer do sentido do texto, sem tirar a passagem do contexto.";
        if(part==3)return "Escolha uma atitude concreta para praticar hoje a partir do que você aprendeu. Transforme a reflexão em uma decisão simples e possível.";
        return "Senhor Deus, ajuda-me a compreender tua Palavra com fidelidade e a colocá-la em prática. Corrige meu coração e guia minhas decisões. Em nome de Jesus, amém.";
    }

    public String reference(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return book+" "+chapter;return book+" "+chapter+":"+startVerse+"–"+endVerse;}
    public String sbbUrl(){return "https://www.sbb.org.br/biblia/NTLH/"+abbreviation+"."+chapter;}
    public String scope(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return "Capítulo inteiro";return "Versículos "+startVerse+"–"+endVerse+"  •  trecho do capítulo";}
}
