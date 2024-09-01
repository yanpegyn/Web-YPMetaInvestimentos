package com.yanpegyn

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.get

class Modal {
    companion object {
        fun makeModal(): HTMLElement {
            val modal = document.create.div(classes = "modal fade") {
                id = "staticBackdrop"
                attributes["data-bs-backdrop"] = "static"
                attributes["data-bs-keyboard"] = "false"
                attributes["tabindex"] = "-1"
                attributes["aria-labelledby"] = "staticBackdropLabel"
                attributes["aria-hidden"] = "true"
                div(classes = "modal-dialog modal-dialog-centered") {
                    div(classes = "modal-content bg-dark") {
                        div(classes = "modal-header") {
                            h1(classes = "modal-title fs-5") {
                                id = "staticBackdropLabel"
                                +"Qual o nome e o valor do seu investimento?" // Título do modal
                            }
                            button(type = ButtonType.button, classes = "btn-close") {
                                attributes["data-bs-dismiss"] = "modal"
                                attributes["aria-label"] = "Close"
                            }
                        }
                        div(classes = "modal-body") {
                            div(classes = "mb-3 d-flex align-items-center") {
                                input(classes = "form-control") {
                                    id = "input_nome"
                                    type = InputType.text
                                    placeholder = "Nome"
                                }
                            }
                            div(classes = "mb-3 d-flex align-items-center") {
                                input(classes = "form-control") {
                                    id = "input_montante"
                                    type = InputType.number
                                    placeholder = "Montante"
                                }
                            }
                        }
                        div(classes = "modal-footer") {
                            button(type = ButtonType.button, classes = "btn btn-secondary") {
                                attributes["data-bs-dismiss"] = "modal"
                                +"Cancelar"
                                onClickFunction = {
                                    val inputNome = document.getElementById("input_nome")!! as HTMLInputElement
                                    val inputMontante = document.getElementById("input_montante")!! as HTMLInputElement
                                    inputNome.value = ""
                                    inputMontante.value = ""
                                }
                            }
                            button(type = ButtonType.button, classes = "btn btn-primary") {
                                +"Adicionar"
                                onClickFunction = {
                                    val inputNome = document.getElementById("input_nome")!! as HTMLInputElement
                                    val inputMontante = document.getElementById("input_montante")!! as HTMLInputElement
                                    var validNome: Boolean
                                    var validMontante: Boolean
                                    val nome = inputNome.value.trim()

                                    var nomeDuplicado = false
                                    for (card in Database.cacheCards) {
                                        if (card.nome == nome) nomeDuplicado = true
                                    }
                                    if (nome.isNotEmpty() && !nomeDuplicado) {
                                        inputNome.classList.remove("is-invalid")
                                        validNome = true
                                    } else {
                                        inputNome.classList.add("is-invalid")
                                        validNome = false
                                    }
                                    var montante: Double? = null
                                    try {
                                        montante = inputMontante.value.trim().toDouble()
                                        inputMontante.classList.remove("is-invalid")
                                        validMontante = true
                                    } catch (_: NumberFormatException) {
                                        inputMontante.classList.add("is-invalid")
                                        validMontante = false
                                    }
                                    val modalElement = document.getElementById("staticBackdrop")
                                    val modal = modalElement?.let { window["bootstrap"]?.Modal.getOrCreateInstance(it) }
                                    if (validNome && validMontante) {
                                        inputNome.value = ""
                                        inputMontante.value = ""
                                        CardInvestimento.create(nome, montante?: 0.0)
                                        modal?.hide()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return modal
        }
    }
}