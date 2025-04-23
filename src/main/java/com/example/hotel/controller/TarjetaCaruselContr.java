package com.example.hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TarjetaCaruselContr {
  @FXML
  private Label lblImagen;
  
  @FXML
  private Label lblTitulo;
  
  @FXML
  private Label lblPais;
  
  @FXML
  private Label lblEstrellas;

  public void setData(String titulo, String pais) {
    String imagePath = "/com/example/imagenes/mara.jpg";
    lblImagen.setStyle(
    "-fx-background-image: url('" + imagePath + "');" +
    "-fx-background-repeat: no-repeat;" +
    "-fx-background-position: center center;" +
    "-fx-background-size: cover;"
    );
    lblTitulo.setText(titulo);
    lblPais.setText(pais);
  }
  


  
}
