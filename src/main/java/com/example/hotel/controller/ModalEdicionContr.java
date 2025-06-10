package com.example.hotel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import com.example.hotel.App;
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

/**
  Controlador de un modal de edición para los atributos de un usuario.
  @see ClienteInfoContr
*/
public class ModalEdicionContr implements Initializable {

  // Atributos principales que cambian de valor según el atributo a editar
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
  private Button btnCancelarM;

  /**
    Inyección de un botón de guardar y cancelar. 
    @see ClienteInfoContr
  */
  private Button btnGuardar;
  private Button btnCancelar;

  private Label lblAEditar;
  private boolean mostrarOcultos; // Se autoadministra para mostrar u ocultar componentes del modal según el campo a editar 
  private Stage modal; // Ventana del modal

  private String iso;
  private boolean guardar = true;
  private boolean isTelefono;
  private boolean isSaldo;
  private int maxLongitud;

  /**
    Aplica eventos de escucha a los componentes gráficos del modal.
  */
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
        lblPrincipal.setText(newValue.length() + "/" + maxLongitud);
      } else {
        txtPrincipal.setText(oldValue);
      }
    });

    pswNuevaContra.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 50) {
        lblNuevaContra.setText(newValue.length() + "/50");
      } else {
        pswNuevaContra.setText(oldValue);
      }
    });

    pswConfirmar.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 50) {
        lblConfirmarContra.setText(newValue.length() + "/50");
      } else {
        pswConfirmar.setText(oldValue);
      }
    });
    

    btnAceptar.setOnMouseClicked(e -> {
      if (isTelefono) { // validación para campo de teléfono
        String resultado = validarTelefono(txtPrincipal.getText()); 
        if (resultado == null) return;
        txtPrincipal.setText(resultado);
      }

      if (isSaldo) { // validación para campo de saldo
        if (!txtPrincipal.getText().matches("^\\d{1,10}(\\.\\d{1,2})?$")) return;
        isSaldo = false;
      }

      if (lblAEditar == null) { // validación para contraseña
        if (!BCrypt.checkpw(txtPrincipal.getText(), App.cliente.getContrasena())) return;
        String contra = txtNuevaContra.getText();
        if (contra.length() < 8) return;
        if (contra.equals(txtConfirmar.getText())) {
          App.cliente.setContrasena(BCrypt.hashpw(contra, BCrypt.gensalt()));
          System.out.println("contraseña cambiada");
        }
      } else {
        lblAEditar.setText(txtPrincipal.getText().trim());
      }
      if (guardar) {
        btnGuardar.setVisible(true);
        btnGuardar.setManaged(true);
        btnCancelar.setVisible(true);
        btnCancelar.setManaged(true);
        guardar = false;
      }
      reiniciar();
      modal.close();
    });

    btnCancelarM.setOnMouseClicked(e -> {
      reiniciar();
      modal.close();
    });
  }

  // Lógica para validar números de telefono según país
  private String validarTelefono(String numero) {
    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    try {
      PhoneNumber number = phoneUtil.parse(numero, iso); //cambiar por iso del país correspondiente
      return phoneUtil.isValidNumber(number) ? phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL) : null;
    } catch (NumberParseException e) {
      System.out.println("Error al analizar el número: " + e);
      return null;
    }
  }

  /**
    Método de rellenar e inicilizar el contenido de un modal (no instancia campos)
    @param campo Campo de texto para cambiar el contenido de {@link #lblPrincipal}
    @param lblAEditar Referencia al label de {@link ClienteInfoContr} para cambiar de contenido en su plantilla
    @param maxLongitud Longitud máxima según el atributo a editar
  */
  public void prepararModal(String campo, Label lblAEditar, int maxLongitud) {
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

  /**
    Método para limpiar el contenido del modal
  */
  public void reiniciar() {
    isSaldo = false;
    isTelefono = false;
    if (mostrarOcultos) {
      mostrarOcultos = false;
      ocultarComponentes();
    }
  }

  // Oculta componentes del modal cuando se accede a editar un atributo que no sea la contraseña
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

  /**
    Autoinyección de dependencia.
    @param modal Referencia a la misma instancia de esta clase creada.
  */
  public void setModal(Stage modal) {
    this.modal = modal;
  }

  /**
    Método para 'avisar' que se editó un campo cualquiera del usuario.
    @param guardar Booleano que solo sirve para hacer visible el botón de guardado
  */
  public void setGuardar(boolean guardar) {
    this.guardar = guardar;
  }

  /**
    Método para 'avisar' que el campo de saldo está en edición
    @param isSaldo Booleano para asignar lógica de validación correspondiente al saldo
  */
  public void setIsSaldo(boolean isSaldo) {
    this.isSaldo = isSaldo;
  }
  
  /**
    Método para 'avisar' que el campo de telefono está en edición
    @param isTelefono Booleano para asignar lógica de validación correspondiente a un número telefónico
  */
  public void setIsTelefono(boolean isTelefono) {
    this.isTelefono = isTelefono;
  }

  public void setISO(String iso) {
    this.iso = iso;
  }
  
  /**
    Inyección del botón de guardar y cancelar
    @param btnGuardar Botón para disparar evento de guardar cambios en la base de datos
    @param btnCancelar Botón para disparar evento de cancelación de cambios
    @see ClienteInfoContr
  */
  public void setBotones(Button btnGuardar, Button btnCancelar) {
    this.btnGuardar = btnGuardar;
    this.btnCancelar = btnCancelar;
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 ModalEdicion eliminado por GC\n\n");
  }
}
