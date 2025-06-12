package com.example.hotel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.example.hotel.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
  Controlador del modal de verificación de código, se encarga 
  de verificar el código de comprobación.
*/
public class CodigoVeriContr implements Initializable {
  
  @FXML
  private VBox root;

  @FXML
  private Label lbl1;
  @FXML
  private Label lbl2;
  @FXML
  private Label lbl3;
  @FXML
  private Label lbl4;
  @FXML
  private Label lbl5;
  @FXML
  private Label lbl6;
  @FXML
  private Label lblWarning;
  
  @FXML
  private Label lblReenviar;
  
  private Label[] lbls;

  @FXML
  private Button btnVerificar;
  private static byte puntero;
  private CrearCuentaContr contr;
  private String email;


  /**
    Método que sirve para instanciar atributos de clase.
    @param contr controlador de creación de cuenta, para inyectar un campo de la clase.
    @param email email ingresado en el formulario.
    @see CrearCuentaContr
  */
  public void init(CrearCuentaContr contr, String email) {
    this.contr = contr;
    this.email = email;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lbls = new Label[] {lbl1, lbl2, lbl3, lbl4, lbl5, lbl6};
    root.setOnKeyPressed(e -> {
      if (puntero > 0 && e.getCode() == KeyCode.BACK_SPACE) {
        puntero--;
        lbls[puntero].setText("");
        return;
      }
      if (!e.getText().matches("[0-9]") || puntero > 5) return;
      lbls[puntero].setText(e.getText());
      puntero++;
    });
    root.requestFocus();
  }

  @FXML
  public void verificarCodigo() {
    String codigo = "";
    for (Label label : lbls) {
      codigo += label.getText();
    }
    if (!contr.getCrearCuentaServicio().verificarCodigo(codigo)) {
      lblWarning.setText("Código inválido, vuelva a intentar");
      return;
    }
    Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
    contr.getRaiz().getStyleClass().removeLast();
    lbls = null;
    lblWarning = null;
    lblReenviar = null;
    try {
      contr.crearCliente();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void reenviarCodigo() {
    App.scheduler.schedule(() -> {
      contr.getCrearCuentaServicio().evnviarComprobante(email);
    }, 0, TimeUnit.MILLISECONDS);
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void reiniciar() {
    for (Label label : lbls) {
      label.setText("");
    }
    puntero = 0;
  }
  

}
