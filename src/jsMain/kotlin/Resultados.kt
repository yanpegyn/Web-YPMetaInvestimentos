package com.yanpegyn

import kotlinx.browser.document
import kotlinx.dom.clear
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.div
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement

class Resultados {
    companion object {
        var hasBeenCreated = false
        var tabela = listOf(
            "Montante Acumulado" to "-",
            "Tempo Investindo" to "-",
            "Taxa a.m. Considerada" to "-",
            "Capital Investido" to "-",
            "Média Inv. Mensal" to "-",
            "Rendimento Total" to "-",
            "Investir esse mês" to "-",
            "Investir próximo mês" to "-",
            "Meses adiantados" to "-",
            "Valor Excedente" to "-",
            "Status Média" to "-",
            "Seguindo o Plano" to "-",
            "Mantendo o Ritmo" to "-"
        )
        var depara = mapOf(
            "Montante Acumulado" to "MontanteAcumulado",
            "Tempo Investindo" to "TempoInvestindo",
            "Taxa a.m. Considerada" to "TaxaConsiderada",
            "Capital Investido" to "CapitalInvestido",
            "Média Inv. Mensal" to "MediaInvestimento",
            "Rendimento Total" to "Rendimento",
            "Investir esse mês" to "InvestimentoEsperadoMesAtual",
            "Investir próximo mês" to "InvestimentoEsperadoProximoMes",
            "Meses adiantados" to "MesesExcedentes",
            "Valor Excedente" to "ValorExcedenteRestante",
            "Status Média" to "StatusMediaInvestimento",
            "Seguindo o Plano" to "TempoSeguindoPlano",
            "Mantendo o Ritmo" to "TempoMantendoRitmo"
        )
        lateinit var container: HTMLDivElement
        fun make(): HTMLDivElement {
            if (hasBeenCreated) container.clear()
            else container = document.create.div(classes = "container-fluid p-0")
            if (Database.isCacheReady() && Database.cacheCards.size > 0) {
                val (ano, mes) = Database.cacheConfig.dataInicio.split("-")
                val investimento: MutableMap<String, Double> = mutableMapOf()
                Database.cacheCards.forEach {
                    investimento[it.nome] = it.montante
                }
                val resultados = Calculos.processar(
                    Database.cacheConfig.objetivo,
                    Database.cacheConfig.taxa/100,
                    investimento,
                    Database.cacheConfig.meta,
                    mes.toInt(), ano.toInt()
                )
                console.log(resultados)
                val novaTabela: MutableList<Pair<String, dynamic>> = mutableListOf()
                for (item in tabela) {
                    novaTabela.add(
                        item.first to resultados[depara[item.first]]
                    )
                }
                console.log(novaTabela)
                tabela = novaTabela
            }

            @Suppress("SENSELESS_COMPARISON", "kotlin:S6510")
            fun formataTextoResultado(index: Int): String {
                if (tabela[index].first == "Meses adiantados") {
                    if (tabela[index].second == null) return "-"
                    return "${tabela[index].second} e ${tabela[index+1].second}"
                } else return tabela[index].second
            }

            var blockColor = "bg-success"
            fun getClassesOfCell(index: Int): Set<String> {
                if ("Média Inv. Mensal" == tabela[index].first && tabela[tabela.lastIndex-2].second == "NOK") {
                    blockColor = "bg-danger"
                }
                return setOf("col-4", blockColor, "text-white", "d-flex", "align-items-center")
            }

            @Suppress("SENSELESS_COMPARISON")
            fun getClassesOfRow(index: Int): Set<String> {
                if (tabela[index].second == null) return setOf("row", "mb-1", "d-none")
                return setOf("row", "mb-1")
            }

            val containerTabela = document.create.div(classes = "container-fluid p-0 vh-70") {
                // Adiciona 9 linhas
                repeat(9) { index ->
                    div {
                        classes = getClassesOfRow(index)
                        // Caixa com background color customizável (30% da linha)
                        div {
                            classes = getClassesOfCell(index)
                            p(classes = "w-100 text-center mb-0") {
                                +tabela[index].first
                            }
                        }
                        // Texto centralizado nos 70% restantes
                        div(classes = "col-8 d-flex align-items-center") {
                            p(classes = "text-center w-100 m-0") {
                                +formataTextoResultado(index)
                            }
                        }
                    }
                }
            }
            val containerInferior = document.create.div(classes = "container-fluid p-0 vh-30") {
                hr()
                b {
                    p {
                        + "Seu objetivo, levará aproximadamente:"
                    }
                }
                p {
                    + "${tabela[tabela.lastIndex-1].first}: ${tabela[tabela.lastIndex-1].second}"
                }
                p {
                    + "${tabela[tabela.lastIndex].first}: ${tabela[tabela.lastIndex].second}"
                }
            }
            container.append(containerTabela, containerInferior)
            hasBeenCreated = true
            return container
        }

        fun open(res: Element) {
            if (!hasBeenCreated) res.append(make())
            else make()
        }
    }
}