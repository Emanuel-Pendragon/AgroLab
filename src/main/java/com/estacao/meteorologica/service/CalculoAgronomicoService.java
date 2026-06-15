package com.estacao.meteorologica.service;

import com.estacao.meteorologica.model.Medicao;
import org.springframework.stereotype.Service;

@Service
public class CalculoAgronomicoService {

    public double calcularETo(double temperatura) {
        // Fórmula provisória (substituir pela FAO Penman-Monteith)
        return 0.0023 * (temperatura + 17.8) * Math.sqrt(temperatura) * 0.408;
    }

    public double calcularETR(double eto, double umidadeSolo, double capacidadeCampo) {
        double kc = 1.0; // coeficiente de cultura (genérico)
        double fatorEstresse = Math.min(1.0, umidadeSolo / capacidadeCampo);
        return eto * kc * fatorEstresse;
    }

    public double calcularArmazenamento(double chuva, double etr,
                                        double armazenamentoAnterior, double capacidadeCampo) {
        double novo = armazenamentoAnterior + chuva - etr;
        if (novo > capacidadeCampo) novo = capacidadeCampo;
        if (novo < 0) novo = 0;
        return novo;
    }

    public double calcularDeficit(double eto, double etr) {
        return Math.max(0, eto - etr);
    }

    public double calcularExcedente(double chuva, double etr,
                                    double armazenamentoAtual, double capacidadeCampo) {
        double disponivel = chuva - etr + armazenamentoAtual;
        return Math.max(0, disponivel - capacidadeCampo);
    }

    public void preencherCalculos(Medicao medicao, double armazenamentoAnterior, double capacidadeCampo) {
        double eto = calcularETo(medicao.getTemperatura());
        double etr = calcularETR(eto, medicao.getUmidadeSolo() != null ? medicao.getUmidadeSolo() : 0, capacidadeCampo);
        double armaz = calcularArmazenamento(
                medicao.getChuva() != null ? medicao.getChuva() : 0,
                etr, armazenamentoAnterior, capacidadeCampo);
        double deficit = calcularDeficit(eto, etr);
        double excedente = calcularExcedente(
                medicao.getChuva() != null ? medicao.getChuva() : 0,
                etr, armaz, capacidadeCampo);

        medicao.setEto(eto);
        medicao.setEtr(etr);
        medicao.setArmazenamento(armaz);
        medicao.setDeficit(deficit);
        medicao.setExcedente(excedente);
    }
}