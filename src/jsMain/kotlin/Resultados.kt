package com.yanpegyn

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.div
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement

class Resultados {
    companion object {
        var hasBeenCreated = false
        fun make(): HTMLDivElement {
            val prazoPlano = "8 anos e 2 meses"
            val prazoRitmo = "7 anos e 0 meses"
            val container = document.create.div(classes = "container-fluid p-0")
            val containerTabela = document.create.div(classes = "container-fluid p-0 vh-70") {
                // Adiciona 9 linhas
                repeat(9) { index ->
                    div(classes = "row mb-1") {
                        // Caixa com background color customizável (30% da linha)
                        div(classes = "col-4 bg-success text-white d-flex align-items-center") {
                            p(classes = "w-100 text-center mb-0") {
                                +"Texto da caixa ${index + 1}"
                            }
                        }
                        // Texto centralizado nos 70% restantes
                        div(classes = "col-8 d-flex align-items-center") {
                            p(classes = "text-center w-100 m-0") {
                                +"Texto centralizado na linha ${index + 1}"
                            }
                        }
                    }
                }
            }
            val containerInferior = document.create.div(classes = "container-fluid p-0 vh-30") {
                p {
                    + "Para atingir o seu objetivo, levará aproximadamente:"
                }
                p {
                    + "Seguindo o Plando: $prazoPlano"
                }
                p {
                    + "Mantendo o Ritmo: $prazoRitmo"
                }
            }
            container.append(containerTabela, containerInferior)
            hasBeenCreated = true
            return container
        }

        fun open(res: Element) {
            if (!hasBeenCreated) res.append(make())
        }
    }
}