package br.com.samuelmarvila.ajornadadabiblia;

public final class Reading {
    public final int order;
    public final String book;
    public final String abbreviation;
    public final int chapter;
    public final int startVerse;
    public final int endVerse;
    public final int totalVerses;
    public final String description;
    public final String[] devotional;

    public Reading(int order, String book, String abbreviation, int chapter, int startVerse,
                   int endVerse, int totalVerses, String description, String[] devotional) {
        this.order = order;
        this.book = book;
        this.abbreviation = abbreviation;
        this.chapter = chapter;
        this.startVerse = startVerse;
        this.endVerse = endVerse;
        this.totalVerses = totalVerses;
        this.description = description;
        this.devotional = devotional;
    }

    public String reference() {
        if (startVerse <= 1 && endVerse == totalVerses) return book + " " + chapter;
        return book + " " + chapter + ":" + startVerse + "–" + endVerse;
    }

    public String sbbUrl() {
        return "https://www.sbb.org.br/biblia/NTLH/" + abbreviation + "." + chapter;
    }

    public String scope() {
        if (startVerse <= 1 && endVerse == totalVerses) return "Capítulo inteiro  •  " + totalVerses + " versículos";
        return "Versículos " + startVerse + "–" + endVerse + "  •  trecho do capítulo";
    }
}
