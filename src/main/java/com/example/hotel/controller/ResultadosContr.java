package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.hotel.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ResultadosContr implements Initializable {

  @FXML
  private Button btnAtras;

  @FXML
  private VBox vboxResultados;

  @FXML
  private void volverInicio() throws IOException {
    App.navegar(App.inicioRoot);
    App.scene.setRoot(App.inicioRoot);
  }

  @FXML
  private Button btnBuscar;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      for (int i = 0; i < 5; i++) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/tarjeta-resultado.fxml"));
        Parent card = loader.load();
        TarjetaResultadoContr contr = loader.getController();
        contr.setData("Esto es un título",
            "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n" +
            "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
            "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
            "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
            "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
            "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
            "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.");
        vboxResultados.getChildren().add(card);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void buscar() throws IOException {
    if (App.detallesRoot == null) {
      App.detallesRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/detalles-hotel.fxml"));
    }
    App.navegar(App.detallesRoot);
  }

}