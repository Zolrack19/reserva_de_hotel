package com.example.hotel.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;


import com.example.hotel.App;
import com.example.hotel.dominio.Pais;
import com.example.hotel.service.ClienteServicio;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClienteInfoContr implements Initializable {

  @FXML
  private Label lblNombre;
  @FXML
  private Label lblEditarNombre;

  @FXML
  private Label lblApellido;
  @FXML
  private Label lblEditarApellido;

  @FXML
  private Label lblEmail;

  @FXML
  private Label lblEditarContra;

  @FXML
  private Label lblSaldo;
  @FXML
  private Label lblEditarSaldo;

  @FXML
  private Label lblPrefijoTelefonico;

  @FXML
  private Label lblTelefono;
  @FXML
  private Label lblEditarTelefono;

  @FXML
  private Label lblPais;
  
  @FXML
  private ComboBox<Pais> cbxPais;

  @FXML
  private Button btnEliminarCuenta;
  
  @FXML
  private Button btnGuardar;

  private Stage modal;
  private ModalEdicionContr modalController;
  private final ClienteServicio clienteServicio = new ClienteServicio();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblNombre.setText(App.cliente.getNombre());
    lblApellido.setText(App.cliente.getApellido());
    lblEmail.setText(App.cliente.getEmail());
    lblSaldo.setText(App.cliente.getSaldo() != null ? App.cliente.getSaldo().toString() : "0.00");
    lblTelefono.setText(App.cliente.getTelefono() != null ? App.cliente.getTelefono() : "");

    btnGuardar.setVisible(false);
    btnGuardar.setManaged(false);
    btnGuardar.setOnMouseClicked(e -> {
      App.cliente.setNombre(lblNombre.getText());
      App.cliente.setApellido(lblApellido.getText());
      App.cliente.setSaldo(new BigDecimal(lblSaldo.getText()));
      App.cliente.setTelefono(lblTelefono.getText());
      clienteServicio.actualizarCliente(App.cliente);
      btnGuardar.setVisible(false);
      btnGuardar.setManaged(false);
      modalController.setGuardar(true);
    });
    lblEditarNombre.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) {
        crearModal();
      }
      modalController.prepararModal("Nombre", lblNombre, (short) 150);
      modal.show();
    });

    lblEditarApellido.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) {
        crearModal();
      }
      modalController.prepararModal("Apellidos", lblApellido, (short) 150);
      modal.show();
    });    

    lblEditarContra.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) {
        crearModal();
      }
      modalController.prepararModal("Contraseña actual", null, (short) 50);
      modal.show();
    });
    
    lblEditarSaldo.setOnKeyPressed(e -> {
    if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) {
        crearModal();
      }
      modalController.setIsSaldo(true);
      modalController.prepararModal("Saldo", lblSaldo, (short) 11);
      modal.show();
    });
    
    lblEditarTelefono.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) {
        crearModal();
      }
      modalController.setIsTelefono(true);
      modalController.prepararModal("Número de teléfono", lblTelefono, (short) 25);
      modal.show();
    });

    lblEditarNombre.setOnMouseClicked(e -> {
      if (modal == null) {
        crearModal();
      }
      modalController.prepararModal("Nombre", lblNombre, (short) 150);
      modal.show();
    });
   
    lblEditarApellido.setOnMouseClicked(e -> {
      if (modal == null) {
        crearModal();
      }
      modalController.prepararModal("Apellidos", lblApellido, (short) 150);
      modal.show();
    });
   
    lblEditarContra.setOnMouseClicked(e -> {
      if (modal == null) {
        crearModal();
      }
      modalController.prepararModal("Contraseña actual", null, (short) 50);
      modal.show();
    });
   
    lblEditarSaldo.setOnMouseClicked(e -> {
      if (modal == null) {
        crearModal();
      }
      modalController.setIsSaldo(true);
      modalController.prepararModal("Saldo", lblSaldo, (short) 11);
      modal.show();
    });
    
    lblEditarTelefono.setOnMouseClicked(e -> {
      if (modal == null) {
        crearModal();
      }
      modalController.setIsTelefono(true);
      modalController.prepararModal("Número de teléfono", lblTelefono, (short) 20);
      modal.show();
    });
  }

  private void crearModal() {
    try {
      modal = new Stage();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/modal-edicion.fxml"));
      Parent parent = loader.load();
      modalController = loader.getController();
      modalController.setBtnGuardar(btnGuardar);
      modal.setOnCloseRequest(e -> {
        modalController.reiniciar();
      });
      modalController.setModal(modal);
      Scene scene = new Scene(parent);
      scene.setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.ESCAPE) {
          modalController.reiniciar();
          modal.close();
        }
      });
      modal.setScene(scene);
      modal.setTitle("Cambiar atributo");
      modal.initModality(Modality.WINDOW_MODAL);
      modal.initOwner(App.primaryStage);
      modal.setResizable(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
