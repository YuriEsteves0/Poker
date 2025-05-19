package com.yuri.pokerlogic

enum class naipe(){
    COPAS,
    ESPADAS,
    OUROS,
    PAUS
}

enum class valor(){
    AS,
    DOIS,
    TRES,
    QUATRO,
    CINCO,
    SEIS,
    SETE,
    OITO,
    NOVE,
    DEZ,
    VALETE,
    DAMA,
    REI
}

class Card (val naipe: naipe, val valor: valor){

    companion object {
        fun getCartaAleatoria(): Card {
                val naipeAleatorio = valor.values().random()  // Seleciona aleatoriamente o naipe
                val valorAleatorio = naipe.values().random()  // Seleciona aleatoriamente o valor
                return Card(valorAleatorio, naipeAleatorio)   // Retorna a carta com naipe e valor aleatório
        }

        fun simularCard(naipe: naipe, valor: valor): Card {
            return Card(naipe, valor)
        }
    }
}
