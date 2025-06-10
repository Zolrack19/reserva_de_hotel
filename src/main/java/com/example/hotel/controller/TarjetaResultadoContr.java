package com.example.hotel.controller;

import java.io.IOException;

import com.example.hotel.App;
import com.example.hotel.auxiliar.ConfRepetitiva;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.util.Rutas;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

/**
 * Controlador de los componentes de una tarjeta en la plantilla Resultados.
 * Muestra los resultados, hoteles, de una búsqueda.
 * @see ResultadosContr
 */
public class TarjetaResultadoContr {

  @FXML
  private Parent root;

  @FXML
  private Label lblImagen;

  @FXML
  private Label lblTitulo;

  @FXML
  private Label lblDescripcion;

  @FXML
  private Label lblEstrellas;

  private Hotel hotel;

  /**
   * Instancia los componentes gráficos de la plantilla. Aún falta implementar la
   * url de imágen.
   * 
   * @param titulo      Título de la carta de carusel.
   * @param descripcion Descripción del hotel.
   * @version 1.0
   */
  public void setData(Hotel hotel, String imagenURL) {
    root.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          verDetalles();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    this.hotel = hotel;
    ConfRepetitiva.setBackground(lblImagen, imagenURL);
    lblTitulo.setText(hotel.getNombre());
    lblDescripcion.setText(hotel.getDescripcion());
    ConfRepetitiva.confEstrellas(lblEstrellas, hotel.getEstrellas());
  }

  @FXML
  private void verDetalles() throws IOException {
    if (App.getVista(Rutas.DETALLES_HOTEL) == null) {
      App.setVista(Rutas.DETALLES_HOTEL);
    }
    ((DetallesController) App.getControlador(Rutas.DETALLES_HOTEL)).setData(hotel);
    App.navegar(Rutas.DETALLES_HOTEL);
  }

}