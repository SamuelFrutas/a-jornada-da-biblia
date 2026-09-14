package br.com.samuelmarvila.ajornadadabiblia;

import java.util.ArrayList;
import java.util.List;

public final class ReadingCatalog {
    private ReadingCatalog() {}
    private static final String[][] BOOKS={
        {"Gênesis","GEN","50","Pentateuco","Livro dos começos: criação, queda, dilúvio e os patriarcas."},
        {"Êxodo","EXO","40","Pentateuco","Libertação de Israel, aliança no Sinai e presença de Deus no Tabernáculo."},
        {"Levítico","LEV","27","Pentateuco","Santidade, culto, sacerdócio, sacrifícios e vida do povo da aliança."},
        {"Números","NUM","36","Pentateuco","A caminhada de Israel no deserto, suas rebeliões e a fidelidade de Deus."},
        {"Deuteronômio","DEU","34","Pentateuco","Os discursos finais de Moisés e a renovação da aliança antes da entrada na terra."},
        {"Josué","JOS","24","História","Entrada na terra prometida, conquista, distribuição da terra e renovação da aliança."},
        {"Juízes","JDG","21","História","Ciclos de pecado, opressão, livramento e a necessidade de liderança fiel."},
        {"Rute","RUT","4","História","Fidelidade, redenção, providência de Deus e a história de Rute, Noemi e Boaz."},
        {"1 Samuel","1SA","31","História","Samuel, Saul e o início da ascensão de Davi como rei."},
        {"2 Samuel","2SA","24","História","O reinado de Davi, suas vitórias, pecados e consequências familiares."},
        {"1 Reis","1KI","22","História","Salomão, o templo, a divisão do reino e os ministérios de Elias."},
        {"2 Reis","2KI","25","História","A história dos reinos divididos até o exílio de Israel e Judá."},
        {"1 Crônicas","1CH","29","História","Genealogias e a história de Davi com foco no culto e no templo."},
        {"2 Crônicas","2CH","36","História","Os reis de Judá, o templo, reformas e o caminho até o exílio e retorno."},
        {"Esdras","EZR","10","História","Retorno do exílio, reconstrução do templo e restauração da aliança."},
        {"Neemias","NEH","13","História","Reconstrução dos muros de Jerusalém e renovação espiritual do povo."},
        {"Ester","EST","10","História","Providência de Deus na preservação do povo judeu no império persa."},
        {"Jó","JOB","42","Sabedoria","Sofrimento, justiça, limites da compreensão humana e soberania de Deus."},
        {"Salmos","PSA","150","Poesia","Orações e cânticos que expressam louvor, lamento, confiança e esperança."},
        {"Provérbios","PRO","31","Sabedoria","Sabedoria prática para temor do Senhor, caráter, trabalho e relacionamentos."},
        {"Eclesiastes","ECC","12","Sabedoria","Reflexão sobre a brevidade da vida e o sentido de viver diante de Deus."},
        {"Cântico dos Cânticos","SNG","8","Poesia","Poemas sobre amor, desejo, compromisso e beleza da relação conjugal."},
        {"Isaías","ISA","66","Profetas","Juízo, esperança, santidade de Deus, restauração e expectativa messiânica."},
        {"Jeremias","JER","52","Profetas","Chamado profético, infidelidade de Judá, juízo e promessa de nova aliança."},
        {"Lamentações","LAM","5","Poesia","Lamento pela queda de Jerusalém e esperança na misericórdia de Deus."},
        {"Ezequiel","EZK","48","Profetas","Glória de Deus, juízo, responsabilidade, restauração e novo coração."},
        {"Daniel","DAN","12","Profetas","Fidelidade no exílio, soberania de Deus e visões sobre reinos e o futuro."},
        {"Oséias","HOS","14","Profetas","Infidelidade de Israel apresentada como ruptura da aliança e amor persistente de Deus."},
        {"Joel","JOL","3","Profetas","Praga, chamado ao arrependimento, dia do Senhor e derramamento do Espírito."},
        {"Amós","AMO","9","Profetas","Denúncia da injustiça e chamado à verdadeira justiça e fidelidade a Deus."},
        {"Obadias","OBA","1","Profetas","Juízo contra Edom e esperança de restauração do povo do Senhor."},
        {"Jonas","JON","4","Profetas","Chamado missionário, fuga, arrependimento de Nínive e misericórdia de Deus."},
        {"Miqueias","MIC","7","Profetas","Juízo, esperança, justiça, misericórdia e promessa de um governante vindo de Belém."},
        {"Naum","NAM","3","Profetas","Juízo sobre Nínive e anúncio de que a violência não ficará impune."},
        {"Habacuque","HAB","3","Profetas","Perguntas sobre o mal, resposta de Deus e fé em meio à crise."},
        {"Sofonias","ZEP","3","Profetas","Dia do Senhor, juízo, chamado à humildade e esperança de restauração."},
        {"Ageu","HAG","2","Profetas","Chamado para reconstruir o templo e colocar a obra de Deus em primeiro lugar."},
        {"Zacarias","ZEC","14","Profetas","Visões, restauração de Jerusalém e esperança no reinado de Deus."},
        {"Malaquias","MAL","4","Profetas","Correção da adoração negligente e promessa da intervenção do Senhor."},
        {"Mateus","MAT","28","Evangelhos","Jesus como Messias e Rei, seu ensino, milagres, morte e ressurreição."},
        {"Marcos","MRK","16","Evangelhos","Jesus em ação, servo poderoso, anúncio do Reino, cruz e ressurreição."},
        {"Lucas","LUK","24","Evangelhos","A vida de Jesus apresentada com atenção aos pobres, pecadores, oração e salvação."},
        {"João","JHN","21","Evangelhos","Jesus como o Filho de Deus e a vida recebida por meio da fé nele."},
        {"Atos","ACT","28","História","O Espírito Santo capacita a igreja e o evangelho avança de Jerusalém até Roma."},
        {"Romanos","ROM","16","Epístola","Evangelho, pecado, graça, justificação pela fé, santificação e vida cristã."},
        {"1 Coríntios","1CO","16","Epístola","Correções para uma igreja dividida e instruções sobre santidade, dons e ressurreição."},
        {"2 Coríntios","2CO","13","Epístola","Ministério, fraqueza, consolo, generosidade e defesa do apostolado de Paulo."},
        {"Gálatas","GAL","6","Epístola","Liberdade no evangelho, graça, fé e vida conduzida pelo Espírito."},
        {"Efésios","EPH","6","Epístola","Identidade em Cristo, unidade da igreja e prática de uma nova vida."},
        {"Filipenses","PHP","4","Epístola","Alegria em Cristo, humildade, perseverança e contentamento."},
        {"Colossenses","COL","4","Epístola","Supremacia de Cristo e transformação da vida por causa dessa realidade."},
        {"1 Tessalonicenses","1TH","5","Epístola","Fé, amor, esperança, santidade e esperança na volta de Cristo."},
        {"2 Tessalonicenses","2TH","3","Epístola","Perseverança, correção de confusões sobre o dia do Senhor e trabalho responsável."},
        {"1 Timóteo","1TI","6","Epístola","Ordem da igreja, liderança, doutrina e piedade prática."},
        {"2 Timóteo","2TI","4","Epístola","Perseverança no ministério, fidelidade à Palavra e coragem diante do sofrimento."},
        {"Tito","TIT","3","Epístola","Liderança saudável, graça e boas obras como fruto da fé."},
        {"Filemom","PHM","1","Epístola","Perdão, reconciliação e transformação das relações pelo evangelho."},
        {"Hebreus","HEB","13","Epístola","Superioridade de Cristo, sua obra sacerdotal e perseverança na fé."},
        {"Tiago","JAS","5","Epístola","Fé verdadeira demonstrada em obras, domínio da língua e perseverança."},
        {"1 Pedro","1PE","5","Epístola","Esperança, santidade e perseverança dos cristãos em meio ao sofrimento."},
        {"2 Pedro","2PE","3","Epístola","Crescimento na fé, alerta contra falsos mestres e esperança na volta do Senhor."},
        {"1 João","1JN","5","Epístola","Comunhão com Deus, amor, obediência e segurança da vida eterna em Cristo."},
        {"2 João","2JN","1","Epístola","Verdade, amor e cuidado com falsos ensinamentos."},
        {"3 João","3JN","1","Epístola","Fidelidade, hospitalidade e contraste entre bons e maus exemplos na igreja."},
        {"Judas","JUD","1","Epístola","Defesa da fé e alerta contra pessoas que distorcem a graça."},
        {"Apocalipse","REV","22","Profecia","Visões de Cristo, conflito espiritual, juízo, perseverança e consumação do Reino de Deus."}
    };

    public static List<Reading> all(){
        List<Reading> out=new ArrayList<>(); int order=0;
        for(String[] b:BOOKS){
            int chapters=Integer.parseInt(b[2]);
            for(int ch=1;ch<=chapters;ch++){
                String[] d=specific(b[0],ch);
                if(d==null)d=generic(b[0],ch,b[3],b[4]);
                out.add(new Reading(order++,b[0],b[1],ch,1,0,0,chapterDescription(b[0],ch,b[4]),d));
            }
        }
        return out;
    }

    private static String chapterDescription(String book,int chapter,String overview){return "Leitura de "+book+" "+chapter+". Leia o capítulo inteiro na NTLH e acompanhe o contexto antes de aplicar o que aprendeu. "+overview;}
    private static String[] generic(String book,int chapter,String genre,String overview){
        return new String[]{
            "Leia "+book+" "+chapter+" inteiro na NTLH antes de refletir. Observe quem fala, quem age, qual é o conflito ou tema principal e como o capítulo termina. Não retire uma frase do contexto. A pergunta principal é: o que este capítulo realmente comunica dentro do livro?",
            "Este capítulo pertence a "+genre+" e precisa ser lido dentro da história maior da Bíblia. O contexto do livro é: "+overview+". Observe costumes, personagens, lugar, época e relação com os capítulos imediatamente anteriores e posteriores. Quando houver linguagem poética, narrativa, lei ou profecia, respeite o gênero antes de fazer uma aplicação.",
            "Depois de entender o sentido original, pergunte o que o texto revela sobre Deus, sobre o ser humano, sobre pecado, fé, obediência, esperança ou relacionamento com o próximo. A aplicação cristã deve nascer do significado do texto, e não substituir o significado. Quando houver uma conexão com Cristo ou com o Novo Testamento, trate-a com cuidado e não force uma equivalência que o capítulo não afirma.",
            "Escolha uma atitude concreta baseada no que você realmente encontrou na leitura. Pode ser uma mudança de comportamento, uma decisão de obediência, uma conversa necessária, uma prática de oração, gratidão, serviço ou abandono de algo errado. Escreva em uma frase: ‘Hoje vou viver este ensinamento desta maneira: ...’.",
            "Senhor Deus, abre meu entendimento para tua Palavra. Ajuda-me a ler "+book+" "+chapter+" com atenção, respeitando seu contexto e sem torcer o sentido para confirmar o que eu já penso. Mostra-me o que devo aprender, corrige o que precisa ser corrigido e dá-me força para praticar. Em nome de Jesus, amém."
        };
    }

    private static String[] specific(String book,int ch){
        if(book.equals("Levítico")&&ch==1)return new String[]{
            "Levítico 1 apresenta as orientações de Deus para a oferta queimada. O Senhor fala a Moisés da Tenda Sagrada e estabelece como o israelita deveria apresentar o animal, como o sangue seria tratado pelos sacerdotes e como a oferta seria totalmente queimada. São apresentadas três possibilidades: gado, rebanho de ovelhas ou cabras e aves.",
            "Levítico vem logo depois da construção do Tabernáculo. Israel estava aprendendo a viver como povo da aliança na presença de um Deus santo. Sacrifícios faziam parte desse sistema de culto. A exigência de um animal sem defeito mostrava que a oferta não deveria ser algo tratado como sem valor. A expressão ‘cheiro agradável’ descreve a oferta aceita por Deus; não significa que Deus precisasse de alimento.",
            "O texto nos lembra que aproximar-se de Deus não deve ser tratado de qualquer maneira. A adoração envolve reverência, entrega e reconhecimento da santidade de Deus. Para o cristão, Levítico 1 deve primeiro ser entendido dentro da aliança de Israel. Depois, à luz do Novo Testamento, reconhecemos que os sacrifícios não eram a solução final e que a obra de Cristo é apresentada como definitiva.",
            "Pratique uma adoração que não seja apenas aparência. Separe um tempo real para Deus, confesse aquilo que precisa ser confessado e não trate a fé como uma negociação para conseguir benefícios. Entregue a Deus suas decisões, hábitos, tempo e prioridades.",
            "Senhor Deus, ensina-me a te tratar com reverência e sinceridade. Que eu não transforme minha fé em aparência nem tente negociar contigo. Dá-me entendimento para ler tua Palavra com fidelidade e coragem para praticá-la. Em nome de Jesus, amém."};
        if(book.equals("Levítico")&&ch==2)return new String[]{
            "Levítico 2 apresenta a oferta de cereais, feita com farinha, azeite e, em algumas formas, incenso. Parte era queimada sobre o altar e o restante ficava para os sacerdotes. O capítulo também orienta sobre ofertas preparadas de diferentes maneiras e sobre os primeiros frutos.",
            "Farinha, azeite, sal e cereais faziam parte da vida cotidiana de Israel. O adorador podia apresentar parte daquilo que produzia. O que sobrava da oferta era destinado aos sacerdotes, integrando também o sustento do serviço do santuário.",
            "A adoração não se limita a momentos extraordinários. Aquilo que recebemos por meio do trabalho e da provisão também pode ser reconhecido diante de Deus. Não devemos copiar mecanicamente o sistema levítico, mas o princípio permanece: reconhecer Deus como fonte e Senhor de tudo.",
            "Agradeça hoje pelo que Deus colocou em suas mãos. Use seus recursos com responsabilidade, seja generoso e não trate sua contribuição como compra de bênçãos. Trabalhe e sirva com gratidão.",
            "Senhor, obrigado por tua provisão. Ensina-me a reconhecer tua bondade nas coisas simples e no fruto do meu trabalho. Forma em mim um coração agradecido, generoso e obediente. Em nome de Jesus, amém."};
        if(book.equals("Levítico")&&ch==3)return new String[]{
            "Levítico 3 descreve a oferta de comunhão, também chamada oferta pacífica. O animal deveria ser sem defeito, e sangue e gordura recebiam tratamento específico no altar. O capítulo apresenta as orientações para gado, ovelhas e cabras e termina com a proibição de comer sangue e gordura.",
            "A oferta de comunhão fazia parte do culto de Israel e estava ligada à paz e à comunhão diante de Deus. A gordura era especialmente dedicada ao Senhor, enquanto o sangue representava a vida. O culto levítico usava sinais concretos para ensinar que a vida pertence a Deus.",
            "Levítico 3 mostra que a relação com Deus envolve comunhão, gratidão e celebração. Para o cristão, a aplicação não é repetir os sacrifícios levíticos, mas viver uma comunhão real com Deus, recebida pela graça e expressa em gratidão e obediência.",
            "Ore, agradeça, participe da igreja, reconcilie-se quando necessário e trate a vida humana com respeito. Não deixe a fé ficar restrita ao culto; leve sua comunhão com Deus para suas relações e decisões.",
            "Senhor, obrigado porque me chamas para perto de ti. Ensina-me a viver em comunhão contigo e com meus irmãos. Dá-me gratidão, respeito pela vida e disposição para buscar reconciliação. Em nome de Jesus, amém."};
        return null;
    }
}
