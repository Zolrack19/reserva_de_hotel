package com.example.hotel.controlersFXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.hotel.App;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LoginController implements Initializable {

  @FXML
  private VBox conte;

  @FXML
  private Button primaryButton;

  @FXML
  private Label lblCrearCuenta;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    lblCrearCuenta.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case SPACE, ENTER -> {
          try {
            cambiarACrearCuenta();
          } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
          }
        }
        default -> {}
      }
    });


    InnerShadow innerShadow = new InnerShadow(0, Color.rgb(209, 58, 255));
    primaryButton.setEffect(innerShadow);

    primaryButton.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        Timeline animacion = new Timeline(
          new KeyFrame(Duration.millis(200),
            new KeyValue(innerShadow.radiusProperty(), 30),
            new KeyValue(innerShadow.offsetXProperty(), 3),
            new KeyValue(innerShadow.offsetYProperty(), 3)
          )
        );
        animacion.play();
      } else {
        Timeline animacion = new Timeline(
          new KeyFrame(Duration.millis(200), 
            new KeyValue(innerShadow.radiusProperty(), 0),
            new KeyValue(innerShadow.offsetXProperty(), 0),
            new KeyValue(innerShadow.offsetYProperty(), 0)
          )
        );
        animacion.play();
      }
    });

    primaryButton.setOnMouseEntered(e -> {
      Timeline animacion = new Timeline(
        new KeyFrame(Duration.millis(200),
          new KeyValue(innerShadow.radiusProperty(), 30),
          new KeyValue(innerShadow.offsetXProperty(), 3),
          new KeyValue(innerShadow.offsetYProperty(), 3)
        )
      );
      animacion.play();
    });

    primaryButton.setOnMouseExited(e -> {
      Timeline animacion = new Timeline(
        new KeyFrame(Duration.millis(200), 
          new KeyValue(innerShadow.radiusProperty(), 0),
          new KeyValue(innerShadow.offsetXProperty(), 0),
          new KeyValue(innerShadow.offsetYProperty(), 0)
        )
      );
      animacion.play();
    });
  }
  
  @FXML
  private void cambiarInicio() throws IOException {
    if (App.inicioRoot == null) {
      App.inicioRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/inicio.fxml"));
    }
    App.scene.setRoot(App.inicioRoot);
    App.primaryStage.setMaximized(true);
    App.inicioRoot = null;
    App.crearCuentaRoot = null;
  }
  

  @FXML
  private void cambiarACrearCuenta() throws IOException {
    if (App.crearCuentaRoot == null) {
      App.crearCuentaRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/crear-cuenta.fxml"));
    }
    App.scene.setRoot(App.crearCuentaRoot);
    App.primaryStage.sizeToScene();
  }
}
