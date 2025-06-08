package com.example.hotel.dominio;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Reserva {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne(mappedBy = "reserva")
  private Boleta boleta;

  @ManyToOne
  @JoinColumn(name = "cliente_id")
  private Cliente cliente;

  @ManyToOne
  @JoinColumn(name = "cuarto_id")
  private Cuarto cuarto;

  @Column(name = "fecha_entrada", nullable = false)
  private LocalDate fechaEntrada;
  
  @Column(name = "fecha_salida", nullable = false)
  private LocalDate fechaSalida;

  @OneToMany(mappedBy = "reserva")
  private List<Acompanante> acompanantes;

  public Reserva() {}
  public Reserva(Boleta boleta, Cliente cliente, Cuarto cuarto, LocalDate fechaEntrada, LocalDate fechaSalida) {
    this.boleta = boleta;
    this.cliente = cliente;
    this.cuarto = cuarto;
    this.fechaEntrada = fechaEntrada;
    this.fechaSalida = fechaSalida;
  }
  public Reserva(long id, Boleta boleta, Cliente cliente, Cuarto cuarto, LocalDate fechaEntrada, LocalDate fechaSalida) {
    this.id = id;
    this.boleta = boleta;
    this.cliente = cliente;
    this.cuarto = cuarto;
    this.fechaEntrada = fechaEntrada;
    this.fechaSalida = fechaSalida;
  }
}
