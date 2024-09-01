package com.yanpegyn

import kotlinx.browser.document
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
    body.append(Header.make())
    body.append(TelaMain.make())
    TelaMain.popular()
}

val menuItems = listOf(
    "Home" to "/",
    "Configurar" to "/config"
)
var activeItem = 0

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