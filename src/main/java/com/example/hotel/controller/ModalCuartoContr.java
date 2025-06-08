package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.util.Imagenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ModalCuartoContr implements Initializable {
  
  @FXML
  private HBox hboxImagenCuarto;
  
  @FXML
  private Button btnIzquierda;
  
  @FXML
  private Button btnDerecha;
  
  @FXML
  private GridPane grdImagenes;
  
  @FXML
  private Label lblTituloCuarto;
  
  @FXML
  private Label lblDescripcionCuarto;

  private Label[] labels;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    labels = new Label[15];
  }

  public void setData(Hotel hotel, Cuarto cuarto) {
    lblTituloCuarto.setText(cuarto.getNombre());
    lblDescripcionCuarto.setText(cuarto.getDescripcion());

    Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl(), cuarto.getImagenUrl());
    try {
      Object[] urls = Files.list(carpeta).filter(Files::isRegularFile).sorted().limit(15).map(path -> path.toUri().toString()).toArray();
      System.out.println("longi: " + urls.length);
      hboxImagenCuarto.setStyle(
        "-fx-background-image: url('" + urls[0] + "');" +
        "-fx-background-repeat: no-repeat;" +
        "-fx-background-position: center center;" +
        "-fx-background-size: cover;"
      );
      for (int i = 0; i < urls.length; i++) {
        if (labels[i] == null) {
          labels[i] = new Label();
          labels[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
          grdImagenes.add(labels[i], i%5, i/5);
        }
        labels[i].setStyle(
          "-fx-background-image: url('" + urls[i] + "');" +
          "-fx-background-repeat: no-repeat;" +
          "-fx-background-position: center center;" +
          "-fx-background-size: cover;"
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
