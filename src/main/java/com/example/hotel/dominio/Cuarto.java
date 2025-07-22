package com.example.hotel.dominio;

import java.math.BigDecimal;
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
public class Cuarto {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToMany(mappedBy = "cuarto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Reserva> reservas;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hotel_id")
  private Hotel hotel;

  @ManyToOne
  @JoinColumn(name = "categoria_cuarto_id")
  private CategoriaCuarto categoriaCuarto;
  
  @Column(name = "imagen_url")
  private String imagenUrl;

  @Column(nullable = false)
  private String nombre;

  private int numero;

  private short estrellas;

  @Column(length = 1000, nullable = false)
  private String descripcion;
  
  @Column(precision = 10, scale = 2, name = "precio_por_noche")
  private BigDecimal precioPorNoche;
  
  private byte capacidad;

  public Cuarto() {}
  public Cuarto(Hotel hotel, CategoriaCuarto categoriaCuarto, String nombre,
    short estrellas, String descripcion, BigDecimal precioPorNoche, byte capacidad) {
    this.hotel = hotel;
    this.categoriaCuarto = categoriaCuarto;
    this.nombre = nombre;
    this.estrellas = estrellas;
    this.descripcion = descripcion;
    this.precioPorNoche = precioPorNoche;
    this.capacidad = capacidad;
  }

  @Override
  public String toString() {
    return String.valueOf(this.getNumero());
  }
  
}
