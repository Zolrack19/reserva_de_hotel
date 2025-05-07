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

@Getter
@Setter
@Entity
public class Ciudad {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Hotel> hoteles;

  @ManyToOne
  @JoinColumn(name = "pais_id")
  private Pais pais;

  // @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  // private List<Cliente> clientes;

  @Column(length = 200, nullable = false)
  private String nombre;

  @Column(name = "nombre_normalizado", length = 200, nullable =  false)
  private String nombreNormalizado;

  public Ciudad() {}
  public Ciudad(Pais pais, String nombre) {
    this.pais = pais;
    this.nombre = nombre;
  }
}
