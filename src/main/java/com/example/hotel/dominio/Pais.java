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

  @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Cliente> clientes;

  @Column(length = 150, nullable = false)
  private String nombre;

  @Column(name = "nombre_normalizado", length = 150, nullable =  false)
  private String nombreNormalizado;

  @Column(length = 10, nullable = false)
  private String prefijoTelefonico;

  @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Ciudad> ciudades;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "divisa_id")
  private Divisa divisa;

  @Column(name = "codigo_iso", length = 5, nullable = false)
  private String codigoISO;

  public Pais() {}
  public Pais(String nombre, String prefijoTelefonico, Divisa divisa) {
    this.nombre = nombre;
    this.prefijoTelefonico = prefijoTelefonico;
    this.divisa = divisa;
  }

  @Override
  public String toString() {
    return this.nombre;
  }
}
