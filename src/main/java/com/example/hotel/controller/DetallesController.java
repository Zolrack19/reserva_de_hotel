package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import com.example.hotel.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class DetallesController implements Initializable {
  
  @FXML
  private Button btnAtras;

  @FXML
  private VBox vboxFotosContainer;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Set<Node> nodos = vboxFotosContainer.lookupAll(".lblSelector");
    String imagePath = "/com/example/imagenes/mara.jpg";
    for (Node node : nodos) {
      node.setStyle(
        "-fx-background-image: url('" + imagePath + "');" +
        "-fx-background-repeat: no-repeat;" +
        "-fx-background-position: center center;" +
        "-fx-background-size: cover;"
      );
    }
  }

  @FXML
  public void volver() throws IOException {
    App.navegar(App.resultadosRoot);
  }

}
