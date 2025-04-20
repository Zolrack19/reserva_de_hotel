package com.example.hotel.controlersFXML;

import java.io.IOException;

import com.example.hotel.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DetallesController {
  
  @FXML
  private Button btnAtras;

  @FXML
  public void volver() throws IOException {
    App.scene.setRoot(App.resultadosRoot);
  }

}
