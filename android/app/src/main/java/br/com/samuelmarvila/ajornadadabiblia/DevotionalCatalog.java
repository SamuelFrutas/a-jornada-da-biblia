package br.com.samuelmarvila.ajornadadabiblia;

/** Devocionais revisados para as próximas etapas da jornada. */
public final class DevotionalCatalog {
    private DevotionalCatalog() {}

    public static String[] get(String book, int chapter) {
        if (!book.equals("Levítico")) return null;
        switch (chapter) {
            case 4: return d(
                "Levítico 4 trata da oferta pelo pecado e mostra que a culpa precisava ser tratada diante de Deus. O procedimento variava conforme a pessoa que havia pecado, mas a lógica permanecia: o pecado não deveria ser ignorado e a expiação fazia parte do culto estabelecido por Deus.",
                "O capítulo pertence ao sistema de sacrifícios do Tabernáculo. Sacerdotes, líderes e membros do povo tinham responsabilidades diferentes, e o ritual usava sangue no altar como sinal da vida oferecida em lugar do culpado. Isso não deve ser lido como uma fórmula mágica, mas como parte da aliança de Israel.",
                "O texto confronta a ideia de que somente pecados públicos importam. Mesmo quando o pecado era cometido sem intenção, ele precisava ser reconhecido e tratado. Para o cristão, isso nos leva a uma vida de confissão sincera e confiança na obra de Cristo, sem apagar o significado original de Levítico.",
                "Não esconda diante de Deus aquilo que você já percebeu que está errado. Reconheça, peça perdão, corrija o que puder e procure não repetir o mesmo caminho. Faça hoje uma confissão específica em vez de uma oração vaga.",
                "Senhor, dá-me humildade para reconhecer meu pecado e não tratá-lo como coisa pequena. Perdoa-me, corrige meu coração e ensina-me a viver em obediência. Que eu confie na tua graça e também leve a sério a santidade. Em nome de Jesus, amém.");
            case 5: return d(
                "Levítico 5 continua tratando de situações que exigiam oferta pelo pecado: omissão diante de um testemunho, impurezas e juramentos precipitados. O capítulo também mostra que havia diferentes ofertas conforme a condição econômica da pessoa.",
                "O sistema de culto israelita alcançava situações comuns da vida. A possibilidade de oferecer aves ou farinha mostrava que a aproximação estabelecida por Deus não dependia de riqueza. O foco estava no reconhecimento da culpa e na resposta adequada dentro da aliança.",
                "Deus se importa com pecados que podem parecer pequenos aos nossos olhos: palavras irresponsáveis, omissões e atitudes impuras. O capítulo também nos ensina que arrependimento verdadeiro envolve reconhecer a responsabilidade, e não apenas sentir culpa.",
                "Hoje, examine suas palavras e compromissos. Se você falou algo que não deveria, omitiu uma responsabilidade ou prejudicou alguém, tome uma atitude concreta para reparar a situação.",
                "Senhor, guarda minhas palavras, decisões e atitudes. Ajuda-me a reconhecer meus erros com sinceridade e a reparar o dano quando eu puder. Dá-me um coração sensível à tua correção. Em nome de Jesus, amém.");
            case 6: return d(
                "Levítico 6 apresenta instruções adicionais aos sacerdotes sobre ofertas, restituição, fogo do altar e serviço sacerdotal. O fogo deveria permanecer aceso, e o trabalho no altar exigia atenção contínua.",
                "O Tabernáculo tinha uma rotina de culto e sacerdotes responsáveis por mantê-la. O capítulo mostra que a adoração de Israel não dependia apenas de momentos emocionantes; havia serviço, cuidado, limpeza, disciplina e responsabilidade.",
                "A vida com Deus também possui constância. É fácil valorizar grandes momentos e desprezar pequenas responsabilidades. O fogo contínuo do altar não deve ser transformado em uma fórmula para ‘manter a presença de Deus’, mas pode nos lembrar da seriedade de uma devoção perseverante.",
                "Estabeleça hoje uma prática simples e sustentável: oração, leitura bíblica, serviço ou uma atitude de reconciliação. Não espere sentir vontade para cumprir aquilo que você sabe que é correto.",
                "Deus, ensina-me a ser constante. Livra-me de uma fé que depende somente de momentos fortes e forma em mim disciplina, reverência e fidelidade. Que minha vida seja uma resposta sincera à tua graça. Em nome de Jesus, amém.");
            case 7: return d(
                "Levítico 7 conclui várias instruções sobre as ofertas e diferencia especialmente a oferta pela culpa, as ofertas de comunhão e as porções destinadas aos sacerdotes. O capítulo termina reforçando as responsabilidades de quem servia no culto.",
                "As ofertas tinham funções e regras próprias. A refeição associada à oferta de comunhão também envolvia gratidão e relacionamento dentro da comunidade. O culto não era uma atividade isolada: havia participação do adorador, sacerdotes e comunidade.",
                "A adoração bíblica envolve gratidão, responsabilidade e comunhão. Deus não é apresentado como alguém que pode ser manipulado por rituais. O culto deveria expressar uma relação real de aliança e obediência.",
                "Agradeça a Deus hoje por algo específico e transforme a gratidão em uma atitude. Procure também uma oportunidade de servir alguém sem esperar reconhecimento.",
                "Senhor, recebe minha gratidão e ensina-me a servir com sinceridade. Que minha adoração apareça também na maneira como trato as pessoas e cumpro minhas responsabilidades. Em nome de Jesus, amém.");
            case 8: return d(
                "Levítico 8 narra a consagração de Arão e seus filhos para o sacerdócio. Moisés lava, veste, unge e apresenta os sacerdotes, seguindo as instruções dadas por Deus.",
                "A consagração acontece no contexto do Tabernáculo e do estabelecimento formal do sacerdócio levítico. As vestes, unção, sangue e ofertas comunicavam separação para um serviço santo; não era uma escolha baseada em prestígio pessoal.",
                "O capítulo mostra que serviço espiritual exige responsabilidade e preparação. Para a igreja, isso não significa reproduzir o sacerdócio levítico, mas reconhecer que servir a Deus não deve ser tratado como palco ou privilégio pessoal.",
                "Faça seu serviço hoje com humildade, mesmo nas tarefas que ninguém vê. Antes de buscar reconhecimento, pergunte: ‘Estou fazendo isto para Deus ou para ser visto?’.",
                "Senhor, purifica minhas motivações. Ensina-me a servir com humildade, responsabilidade e reverência. Que eu não busque posição, mas fidelidade. Em nome de Jesus, amém.");
            case 9: return d(
                "Levítico 9 descreve o início efetivo do serviço sacerdotal de Arão. Ele oferece os sacrifícios determinados e, depois, a glória do Senhor aparece ao povo; fogo sai da presença do Senhor e consome a oferta sobre o altar.",
                "Era um momento decisivo para Israel: o sacerdócio e o sistema de culto estavam sendo estabelecidos. A manifestação da glória confirma, dentro da narrativa, que Deus havia aceitado o serviço conforme suas instruções.",
                "O capítulo ensina que a adoração bíblica não é construída apenas pela criatividade humana. O povo responde ao Deus que se revela e estabelece como deve ser cultuado. Isso nos chama a reverência e não a transformar culto em espetáculo.",
                "Antes de fazer algo em nome de Deus, volte à Palavra e examine suas motivações. Pratique hoje uma forma de adoração simples, sincera e centrada em Deus, não em sua própria imagem.",
                "Senhor, recebe minha adoração e livra-me de transformar coisas santas em espetáculo. Dá-me reverência, sinceridade e desejo de obedecer à tua Palavra. Em nome de Jesus, amém.");
            case 10: return d(
                "Levítico 10 relata a morte de Nadabe e Abiú depois de oferecerem fogo não autorizado diante do Senhor. Depois, o capítulo apresenta orientações aos sacerdotes sobre sua conduta e sobre distinguir o santo do comum.",
                "Os dois filhos de Arão pertenciam à família sacerdotal e tinham responsabilidade diante do Tabernáculo. O relato reforça que a santidade de Deus não era tratada como algo comum dentro daquele sistema de culto.",
                "Este texto nos lembra que proximidade com coisas religiosas não elimina a responsabilidade diante de Deus. Também nos ensina a não usar a narrativa para justificar medo irracional: o ponto do capítulo é a santidade e a responsabilidade do serviço estabelecido por Deus.",
                "Trate hoje sua fé com seriedade. Não use ministério, posição ou experiência religiosa como desculpa para desobedecer. Se você serve na igreja, examine se suas atitudes fora do público combinam com aquilo que você ensina.",
                "Senhor, dá-me temor santo e equilíbrio. Que eu não trate tua Palavra com desprezo nem use coisas espirituais para alimentar meu orgulho. Faz de mim alguém fiel no público e no secreto. Em nome de Jesus, amém.");
            default: return null;
        }
    }

    private static String[] d(String a,String b,String c,String e,String f){return new String[]{a,b,c,e,f};}
}
