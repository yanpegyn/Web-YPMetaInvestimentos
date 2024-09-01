package com.yanpegyn

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.onClickFunction
import org.w3c.dom.*

data class CardInvestimento(val nome: String, val montante: Double) {
    companion object {
        fun create(cardName: String, montante: Number, save: Boolean = true) {
            val home = document.getElementById("Home") ?: return
            val card = document.create.div(classes = "card bg-dark mt-2 mb-2") {
                div(classes = "card-header d-flex justify-content-between align-items-center") {
                    h5(classes = "mb-0") {
                        +cardName
                    }
                    button(classes = "btn btn-danger") {
                        +"Remover"
                        onClickFunction = { event ->
                            val button = event.currentTarget as HTMLButtonElement
                            var parent = button.parentElement
                            while (parent != null && !parent.classList.contains("card")) {
                                parent = parent.parentElement
                            }
                            val selfCard = parent!!
                            val nome = (selfCard.children[0]!!.children[0] as HTMLElement?)!!.innerText
                            val montante = (selfCard.children[1]!!.children[0]!!.children[1] as HTMLInputElement?)!!.value
                            deletarCard(nome, montante.toDouble())
                            (parent as? HTMLDivElement)?.remove()
                        }
                    }
                }
                div(classes = "card-body") {
                    div(classes = "mb-3 d-flex align-items-center") {
                        label(classes = "form-label me-2") {
                            +"Montante:" // Rótulo do campo
                        }
                        input(classes = "form-control") {
                            type = InputType.text
                            placeholder = "Digite o montante"
                            value = montante.toString()
                        }
                    }
                }
            }
            if (save) salvarCard(cardName, montante.toDouble())
            home.insertBefore(card, home.firstChild)
        }

        private fun salvarCard(nome: String, montante: Double) {
            val cards = Database.recuperarListaDeCards("listaDeCards") ?: mutableListOf()
            val novoCard = CardInvestimento(nome, montante)
            cards.add(novoCard)
            Database.salvarListaDeCards(cards, "listaDeCards")
        }

        private fun deletarCard(nome: String, montante: Double) {
            val cards = Database.recuperarListaDeCards("listaDeCards") ?: mutableListOf()
            val toDelete = CardInvestimento(nome, montante)
            cards.remove(toDelete)
            Database.salvarListaDeCards(cards, "listaDeCards")
        }
    }
}