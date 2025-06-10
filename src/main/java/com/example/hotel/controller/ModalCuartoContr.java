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
  private Label lblActual;
  private Object[] urls;
  private byte puntero;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    labels = new Label[15];
    
    btnIzquierda.focusedProperty().addListener((obs, oldVal, newVal) -> {
      hboxImagenCuarto.requestFocus();
    });
    btnDerecha.focusedProperty().addListener((obs, oldVal, newVal) -> {
      hboxImagenCuarto.requestFocus();
    });
  }

  public void setData(Hotel hotel, Cuarto cuarto) {
    puntero = 0;
    lblTituloCuarto.setText(cuarto.getNombre());
    lblDescripcionCuarto.setText(cuarto.getDescripcion());

    Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl(), cuarto.getImagenUrl());
    try {
      urls = Files.list(carpeta).filter(Files::isRegularFile).sorted().limit(15).map(path -> path.toUri().toString()).toArray();
      hboxImagenCuarto.setStyle(
        "-fx-background-image: url('" + urls[0] + "');" +
        "-fx-background-repeat: no-repeat;" +
        "-fx-background-position: center center;" +
        "-fx-background-size: cover;"
      );
      byte i = 0;
      for (; i < urls.length; i++) {
        if (labels[i] == null) {
          labels[i] = new Label();
          labels[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
          grdImagenes.add(labels[i], i%5, i/5);
          final byte p = i;
          labels[i].setOnMouseClicked(e -> {
            if (lblActual == labels[p]) return;
            lblActual.getStyleClass().remove("lbl-imagen-cuarto");
            puntero = p;
            lblActual = labels[p];
            lblActual.getStyleClass().add("lbl-imagen-cuarto");
            hboxImagenCuarto.setStyle(
              "-fx-background-image: url('" + urls[p] + "');" +
              "-fx-background-repeat: no-repeat;" +
              "-fx-background-position: center center;" +
              "-fx-background-size: cover;"
            );
          });
        }
        labels[i].setStyle(
          "-fx-background-image: url('" + urls[i] + "');" +
          "-fx-background-repeat: no-repeat;" +
          "-fx-background-position: center center;" +
          "-fx-background-size: cover;"
        );
      }
      while (labels[i] != null) {
        grdImagenes.getChildren().remove(labels[i]);
        labels[i] = null;
        i++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (lblActual != null) {
      lblActual.getStyleClass().remove("lbl-imagen-cuarto");
    }
    lblActual = labels[0];
    lblActual.getStyleClass().add("lbl-imagen-cuarto");
  }

  @FXML
  private void clickIzquierdo() {
    if (puntero == 0) return;
    lblActual.getStyleClass().remove("lbl-imagen-cuarto");
    lblActual = labels[--puntero];
    lblActual.getStyleClass().add("lbl-imagen-cuarto");
    hboxImagenCuarto.setStyle(
      "-fx-background-image: url('" + urls[puntero] + "');" +
      "-fx-background-repeat: no-repeat;" +
      "-fx-background-position: center center;" +
      "-fx-background-size: cover;"
    );
  }

  @FXML
  private void clickDerecho() {
    if (labels[puntero + 1] == null) return;
    lblActual.getStyleClass().remove("lbl-imagen-cuarto");
    lblActual = labels[++puntero];
    lblActual.getStyleClass().add("lbl-imagen-cuarto");
    hboxImagenCuarto.setStyle(
      "-fx-background-image: url('" + urls[puntero] + "');" +
      "-fx-background-repeat: no-repeat;" +
      "-fx-background-position: center center;" +
      "-fx-background-size: cover;"
    );
  }
}
