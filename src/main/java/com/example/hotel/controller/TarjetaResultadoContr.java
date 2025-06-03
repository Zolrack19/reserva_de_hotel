package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.persistence.criteria.Root;

import com.example.hotel.App;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.util.Imagenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

/**
 * Controlador de los componentes de una tarjeta en la plantilla Resultados.
 * Muestra los resultados, hoteles, de una búsqueda.
 * 
 * @see ResultadosContr
 */
public class TarjetaResultadoContr implements Initializable {

  @FXML
  private Parent root;

  @FXML
  private Label lblImagen;

  @FXML
  private Label lblTitulo;

  @FXML
  private Label lblDescripcion;

  @FXML
  private Label lblEstrellas;

  private static DetallesController dController;
  private Hotel hotel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    root.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          verDetalles();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
  }

  /**
   * Instancia los componentes gráficos de la plantilla. Aún falta implementar la
   * url de imágen.
   * 
   * @param titulo      Título de la carta de carusel.
   * @param descripcion Descripción del hotel.
   * @version 1.0
   */
  public void setData(Hotel hotel, String imagenURL) {
    this.hotel = hotel;
    lblImagen.setStyle(
      "-fx-background-image: url('" + imagenURL + "');" +
      "-fx-background-repeat: no-repeat;" +
      "-fx-background-position: center center;" +
      "-fx-background-size: cover;"
    );
    lblTitulo.setText(hotel.getNombre());
    lblDescripcion.setText(hotel.getDescripcion());
    lblEstrellas.setPrefWidth(23.5 * hotel.getEstrellas());
    lblEstrellas.setStyle(
      "-fx-background-image: url('" + Imagenes.ESTRELLA.getUrl() + "');" +
      "-fx-background-repeat: repeat-x;" +
      "-fx-background-position: left center;"
    );
  }

  @FXML
  private void verDetalles() throws IOException {
    if (App.detallesRoot == null) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/detalles-hotel.fxml"));
      App.detallesRoot = loader.load();
      dController = loader.getController();
    }
    TarjetaResultadoContr.dController.setData(hotel);
    App.navegar(App.detallesRoot);
  }
}