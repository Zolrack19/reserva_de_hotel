package com.example.hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import lombok.Getter;

/**
 * Controlador de los componentes de una tarjeta de carusel.
 * Muestra recomendaciones de hoteles al inicio de la aplicación.
 * @see InicioController 
*/
@Getter
public class TarjetaCaruselContr {
  
  @FXML
  private Parent root;

  @FXML
  private Label lblImagen;
  
  @FXML
  private Label lblTitulo;
  
  @FXML
  private Label lblPais;
  
  @FXML
  private Label lblEstrellas;
  
}