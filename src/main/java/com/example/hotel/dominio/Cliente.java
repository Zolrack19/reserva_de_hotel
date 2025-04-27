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

@Entity
@Getter
@Setter
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne()
	@JoinColumn(name = "pais_id")
	private Pais pais;

	// @ManyToOne
	// @JoinColumn(name = "ciudad_id")
	// private Ciudad ciudad;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Comentario> comentarios;


	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Reserva> reservas;

	@Column(length = 200, nullable = false)
	private String email;

	@Column(length = 50, nullable = false)
	private String contrasena;

	@Column(length = 150, nullable = false)
	private String nombre;

	@Column(length = 150, nullable = false)
	private String apellido;

	@Column(precision = 10, scale = 2)
	private BigDecimal saldo;

	@Column(length = 25)
	private String telefono;


	public Cliente() {}
	public Cliente(Pais pais, String email, String contrasena,
	String nombre, String apellido, BigDecimal saldo, String telefono) {
		this.pais = pais;
		this.email = email;
		this.contrasena = contrasena;
		this.nombre = nombre;
		this.apellido = apellido;
		this.saldo = saldo;
		this.telefono = telefono;
}

}