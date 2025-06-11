package com.example.hotel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chxReserva.setSelected(true);
    lblAcomponantes.setVisible(false);
    lblAcomponantes.setManaged(false);
    chxReserva.setOnAction(e -> {
      lblAcomponantes.setVisible(!lblAcomponantes.isVisible());
      lblAcomponantes.setManaged(!lblAcomponantes.isManaged());
    });
  }

  
  


}
