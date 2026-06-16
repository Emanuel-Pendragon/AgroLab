package com.estacao.meteorologica.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Estacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    private String localizacao;

    @JsonIgnore
    @OneToMany(mappedBy = "estacao", cascade = CascadeType.ALL)
    private List<Medicao> medicoes = new ArrayList<>();

    public Estacao() {}

    public Estacao(String nome, String localizacao) {
        this.nome = nome;
        this.localizacao = localizacao;
    }


    // Getters e Setters (completos)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public List<Medicao> getMedicoes() { return medicoes; }
    public void setMedicoes(List<Medicao> medicoes) { this.medicoes = medicoes; }

    // localização das estações
    private Double latitude;
    private Double longitude;

    // getters e setters
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

}

