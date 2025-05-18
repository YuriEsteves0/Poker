package com.yuri.pokerlogic

open class Player(val nome: String, var saldo: Double, val mao: MutableList<Card> = mutableListOf(), var aindaNaMesa: Boolean) {
    companion object{
        fun receberCarta(player: Player, carta: Card) {
            player.mao.add(carta)
        }
    }

    open fun canCheck(betAtual: Int, contribuicaoAtual: Int): Boolean {
        return contribuicaoAtual == betAtual
    }

    open fun call(betAtual: Int, contribuicaoAtual: Int): Int {
        val diff = betAtual - contribuicaoAtual
        if (diff <= saldo) {
            saldo -= diff
            return diff
        } else {
            val allIn = saldo.toInt()
            saldo = 0.0
            return allIn
        }
    }

    open fun raise(betAtual: Int, valorAumento: Int, contribuicaoAtual: Int): Int {
        val novaAposta = betAtual + valorAumento
        val diff = novaAposta - contribuicaoAtual
        return if (diff <= saldo) {
            saldo -= diff
            diff
        } else {
            val allIn = saldo.toInt()
            saldo = 0.0
            allIn
        }
    }

    open fun fold(): Boolean {
        aindaNaMesa = false
        return true
    }
}