package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import com.example.hotel.App;
import com.example.hotel.dominio.Pais;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class CrearCuentaContr implements Initializable {

  @FXML
  private Label lblCambiarLogin;

  @FXML
  private ComboBox<Pais> cbxPais;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    lblCambiarLogin.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ENTER, SPACE -> cambiarALogin();
        default -> {}
      }
    });
  }

  @FXML
  public void crearCuenta() throws IOException {
    if (App.inicioRoot == null) {
      App.inicioRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/inicio.fxml"));
    }
    App.navegar(App.inicioRoot);
    App.primaryStage.setMaximized(true);
    App.loginRoot = null;
    App.crearCuentaRoot = null;
  }

  @FXML
  private void cambiarALogin() {
    App.scene.setRoot(App.loginRoot);
    App.primaryStage.sizeToScene();
  }

}
