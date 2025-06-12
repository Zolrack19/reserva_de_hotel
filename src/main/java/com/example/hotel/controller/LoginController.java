package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.hotel.App;
import com.example.hotel.service.ClienteServicio;
import com.example.hotel.singleton.Rutas;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
  Controlador de la plantilla de inicio de sesión.
  Verifica la cuenta del usuario.
*/
public class LoginController implements Initializable {

  @FXML
  private VBox conte;

  @FXML
  private Button primaryButton;

  @FXML
  private Label lblCrearCuenta;
  
  @FXML
  private Label lblWarning;

  @FXML
  private TextField txtEmail;
  
  @FXML
  private TextField txtContrasena;

  private final ClienteServicio loginServicio = ClienteServicio.getInstancia();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    lblCrearCuenta.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        cambiarACrearCuenta();
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
  
  // Se encarga de hacer validaciones con la data de la gui, a fin de obtener credenciales válidas
  @FXML
  private void iniciarSesion() throws IOException {
    String email = txtEmail.getText().trim();
    String contra = txtContrasena.getText().trim();
    boolean cortar = false;

    if (email.isEmpty()) {
      cortar = true;
      if (!txtEmail.getStyleClass().contains("txtInvalido")) {
        txtEmail.getStyleClass().add("txtInvalido");
      }
    } else if (txtEmail.getStyleClass().contains("txtInvalido")) {
      txtEmail.getStyleClass().removeLast();
    }
    
    if (contra.isEmpty()) {
      cortar = true;
      if (!txtContrasena.getStyleClass().contains("txtInvalido")) {
        txtContrasena.getStyleClass().add("txtInvalido");
      }
    } else if (txtContrasena.getStyleClass().contains("txtInvalido")) {
      txtContrasena.getStyleClass().removeLast();
    }

    if (cortar) return;

    if (loginServicio.signIn(email, contra)) {
      loginServicio.instanciarPais(App.cliente.getPais());
      if (App.getVista(Rutas.INICIO) == null) {
        App.setVista(Rutas.INICIO);
      }
      App.navegar(Rutas.INICIO);
      App.primaryStage.setMaximized(true);
      App.removeVista(Rutas.CREAR_CUENTA);
      App.removeVista(Rutas.LOGIN);
    } else {
      lblWarning.setText("Contraseña o email incorrectos");
    }
  }
  

  @FXML
  private void cambiarACrearCuenta() {
    if (App.getVista(Rutas.CREAR_CUENTA) == null) {
      App.setVista(Rutas.CREAR_CUENTA);
    }
    App.scene.setRoot(App.getVista(Rutas.CREAR_CUENTA));
    App.primaryStage.sizeToScene();
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 Login eliminado por GC\n\n");
  }
}
