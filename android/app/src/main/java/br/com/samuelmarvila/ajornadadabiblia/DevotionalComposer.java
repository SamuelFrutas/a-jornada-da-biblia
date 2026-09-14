package br.com.samuelmarvila.ajornadadabiblia;

/** Monta devocionais próprios para cada unidade dividida do plano. */
public final class DevotionalComposer {
    private DevotionalComposer() {}

    public static String[] forRange(String book,int chapter,int start,int end,int total,String[] chapterDevotional){
        String focus=focus(book,chapter,start,end);
        String scope=""+book+" "+chapter+":"+start+"–"+end;
        String[] out=new String[5];
        String[] base=chapterDevotional;
        String b0=pick(base,0,"Leia o trecho com atenção.");
        String b1=pick(base,1,"Observe o contexto histórico e literário.");
        String b2=pick(base,2,"Reflita sobre o que o texto ensina.");
        String b3=pick(base,3,"Transforme o aprendizado em uma atitude.");
        String b4=pick(base,4,"Ore pedindo entendimento e obediência.");
        out[0]="Leitura de " + scope + ". Este trecho não deve ser lido isoladamente: " + focus + " " + b0;
        out[1]="Contexto — tempo e cultura. " + focus + " " + b1;
        out[2]="Aplicação — reflexão. O foco deste trecho é: " + focus + " " + b2 + " Não procure apenas uma frase bonita; procure o sentido que o texto realmente comunica.
";
        out[3]="Prática — como viver hoje. A partir de " + scope + ", escolha uma atitude concreta relacionada ao ensino do texto. " + b3;
        out[4]="Oração. Senhor Deus, ajuda-me a compreender " + scope + " com fidelidade. " + focus + " Dá-me humildade para aceitar tua correção, fé para confiar em ti e coragem para obedecer. " + b4;
        return out;
    }

    private static String pick(String[] a,int i,String fallback){return a!=null&&i<a.length&&a[i]!=null&&!a[i].trim().isEmpty()?a[i].trim():fallback;}

    private static String focus(String book,int chapter,int start,int end){
        String k=book+" "+chapter+":"+start+"-"+end;
        switch(k){
            case "Gênesis 24:1-27": return "A primeira parte mostra Abraão providenciando uma esposa para Isaque e o servo buscando direção de Deus antes de agir.";
            case "Gênesis 24:28-49": return "O servo chega à casa de Rebeca e relata como reconheceu a direção de Deus durante sua missão.";
            case "Gênesis 24:50-67": return "A família reconhece a providência de Deus, Rebeca decide partir e encontra Isaque.";
            case "Gênesis 37:1-11": return "O trecho apresenta José, os sonhos que recebeu e o conflito que começou a crescer entre ele e seus irmãos.";
            case "Gênesis 37:12-22": return "José é enviado para procurar os irmãos, que passam a planejar contra ele.";
            case "Gênesis 37:23-36": return "Os irmãos vendem José e enganam Jacó, iniciando uma longa cadeia de sofrimento e providência.";
            case "Gênesis 41:1-13": return "Faraó tem sonhos perturbadores e ninguém entre seus sábios consegue explicá-los.";
            case "Gênesis 41:14-36": return "José interpreta os sonhos de Faraó e apresenta uma estratégia para enfrentar os anos de fome.";
            case "Gênesis 41:37-57": return "José é elevado a uma posição de autoridade e administra os recursos durante a crise.";
            case "Êxodo 3:1-6": return "Moisés encontra Deus na sarça que ardia sem se consumir e é confrontado com a santidade divina.";
            case "Êxodo 3:7-15": return "Deus revela que ouviu o clamor de Israel e chama Moisés para participar da libertação.";
            case "Êxodo 3:16-22": return "Deus explica a Moisés como Israel seria conduzido para fora do Egito e orienta o povo sobre a saída.";
            case "Êxodo 12:1-28": return "Deus estabelece a Páscoa e orienta Israel sobre o cordeiro, o sangue e a refeição que marcaria a libertação.";
            case "Êxodo 12:29-42": return "A décima praga acontece, Faraó permite a saída e Israel começa sua caminhada para fora do Egito.";
            case "Êxodo 14:1-14": return "Israel fica encurralado diante do mar e Moisés chama o povo a confiar no livramento de Deus.";
            case "Êxodo 14:15-25": return "Deus abre caminho pelo mar e o exército egípcio começa a ser vencido.";
            case "Levítico 4:1-21": return "O trecho trata da oferta pelo pecado quando o sacerdote ou a comunidade pecavam, mostrando a seriedade da culpa e da reconciliação.";
            case "Levítico 4:22-35": return "São apresentadas orientações para pecados cometidos por líderes e pessoas comuns, destacando responsabilidade e expiação.";
            case "Levítico 6:1-13": return "As instruções aos sacerdotes mostram como manter o altar e o fogo continuamente diante do Senhor.";
            case "Levítico 6:14-30": return "O texto orienta sobre a oferta de cereais e sobre o tratamento das ofertas pelo pecado dentro do culto.";
            case "Levítico 7:1-21": return "A primeira parte detalha a oferta pela culpa e as regras relacionadas às ofertas de comunhão.";
            case "Levítico 7:22-38": return "O trecho conclui as regras das ofertas, incluindo sangue, gordura e a porção destinada aos sacerdotes.";
            case "Levítico 8:1-13": return "Moisés inicia a consagração de Arão e seus filhos para o serviço sacerdotal.";
            case "Levítico 8:14-29": return "Os sacrifícios de consagração mostram que o serviço sacerdotal exigia purificação, sangue e dedicação ao Senhor.";
            case "Levítico 8:30-36": return "Moisés conclui a consagração e orienta os sacerdotes a permanecerem junto à entrada da Tenda conforme a ordem de Deus.";
            case "Levítico 11:1-23": return "O trecho começa as distinções entre animais considerados puros e impuros para Israel.";
            case "Levítico 11:24-47": return "As regras continuam mostrando como a santidade alcançava até situações cotidianas envolvendo contato com animais e objetos.";
            case "Levítico 13:1-17": return "O texto apresenta o exame sacerdotal de doenças de pele e a responsabilidade de distinguir situações de impureza.";
            case "Levítico 13:18-46": return "As orientações ampliam o exame de diferentes condições de pele e tratam da condição da pessoa perante a comunidade.";
            case "Levítico 13:47-59": return "O trecho trata de sinais de mofo ou manchas em tecidos e da avaliação sacerdotal daquilo que poderia tornar algo impuro.";
            case "Levítico 14:1-20": return "São apresentadas as etapas de purificação de uma pessoa curada de doença de pele.";
            case "Levítico 14:21-32": return "O texto adapta o ritual de purificação para quem não tinha recursos para uma oferta completa.";
            case "Levítico 14:33-42": return "A atenção passa para casas atingidas por mofo, mostrando que a pureza também envolvia o ambiente comunitário.";
            case "Levítico 14:43-57": return "O trecho conclui as regras para casas contaminadas e resume os princípios de distinção entre puro e impuro.";
            case "Levítico 16:1-19": return "O trecho inicial do Dia da Expiação descreve a entrada de Arão no lugar santíssimo e os sacrifícios pelo pecado do povo.";
            case "Levítico 16:20-34": return "A segunda parte conclui o Dia da Expiação com o envio do bode e a instituição anual do rito de purificação de Israel.";
            case "Levítico 19:1-18": return "Deus chama Israel à santidade e relaciona o amor ao próximo com mandamentos concretos de justiça e cuidado.";
            case "Levítico 19:19-37": return "O restante do capítulo aplica a santidade a diversas áreas da vida comunitária, dos relacionamentos ao comércio.";
            case "Levítico 23:1-22": return "O trecho apresenta festas do Senhor e orientações que ligam culto, memória da redenção e cuidado com os necessitados.";
            case "Levítico 23:23-44": return "As últimas festas do calendário são apresentadas, culminando na celebração que recordava a peregrinação de Israel no deserto.";
            case "Levítico 25:1-24": return "O ano sabático e o jubileu ensinam Israel a reconhecer que a terra pertence ao Senhor e que a vida econômica deveria respeitar limites.";
            case "Levítico 25:25-38": return "As regras de resgate de propriedades e cuidado com irmãos pobres procuram impedir que a necessidade destruísse definitivamente uma família.";
            case "Levítico 25:39-55": return "O texto limita a exploração de israelitas empobrecidos e lembra que o povo pertence ao Senhor.";
            case "Levítico 26:1-13": return "Deus apresenta bênçãos associadas à obediência e reafirma sua presença e aliança com Israel.";
            case "Levítico 26:14-26": return "O texto alerta para as consequências da desobediência e chama o povo a levar a aliança a sério.";
            case "Levítico 26:27-46": return "Mesmo diante do juízo, Deus mantém aberta a possibilidade de confissão, humildade e restauração da aliança.";
            case "Levítico 27:1-13": return "O trecho trata de votos pessoais e do valor estabelecido para pessoas dedicadas ao Senhor.";
            case "Levítico 27:14-27": return "São tratadas dedicações de casas, terras e animais, mostrando que votos diante de Deus não deveriam ser feitos de maneira irresponsável.";
            case "Levítico 27:28-34": return "O capítulo termina distinguindo coisas dedicadas irrevogavelmente e reafirmando que os dízimos pertencem ao Senhor.";
            case "Jonas 1:1-10": return "Jonas recebe a missão de anunciar a mensagem do Senhor a Nínive, foge na direção oposta e acaba envolvido numa tempestade.";
            case "Jonas 1:11-17": return "Os marinheiros tentam preservar suas vidas, mas Jonas é lançado ao mar e Deus providencia um grande peixe.";
            case "Jonas 3:1-5": return "Depois de ser chamado novamente, Jonas vai a Nínive e anuncia a mensagem que Deus lhe confiou.";
            case "Jonas 3:6-10": return "O rei e o povo de Nínive respondem com arrependimento, e Deus demonstra misericórdia diante dessa mudança.";
            case "Jonas 4:1-4": return "Jonas se irrita com a misericórdia de Deus e precisa confrontar a própria falta de compaixão.";
            case "Jonas 4:5-11": return "Deus usa a planta, o verme e o vento para ensinar Jonas sobre compaixão e sobre o valor das pessoas.";
            case "Mateus 5:1-20": return "Jesus apresenta as bem-aventuranças e mostra que a justiça do Reino precisa alcançar o coração, não apenas a aparência religiosa.";
            case "Mateus 5:21-37": return "Jesus aprofunda mandamentos sobre ira, reconciliação, adultério, casamento, juramentos e integridade.";
            case "Mateus 5:38-48": return "Jesus chama seus discípulos a uma justiça que supera a vingança e alcança até o amor aos inimigos.";
            case "Mateus 6:1-18": return "Jesus corrige a prática religiosa feita para receber aprovação humana e ensina sobre esmolas, oração e jejum.";
            case "Mateus 6:19-34": return "Jesus orienta sobre tesouros, prioridades, ansiedade e confiança no cuidado de Deus.";
            case "Mateus 7:1-14": return "Jesus ensina sobre julgamento, oração, discernimento e o caminho que conduz à vida.";
            case "Mateus 7:15-29": return "Jesus alerta contra falsos profetas e mostra que ouvir sua Palavra precisa resultar em prática.";
            case "João 3:1-10": return "Nicodemos conversa com Jesus e é confrontado com a necessidade de nascer de novo.";
            case "João 3:11-21": return "Jesus explica a iniciativa de Deus em oferecer salvação e apresenta a fé nele como resposta à luz.";
            case "João 15:1-11": return "Jesus usa a imagem da videira e dos ramos para ensinar permanência nele, fruto e obediência.";
            case "João 15:12-27": return "Jesus chama seus discípulos a amarem uns aos outros e os prepara para a oposição do mundo.";
            case "João 17:1-8": return "Jesus ora ao Pai, fala de sua missão concluída e da vida eterna como conhecer o verdadeiro Deus.";
            case "João 17:9-19": return "Jesus intercede por seus discípulos, pedindo proteção e santificação enquanto permanecem no mundo.";
            case "João 17:20-26": return "Jesus amplia sua oração para os futuros discípulos e pede unidade entre aqueles que crerão nele.";
            case "Romanos 8:1-17": return "Paulo contrasta a vida segundo a carne com a vida segundo o Espírito e fala da adoção como filhos de Deus.";
            case "Romanos 8:18-30": return "O sofrimento presente é colocado diante da esperança futura, enquanto Paulo fala da criação, da oração do Espírito e do propósito de Deus.";
            case "Romanos 8:31-39": return "Paulo conclui afirmando que nada pode separar os que estão em Cristo do amor de Deus.";
            case "1 Coríntios 13:1-7": return "O amor é apresentado como indispensável e como uma prática concreta, paciente e perseverante.";
            case "1 Coríntios 13:8-13": return "Paulo mostra a permanência do amor e a superioridade dele diante dos dons e do conhecimento parcial.";
            case "1 Coríntios 15:1-28": return "Paulo reafirma a ressurreição de Cristo e mostra por que ela é essencial para a esperança cristã.";
            case "1 Coríntios 15:29-58": return "Paulo desenvolve a esperança da ressurreição dos mortos e conclui chamando os crentes à firmeza no trabalho do Senhor.";
            case "Efésios 6:1-20": return "Paulo trata de relações familiares, trabalho, fortalecimento no Senhor e da armadura de Deus para permanecer firme.";
            case "Apocalipse 1:1-8": return "A revelação começa apresentando Jesus Cristo, sua autoridade e a esperança de sua vinda.";
            case "Apocalipse 1:9-20": return "João descreve a visão do Cristo glorificado e recebe a ordem de registrar a mensagem para as igrejas.";
        }
        return "Este trecho foi separado no plano por sua unidade literária. Leia os versículos indicados como uma cena, argumento, instrução ou movimento próprio e observe especialmente a transição entre o início e o fim da unidade. O restante do capítulo ajuda a interpretar o trecho, mas hoje o foco é compreender esta unidade antes de avançar.";
    }
}
