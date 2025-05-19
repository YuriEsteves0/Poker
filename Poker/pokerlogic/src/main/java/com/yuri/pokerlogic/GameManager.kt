package com.yuri.pokerlogic

import kotlin.system.exitProcess

enum class rodadas(){
    PRE_FLOP,
    FLOP,
    TURN,
    RIVER,
    SHOWDOWN
}

class GameManager(var players: MutableList<Player> = mutableListOf()) {
    var etapa_atual = rodadas.PRE_FLOP
    var pot: Int = 0
    val smallBlind: Int = 1;
    val bigBlind: Int = 2;
    var dealer: Player? = null
    var checkVez: Boolean = false;
    var qntsChecksRound: Int = 0;
    var apostaMaiorDaRodada: Int= 0;
    var apostasDaRodada: MutableMap<Player, Int> = mutableMapOf()
    var cartaMesa1: Card? = null
    var cartaMesa2: Card? = null
    var cartaMesa3: Card? = null
    var cartaMesa4: Card? = null
    var cartaMesa5: Card? = null

    fun inicio() {
        dealer = players.random() // DEALER ALEATORIO

//        dealer = players[0] // MUDAR ISSO AQUI DEPOIS

        players[0].mao.clear()
        players[1].mao.clear()

        players[0].mao.add(Card.getCartaAleatoria())
        players[0].mao.add(Card.getCartaAleatoria())

        players[1].mao.add(Card.getCartaAleatoria())
        players[1].mao.add(Card.getCartaAleatoria())
//        verifVariaveisZeradas()

        if (dealer == players[0]) {
            checkVez = true
        } else {
            checkVez = false
        }

        dealerInfo();
        blinds(dealer!!);

        println("Sua mão é: ${players[0].mao[0].valor} de ${players[0].mao[0].naipe} e ${players[0].mao[1].valor} de ${players[0].mao[1].naipe}")

        turno()
    }

    fun verifVariaveisZeradas(){
        println()
        println("---------- VERIFICAÇÃO ------------")
        println("Etapa atual: ${etapa_atual}")
        println("Pot: ${pot}")
        println("Dealer: ${dealer?.nome}")
        println("Check Vez: ${checkVez}")
        println("Qnts Checks Round: ${qntsChecksRound}")
        println("Aposta Maior Da Rodada: ${apostaMaiorDaRodada}")
        println("Apostas Da Rodada: ${apostasDaRodada}")
        println("-----------------------------------")
        println()
    }

    fun opcoesJogador(): String? {
        val apostaFeitaPeloJogador = apostasDaRodada[players[0]] ?: 0

        println("AÇÕES: CHECK(1), CALL(2), RAISE(3), FOLD(4)")
        println()
        print("Escolha sua ação: ")
        var opcao = readLine()
        println()

        if (opcao.isNullOrEmpty()) {
            println("Escolha inválida")
            return null
        } else {
            when (opcao) {
                "1" -> { // CHECK DO JOGOADOR
                    if (players[0].canCheck(apostaMaiorDaRodada, apostaFeitaPeloJogador)) {
                        println("${players[0].nome} deu CHECK")
                        qntsChecksRound++
                        trocarCheck()
                        verificarAvancoRodada()
                        turno()
                        return "CHECK"
                    } else {
                        println("Você não pode dar CHECK, há uma aposta ativa de ${apostaMaiorDaRodada}¢")
                        return opcoesJogador()
                    }
                }
                "2" -> {
                    val diff = apostaMaiorDaRodada - apostaFeitaPeloJogador
                    if (diff <= 0) {
                        println("${players[0].nome} deu CHECK")
                        qntsChecksRound++
                        trocarCheck()
                        verificarAvancoRodada()
                        turno()
                    }

                    val valorCall = players[0].call(apostaMaiorDaRodada, apostaFeitaPeloJogador)
                    pot += valorCall;
                    apostasDaRodada[players[0]] = apostaFeitaPeloJogador + valorCall
                    println("${players[0].nome} deu CALL -> ${valorCall}¢")
                    println("POTE: ${pot}¢")
                    trocarCheck()
                    qntsChecksRound++
                    verificarAvancoRodada()
                    turno()
                    return "CALL"
                }
                "3" -> {
                    print("Digite o valor do RAISE: ")
                    val raiseStr = readLine()
                    var raiseValor = raiseStr?.toIntOrNull()

                    if(raiseValor == null || raiseValor <= 0){
                        println("Valor Inválido de raise")
                        return opcoesJogador()
                    }

                    if (raiseValor != null) {
                        if(raiseValor > players[0].saldo){
                            raiseValor = players[1].saldo.toInt()
                        }
                    }

                    val totalAposta = players[0].raise(apostaMaiorDaRodada, raiseValor, apostaFeitaPeloJogador)
                    pot += totalAposta
                    apostaMaiorDaRodada += raiseValor
                    apostasDaRodada[players[0]] = (apostaFeitaPeloJogador + totalAposta)
                    println("${players[0].nome} aumentou para ${apostaMaiorDaRodada}¢ com um RAISE de ${raiseValor}¢ (apostou ${totalAposta}¢ nesta rodada)")
                    println("POTE: ${pot}¢")
                    qntsChecksRound = 0;
                    trocarCheck()
                    turno()
                    return "RAISE"

                }
                "4" -> {
                    players[0].fold()
                    println("${players[0].nome} desistiu (FOLD)")
                    players[1].saldo += pot.toDouble();
                    limparTudo();
                    println("")
                    println("-----------------------")
                    println("ACABOU")
                    println("-----------------------")
                    println("")
                    inicio()
                }

                else -> {
                    println("Opção inválida")
                    return opcoesJogador()
                }
            }
            return null
        }
        return null
    }

    fun verificarAvancoRodada(){
        if(qntsChecksRound == 2){
            println("")
            println("-----------------------")
            println("Indo para a proxima etapa...")
            avancarEtapa()
            println("-----------------------")
            println("")
            println("Sua mão é: ${players[0].mao[0].valor} de ${players[0].mao[0].naipe} e ${players[0].mao[1].valor} de ${players[0].mao[1].naipe}")
        }
    }

    fun avancarEtapa(){
        //AQUI VAI ZERAR AS VARIAVEIS DE CADA ETAPA
        qntsChecksRound = 0;
        apostaMaiorDaRodada = 0;
        apostasDaRodada.clear()
        checkVez = dealer == players[0]

        when(etapa_atual) {
            rodadas.PRE_FLOP -> {
                etapa_atual = rodadas.FLOP
                dadosCartasEtapa(3);
            }
            rodadas.FLOP -> {
                etapa_atual = rodadas.TURN
                dadosCartasEtapa(4);
            }
            rodadas.TURN -> {
                etapa_atual = rodadas.RIVER
                dadosCartasEtapa(5);
            }
            rodadas.RIVER -> {
                etapa_atual = rodadas.SHOWDOWN
                println("Fim da mão. Distribuindo ganhos...")
                analisarMaosPlayer()
                inicio()
            }
            rodadas.SHOWDOWN -> {

            }
        }
        println("Etapa atual: $etapa_atual")
    }

    fun analisarMaosPlayer() {
        val cartasMesa = listOfNotNull(cartaMesa1, cartaMesa2, cartaMesa3, cartaMesa4, cartaMesa5)

        val resultados = mutableMapOf<Player, String>()

        for (player in players) {
            val todasCartas = player.mao + cartasMesa

            // EX: SE TIVER 3 CARTAS ÁS E 2 CARTAS 4 FICARIA ASSIM NO OUTPUT: {A=3} {4=2}
            val contagemValores = todasCartas.groupingBy { it.valor }.eachCount()

            val flush = todasCartas.groupBy { it.naipe }.any() { it.value.size >= 5 }
            val quadras = contagemValores.filter { it.value == 4 }
            val pares = contagemValores.filter { it.value == 2 } // AQUI ELE TA FILTRANDO OS VALORES QUE APARECEM COMO PARES
            val fullhouse = contagemValores.filter { it.value == 3 && pares.isNotEmpty() }
            val trincas = contagemValores.filter { it.value == 3 } // AQUI ELE TA FAZENDO A MESMA COISA SÓ QUE COM TRINCAS

            when {
                flush -> resultados[player] = "Flush de ${todasCartas.first().naipe}"
                fullhouse.isNotEmpty() -> resultados[player] = "Fullhouse de ${fullhouse.keys.first()} e ${pares.keys.first()}"
                quadras.isNotEmpty() -> resultados[player] = "Quadra de ${quadras.keys.first()}"
                trincas.isNotEmpty() -> resultados[player] = "Trinca de ${trincas.keys.first()}"
                pares.isNotEmpty() -> resultados[player] = "Par de ${pares.keys.first()}"
                else -> resultados[player] = "Nada"
            }

        }

        println("RESULTADOS:")
        for ((player, resultado) in resultados) {
            println("${player.nome}: $resultado - Mão: ${player.mao[0].valor} de ${player.mao[0].naipe} e ${player.mao[1].valor} de ${player.mao[1].naipe}")
        }

        val vencedor = when {

            resultados.values.any { it.startsWith("Flush") } -> {
                resultados.filterValues { it.startsWith("Flush") }.keys.first()
            }

            resultados.values.any { it.startsWith("Fullhouse") } -> {
                resultados.filterValues { it.startsWith("Fullhouse") }.keys.first()
            }

            resultados.values.any { it.startsWith("Quadra") } -> {
                resultados.filterValues { it.startsWith("Quadra") }.keys.first()
            }

            resultados.values.any { it.startsWith("Trinca") } -> {
                resultados.filterValues { it.startsWith("Trinca") }.keys.first()
            }

            resultados.values.any { it.startsWith("Par") } -> {
                resultados.filterValues { it.startsWith("Par") }.keys.first()
            }
            else -> null
        }

        if (vencedor != null) {
            println("VENCEDOR: ${vencedor.nome} - Mão: ${vencedor.mao[0].valor} de ${vencedor.mao[0].naipe} e ${vencedor.mao[1].valor} de ${vencedor.mao[1].naipe}")
            vencedor.saldo += pot.toDouble()
        } else {
            println("Empate. Pot dividido.")
            val div = pot / players.size
            players.forEach { it.saldo += div.toDouble() }
        }

        limparTudo()
    }

    fun dadosCartasEtapa(qntCartas: Int){
        if(qntCartas == 3){
            cartaMesa1 = Card.getCartaAleatoria()
            cartaMesa2 = Card.getCartaAleatoria()
            cartaMesa3 = Card.getCartaAleatoria()

            println("Cartas da mesa: ${cartaMesa1!!.valor} de ${cartaMesa1!!.naipe}, ${cartaMesa2!!.valor} de ${cartaMesa2!!.naipe}, ${cartaMesa3!!.valor} de ${cartaMesa3!!.naipe}")
        }
        if(qntCartas == 4) {
            cartaMesa4 = Card.getCartaAleatoria()
            println("Cartas da mesa: ${cartaMesa1!!.valor} de ${cartaMesa1!!.naipe}, ${cartaMesa2!!.valor} de ${cartaMesa2!!.naipe}, ${cartaMesa3!!.valor} de ${cartaMesa3!!.naipe}, ${cartaMesa4!!.valor} de ${cartaMesa4!!.naipe}")
        }
        if(qntCartas == 5){
            cartaMesa5 = Card.getCartaAleatoria()
            println("Cartas da mesa: ${cartaMesa1!!.valor} de ${cartaMesa1!!.naipe}, ${cartaMesa2!!.valor} de ${cartaMesa2!!.naipe}, ${cartaMesa3!!.valor} de ${cartaMesa3!!.naipe}, ${cartaMesa4!!.valor} de ${cartaMesa4!!.naipe}, ${cartaMesa5!!.valor} de ${cartaMesa5!!.naipe}")
        }
        println("POTE: ${pot}¢")
    }

    fun limparTudo(){
        etapa_atual = rodadas.PRE_FLOP
        pot = 0
        dealer = null
        checkVez = false
        qntsChecksRound = 0
        apostaMaiorDaRodada = 0
        apostasDaRodada.clear()
    }

    fun vezBot() {
        if (!checkVez) {
            println()
            println("-----------------------")
            print("BOT PENSANDO")

            for (i in 1..3) {
                Thread.sleep(500)
                print(".")
                System.out.flush()
            }

            Thread.sleep(500)
            print("\r")
            print(" ".repeat(20))
            print("\r")

            val apostaFeitaPeloBot = apostasDaRodada[players[1]] ?: 0;
            val numeroAleatorio: Int = numeroAleatorio()

            when (numeroAleatorio) {
                in 1..4 -> { // CHECK
                    if(players[1].canCheck(apostaMaiorDaRodada, apostaFeitaPeloBot)){
                        println("${players[1].nome} deu CHECK")
                        qntsChecksRound++
                        trocarCheck()
                        verificarAvancoRodada()
                        turno()
                    }else{
                        vezBot()
                    }
                }
                in 5..7 -> { // CALL
                    val diff = apostaMaiorDaRodada - apostaFeitaPeloBot
                    if(diff <= 0){
                        println("${players[1].nome} deu CHECK")
                        qntsChecksRound++
                        trocarCheck()
                        verificarAvancoRodada()
                        turno()
                    }

                    val valorCall = players[1].call(apostaMaiorDaRodada, apostaFeitaPeloBot)
                    pot += valorCall;
                    apostasDaRodada[players[1]] = apostaFeitaPeloBot + valorCall
                    println("${players[1].nome} deu CALL -> ${valorCall}¢")
                    println("POTE: ${pot}¢")
                    trocarCheck()
                    qntsChecksRound++
                    verificarAvancoRodada()
                    turno()
                }

                in 8..9 -> { // RAISE
                    var raiseValor: Int = apostaMaiorDaRodada * 2

                    if(raiseValor > players[1].saldo){
                        raiseValor = players[1].saldo.toInt()
                    }

                    if (raiseValor == null) { //VALOR INVALIDO DE RAISE
                        vezBot()
                    }

                    val totalAposta =
                        players[1].raise(apostaMaiorDaRodada, raiseValor, apostaFeitaPeloBot)
                    pot += totalAposta
                    apostaMaiorDaRodada += raiseValor
                    apostasDaRodada[players[1]] = (apostaFeitaPeloBot + totalAposta)
                    println("${players[1].nome} aumentou para ${apostaMaiorDaRodada}¢ com um RAISE de ${raiseValor}¢ (apostou ${totalAposta}¢ nesta rodada)")
                    println("POTE: ${pot}¢")
                    qntsChecksRound = 0;
                    trocarCheck()
                    turno()
                }

                10 -> { // FOLD
                    players[1].fold()
                    println("${players[1].nome} desistiu (FOLD)")
                    players[0].saldo += pot.toDouble();
                    limparTudo()
                    println("")
                    println("-----------------------")
                    println("ACABOU")
                    println("-----------------------")
                    println("")
                    inicio()
                }
            }

            trocarCheck()
            turno()
            println("-----------------------")
        }
    }

    fun numeroAleatorio(): Int{
        val numeroAleatorio = (1..10).random()
        return numeroAleatorio;
    }

    fun turno() {
        if (checkVez) {
            opcoesJogador()
        } else {
            vezBot()
        }
    }

    fun trocarCheck(){
        checkVez = !checkVez;
    }

    fun blinds(dealer: Player){
        println("")
        if(players[0] == dealer){
            println("----------- BLINDS ------------")

            players[0].saldo -= smallBlind
            players[1].saldo -= bigBlind

            println("Small Blind de ${players[0].nome} -> ${smallBlind}¢ || (total = ${players[0].saldo}¢)")
            println("Big Blind de ${players[1].nome} -> ${bigBlind}¢ || (total = ${players[1].saldo}¢)")

            pot = smallBlind + bigBlind
            println("POTE: ${pot}¢")

            apostasDaRodada[players[0]] = smallBlind
            apostasDaRodada[players[1]] = bigBlind
            apostaMaiorDaRodada = bigBlind

            println("-------------------------------")
        }else{
            println("-----------------------")

            players[1].saldo -= smallBlind
            players[0].saldo -= bigBlind

            println("Small Blind de ${players[1].nome} -> ${smallBlind}¢ || (total = ${players[0].saldo}¢)")
            println("Big Blind de ${players[0].nome} -> ${bigBlind}¢ || (total = ${players[1].saldo}¢)")

            pot = smallBlind + bigBlind
            println("POTE: ${pot}¢")

            apostasDaRodada[players[1]] = smallBlind
            apostasDaRodada[players[0]] = bigBlind
            apostaMaiorDaRodada = bigBlind

            println("-----------------------")
        }
        println("")
    }

    fun dealerInfo(){
        println()
        println("-----------------------")
        println("| O dealer é ${dealer!!.nome} |")
        println("-----------------------")
    }
}
