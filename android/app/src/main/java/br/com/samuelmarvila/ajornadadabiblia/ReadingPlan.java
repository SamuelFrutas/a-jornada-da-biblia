package br.com.samuelmarvila.ajornadadabiblia;

import java.util.ArrayList;
import java.util.List;

/** Organiza a jornada em unidades de leitura por unidades literárias. */
public final class ReadingPlan {
    private ReadingPlan() {}

    public static List<Reading> build() {
        List<Reading> source = ReadingCatalog.all();
        List<Reading> result = new ArrayList<>();
        int order = 0;
        for (Reading r : source) {
            int[] split = splitAt(r.book, r.chapter);
            int verses = chapterVerses(r.book, r.chapter);
            if (split == null || verses == 0) {
                result.add(copy(r, order++));
                continue;
            }
            int start = 1;
            for (int end : split) {
                result.add(part(r, order++, start, end, verses));
                start = end + 1;
            }
            if (start <= verses) result.add(part(r, order++, start, verses, verses));
        }
        return result;
    }

    private static Reading copy(Reading r, int order) {
        return new Reading(order,r.book,r.abbreviation,r.chapter,r.startVerse,r.endVerse,r.totalVerses,r.description,r.devotional);
    }
    private static Reading part(Reading r,int order,int start,int end,int verses){
        String description=r.description+" Parte "+start+"–"+end+". Leia somente este trecho hoje e observe como ele se conecta ao restante do capítulo.";
        return new Reading(order,r.book,r.abbreviation,r.chapter,start,end,verses,description,r.devotional);
    }

    private static int chapterVerses(String book,int chapter){
        if(book.equals("Gênesis")){int[] a={31,25,24,26,32,22,24,22,29,32,32,20,18,24,21,16,27,33,38,18,34,24,20,67,34,35,46,22,35,43,55,32,20,31,29,43,36,30,23,23,57,38,34,34,28,34,31,22,33,26};return v(a,chapter);}
        if(book.equals("Êxodo")){int[] a={22,25,22,31,23,30,25,32,35,29,10,51,22,31,27,36,16,27,25,26,36,31,33,18,40,37,21,43,46,38,18,35,23,35,35,38,29,31,43,38};return v(a,chapter);}
        if(book.equals("Levítico")){int[] a={17,16,17,35,19,30,38,36,24,20,47,8,59,57,33,34,16,30,37,27,24,33,44,23,55,46,34};return v(a,chapter);}
        if(book.equals("Números")){int[] a={54,34,51,49,31,27,89,26,23,36,35,16,33,45,41,50,13,32,22,29,35,41,30,25,18,65,23,31,40,16,54,42,56,29,34,13};return v(a,chapter);}
        if(book.equals("Deuteronômio")){int[] a={46,37,29,49,33,25,26,20,29,22,32,31,18,29,23,20,20,22,21,20,23,30,25,22,20,19,26,68,29,20,30,52,29,12};return v(a,chapter);}
        if(book.equals("Josué")){int[] a={18,24,17,24,15,27,26,35,27,43,23,24,33,15,63,10,18,28,51,9,45,34,16,33};return v(a,chapter);}
        if(book.equals("Juízes")){int[] a={36,23,31,24,31,40,25,35,57,18,40,35,25,20,20,31,13,31,30,48,25};return v(a,chapter);}
        if(book.equals("1 Samuel")){int[] a={28,36,21,22,22,27,12,22,17,27,15,25,23,52,34,23,58,30,24,42,15,23,29,22,44,25,12,25,11,31,13};return v(a,chapter);}
        if(book.equals("2 Samuel")){int[] a={27,32,39,12,25,23,29,18,13,19,27,31,39,33,37,23,29,33,43,26,22,51,39,25};return v(a,chapter);}
        if(book.equals("1 Reis")){int[] a={53,46,28,34,51,35,29,66,28,29,43,33,34,31,34,34,24,46,21,43,43,53};return v(a,chapter);}
        if(book.equals("2 Reis")){int[] a={18,25,27,44,27,33,20,29,37,36,21,21,25,29,38,20,41,37,37,21,25,20,37,20,30};return v(a,chapter);}
        if(book.equals("Ester")){int[] a={22,23,15,17,14,14,10,17,32,3};return v(a,chapter);}
        if(book.equals("Jó")){int[] a={22,13,26,21,27,16,21,22,35,22,20,25,28,22,35,22,16,21,29,29,34,30,17,25,18,21,27,23,30,31,25,22,33,37,16,33,37,41,30,24,34,17};return v(a,chapter);}
        if(book.equals("Isaías")){int[] a={31,22,22,6,30,13,25,22,21,34,16,6,22,23,9,14,14,7,25,6,17,11,31,6,25,20,27,13,27,21,17,20,24,17,10,22,38,22,8,31,29,25,28,25,13,15,7,11,15,26,11,15,12,17,13,12,21,14,26,21,22,11,12,19,12,9,24,9};return v(a,chapter);}
        if(book.equals("Jeremias")){int[] a={19,37,25,31,40,30,34,22,26,25,23,17,27,22,21,21,27,23,15,18,14,30,40,10,38,24,22,17,32,24,40,44,26,22,19,32,31,28,18,6,18,22,13,30,5,28,28,34,39,17,64,34};return v(a,chapter);}
        if(book.equals("Ezequiel")){int[] a={28,10,27,17,17,14,27,18,11,22,25,28,23,23,8,63,24,32,14,49,32,31,49,27,17,21,36,26,21,26,18,32,33,31,15,38,28,23,29,49,26,20,19,26,17,11,13,24};return v(a,chapter);}
        if(book.equals("Daniel")){int[] a={21,49,30,37,37,28,28,27,27,21,45,13};return v(a,chapter);}
        if(book.equals("Jonas")){int[] a={17,10,10,11};return v(a,chapter);}
        if(book.equals("Mateus")){int[] a={25,23,17,25,48,34,29,34,38,42,30,50,58,36,39,28,27,35,30,34,46,46,39,51,46,75,66,20};return v(a,chapter);}
        if(book.equals("Marcos")){int[] a={45,28,35,41,43,56,37,38,50,52,34,44,37,72,47,20};return v(a,chapter);}
        if(book.equals("Lucas")){int[] a={80,52,38,44,39,49,50,56,62,42,54,59,35,35,32,31,37,43,48,47,38,71,56,53};return v(a,chapter);}
        if(book.equals("João")){int[] a={51,25,36,54,47,71,53,59,41,42,57,50,38,31,27,33,26,40,42,31,25};return v(a,chapter);}
        if(book.equals("Atos")){int[] a={26,47,26,37,42,15,60,40,43,48,30,25,52,28,41,40,34,28,41,38,40,30,35,27,27,32,44,31};return v(a,chapter);}
        if(book.equals("Romanos")){int[] a={32,29,31,25,21,23,25,39,33,21,36,21,14,23,33,27};return v(a,chapter);}
        if(book.equals("1 Coríntios")){int[] a={31,16,16,21,13,20,40,13,27,33,34,31,13,40,58,24};return v(a,chapter);}
        if(book.equals("Efésios")){int[] a={23,22,21,32,33,24};return v(a,chapter);}
        if(book.equals("Filipenses")){int[] a={30,30,21,23};return v(a,chapter);}
        if(book.equals("Hebreus")){int[] a={14,18,19,16,14,20,28,13,28,39,40,29,25};return v(a,chapter);}
        if(book.equals("Tiago")){int[] a={27,26,18,17,20};return v(a,chapter);}
        if(book.equals("Apocalipse")){int[] a={20,29,22,11,14,17,17,14,21,11,19,17,18,20,8,21,18,24,21,15,27,21};return v(a,chapter);}
        return 0;
    }
    private static int v(int[] a,int chapter){return chapter>=1&&chapter<=a.length?a[chapter-1]:0;}

    private static int[] splitAt(String book,int chapter){
        if(book.equals("Gênesis")){if(chapter==24)return new int[]{27,49};if(chapter==37)return new int[]{11,22};if(chapter==39)return new int[]{6};if(chapter==41)return new int[]{13,36};if(chapter==45)return new int[]{15};}
        if(book.equals("Êxodo")){if(chapter==3)return new int[]{6,15};if(chapter==12)return new int[]{28,42};if(chapter==14)return new int[]{14,25};if(chapter==32)return new int[]{14,29};}
        if(book.equals("Levítico")){if(chapter==4)return new int[]{21};if(chapter==6)return new int[]{13};if(chapter==7)return new int[]{21};if(chapter==8)return new int[]{13,29};if(chapter==11)return new int[]{23,47};if(chapter==13)return new int[]{17,46};if(chapter==14)return new int[]{20,32,42};if(chapter==16)return new int[]{19};if(chapter==19)return new int[]{18};if(chapter==23)return new int[]{22,44};if(chapter==25)return new int[]{24,38,55};if(chapter==26)return new int[]{13,26};if(chapter==27)return new int[]{13,27};}
        if(book.equals("Números")){if(chapter==1)return new int[]{46};if(chapter==7)return new int[]{11,41};if(chapter==13)return new int[]{20};if(chapter==14)return new int[]{10,25};if(chapter==20)return new int[]{13};if(chapter==21)return new int[]{9,20};}
        if(book.equals("Deuteronômio")){if(chapter==5)return new int[]{21};if(chapter==6)return new int[]{9};if(chapter==28)return new int[]{14,46};if(chapter==30)return new int[]{10};}
        if(book.equals("Josué")){if(chapter==6)return new int[]{14,21};if(chapter==7)return new int[]{15};if(chapter==24)return new int[]{15};}
        if(book.equals("Juízes")){if(chapter==6)return new int[]{10,24};if(chapter==7)return new int[]{8,25};if(chapter==16)return new int[]{14,22};}
        if(book.equals("1 Samuel")){if(chapter==17)return new int[]{30};if(chapter==24)return new int[]{7,15};}
        if(book.equals("2 Samuel")){if(chapter==11)return new int[]{13};if(chapter==12)return new int[]{12,25};}
        if(book.equals("1 Reis")&&chapter==18)return new int[]{15,29};
        if(book.equals("2 Reis")){if(chapter==5)return new int[]{14};if(chapter==18)return new int[]{12,25};if(chapter==19)return new int[]{20};}
        if(book.equals("Ester")){if(chapter==4)return new int[]{11};if(chapter==7)return new int[]{6};}
        if(book.equals("Jó")){if(chapter==38)return new int[]{18};if(chapter==39)return new int[]{12};if(chapter==40)return new int[]{14};}
        if(book.equals("Isaías")){if(chapter==6)return new int[]{7};if(chapter==36)return new int[]{10,22};if(chapter==37)return new int[]{20};if(chapter==53)return new int[]{6};}
        if(book.equals("Jeremias")){if(chapter==7)return new int[]{15};if(chapter==29)return new int[]{14};if(chapter==31)return new int[]{20};}
        if(book.equals("Ezequiel")){if(chapter==36)return new int[]{15,28};if(chapter==37)return new int[]{14,28};}
        if(book.equals("Daniel")){if(chapter==3)return new int[]{18};if(chapter==6)return new int[]{18};}
        if(book.equals("Jonas")){if(chapter==1)return new int[]{10};if(chapter==3)return new int[]{5};if(chapter==4)return new int[]{4};}
        if(book.equals("Mateus")){if(chapter==5)return new int[]{20,37};if(chapter==6)return new int[]{18};if(chapter==7)return new int[]{14};if(chapter==13)return new int[]{23,43};if(chapter==24)return new int[]{31};}
        if(book.equals("Marcos")){if(chapter==4)return new int[]{20,34};if(chapter==13)return new int[]{23};}
        if(book.equals("Lucas")){if(chapter==15)return new int[]{10,24};if(chapter==24)return new int[]{12,35};}
        if(book.equals("João")){if(chapter==3)return new int[]{10};if(chapter==15)return new int[]{11};if(chapter==17)return new int[]{8,19};}
        if(book.equals("Atos")){if(chapter==2)return new int[]{21};if(chapter==9)return new int[]{19};if(chapter==10)return new int[]{23};if(chapter==15)return new int[]{21};if(chapter==27)return new int[]{26};}
        if(book.equals("Romanos")){if(chapter==5)return new int[]{11};if(chapter==8)return new int[]{17,30};if(chapter==12)return new int[]{8};}
        if(book.equals("1 Coríntios")){if(chapter==13)return new int[]{7};if(chapter==15)return new int[]{28};}
        if(book.equals("Efésios")&&chapter==2)return new int[]{10};
        if(book.equals("Filipenses")&&chapter==2)return new int[]{11};
        if(book.equals("Hebreus")){if(chapter==4)return new int[]{10};if(chapter==10)return new int[]{18};if(chapter==11)return new int[]{16,31};}
        if(book.equals("Tiago")&&chapter==2)return new int[]{13};
        if(book.equals("Apocalipse")){if(chapter==1)return new int[]{8};if(chapter==5)return new int[]{7};if(chapter==21)return new int[]{8};if(chapter==22)return new int[]{13};}
        return null;
    }
}
