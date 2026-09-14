package br.com.samuelmarvila.ajornadadabiblia;

import java.util.ArrayList;
import java.util.List;

public final class ReadingCatalog {
    private ReadingCatalog() {}

    public static List<Reading> all() {
        List<Reading> r = new ArrayList<>();
        r.add(new Reading(0, "Levítico", "LEV", 1, 1, 17, 17,
            "A oferta queimada e a adoração diante de Deus.",
            new String[]{
                "Levítico 1 apresenta as orientações de Deus para a oferta queimada. O Senhor fala a Moisés da Tenda Sagrada e estabelece como o israelita deveria apresentar o animal, como o sangue seria tratado pelos sacerdotes e como a oferta seria totalmente queimada. São apresentadas três possibilidades: gado, rebanho de ovelhas ou cabras e aves.",
                "Levítico vem logo depois da construção do Tabernáculo. Israel estava aprendendo a viver como povo da aliança na presença de um Deus santo. Sacrifícios faziam parte desse sistema de culto. A exigência de um animal sem defeito mostrava que a oferta não deveria ser algo tratado como sem valor. A expressão ‘cheiro agradável’ descreve a oferta aceita por Deus; não significa que Deus precisasse de alimento.",
                "O texto nos lembra que aproximar-se de Deus não deve ser tratado de qualquer maneira. A adoração envolve reverência, entrega e reconhecimento da santidade de Deus. Para o cristão, Levítico 1 deve primeiro ser entendido dentro da aliança de Israel. Depois, à luz do Novo Testamento, reconhecemos que os sacrifícios não eram a solução final e que a obra de Cristo é apresentada como definitiva.",
                "Pratique uma adoração que não seja apenas aparência. Separe um tempo real para Deus, confesse aquilo que precisa ser confessado e não trate a fé como uma negociação para conseguir benefícios. Entregue a Deus suas decisões, hábitos, tempo e prioridades.",
                "Senhor Deus, ensina-me a te tratar com reverência e sinceridade. Que eu não transforme minha fé em aparência nem tente negociar contigo. Dá-me entendimento para ler tua Palavra com fidelidade e coragem para praticá-la. Em nome de Jesus, amém."
            }));
        r.add(new Reading(1, "Levítico", "LEV", 2, 1, 16, 16,
            "A oferta de cereais e a dedicação dos primeiros frutos.",
            devotional("Levítico 2 apresenta a oferta de cereais, feita com farinha, azeite e, em algumas formas, incenso. Parte era queimada sobre o altar e o restante ficava para os sacerdotes. O capítulo também orienta sobre ofertas preparadas de diferentes maneiras e sobre os primeiros frutos.", "Farinha, azeite, sal e cereais faziam parte da vida cotidiana de Israel. O adorador podia apresentar parte daquilo que produzia. O que sobrava da oferta era destinado aos sacerdotes, integrando também o sustento do serviço do santuário.", "A adoração não se limita a momentos extraordinários. Aquilo que recebemos por meio do trabalho e da provisão também pode ser reconhecido diante de Deus. Não devemos copiar mecanicamente o sistema levítico, mas o princípio permanece: reconhecer Deus como fonte e Senhor de tudo.", "Agradeça hoje pelo que Deus colocou em suas mãos. Use seus recursos com responsabilidade, seja generoso e não trate sua contribuição como compra de bênçãos. Trabalhe e sirva com gratidão.", "Senhor, obrigado por tua provisão. Ensina-me a reconhecer tua bondade nas coisas simples e no fruto do meu trabalho. Forma em mim um coração agradecido, generoso e obediente. Em nome de Jesus, amém."));
        r.add(new Reading(2, "Levítico", "LEV", 3, 1, 17, 17,
            "A oferta de comunhão e a vida diante de Deus.",
            devotional("Levítico 3 descreve a oferta de comunhão, também chamada oferta pacífica. O animal deveria ser sem defeito, e sangue e gordura recebiam tratamento específico no altar. O capítulo apresenta as orientações para gado, ovelhas e cabras e termina com a proibição de comer sangue e gordura.", "A oferta de comunhão fazia parte do culto de Israel e estava ligada à paz e à comunhão diante de Deus. A gordura era especialmente dedicada ao Senhor, enquanto o sangue representava a vida. O culto levítico usava sinais concretos para ensinar que a vida pertence a Deus.", "Levítico 3 mostra que a relação com Deus envolve comunhão, gratidão e celebração. Para o cristão, a aplicação não é repetir os sacrifícios levíticos, mas viver uma comunhão real com Deus, recebida pela graça e expressa em gratidão e obediência.", "Ore, agradeça, participe da igreja, reconcilie-se quando necessário e trate a vida humana com respeito. Não deixe a fé ficar restrita ao culto; leve sua comunhão com Deus para suas relações e decisões.", "Senhor, obrigado porque me chamas para perto de ti. Ensina-me a viver em comunhão contigo e com meus irmãos. Dá-me gratidão, respeito pela vida e disposição para buscar reconciliação. Em nome de Jesus, amém."));
        return r;
    }

    private static String[] devotional(String a, String b, String c, String d, String e) {
        return new String[]{a, b, c, d, e};
    }
}
