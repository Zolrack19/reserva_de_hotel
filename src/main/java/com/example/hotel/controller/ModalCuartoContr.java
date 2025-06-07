package com.example.hotel.controller;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ModalCuartoContr implements Initializable {
  
  @FXML
  private Label hboxImagenCuarto;
  
  @FXML
  private Label btnIzquierda;
  
  @FXML
  private Label btnDerecha;
  
  @FXML
  private GridPane grdImagenes;
  
  @FXML
  private Label lblTituloCuarto;
  
  @FXML
  private Label lblDescripcionCuarto;

  private Label[] lables;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lables = new Label[15];
  }

  public void setData() {
    
  }
}
