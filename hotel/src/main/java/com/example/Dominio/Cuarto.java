package com.example.Dominio;

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

  @ManyToOne()
  @JoinColumn(name = "hotel_id")
  private Hotel hotel;

  @ManyToOne
  @JoinColumn(name = "categoria_cuarto_id")
  private CategoriaCuarto categoriaCuarto;
  
  @Column(name = "imagen_url")
  private String imagenUrl;

  @Column(length = 25)
  private String numero;

  private byte estrella;

  @Column(length = 700)
  private String descripcion;
  
  @Column(precision = 10, scale = 2)
  private BigDecimal precioPorNoche;
  
  private byte capacidad;

  public Cuarto() {}
  public Cuarto(Hotel hotel, CategoriaCuarto categoriaCuarto, String imagenUrl, String numero,
      byte estrella, String descripcion, BigDecimal precioPorNoche, byte capacidad) {
    this.hotel = hotel;
    this.categoriaCuarto = categoriaCuarto;
    this.imagenUrl = imagenUrl;
    this.numero = numero;
    this.estrella = estrella;
    this.descripcion = descripcion;
    this.precioPorNoche = precioPorNoche;
    this.capacidad = capacidad;
  }
  
}
