package com.example.hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ComentarioContr {
  
  @FXML
  private Label lblNombreComentario;
  
  @FXML
  private Label lblContenidoComentario;

  public void init(String nombre, String contenido) {
    this.lblNombreComentario.setText(nombre);
    this.lblContenidoComentario.setText(contenido);
  }

}
