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
public class Hotel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Comentario> comentarios;

  @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Cuarto> cuartos;

  @ManyToOne
  @JoinColumn(name = "ciudad_id")
  private Ciudad ciudad;

  @ManyToOne
  @JoinColumn(name = "categoria_id")
  private Categoria categoria;
  
  @Column(name = "imagen_url")
  private String imagenUrl;
  
  @Column(name = "suma_votos")
  private int contador;

  @Column(name = "cantidad_votos")
  private int cantidadVotos;

  @Column(length = 150, nullable =  false)
  private String nombre;

  @Column(length = 3000, nullable = false)
  private String descripcion;

  public Hotel() {} 
  public Hotel(Ciudad ciudad, Categoria categoria, String imagenUrl,
    String nombre, String descripcion) {
    this.ciudad = ciudad;
    this.categoria = categoria;
    this.imagenUrl = imagenUrl;
    this.nombre = nombre;
    this.descripcion = descripcion;
  }
}
