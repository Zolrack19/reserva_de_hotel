package com.example.hotel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalEdicionContr implements Initializable {

  @FXML
  private Label lblTituloPrincipal;
  @FXML
  private TextField txtPrincipal;
  @FXML
  private Label lblPrincipal;
  
  @FXML
  private Label lblContrasena;  
  @FXML
  private PasswordField pswNuevaContra;
  @FXML
  private TextField txtNuevaContra;
  @FXML
  private Label lblNuevaContra;
  
  @FXML
  private Label lblConfirmar;
  @FXML
  private PasswordField pswConfirmar;
  @FXML
  private TextField txtConfirmar;
  @FXML
  private Label lblConfirmarContra;
  
  @FXML
  private Button toggle1;
  @FXML
  private Button toggle2;
  

  @FXML
  private Button btnAceptar;
  @FXML
  private Button btnCancelar;

  private Button btnGuardar;

  private Label lblAEditar;
  private boolean mostrarOcultos;
  private Stage modal;
  private StringBuilder longitud = new StringBuilder(7);
  private StringBuilder contraL = new StringBuilder(5);
  private StringBuilder confirmaL = new StringBuilder(5);

  private boolean guardar = true;
  private boolean isTelefono;
  private boolean isSaldo;
  private short maxLongitud;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    txtNuevaContra.setVisible(false);
    txtNuevaContra.setManaged(false);
    txtConfirmar.setVisible(false);
    txtConfirmar.setManaged(false);
    
    pswNuevaContra.textProperty().bindBidirectional(txtNuevaContra.textProperty());
    pswConfirmar.textProperty().bindBidirectional(txtConfirmar.textProperty());

    toggle1.setOnMousePressed(e -> {
      txtNuevaContra.setVisible(true);
      txtNuevaContra.setManaged(true);
      pswNuevaContra.setVisible(false);
      pswNuevaContra.setManaged(false);
    });
    
    toggle1.setOnMouseReleased(e -> {
      txtNuevaContra.setVisible(false);
      txtNuevaContra.setManaged(false);
      pswNuevaContra.setVisible(true);
      pswNuevaContra.setManaged(true);
    });
    
    toggle2.setOnMousePressed(e -> {
      txtConfirmar.setVisible(true);
      txtConfirmar.setManaged(true);
      pswConfirmar.setVisible(false);
      pswConfirmar.setManaged(false);
    });
    
    toggle2.setOnMouseReleased(e -> {
      txtConfirmar.setVisible(false);
      txtConfirmar.setManaged(false);
      pswConfirmar.setVisible(true);
      pswConfirmar.setManaged(true);
    });

    ocultar();
    txtPrincipal.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= maxLongitud) {
        longitud.setLength(0);
        longitud.append(newValue.length()).append("/").append(maxLongitud);
        lblPrincipal.setText(longitud.toString());
      } else {
        txtPrincipal.setText(oldValue);
      }
    });

    pswNuevaContra.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 50) {
        contraL.setLength(0);
        contraL.append(newValue.length()).append("/50");
        lblNuevaContra.setText(contraL.toString());
      } else {
        pswNuevaContra.setText(oldValue);
      }
    });

    pswConfirmar.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 50) {
        confirmaL.setLength(0);
        confirmaL.append(newValue.length()).append("/50");
        lblConfirmarContra.setText(confirmaL.toString());
      } else {
        pswConfirmar.setText(oldValue);
      }
    });
    

    btnAceptar.setOnMouseClicked(e -> {
      if (isTelefono) {
        String resultado = validarTelefono(txtPrincipal.getText()); 
        if (resultado == null) return;
        txtPrincipal.setText(resultado);
      }

      if (isSaldo) {
        if (!txtPrincipal.getText().matches("^\\d{1,10}(\\.\\d{1,2})?$")) return;
        isSaldo = false;
      }

      if (lblAEditar != null) {
        lblAEditar.setText(txtPrincipal.getText());
      }
      if (guardar) {
        btnGuardar.setVisible(true);
        btnGuardar.setManaged(true);
        guardar = false;
      }
      reiniciar();
      modal.close();
    });

    btnCancelar.setOnMouseClicked(e -> {
      reiniciar();
      modal.close();
    });
  }

  public String validarTelefono(String numero) {
    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    try {
      PhoneNumber number = phoneUtil.parse(numero, "PE"); //cambiar por iso del país correspondiente
      return phoneUtil.isValidNumber(number) ? phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL) : null;
    } catch (NumberParseException e) {
      System.out.println("Error al analizar el número: " + e);
      return null;
    }
  }

  public void prepararModal(String campo, Label lblAEditar, short maxLongitud) {
    this.lblAEditar = lblAEditar;
    this.maxLongitud = maxLongitud;
    lblTituloPrincipal.setText(campo);
    if (lblAEditar == null) {
      mostrarOcultos = true;
      modal.setResizable(true);
      modal.setHeight(387);
      modal.setResizable(false);
      ocultar();
      return;
    };
    txtPrincipal.setText(lblAEditar.getText());
  }


  public void reiniciar() {
    isSaldo = false;
    isTelefono = false;
    if (mostrarOcultos) {
      mostrarOcultos = false;
      ocultarComponentes();
    }
  }

  private void ocultarComponentes() {
    pswNuevaContra.setText("");
    pswConfirmar.setText("");
    modal.setResizable(true);
    modal.setHeight(207);
    modal.setResizable(false);
    ocultar();
  }

  private void ocultar() {
    toggle1.setVisible(mostrarOcultos);
    toggle1.setManaged(mostrarOcultos);
    toggle2.setVisible(mostrarOcultos);
    toggle2.setManaged(mostrarOcultos);

    lblContrasena.setVisible(mostrarOcultos);
    lblContrasena.setManaged(mostrarOcultos);
    pswNuevaContra.setVisible(mostrarOcultos);
    pswNuevaContra.setManaged(mostrarOcultos);
    
    lblNuevaContra.setVisible(mostrarOcultos);
    lblNuevaContra.setManaged(mostrarOcultos);
    lblConfirmarContra.setVisible(mostrarOcultos);
    lblConfirmarContra.setManaged(mostrarOcultos);
    
    lblConfirmar.setVisible(mostrarOcultos);
    lblConfirmar.setManaged(mostrarOcultos);
    pswConfirmar.setVisible(mostrarOcultos);
    pswConfirmar.setManaged(mostrarOcultos);
  }

  public void setModal(Stage modal) {
    this.modal = modal;
  }

  public void setIsSaldo(boolean isSaldo) {
    this.isSaldo = isSaldo;
  }

  public void setIsTelefono(boolean isTelefono) {
    this.isTelefono = isTelefono;
  }
  
  public void setBtnGuardar(Button btnGuardar) {
    this.btnGuardar = btnGuardar;
  }
}
