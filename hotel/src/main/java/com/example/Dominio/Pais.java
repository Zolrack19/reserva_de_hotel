package com.example.Dominio;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pais {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @Column(length = 150, nullable = false)
  private String nombre;

  @Column(length = 10, nullable = false)
  private String prefijoTelefonico;

  @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Ciudad> ciudads;

  @ManyToOne
  @JoinColumn(name = "divisa_id")
  private Divisa divisa;

  public Pais() {}
  public Pais(String nombre, String prefijoTelefonico, Divisa divisa) {
    this.nombre = nombre;
    this.prefijoTelefonico = prefijoTelefonico;
    this.divisa = divisa;
  }
}
