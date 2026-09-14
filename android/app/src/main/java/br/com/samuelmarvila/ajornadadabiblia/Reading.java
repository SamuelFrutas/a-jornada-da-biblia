package br.com.samuelmarvila.ajornadadabiblia;

public final class Reading {
    public final int order, chapter, startVerse, endVerse, totalVerses;
    public final String book, abbreviation, description;
    public final String[] devotional;

    public Reading(int order,String book,String abbreviation,int chapter,int startVerse,int endVerse,int totalVerses,String description,String[] devotional){
        this(order,book,abbreviation,chapter,startVerse,endVerse,totalVerses,description,devotional,false);
    }

    /**
     * Constructor usado por unidades divididas. Quando useProvided=true, o
     * devocional já foi montado especificamente para o trecho e não deve ser
     * substituído pelo devocional do capítulo inteiro.
     */
    public Reading(int order,String book,String abbreviation,int chapter,int startVerse,int endVerse,int totalVerses,String description,String[] devotional,boolean useProvided){
        this.order=order;this.book=book;this.abbreviation=abbreviation;this.chapter=chapter;this.startVerse=startVerse;this.endVerse=endVerse;this.totalVerses=totalVerses;this.description=description;
        String[] reviewed=useProvided?devotional:DevotionalCatalog.get(book,chapter);
        if(!useProvided && reviewed==null) reviewed=DevotionalCatalog2.get(book,chapter);
        this.devotional=normalizeDevotional(reviewed!=null?reviewed:devotional,book,chapter,startVerse,endVerse,totalVerses);
    }

    private static String[] normalizeDevotional(String[] source,String book,int chapter,int start,int end,int total){
        String[] out=new String[5];
        for(int i=0;i<5;i++){
            if(source!=null && i<source.length && source[i]!=null && !source[i].trim().isEmpty()) out[i]=source[i].trim();
            else out[i]=fallback(i,book,chapter,start,end,total);
        }
        return out;
    }

    private static String[] fallbackDevotional(String book,int chapter,int start,int end,int total){
        String scope=(start<=1&&end>=total)?"o capítulo inteiro":"os versículos "+start+"–"+end;
        return new String[]{
            "Leia "+book+" "+chapter+" com atenção, concentrando-se em "+scope+". Observe quem fala, quem age, qual é a situação apresentada e como o trecho se relaciona com o que vem antes e depois.",
            "Respeite o contexto histórico, cultural e literário de "+book+". Considere o gênero do texto, os personagens, os costumes e o propósito do autor. O contexto do capítulo é indispensável para não transformar uma frase isolada em uma regra que o texto não pretende ensinar.",
            "Pergunte o que este trecho revela sobre Deus, sobre o ser humano e sobre a vida diante de Deus. A aplicação cristã deve nascer do significado do texto. Primeiro compreendemos o que a passagem comunicou aos seus primeiros leitores; depois pensamos em como esse princípio alcança nossa vida hoje.",
            "Escolha uma resposta concreta ao que você aprendeu. Pode ser uma atitude de fé, arrependimento, obediência, serviço, domínio próprio, reconciliação ou esperança. Escreva uma decisão simples e pratique-a hoje.",
            "Senhor Deus, ajuda-me a compreender tua Palavra com fidelidade. Guarda-me de ler apenas para confirmar minhas próprias ideias. Mostra-me o que preciso aprender, corrige meu coração e dá-me graça para obedecer ao que tua Palavra ensina. Em nome de Jesus, amém."
        };
    }

    private static String fallback(int part,String book,int chapter,int start,int end,int total){
        return fallbackDevotional(book,chapter,start,end,total)[part];
    }

    public String reference(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return book+" "+chapter;return book+" "+chapter+":"+startVerse+"–"+endVerse;}
    public String sbbUrl(){return "https://www.sbb.org.br/biblia/NTLH/"+abbreviation+"."+chapter;}
    public String scope(){if(startVerse<=1&&(totalVerses<=0||endVerse>=totalVerses))return "Capítulo inteiro";return "Versículos "+startVerse+"–"+endVerse+"  •  trecho do capítulo";}
}
