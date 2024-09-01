package com.yanpegyn

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.script
import org.w3c.dom.*

//https://github.com/allangomes/kotlinwind.css

fun main() {
    console.log("Hello, Kotlin/JS!")
    val body = document.body ?: error("'body' not found")
    body.classList.add(
        BootstrapConstants.DISPLAY_FLEX,
        "flex-column",
        "vh-100"
    )
    body.style.backgroundColor = "#1c1c1c"
    body.style.color = "#FFFFFF"
    body.append(makeHeader())
    body.append(makeMain())
}

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
                            val validNome: Boolean
                            var validMontante: Boolean
                            val nome = inputNome.value.trim()
                            if (nome.isNotEmpty()) {
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
                            console.log(validNome)
                            console.log(validMontante)
                            val modalElement = document.getElementById("staticBackdrop")
                            val modal = modalElement?.let { window["bootstrap"]?.Modal.getOrCreateInstance(it) }
                            if (validNome && validMontante) {
                                inputNome.value = ""
                                inputMontante.value = ""
                                createCardInvestimento(nome, montante?: 0.0)
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

fun createCardInvestimento(cardName: String, montante: Number) {
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
    home.insertBefore(card, home.firstChild)
}

fun makeMain(): HTMLElement {
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
    main.append(makeModal())
    return main
}

val menuItems = listOf(
    "Home" to "/",
    "Configurar" to "/config"
)
var activeItem = 0

fun makeHeader(): HTMLElement {
    val headerImg = "mipmap-xhdpi/ic_launcher.png"
    val headerImgAlt = "Icone do contendo YP com fundo laranja"
    val nav = document.create.nav {
        classes = setOf("navbar", "navbar-expand-lg", "bg-body-tertiary")
        style = "box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);"
        div {
            classes = setOf("container-fluid")
            span {
                classes = setOf(
                    BootstrapConstants.DISPLAY_FLEX,
                    "align-items-center",
                    "mb-md-0",
                    "me-md-auto",
                    "link-light",
                    "text-decoration-none"
                )
                img {
                    alt = headerImgAlt
                    src = headerImg
                    classes = setOf("bi", "me-2")
                    height = "44px"
                    width = "44px"
                }
            }
            ul {
                classes = setOf("nav", "nav-pills")
                menuItems.forEach { (text, link) ->
                    li {
                        classes = setOf("nav-item")
                        a(href = link) {
                            onClickFunction = {
                                it.preventDefault()
                                mudaTela(text)
                            }
                            +text
                            classes =
                                setOfNotNull("nav-link", "active".takeIf { text == menuItems[activeItem].first })
                        }
                    }
                }
            }
        }
    }
    return nav
}

fun mudaTela(text: String) {
    if (text == menuItems[activeItem].first) return
    val navLinks: HTMLCollection = document.getElementsByClassName("nav-link")
    if (navLinks.length == 0) return
    val conteudoFechar = document.getElementById(menuItems[activeItem].first) ?: return
    for (i in 0..navLinks.length) {
        val atual = navLinks[i] ?: continue
        if (atual.classList.contains("active")) {
            atual.classList.remove("active")
            conteudoFechar.classList.add("d-none")
        }
    }
    if (activeItem + 1 < menuItems.size) {
        activeItem += 1

    } else {
        activeItem = 0
    }
    val itemAtivo = navLinks[activeItem] ?: return
    itemAtivo.classList.add("active")
    val conteudoAbrir = document.getElementById(text) ?: return
    conteudoAbrir.classList.remove("d-none")
    conteudoAbrir.classList.add("d-flex")
}