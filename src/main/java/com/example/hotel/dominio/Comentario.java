package com.example.hotel.dominio;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Comentario {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(length = 680, nullable = false)
  private String texto;
  
  @ManyToOne
  @JoinColumn(name = "cliente_id")
  private Cliente cliente;

  @ManyToOne
  @JoinColumn(name = "hotel_id")
  private Hotel hotel;

  private LocalDate fecha;

  public Comentario() {}
  public Comentario(String texto, Cliente cliente, Hotel hotel, LocalDate fecha) {
    this.texto = texto;
    this.cliente = cliente;
    this.hotel = hotel;
    this.fecha = fecha;
  }
  
}
