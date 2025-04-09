package com.example.Dominio;

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
@Entity(name = "categoria_cuarto")
public class CategoriaCuarto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @OneToMany(mappedBy = "categoriaCuarto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Cuarto> cuartos;

  @Column(length = 100, nullable = false)
  private String nombre;

  
  public CategoriaCuarto() {}
  public CategoriaCuarto(String nombre) {
    this.nombre = nombre;
  }
}
