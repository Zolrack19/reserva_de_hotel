package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.example.hotel.App;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.util.Imagenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;

/**
 * Controla la plantilla de los detalles de hotel, encargado de mostrar
 * información más específica y es la parte final para ir al formulario de
 * reserva de hotel.
 */
public class DetallesController implements Initializable {

  private Hotel hotel;

  @FXML
  private VBox vboxFotosContainer;

  @FXML
  private Label lblImagen1;

  @FXML
  private Label lblImagen2;

  @FXML
  private Label lblImagen3;

  @FXML
  private Label lblImagen4;

  @FXML
  private Label lblImagen5;

  @FXML
  private Label lblImagen6;

  @FXML
  private Label lblImagen7;
  
  @FXML
  private Label lblNombre;
  
  @FXML
  private Label lblEstrellas;
  
  @FXML
  private Label lblDescripcion;  
  
  @FXML
  private Label lblVerMenos;

  private Label[] labels;
  private byte estrella;
  private boolean lblOculto;
  

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    labels = new Label[] { lblImagen1, lblImagen2, lblImagen3, lblImagen4, lblImagen5, lblImagen6, lblImagen7 };
    lblVerMenos.setVisible(false);
    lblVerMenos.setManaged(false);
    lblOculto = true;

    lblVerMenos.setOnMouseClicked(e -> {
      lblDescripcion.setMinHeight(150);
      lblVerMenos.setVisible(false);
      lblVerMenos.setManaged(false);
      lblOculto = true;
    });

    lblDescripcion.setOnMouseClicked(e -> {
      if (lblOculto) {
        lblDescripcion.setMinHeight(Label.USE_PREF_SIZE);
        lblVerMenos.setVisible(true);
        lblVerMenos.setManaged(true);
        lblOculto = false;
      }
    });
  }

  public void setData(Hotel hotel) {
    if (this.hotel == null || this.hotel != hotel) {
      this.hotel = hotel;
      prepararPlantilla();
    }
  }

  private void prepararPlantilla() {
    lblNombre.setText(hotel.getNombre());
    if (estrella != hotel.getEstrellas()) {
      estrella = (byte) hotel.getEstrellas();
      lblEstrellas.setPrefWidth(23.5 * estrella);
      lblEstrellas.setStyle(
        "-fx-background-image: url('" + Imagenes.ESTRELLA.getUrl() + "');" +
        "-fx-background-repeat: repeat-x;" +
        "-fx-background-position: left center;"
      );
    }
    lblDescripcion.setText(hotel.getDescripcion());
    Path carpeta = Paths.get(hotel.getImagenUrl());
    try {
      Object[] urls = Files.list(carpeta).sorted().limit(7).map(path -> path.toUri().toString()).toArray();
      for (int i = 0; i < urls.length; i++) {
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

  @FXML
  public void volver() throws IOException {
    if (App.resultadosRoot == null) {
      App.resultadosRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/resultados.fxml"));
    }
    App.navegar(App.resultadosRoot);
  }

}
