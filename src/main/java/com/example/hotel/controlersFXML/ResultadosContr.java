package com.example.hotel.controlersFXML;

import java.io.IOException;

import com.example.hotel.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class ResultadosContr {
 
  @FXML
  private Button btnAtras;

  @FXML
  private void volverInicio() throws IOException {
    App.scene.setRoot(App.inicioRoot);
  }

  @FXML 
  private Button btnBuscar;

  @FXML
  public void buscar() throws IOException {
    if (App.detallesRoot == null) {
      App.detallesRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/detalles-hotel.fxml"));
    }
    App.scene.setRoot(App.detallesRoot);
  }
  
  
}