package com.yuri.pokerlogic

class Bot(nome: String, saldo: Double, mao: MutableList<Card> = mutableListOf(), aindaNaMesa: Boolean) : Player(nome, saldo, mao, aindaNaMesa){
    companion object{
        fun receberCarta(bot: Bot, carta: Card) {
            bot.mao.add(carta)
        }
    }

    override fun canCheck(betAtual: Int, contribuicaoAtual: Int): Boolean {
        return contribuicaoAtual == betAtual
    }

    override fun call(betAtual: Int, contribuicaoAtual: Int): Int {
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

    override fun raise(betAtual: Int, valorAumento: Int, contribuicaoAtual: Int): Int {
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


    override fun fold(): Boolean {
        aindaNaMesa = false
        return true
    }
}

enum class nomesBot{
    BOT_GABRIEL,
    BOT_PEDRO,
    BOT_ANA,
    BOT_MARIA,
    BOT_LUCAS,
    BOT_BEATRIZ,
    BOT_CARLOS
}