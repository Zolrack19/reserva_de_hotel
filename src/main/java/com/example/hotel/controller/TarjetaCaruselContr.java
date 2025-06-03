package com.example.hotel.controller;

import java.io.IOException;

import com.example.hotel.App;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.util.Imagenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

/**
 * Controlador de los componentes de una tarjeta de carusel.
 * Muestra recomendaciones de hoteles al inicio de la aplicación.
 * @see InicioController 
*/
public class TarjetaCaruselContr {
  
  private static DetallesController dController;
  private Hotel hotel;
  
  @FXML
  private Label lblImagen;
  
  @FXML
  private Label lblTitulo;
  
  @FXML
  private Label lblPais;
  
  @FXML
  private Label lblEstrellas;
  

  @FXML
  private void verDetalles() throws IOException {
    if (App.detallesRoot == null) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/detalles-hotel.fxml"));
      App.detallesRoot = loader.load();
      dController = loader.getController();
    }
    TarjetaCaruselContr.dController.setData(hotel);
    App.navegar(App.detallesRoot);
  }

  /**
  * @version 1.0
  * Instancia los componentes gráficos de la plantilla.
  * @param hotel Entidad de un hotel.
  * @param imagenURL Url de la imagen a mostrar.
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
    lblPais.setText(hotel.getCiudad().getNombre() + ", " + hotel.getCiudad().getPais());
    lblEstrellas.setPrefWidth(23.5 * hotel.getEstrellas());
    lblEstrellas.setStyle(
      "-fx-background-image: url('" + Imagenes.ESTRELLA.getUrl() + "');" +
      "-fx-background-repeat: repeat-x;" +
      "-fx-background-position: left center;"
    );
  }

}