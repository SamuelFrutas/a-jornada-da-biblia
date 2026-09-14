package br.com.samuelmarvila.ajornadadabiblia;

import java.util.ArrayList;
import java.util.List;

/**
 * Organiza a jornada em unidades de leitura. Capítulos muito extensos podem
 * ser divididos somente em pontos de transição conhecidos do próprio texto.
 * A maior parte da Bíblia continua sendo lida capítulo a capítulo.
 */
public final class ReadingPlan {
    private ReadingPlan() {}

    public static List<Reading> build() {
        List<Reading> source = ReadingCatalog.all();
        List<Reading> result = new ArrayList<>();
        int order = 0;
        for (Reading r : source) {
            int[] split = splitAt(r.book, r.chapter);
            if (split == null || r.totalVerses <= 0) {
                result.add(copy(r, order++));
                continue;
            }
            int start = 1;
            for (int end : split) {
                result.add(part(r, order++, start, end));
                start = end + 1;
            }
            if (start <= r.totalVerses) {
                result.add(part(r, order++, start, r.totalVerses));
            }
        }
        return result;
    }

    private static Reading copy(Reading r, int order) {
        return new Reading(order, r.book, r.abbreviation, r.chapter, r.startVerse,
                r.endVerse, r.totalVerses, r.description, r.devotional);
    }

    private static Reading part(Reading r, int order, int start, int end) {
        String description = r.description + " Parte " + start + "–" + end + ". Leia somente este trecho hoje e observe como ele se conecta ao restante do capítulo.";
        return new Reading(order, r.book, r.abbreviation, r.chapter, start, end,
                r.totalVerses, description, r.devotional);
    }

    /**
     * Pontos de divisão escolhidos por mudança de cena, discurso ou unidade
     * literária. Não é uma divisão automática por quantidade de versículos.
     */
    private static int[] splitAt(String book, int chapter) {
        if (book.equals("Gênesis")) {
            if (chapter == 24) return new int[]{27, 49, 67};
            if (chapter == 37) return new int[]{11, 22, 36};
            if (chapter == 39) return new int[]{6, 23};
            if (chapter == 41) return new int[]{13, 36};
            if (chapter == 45) return new int[]{15, 28};
        }
        if (book.equals("Êxodo")) {
            if (chapter == 3) return new int[]{6, 15};
            if (chapter == 12) return new int[]{28, 42};
            if (chapter == 14) return new int[]{14, 25};
            if (chapter == 32) return new int[]{14, 29};
        }
        if (book.equals("Números")) {
            if (chapter == 13) return new int[]{20, 33};
            if (chapter == 14) return new int[]{10, 25};
            if (chapter == 20) return new int[]{13, 29};
            if (chapter == 21) return new int[]{9, 20};
        }
        if (book.equals("Deuteronômio")) {
            if (chapter == 5) return new int[]{21, 33};
            if (chapter == 6) return new int[]{9, 19};
            if (chapter == 28) return new int[]{14, 46, 68};
            if (chapter == 30) return new int[]{10, 20};
        }
        if (book.equals("Josué")) {
            if (chapter == 6) return new int[]{14, 21};
            if (chapter == 7) return new int[]{15, 26};
            if (chapter == 24) return new int[]{15, 28};
        }
        if (book.equals("Juízes")) {
            if (chapter == 6) return new int[]{10, 24};
            if (chapter == 7) return new int[]{8, 25};
            if (chapter == 16) return new int[]{14, 22};
        }
        if (book.equals("1 Samuel")) {
            if (chapter == 17) return new int[]{30, 49};
            if (chapter == 24) return new int[]{7, 15};
        }
        if (book.equals("2 Samuel")) {
            if (chapter == 11) return new int[]{13, 27};
            if (chapter == 12) return new int[]{12, 25};
        }
        if (book.equals("1 Reis")) {
            if (chapter == 18) return new int[]{15, 29};
        }
        if (book.equals("2 Reis")) {
            if (chapter == 5) return new int[]{14, 19};
            if (chapter == 18) return new int[]{12, 25};
            if (chapter == 19) return new int[]{20, 37};
        }
        if (book.equals("Ester")) {
            if (chapter == 4) return new int[]{11, 17};
            if (chapter == 7) return new int[]{6, 10};
        }
        if (book.equals("Jó")) {
            if (chapter == 38) return new int[]{18, 41};
            if (chapter == 39) return new int[]{12, 30};
            if (chapter == 40) return new int[]{14, 24};
        }
        if (book.equals("Isaías")) {
            if (chapter == 6) return new int[]{7, 13};
            if (chapter == 36) return new int[]{10, 22};
            if (chapter == 37) return new int[]{20, 38};
            if (chapter == 53) return new int[]{6, 12};
        }
        if (book.equals("Jeremias")) {
            if (chapter == 7) return new int[]{15, 34};
            if (chapter == 29) return new int[]{14, 23};
            if (chapter == 31) return new int[]{20, 40};
        }
        if (book.equals("Ezequiel")) {
            if (chapter == 36) return new int[]{15, 28};
            if (chapter == 37) return new int[]{14, 28};
        }
        if (book.equals("Daniel")) {
            if (chapter == 3) return new int[]{18, 30};
            if (chapter == 6) return new int[]{18, 28};
        }
        if (book.equals("Jonas")) {
            if (chapter == 1) return new int[]{10, 17};
            if (chapter == 3) return new int[]{5, 10};
            if (chapter == 4) return new int[]{4, 11};
        }
        if (book.equals("Mateus")) {
            if (chapter == 5) return new int[]{20, 37};
            if (chapter == 6) return new int[]{18, 34};
            if (chapter == 7) return new int[]{14, 29};
            if (chapter == 13) return new int[]{23, 43};
            if (chapter == 24) return new int[]{31, 51};
        }
        if (book.equals("Marcos")) {
            if (chapter == 4) return new int[]{20, 34};
            if (chapter == 13) return new int[]{23, 37};
        }
        if (book.equals("Lucas")) {
            if (chapter == 15) return new int[]{10, 24};
            if (chapter == 24) return new int[]{12, 35};
        }
        if (book.equals("João")) {
            if (chapter == 3) return new int[]{10, 21};
            if (chapter == 15) return new int[]{11, 27};
            if (chapter == 17) return new int[]{8, 19};
        }
        if (book.equals("Atos")) {
            if (chapter == 2) return new int[]{21, 36};
            if (chapter == 9) return new int[]{19, 31};
            if (chapter == 10) return new int[]{23, 48};
            if (chapter == 15) return new int[]{21, 35};
            if (chapter == 27) return new int[]{26, 44};
        }
        if (book.equals("Romanos")) {
            if (chapter == 5) return new int[]{11, 21};
            if (chapter == 8) return new int[]{17, 30};
            if (chapter == 12) return new int[]{8, 21};
        }
        if (book.equals("1 Coríntios")) {
            if (chapter == 13) return new int[]{7, 13};
            if (chapter == 15) return new int[]{28, 49};
        }
        if (book.equals("Efésios") && chapter == 2) return new int[]{10, 22};
        if (book.equals("Filipenses") && chapter == 2) return new int[]{11, 18};
        if (book.equals("Hebreus")) {
            if (chapter == 4) return new int[]{10, 16};
            if (chapter == 10) return new int[]{18, 31};
            if (chapter == 11) return new int[]{16, 31, 40};
        }
        if (book.equals("Tiago") && chapter == 2) return new int[]{13, 26};
        if (book.equals("Apocalipse")) {
            if (chapter == 1) return new int[]{8, 20};
            if (chapter == 5) return new int[]{7, 14};
            if (chapter == 21) return new int[]{8, 27};
            if (chapter == 22) return new int[]{13, 21};
        }
        return null;
    }
}
