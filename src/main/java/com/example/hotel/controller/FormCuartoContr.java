package com.example.hotel.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import com.example.hotel.App;
import com.example.hotel.auxiliar.ConfRepetitiva;
import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.dominio.MedioPago;
import com.example.hotel.service.CuartoServicio;
import com.example.hotel.singleton.Rutas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class FormCuartoContr implements Initializable {

  @FXML
  private Label lblTituloHotel;

  @FXML
  private Label lblEstrella;

  @FXML
  private Label lblImagen;

  @FXML
  private Label lblFechaEntrada;

  @FXML
  private Label lblFechaSalida;

  @FXML
  private Label lblNoches;

  @FXML
  private Label lblNumHabitacion;

  @FXML
  private Label lblEmail;

  @FXML
  private Label lblPais;

  @FXML
  private Label lblDivisa;

  @FXML
  private Label lblPrecio;

  @FXML
  private TextField txtNombre;

  @FXML
  private Label lblPrefijoTelefono;

  @FXML
  private TextField txtTelefono;
  
  @FXML
  private ComboBox<MedioPago> cbxMedioPago;
  
  @FXML
  private Button btnReserva;

  private Cuarto cuarto;
  private BigDecimal precioTotal;
  private LocalDate fechaInicio, fechaFin;
  private CuartoServicio cuartoServicio = new CuartoServicio();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    cbxMedioPago.getItems().addAll(cuartoServicio.getMedioPago());
    txtNombre.setEditable(false);
    txtTelefono.setEditable(false);
    btnReserva.setOnMouseClicked(e -> {
      reservar();
    });
    btnReserva.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        reservar();
      }
    });
  }

  public void reservar() {
    if (App.cliente.getSaldo().compareTo(precioTotal) < 0) {
      System.out.println("No se puede continuar con la transacción");
      return;
    }
    cuartoServicio.reservar(cuarto, fechaInicio, fechaFin, precioTotal);
    ((DetallesController) App.getControlador(Rutas.DETALLES_HOTEL)).nose();
  }



  public void rellenarData(Hotel hotel, Cuarto cuarto, String imagen, LocalDate fechaInicio, LocalDate fechaFin) {
    this.cuarto = cuarto;
    lblTituloHotel.setText(hotel.getNombre());
    lblEmail.setText(App.cliente.getEmail());
    lblPais.setText(App.cliente.getPais().getNombre());
    lblDivisa.setText(App.cliente.getPais().getDivisa().getNombre());
    lblPrefijoTelefono.setText(App.cliente.getPais().getPrefijoTelefonico());
    ConfRepetitiva.setBackground(lblImagen, imagen);
    ConfRepetitiva.confEstrellas(lblEstrella, hotel.getEstrellas());
    txtNombre.setText(App.cliente.getNombre() + " " + App.cliente.getApellido());
    txtTelefono.setText(App.cliente.getTelefono());
    lblFechaEntrada.setText(fechaInicio.toString());
    lblFechaSalida.setText(fechaFin.toString());

    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;

    long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    precioTotal = cuarto.getPrecioPorNoche().multiply(BigDecimal.valueOf(dias)).setScale(2, RoundingMode.HALF_UP);

    lblNoches.setText(dias + ((dias == 1) ? " noche" : " noches"));
    lblPrecio.setText(precioTotal.toString());
  }

}
