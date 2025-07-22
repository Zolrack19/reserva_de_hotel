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


@Setter
@Getter
@Entity(name = "medio_pago")
public class MedioPago {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @OneToMany(mappedBy = "medioPago", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Boleta> boletas;

  @Column(length = 100, nullable = false)
  private String nombre;

  public MedioPago() {}
  public MedioPago(String nombre) {
    this.nombre = nombre;
  }

  @Override
  public String toString() {
    return this.nombre;
  }

  @Override
  public boolean equals(Object obj) {
    MedioPago p = (MedioPago) obj;
    return p != null && id == p.getId();
  }
}
