package com.example.hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import lombok.Getter;

/**
 * Controlador de los componentes de una tarjeta en la plantilla Resultados.
 * Muestra los resultados, hoteles, de una búsqueda.
 * @see ResultadosContr
 */
@Getter
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

}