package com.example.hotel.dominio;
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

  @Column(name = "numero_long_min")
  private short minNumLongitud; 
  
  @Column(name = "numero_long_max")
  private short maxNumLongitud;

  @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Ciudad> ciudads;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "divisa_id")
  private Divisa divisa;

  public Pais() {}
  public Pais(String nombre, String prefijoTelefonico, short minNumLongitud, short maxNumLongitud, Divisa divisa) {
    this.nombre = nombre;
    this.prefijoTelefonico = prefijoTelefonico;
    this.minNumLongitud = minNumLongitud;
    this.maxNumLongitud = maxNumLongitud;
    this.divisa = divisa;
  }

  @Override
  public String toString() {
    return this.nombre;
  }
}
