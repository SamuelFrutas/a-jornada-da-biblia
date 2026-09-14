package br.com.samuelmarvila.ajornadadabiblia;

/** Monta conteúdo próprio para cada unidade dividida do plano. */
public final class DevotionalComposer {
    private DevotionalComposer() {}

    public static String[] forRange(String book,int chapter,int start,int end,int total,String[] chapterDevotional){
        String focus=focus(book,chapter,start,end);
        String scope=book+" "+chapter+":"+start+"–"+end;
        String[] out=new String[5];
        out[0]="Explicação do texto. Esta unidade corresponde a "+scope+". "+focus+" Leia os versículos indicados na NTLH e observe como a unidade começa, se desenvolve e termina. Não a separe do restante do capítulo.";
        out[1]="Contexto — tempo e cultura. "+focus+" Observe personagens, costumes, gênero literário e a situação histórica do livro. O contexto do capítulo deve ajudar a interpretar este trecho, e não ser usado para forçar uma conclusão que o texto não apresenta.";
        out[2]="Aplicação — reflexão. O que esta unidade revela sobre Deus, sobre o ser humano e sobre a vida diante dele? "+focus+" A aplicação cristã deve nascer do significado do texto, respeitando seu contexto original.";
        out[3]="Prática — como viver hoje. Transforme o aprendizado de "+scope+" em uma atitude concreta: obediência, arrependimento, fé, serviço, domínio próprio, reconciliação ou esperança, conforme o ensino do texto. Escolha uma ação para praticar hoje.";
        out[4]="Oração. Senhor Deus, ajuda-me a compreender "+scope+" com fidelidade. "+focus+" Dá-me humildade para aceitar tua correção, fé para confiar em ti e coragem para obedecer. Em nome de Jesus, amém.";
        return out;
    }

    private static String focus(String book,int chapter,int start,int end){
        String k=book+" "+chapter+":"+start+"-"+end;
        switch(k){
            case "Gênesis 24:1-27": return "Abraão envia seu servo para buscar uma esposa para Isaque, e o servo busca direção de Deus antes de agir.";
            case "Gênesis 24:28-49": return "O servo chega à família de Rebeca e relata como reconheceu a providência de Deus durante sua missão.";
            case "Gênesis 24:50-67": return "A família reconhece a mão de Deus, Rebeca decide partir e encontra Isaque.";
            case "Gênesis 37:1-11": return "José recebe sonhos e o conflito com seus irmãos começa a crescer.";
            case "Gênesis 37:12-22": return "José procura seus irmãos e eles passam a planejar contra ele.";
            case "Gênesis 37:23-36": return "José é vendido, e seus irmãos enganam Jacó, iniciando um período de sofrimento e providência.";
            case "Gênesis 41:1-13": return "Faraó tem sonhos que ninguém consegue interpretar.";
            case "Gênesis 41:14-36": return "José interpreta os sonhos de Faraó e propõe uma estratégia diante da fome que viria.";
            case "Gênesis 41:37-57": return "José recebe autoridade e administra os recursos do Egito durante a crise.";
            case "Êxodo 3:1-6": return "Moisés encontra Deus na sarça e é confrontado com a santidade divina.";
            case "Êxodo 3:7-15": return "Deus ouve o clamor de Israel e chama Moisés para participar da libertação.";
            case "Êxodo 3:16-22": return "Deus explica a Moisés como conduzirá Israel para fora do Egito.";
            case "Êxodo 12:1-28": return "Deus estabelece a Páscoa e orienta Israel sobre o cordeiro, o sangue e a refeição da libertação.";
            case "Êxodo 12:29-42": return "A décima praga acontece e Israel começa sua saída do Egito.";
            case "Êxodo 14:1-14": return "Israel fica diante do mar e Moisés chama o povo a confiar no livramento do Senhor.";
            case "Êxodo 14:15-25": return "Deus abre caminho pelo mar e o exército egípcio é atingido.";
            case "Levítico 4:1-21": return "O texto trata da oferta pelo pecado do sacerdote e da comunidade, mostrando a seriedade da culpa e da expiação.";
            case "Levítico 4:22-35": return "São apresentadas ofertas para pecados de líderes e pessoas comuns, destacando responsabilidade e reconciliação.";
            case "Levítico 6:1-13": return "Os sacerdotes recebem instruções sobre restituição, ofertas e a manutenção do fogo no altar.";
            case "Levítico 6:14-30": return "O texto orienta sobre a oferta de cereais e sobre as ofertas pelo pecado.";
            case "Levítico 7:1-21": return "São detalhadas a oferta pela culpa e as regras das ofertas de comunhão.";
            case "Levítico 7:22-38": return "As regras das ofertas são concluídas, incluindo sangue, gordura e a porção dos sacerdotes.";
            case "Levítico 8:1-13": return "Moisés inicia a consagração de Arão e seus filhos para o sacerdócio.";
            case "Levítico 8:14-29": return "Os sacrifícios mostram a purificação e dedicação necessárias para o serviço sacerdotal.";
            case "Levítico 8:30-36": return "A consagração é concluída e os sacerdotes permanecem junto à Tenda conforme a ordem de Deus.";
            case "Levítico 11:1-23": return "Começam as distinções entre animais considerados puros e impuros para Israel.";
            case "Levítico 11:24-47": return "As regras continuam mostrando como a santidade alcançava situações cotidianas de contato com animais.";
            case "Levítico 13:1-17": return "O sacerdote examina sinais de doenças de pele e determina condições de impureza.";
            case "Levítico 13:18-46": return "O exame sacerdotal continua com diferentes condições de pele e a situação da pessoa na comunidade.";
            case "Levítico 13:47-59": return "O texto trata de manchas em tecidos e da avaliação sacerdotal de possível impureza.";
            case "Levítico 14:1-20": return "São apresentadas as etapas de purificação de uma pessoa curada de doença de pele.";
            case "Levítico 14:21-32": return "O ritual de purificação é adaptado para quem não tinha recursos para uma oferta completa.";
            case "Levítico 14:33-42": return "A atenção passa para casas atingidas por mofo e para sua avaliação ritual.";
            case "Levítico 14:43-57": return "As regras para casas contaminadas são concluídas e resumidas.";
            case "Levítico 16:1-19": return "O Dia da Expiação começa com a entrada de Arão no lugar santíssimo e os sacrifícios pelo povo.";
            case "Levítico 16:20-34": return "O Dia da Expiação termina com o envio do bode e a instituição anual do rito de purificação.";
            case "Levítico 19:1-18": return "Deus chama Israel à santidade e liga o amor ao próximo a mandamentos concretos.";
            case "Levítico 19:19-37": return "A santidade é aplicada a diversas áreas da vida comunitária e dos relacionamentos.";
            case "Levítico 23:1-22": return "As festas do Senhor unem culto, memória da redenção e cuidado com os necessitados.";
            case "Levítico 23:23-44": return "As últimas festas do calendário são apresentadas, incluindo a lembrança da peregrinação no deserto.";
            case "Levítico 25:1-24": return "O ano sabático e o jubileu lembram que a terra pertence ao Senhor e limitam a exploração econômica.";
            case "Levítico 25:25-38": return "As regras de resgate e cuidado com pobres procuram impedir que a necessidade destrua definitivamente uma família.";
            case "Levítico 25:39-55": return "O texto limita a exploração de israelitas empobrecidos e lembra que o povo pertence ao Senhor.";
            case "Levítico 26:1-13": return "Deus apresenta bênçãos da obediência e reafirma sua presença e aliança.";
            case "Levítico 26:14-26": return "O texto alerta para as consequências da desobediência à aliança.";
            case "Levítico 26:27-46": return "Mesmo diante do juízo, Deus aponta para confissão, humildade e restauração da aliança.";
            case "Levítico 27:1-13": return "São tratadas promessas e votos pessoais feitos ao Senhor.";
            case "Levítico 27:14-27": return "O texto trata de casas, terras e animais dedicados ao Senhor.";
            case "Levítico 27:28-34": return "O capítulo conclui com coisas dedicadas e a afirmação de que os dízimos pertencem ao Senhor.";
            case "Jonas 1:1-10": return "Jonas foge da missão recebida e acaba envolvido em uma tempestade.";
            case "Jonas 1:11-17": return "Jonas é lançado ao mar e Deus providencia um grande peixe.";
            case "Jonas 3:1-5": return "Jonas recebe novamente a missão e anuncia a mensagem de Deus em Nínive.";
            case "Jonas 3:6-10": return "O rei e o povo de Nínive respondem com arrependimento, e Deus demonstra misericórdia.";
            case "Jonas 4:1-4": return "Jonas se irrita com a misericórdia de Deus e precisa confrontar seu próprio coração.";
            case "Jonas 4:5-11": return "Deus usa a planta e o vento para ensinar Jonas sobre compaixão.";
            case "Mateus 5:1-20": return "Jesus apresenta as bem-aventuranças e mostra que a justiça do Reino alcança o coração.";
            case "Mateus 5:21-37": return "Jesus aprofunda ensinamentos sobre ira, reconciliação, casamento e integridade.";
            case "Mateus 5:38-48": return "Jesus ensina uma justiça que supera a vingança e alcança o amor aos inimigos.";
            case "Mateus 6:1-18": return "Jesus corrige a religiosidade feita para aprovação humana e ensina sobre oração e jejum.";
            case "Mateus 6:19-34": return "Jesus ensina sobre tesouros, prioridades, ansiedade e confiança no cuidado de Deus.";
            case "Mateus 7:1-14": return "Jesus ensina sobre julgamento, oração, discernimento e o caminho da vida.";
            case "Mateus 7:15-29": return "Jesus alerta contra falsos profetas e mostra que ouvir sua Palavra exige prática.";
            case "João 3:1-10": return "Nicodemos conversa com Jesus e é confrontado com a necessidade de nascer de novo.";
            case "João 3:11-21": return "Jesus apresenta a iniciativa de Deus na salvação e chama à fé diante da luz.";
            case "João 15:1-11": return "Jesus usa a videira e os ramos para ensinar permanência nele, fruto e obediência.";
            case "João 15:12-27": return "Jesus chama os discípulos ao amor e os prepara para a oposição do mundo.";
            case "João 17:1-8": return "Jesus ora ao Pai e fala de sua missão e da vida eterna.";
            case "João 17:9-19": return "Jesus intercede pelos discípulos, pedindo proteção e santificação.";
            case "João 17:20-26": return "Jesus ora pelos futuros discípulos e pede unidade entre os que crerão nele.";
            case "Romanos 8:1-17": return "Paulo contrasta a vida segundo a carne com a vida segundo o Espírito e fala da adoção como filhos.";
            case "Romanos 8:18-30": return "O sofrimento presente é colocado diante da esperança futura e da ação de Deus.";
            case "Romanos 8:31-39": return "Paulo afirma que nada pode separar os que estão em Cristo do amor de Deus.";
            case "1 Coríntios 13:1-7": return "O amor é apresentado como indispensável e como prática concreta de paciência e bondade.";
            case "1 Coríntios 13:8-13": return "Paulo mostra a permanência do amor diante dos dons e do conhecimento parcial.";
            case "1 Coríntios 15:1-28": return "Paulo reafirma a ressurreição de Cristo como fundamento da esperança cristã.";
            case "1 Coríntios 15:29-58": return "Paulo desenvolve a esperança da ressurreição e chama os crentes à firmeza no Senhor.";
            case "Efésios 6:1-20": return "Paulo trata de família, trabalho, fortalecimento no Senhor e armadura de Deus.";
            case "Apocalipse 1:1-8": return "A revelação apresenta Jesus Cristo, sua autoridade e a esperança de sua vinda.";
            case "Apocalipse 1:9-20": return "João descreve o Cristo glorificado e recebe a ordem de registrar a mensagem às igrejas.";
            default: return "Esta unidade foi separada no plano por uma mudança de cena, argumento ou assunto. Leia seus versículos como uma unidade, observe a transição entre o início e o fim e compare com o contexto imediatamente anterior e posterior."
                    +" O objetivo é compreender primeiro o sentido do texto e somente depois formular a aplicação.";
        }
    }
}
