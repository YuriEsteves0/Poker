package com.yuri.pokerlogic

import kotlin.system.exitProcess

fun main() {
    println("-----------------------")
    println("INICIANDO JOGO...")

    var nomeUsu: String = carregarInteracoes();
    var escolha: Int? = escolhaJogo();
    if(escolha == null){
        println("Escolha inválida")
        exitProcess(0);
    }else{
        if(escolha == 1){
            println("Ainda em desenvolvimento!")
        }else{

            println("-----------------------")

            // CONFIG PLAYER

            val player: Player = Player(nomeUsu, 200.0, aindaNaMesa = true)

            // CONFIG BOT

            val nomeBotAleatorio: String = nomesBot.values().random().toString()

            val bot: Bot = Bot(nomeBotAleatorio, 200.0, aindaNaMesa = true)

            // CONFIG GAME MANAGER

            val gameManager: GameManager = GameManager(mutableListOf(player, bot))
            gameManager.inicio()

            // TESTES
//            testesPlayers(player, bot)
//            testeCartas(player)
        }
    }
}

fun carregarInteracoes(): String{
    print("Digite seu nome: ")
    var nome = readLine();
    if(nome.isNullOrEmpty()){
        nome = "Anonimo"
    }
    return nome.trim().replaceFirstChar { it.uppercase() };
}

fun escolhaJogo(): Int?{
    println("Você deseja jogar Online ou Offline?")
    println("[1] - Online")
    println("[2] - Offline")
    var escolha = readLine()
    if(escolha.isNullOrEmpty()){
        println("Escolha inválida")
    }else{
        if(escolha == "1"){
            return 1;
        }
        if(escolha == "2"){
            return 2;
        }
        else{
            return null;
        }
    }
    return null;
}

fun testesPlayers(player: Player, bot: Bot){
    println("Player: ${player.nome}")
    println("Saldo: ${player.saldo}")
    for (carta in player.mao) {
        println("Carta: ${carta.valor} de ${carta.naipe}")
    }
    println("Ainda na mesa: ${player.aindaNaMesa}")

    // BOT
    println("-----------------------")

    println("Bot: ${bot.nome}")
    println("Saldo: ${bot.saldo}")
    for (carta in bot.mao) {
        println("Carta: ${carta.valor} de ${carta.naipe}")
    }
    println("Ainda na mesa: ${bot.aindaNaMesa}")
}

fun testeCartas(player: Player){
    println("------------- TESTE -------------")
    println("Player: ${player.nome}")
    println("Saldo: ${player.saldo}")
    for (carta in player.mao) {
        println("Carta: ${carta.valor} de ${carta.naipe}")
    }
}