package com.yanpegyn
import kotlinx.browser.localStorage
import kotlinx.html.OBJECT

class Database {
    companion object {

        fun salvarListaDeCards(cards: List<CardInvestimento>, chave: String) {
            var li = "["
            var first = true
            cards.forEach {
                if(!first) {
                    li += ","
                } else first = false
                li+="{\"nome\": \"${it.nome}\", \"montante\":${it.montante}}"
            }
            li += "]"
            val jsonString = JSON.stringify(JSON.parse(li))
            localStorage.setItem(chave, jsonString)
        }

        fun recuperarListaDeCards(chave: String): MutableList<CardInvestimento>? {
            val jsonString = localStorage.getItem(chave) ?: return null
            val arraySalvo: Array<dynamic> = JSON.parse(jsonString)?: arrayOf()
            val arrayMapeado = arraySalvo.map {
                CardInvestimento(it.nome as String, it.montante as Double)
            }
            val mutable = arrayMapeado.toMutableList()
            return mutable
        }
    }
}