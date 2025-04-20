package com.example.hotel.controlersFXML;

import java.io.IOException;

import com.example.hotel.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class InicioController {

  @FXML
  private Button btnBuscar;

  @FXML
  private void buscarHotel() throws IOException {
    if (App.resultadosRoot == null) {
      App.resultadosRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/resultados.fxml"));
    }
    App.scene.setRoot(App.resultadosRoot);
  }
}