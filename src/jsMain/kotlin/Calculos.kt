package com.yanpegyn

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.pow

class Calculos {
    companion object {
        private fun tempo_de_analise(anoInicio: Int, mesInicio: Int, anoAtual: Int, mesAtual: Int): Int {
            return ((anoAtual - anoInicio) * 12) + (mesAtual - mesInicio) + 1
        }

        private fun get_cap_investido(m: Double, i: Double, n: Int): Double {
            return m / (1 + i).pow(n)
        }

        private fun calcular_tempo_para_objetivo(
            capitalInicial: Double,
            aporteMensal: Double,
            taxaJurosMensal: Double,
            montanteDesejado: Double,
            skipAporte: Int
        ): Int {
            // Inicializa o contador de meses
            var meses = 1
            var ci = capitalInicial
            var sa = skipAporte
            // Inicia o loop para calcular o tempo necessário
            while (ci < montanteDesejado) {
                // Aplica os juros compostos ao capital inicial
                ci *= 1 + taxaJurosMensal
                // print(f"{meses} = {capital_inicial:.2f}")
                // Adiciona o aporte mensal
                if (sa == 0)
                    ci += aporteMensal
                else
                    sa -= 1
                // Incrementa o contador de meses
                meses += 1
            }
            return meses
        }

        fun processar(
            objetivo: Double,
            taxaRetornoEsperadoAoMes: Double,
            investimentos: MutableMap<String, Double>,
            metaInvestirMensal: Double,
            mesInicio: Int,
            anoInicio: Int
        ): MutableMap<String, Any> {
            val retorno = mutableMapOf<String, Any>()

            val montante = investimentos.values.sum()
            retorno["MontanteAcumulado"] = "R$${montante.asDynamic().toFixed(2)}"

            val dataAtual = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val tempoInvestindo =
                tempo_de_analise(anoInicio, mesInicio, dataAtual.year, dataAtual.monthNumber)
            retorno["TempoInvestindo"] = "$tempoInvestindo Meses"

            val capInvestido =
                get_cap_investido(montante, taxaRetornoEsperadoAoMes, tempoInvestindo)
            retorno["TaxaConsiderada"] = "${(taxaRetornoEsperadoAoMes * 100).asDynamic().toFixed(2)}% ao mês"
            retorno["TaxaConsiderada"] = "${(taxaRetornoEsperadoAoMes * 100).asDynamic().toFixed(2)}% ao mês"
            retorno["CapitalInvestido"] = "R$${capInvestido.asDynamic().toFixed(2)}"

            val mediaMensalInvestida = capInvestido / tempoInvestindo
            retorno["MediaInvestimento"] = "R$${mediaMensalInvestida.asDynamic().toFixed(2)}"
            retorno["StatusMediaInvestimento"] =
                if (mediaMensalInvestida < metaInvestirMensal) "NOK" else "OK"
            retorno["ObservacaoMediaInvestimento"] =
                "Se o valor for acima do real investido, a taxa esperada foi superada, incrementando seu investimento"

            val rendimento = montante - capInvestido
            retorno["Rendimento"] = "R$${rendimento.asDynamic().toFixed(2)}"

            var variandoMeta = capInvestido - (tempoInvestindo * metaInvestirMensal)
            if (variandoMeta < 0) variandoMeta += variandoMeta * taxaRetornoEsperadoAoMes
            var proxMesValor = metaInvestirMensal - variandoMeta
            proxMesValor = if (proxMesValor > 0) proxMesValor else 0.0
            if (tempoInvestindo > 0) {
                val variandoMetaMesAtual =
                    capInvestido - ((tempoInvestindo - 1) * metaInvestirMensal)
                var esseMesValor = metaInvestirMensal - variandoMetaMesAtual
                esseMesValor = if (esseMesValor > 0) esseMesValor else 0.0
                retorno["InvestimentoEsperadoMesAtual"] = "R$${esseMesValor.asDynamic().toFixed(2)}"
            }
            retorno["InvestimentoEsperadoProximoMes"] = "R$${proxMesValor.asDynamic().toFixed(2)}"

            var excessoMesesInvestido = (variandoMeta / metaInvestirMensal).toInt()
            excessoMesesInvestido = if (excessoMesesInvestido > 0) excessoMesesInvestido else 0
            val valorExcedenteRestante = variandoMeta % metaInvestirMensal
            if (proxMesValor == 0.0) {
                retorno["MesesExcedentes"] = "$excessoMesesInvestido Meses"
                retorno["ValorExcedenteRestante"] = "R$${valorExcedenteRestante.asDynamic().toFixed(2)}"
            }

            val tempoNecessarioPlano = calcular_tempo_para_objetivo(
                montante - valorExcedenteRestante,
                metaInvestirMensal,
                taxaRetornoEsperadoAoMes,
                objetivo,
                excessoMesesInvestido
            )
            retorno["TempoSeguindoPlano"] =
                "${(tempoNecessarioPlano / 12)} anos e ${tempoNecessarioPlano % 12} meses"

            val tempoNecessarioRitmo = calcular_tempo_para_objetivo(
                montante, mediaMensalInvestida, taxaRetornoEsperadoAoMes, objetivo, 0
            )
            retorno["TempoMantendoRitmo"] =
                "${(tempoNecessarioRitmo / 12)} anos e ${tempoNecessarioRitmo % 12} meses"

            return retorno
        }
    }
}