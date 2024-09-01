package com.yanpegyn

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.create
import org.w3c.dom.HTMLElement

class TelaMain {
    companion object {
        fun make(): HTMLElement {
            val main = document.create.main {
                classes = setOf("container-fluid", "flex-fill", "pt-2", "pb-90px")
                div {
                    id = "Home"
                    classes = setOf("content")
                }
                div {
                    id = "Configurar"
                    classes = setOf("content", "d-none")
                    +"ipsum"
                }
                div {
                    id = "Resultado"
                    classes = setOf("content", "d-none")
                    +"dolot"
                }
                button(classes = "floating-btn btn btn-success btn-calculate") {
                    +"Calcular"
                    type = ButtonType.button
                }
                button(classes = "floating-btn btn btn-primary btn-plus") {
                    type = ButtonType.button
                    title = "Botão Adicionar"
                    attributes["data-bs-toggle"] = "modal"
                    attributes["data-bs-target"] = "#staticBackdrop"
                }
            }
            main.append(Modal.makeModal())
            return main
        }

        fun popular() {
            val cards = Database.recuperarListaDeCards("listaDeCards") ?: mutableListOf()
            for(card in cards) {
                CardInvestimento.create(card.nome, card.montante, save=false)
            }
        }
    }
}