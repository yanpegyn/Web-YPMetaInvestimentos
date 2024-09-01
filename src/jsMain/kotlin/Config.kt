package com.yanpegyn

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.org.w3c.dom.events.Event
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

data class Config(val taxa: Double, val objetivo: Double, val meta: Double, val dataInicio: String) {
    companion object {
        fun make(): HTMLElement {
            val configs = Database.recuperarConfigs("config-page")
            if (configs != null) Database.cacheConfig = configs
            val formulario = document.create.div(classes = "d-flex flex-column w-100"){
                div(classes = "mb-3 d-flex align-items-center") {
                    label(classes = "form-label me-2") {
                        +"% da Taxa (Mensal):"
                    }
                    input(classes = "form-control") {
                        id = "inputTaxa"
                        type = InputType.number
                        placeholder = "Ex. 0.8"
                        value = "${configs?.taxa}"
                    }
                }
                div(classes = "mb-3 d-flex align-items-center") {
                    label(classes = "form-label me-2") {
                        +"Objetivo Final (R$):"
                    }
                    input(classes = "form-control") {
                        id = "inputObjetivo"
                        type = InputType.number
                        placeholder = "Ex. 1000000"
                        value = "${configs?.objetivo}"
                    }
                }
                div(classes = "mb-3 d-flex align-items-center") {
                    label(classes = "form-label me-2") {
                        +"Investimento Mensal (R$):"
                    }
                    input(classes = "form-control") {
                        id = "inputMeta"
                        type = InputType.number
                        placeholder = "Ex. 5000"
                        value = "${configs?.meta}"
                    }
                }
                div(classes = "mb-3 d-flex align-items-center") {
                    label(classes = "form-label me-2") {
                        +"Data Início:"
                    }
                    input(classes = "form-control") {
                        id = "inputDataInicio"
                        type = InputType.month
                        value = "${configs?.dataInicio}"
                    }
                }
            }
            return formulario
        }

        fun salvar(event: Event) {
            val button = event.currentTarget as? HTMLElement ?: return
            button.classList.add("d-none")
            val inputTaxa = document.getElementById("inputTaxa")!! as HTMLInputElement
            val inputObjetivo = document.getElementById("inputObjetivo")!! as HTMLInputElement
            val inputMeta = document.getElementById("inputMeta")!! as HTMLInputElement
            val inputDataInicio = document.getElementById("inputDataInicio")!! as HTMLInputElement
            var validTaxa: Boolean
            var validObjetivo: Boolean
            var validMeta: Boolean
            var validDataInicio: Boolean

            var taxa: Double? = null
            try {
                taxa = inputTaxa.value.trim().toDouble()
                inputTaxa.classList.remove("is-invalid")
                validTaxa = true
            } catch (_: NumberFormatException) {
                inputTaxa.classList.add("is-invalid")
                validTaxa = false
            }

            var objetivo: Double? = null
            try {
                objetivo = inputObjetivo.value.trim().toDouble()
                inputObjetivo.classList.remove("is-invalid")
                validObjetivo = true
            } catch (_: NumberFormatException) {
                inputObjetivo.classList.add("is-invalid")
                validObjetivo = false
            }

            var meta: Double? = null
            try {
                meta = inputMeta.value.trim().toDouble()
                inputMeta.classList.remove("is-invalid")
                validMeta = true
            } catch (_: NumberFormatException) {
                inputMeta.classList.add("is-invalid")
                validMeta = false
            }

            var dataInicio = inputDataInicio.value.trim()
            if (dataInicio.isNotEmpty()) {
                inputDataInicio.classList.remove("is-invalid")
                validDataInicio = true
            } else {
                inputDataInicio.classList.add("is-invalid")
                validDataInicio = false
            }

            if (validTaxa && validObjetivo && validMeta && validDataInicio) {
                Database.salvarConfigs(
                    Config(taxa!!, objetivo!!, meta!!, dataInicio!!),
                    "config-page"
                )
                showBootstrapToast("Dados salvos com sucesso!")
            } else {
                showBootstrapToast("Dados inválidos!")
            }
            val toast = document.querySelector(".toast") as HTMLElement
            toast.addEventListener("hidden.bs.toast", {
                button.classList.remove("d-none")
            })
        }

        fun showBootstrapToast(message: String) {
            val toast = document.createElement("div") as HTMLElement
            toast.classList.add("toast", "align-items-center", "bg-dark", "text-white", "border-0", "w-100")
            toast.setAttribute("role", "alert")
            toast.setAttribute("aria-live", "assertive")
            toast.setAttribute("aria-atomic", "true")

            val toastBody: HTMLElement = document.create.div {
                classes = setOf(BootstrapConstants.DISPLAY_FLEX)
                div(classes = "toast-body") {
                    +message
                }
                button(classes = "btn-close btn-close-white me-2 m-auto") {
                    type = ButtonType.button
                    attributes["data-bs-dismiss"] = "toast"
                    attributes["aria-label"] = "Close"
                }
            }
            toast.appendChild(toastBody)
            document.body?.appendChild(toast)

            js("""
                var toastElement = new bootstrap.Toast(toast, { delay: 30000 });
                toastElement.show();
            """)
            toast.addEventListener("hidden.bs.toast", {
                document.body?.removeChild(toast)
            })
        }
    }
}