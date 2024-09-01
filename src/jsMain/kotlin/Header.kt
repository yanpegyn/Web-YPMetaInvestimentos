package com.yanpegyn

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

class Header {
    companion object {
        fun make(): HTMLElement {
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
    }
}