package com.yanpegyn
import kotlinx.browser.localStorage
import kotlinx.datetime.Month

class Database {
    companion object {

        lateinit var cacheCards: MutableList<CardInvestimento>
        lateinit var cacheConfig: Config

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

        fun salvarConfigs(config: Config, chave: String) {
            cacheConfig = config
            val obj = "{\"taxa\":${config.taxa}, \"objetivo\":${config.objetivo}, \"meta\":${config.meta}, \"dataInicio\":\"${config.dataInicio}\"}"
            val jsonString = JSON.stringify(JSON.parse(obj))
            localStorage.setItem(chave, jsonString)
        }

        fun recuperarConfigs(chave: String): Config? {
            val jsonString = localStorage.getItem(chave) ?: return null
            val obj: dynamic = JSON.parse(jsonString)?: return null
            return Config(
                obj.taxa as Double, obj.objetivo as Double,
                obj.meta as Double, obj.dataInicio as String,
            )
        }
    }
}