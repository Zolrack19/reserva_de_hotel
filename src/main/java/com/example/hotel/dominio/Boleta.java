package com.example.hotel.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Boleta implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "reserva_id")
  private Reserva reserva;

  @Column(length = 25)
  private String codigo;
  
  @ManyToOne
  @JoinColumn(name = "medio_pago_id")
  private MedioPago medioPago;
  
  @Column(precision = 10, scale = 2, nullable = false) //cambiar
  private BigDecimal montoEmitido;

  private LocalDateTime fechaPago; //con hora


  public Boleta() {}

  public Boleta(Reserva reserva, String codigo, MedioPago medioPago, BigDecimal montoEmitido, LocalDateTime fechaPago) {
    this.reserva = reserva;
    this.codigo = codigo;
    this.medioPago = medioPago;
    this.montoEmitido = montoEmitido;
    this.fechaPago = fechaPago;
  }
}
