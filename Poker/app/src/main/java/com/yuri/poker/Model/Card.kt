package com.yuri.poker.Model

enum class Naipe {
    OURO, PAUS, ESPADAS, COPAS
}

enum class ValorCarta(val simbolo: String) {
    A("A"), DOIS("2"), TRES("3"), QUATRO("4"), CINCO("5"),
    SEIS("6"), SETE("7"), OITO("8"), NOVE("9"), DEZ("10"),
    J("J"), Q("Q"), K("K")
}

data class Carta(val naipe: Naipe, val valor: ValorCarta){
    companion object{
        fun getCartaAleatoria(): Carta {
            val naipeAleatorio = Naipe.values().random()
            val valorAleatorio = ValorCarta.values().random()
            return Carta(naipeAleatorio, valorAleatorio)
        }
    }
}