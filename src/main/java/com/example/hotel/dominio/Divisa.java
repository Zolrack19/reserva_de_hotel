package com.example.hotel.dominio;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Divisa {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @OneToMany(mappedBy = "divisa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Pais> paises;

  @Column(length = 100)
  private String nombre;

  @Column(length = 10)
  private String simbolo;

  public Divisa() {}
  public Divisa(String nombre, String simbolo) {
    this.nombre = nombre;
    this.simbolo = simbolo;
  }
}
