package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import com.example.hotel.dominio.Cuarto;
import com.example.hotel.singleton.Rutas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FormCuartoContr implements Initializable {

  @FXML
  private Label lblTituloHotel;

  @FXML
  private Label lblEstrella;

  @FXML
  private Label lblImagen;

  @FXML
  private Label lblFechaEntrada;

  @FXML
  private Label lblFechaSalida;

  @FXML
  private Label lblNoches;

  @FXML
  private Label lblNumHabitacion;

  @FXML
  private Label lblDivisa;

  @FXML
  private Label lblPrecio;

  @FXML
  private TextField txtNombre;

  @FXML
  private Label lblPrefijoTelefono;

  @FXML
  private TextField txtTelefono;

  @FXML
  private CheckBox chxReserva;

  @FXML
  private Label lblAcomponantes;

  @FXML
  private VBox vbxAcompanantes;

  private Cuarto cuarto;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chxReserva.setSelected(true);
    lblAcomponantes.setVisible(false);
    lblAcomponantes.setManaged(false);
    vbxAcompanantes.setVisible(false);
    vbxAcompanantes.setManaged(false);
    chxReserva.setOnAction(e -> {
      if (vbxAcompanantes.getChildren().isEmpty()) {
        try {
          rellenar();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      lblAcomponantes.setVisible(!lblAcomponantes.isVisible());
      lblAcomponantes.setManaged(!lblAcomponantes.isManaged());
      vbxAcompanantes.setVisible(!vbxAcompanantes.isVisible());
      vbxAcompanantes.setManaged(!vbxAcompanantes.isManaged());
    });
  }

  public void rellenarData(Cuarto cuarto) {
    this.cuarto = cuarto;
    // lblFechaEntrada.setText(fechaEntrada.toString());
    // lblFechaEntrada.setText(fechaSalida.toString());
  }

  public void limpiarForm() {
    vbxAcompanantes.getChildren().clear();
  }

  private void rellenar() throws IOException {
    for (int i = 0; i < cuarto.getCapacidad() - 1; i++) {
      FXMLLoader loader = new FXMLLoader(Rutas.ACOMPANANTE.getUrlVista());
      Parent card = loader.load();
      // TarjetaCaruselContr contr = loader.getController();
      // setData(contr, hotel, url);
      vbxAcompanantes.getChildren().add(card);
    }
  }

}
