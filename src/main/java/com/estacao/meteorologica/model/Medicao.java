package com.estacao.meteorologica.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private Double temperatura;
    private Double umidade;
    private Double pressao;
    private Double luminosidade;     // agora guarda irradiância (W/m²)
    private Double chuva;
    private Double velocidadeVento;
    private Double direcaoVento;
    private Double umidadeSolo;

    // Campos calculados (não utilizados nesta versão, ficarão null)
    private Double eto;
    private Double etr;
    private Double armazenamento;
    private Double deficit;
    private Double excedente;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estacao_id", nullable = false)
    private Estacao estacao;

    public Medicao() {}

    public Medicao(Double temperatura, Double umidade, Double pressao,
                   Double luminosidade, Double chuva,
                   Double velocidadeVento, Double direcaoVento,
                   Double umidadeSolo, Estacao estacao) {
        this.timestamp = LocalDateTime.now();
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.pressao = pressao;
        this.luminosidade = luminosidade;
        this.chuva = chuva;
        this.velocidadeVento = velocidadeVento;
        this.direcaoVento = direcaoVento;
        this.umidadeSolo = umidadeSolo;
        this.estacao = estacao;
    }

    // Getters e Setters para todos os campos (gere com a IDE)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public Double getUmidade() { return umidade; }
    public void setUmidade(Double umidade) { this.umidade = umidade; }
    public Double getPressao() { return pressao; }
    public void setPressao(Double pressao) { this.pressao = pressao; }
    public Double getLuminosidade() { return luminosidade; }
    public void setLuminosidade(Double luminosidade) { this.luminosidade = luminosidade; }
    public Double getChuva() { return chuva; }
    public void setChuva(Double chuva) { this.chuva = chuva; }
    public Double getVelocidadeVento() { return velocidadeVento; }
    public void setVelocidadeVento(Double velocidadeVento) { this.velocidadeVento = velocidadeVento; }
    public Double getDirecaoVento() { return direcaoVento; }
    public void setDirecaoVento(Double direcaoVento) { this.direcaoVento = direcaoVento; }
    public Double getUmidadeSolo() { return umidadeSolo; }
    public void setUmidadeSolo(Double umidadeSolo) { this.umidadeSolo = umidadeSolo; }
    public Double getEto() { return eto; }
    public void setEto(Double eto) { this.eto = eto; }
    public Double getEtr() { return etr; }
    public void setEtr(Double etr) { this.etr = etr; }
    public Double getArmazenamento() { return armazenamento; }
    public void setArmazenamento(Double armazenamento) { this.armazenamento = armazenamento; }
    public Double getDeficit() { return deficit; }
    public void setDeficit(Double deficit) { this.deficit = deficit; }
    public Double getExcedente() { return excedente; }
    public void setExcedente(Double excedente) { this.excedente = excedente; }
    public Estacao getEstacao() { return estacao; }
    public void setEstacao(Estacao estacao) { this.estacao = estacao; }
}