package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.example.hotel.App;
import com.example.hotel.dominio.Pais;
import com.example.hotel.service.CrearCuentaServicio;
import com.example.hotel.util.Rutas;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
  Controlador de la plantilla de creaación de usuario.
  Verifica que la data del formulario sea verídica y crea un un usuario en la base de datos.
*/
public class CrearCuentaContr implements Initializable {

  @FXML
  private VBox raiz;

  @FXML
  private Label lblCambiarLogin;

  @FXML
  private ComboBox<Pais> cbxPais;

  @FXML
  private TextField txtNombre;
  
  @FXML
  private TextField txtApellido;
  
  @FXML
  private TextField txtEmail;
  
  @FXML
  private TextField txtContrasena;

  @FXML
  private Label lblNombre;
  
  @FXML
  private Label lblApellido;
  
  @FXML
  private Label lblEmail;
  @FXML
  private Label lblEmailWarning;
  
  @FXML
  private Label lblContrasena;

  @FXML
  private CheckBox chxTerminos;

  private final CrearCuentaServicio crearCuentaServicio = new CrearCuentaServicio();

  private String nombre;
  private String apellido;
  private String email;
  private String contra;
  private Stage secondaryStage;
  private CodigoVeriContr contr;
  
  /**
    Inicializa componentes gráficos y asigna eventos para manejar la lógica de formulario. 
  */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    App.scheduler.schedule(() -> {
      cbxPais.getItems().addAll(crearCuentaServicio.getPaises());
    }, 0, TimeUnit.SECONDS);
    
    lblNombre.setText("0/150");
    lblApellido.setText("0/150");
    lblEmail.setText("0/200");
    lblContrasena.setText("0/50");
    lblEmailWarning.setVisible(false);
    lblEmailWarning.setManaged(false);

    txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 150) {
        lblNombre.setText(newValue.length() + "/150");
      } else {
        txtNombre.setText(oldValue);
      }
    });
    
    txtApellido.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 150) {
        lblApellido.setText(newValue.length() + "/150");
      } else {
        txtApellido.setText(oldValue);
      }
    });
    
    txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 200) {
        lblEmail.setText(newValue.length() + "/150");
      } else {
        txtEmail.setText(oldValue);
      }
    });
    
    txtContrasena.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() <= 50) {
        lblContrasena.setText(newValue.length() + "/150");
      } else {
        txtContrasena.setText(oldValue);
      }
    });


    lblCambiarLogin.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ENTER, SPACE -> cambiarALogin();
        default -> {}
      }
    });
  }

  @FXML
  private void crearCuenta() throws IOException {
    if (cbxPais.getValue() == null) return;
    if (!chxTerminos.isSelected()) {
      chxTerminos.requestFocus();
      return;
    }
    nombre = txtNombre.getText().trim();
    apellido = txtApellido.getText().trim();
    email = txtEmail.getText().trim();
    contra = txtContrasena.getText().trim();
    boolean cortar = false;

    if (nombre.isEmpty()) {
      cortar = true;
      if (!txtNombre.getStyleClass().contains("txtInvalido")) {
        txtNombre.getStyleClass().add("txtInvalido");
      }
    } else if (txtNombre.getStyleClass().contains("txtInvalido")) {
      txtNombre.getStyleClass().removeLast();
    }
    
    if (apellido.isEmpty()) {
      cortar = true;
      if (!txtApellido.getStyleClass().contains("txtInvalido")) {
        txtApellido.getStyleClass().add("txtInvalido");
      }
    } else if (txtApellido.getStyleClass().contains("txtInvalido")) {
      txtApellido.getStyleClass().removeLast();
    }
    
    if (email.isEmpty() || !email.matches("^(.+)@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*\\.(com|net|org|edu|gov|pe|mx|co|info|dev)$")) {
      cortar = true;
      if (!txtEmail.getStyleClass().contains("txtInvalido")) {
        txtEmail.getStyleClass().add("txtInvalido");
      }
    } else if (txtEmail.getStyleClass().contains("txtInvalido")) {
      txtEmail.getStyleClass().removeLast();
    }

    if (contra.isEmpty() || contra.length() < 8) {
      cortar = true;
      if (!txtContrasena.getStyleClass().contains("txtInvalido")) {
        txtContrasena.getStyleClass().add("txtInvalido");
      }
    } else if (txtContrasena.getStyleClass().contains("txtInvalido")) {
      txtContrasena.getStyleClass().removeLast();
    }

    if (cortar || crearCuentaServicio.emailRepetido(email)) {
      lblEmailWarning.setVisible(true);
      lblEmailWarning.setManaged(true);
      return;
    }
    raiz.getStyleClass().add("espera");

    if (secondaryStage == null) {
      secondaryStage = new Stage();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/codigo-veri.fxml"));
      Parent parent = loader.load();
      contr = loader.getController();
      contr.init(this, email);
      Scene scene = new Scene(parent);
      secondaryStage.setOnCloseRequest(e -> {
        raiz.getStyleClass().removeLast();
        contr.reiniciar();
      });
      secondaryStage.setScene(scene);
      secondaryStage.initModality(Modality.WINDOW_MODAL);
      secondaryStage.initOwner(App.scene.getWindow());
      secondaryStage.setResizable(false);
    }
    contr.setEmail(email);
    secondaryStage.show();
    
    App.scheduler.schedule(() -> {
      crearCuentaServicio.evnviarComprobante(email);
    }, 0, TimeUnit.MILLISECONDS);
  }

  @FXML
  private void cambiarALogin() {
    App.scene.setRoot(App.getVista(Rutas.LOGIN));
    App.primaryStage.sizeToScene();
  }


  public void crearCliente() throws IOException {
    crearCuentaServicio.crearCliente(cbxPais.getValue(), nombre, apellido, email, contra);
    if (App.getVista(Rutas.INICIO) == null) {
      App.setVista(Rutas.INICIO);
    }
    Platform.runLater(() -> {
      App.navegar(Rutas.INICIO);
      App.primaryStage.setMaximized(true);
    });
    App.removeVista(Rutas.LOGIN);
    App.removeVista(Rutas.CREAR_CUENTA);
  }

  public VBox getRaiz() {
    return raiz;
  }

  public CrearCuentaServicio getCrearCuentaServicio() {
    return crearCuentaServicio;
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 CraerCuenta eliminado por GC\n\n");
  }
}
