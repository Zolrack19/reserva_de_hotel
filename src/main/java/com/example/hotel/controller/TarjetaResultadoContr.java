package com.example.hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
  Controlador de los componentes de una tarjeta en la plantilla Resultados.
  Muestra los resultados, hoteles, de una búsqueda.
  @see ResultadosContr
*/
public class TarjetaResultadoContr {
  
  @FXML
  private Label lblImagen;
  
  @FXML
  private Label lblTitulo;
  
  @FXML
  private Label lblDescripcion;
  
  @FXML
  private Label lblEstrellas;

  /**
   Instancia los componentes gráficos de la plantilla. Aún falta implementar la url de imágen.
   @param titulo Título de la carta de carusel.
   @param descripcion Descripción del hotel.
   @version 1.0
  */
  public void setData(String titulo, String descripcion) {
    String imagePath = "/com/example/imagenes/mara.jpg";
    lblImagen.setStyle(
    "-fx-background-image: url('" + imagePath + "');" +
    "-fx-background-repeat: no-repeat;" +
    "-fx-background-position: center center;" +
    "-fx-background-size: cover;"
    );
    lblTitulo.setText(titulo);
    lblDescripcion.setText(descripcion);
  }


}