package com.example.hotel.controller;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PantallaCarga extends StackPane {

  private static PantallaCarga obj;

  private PantallaCarga() {
    Rectangle fondo = new Rectangle();
    fondo.setFill(Color.rgb(0, 0, 0, 0.1));
    fondo.setHeight(500);

    ProgressIndicator indicador = new ProgressIndicator();
    indicador.setStyle("-fx-progress-color: #dcd0f0;");
    Label etiqueta = new Label("Por favor espere...");
    etiqueta.setStyle("-fx-text-fill: #e2d8f5; -fx-font-size: 13px;");

    VBox contenedor = new VBox(15, indicador, etiqueta);
    contenedor.setAlignment(Pos.CENTER);

    contenedor.setStyle(
      "-fx-background-color: rgba(199, 175, 255, 0.05);" + 
      "-fx-padding: 25;" +
      "-fx-background-radius: 12;"
    );

    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(fondo, contenedor);
    this.setVisible(false);
  }

  public void mostrar() {
    this.setVisible(true);
  }

  public void ocultar() {
    this.setVisible(false);
  }

  public static PantallaCarga getPantallaCarga() {
    if (obj == null) {
      obj = new PantallaCarga();
    }
    return obj;
  }

}
