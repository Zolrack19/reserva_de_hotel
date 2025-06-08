package com.example.hotel.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Acompanante {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @Column(length = 100)
  private String nombre;

  @Column(length = 100)
  private String apellidos;
  
  @ManyToOne
  @JoinColumn(name = "reserva_id")
  private Reserva reserva;
}
