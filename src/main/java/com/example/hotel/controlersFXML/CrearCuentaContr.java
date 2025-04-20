package com.example.hotel.controlersFXML;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;

import com.example.hotel.App;
import com.example.hotel.HibernateUtil;
import com.example.hotel.dominio.Pais;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class CrearCuentaContr implements Initializable {

  @FXML
  private Label lblCambiarLogin;

  @FXML
  private ComboBox<String> cbxPais;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Task<List<Pais>> task = new Task<>() {
      @Override
      protected List<Pais> call() throws Exception {
        Session sesion = HibernateUtil.getSession().openSession();
        List<Pais> paises = sesion.createQuery("from Pais", Pais.class)
          .list();
        
        sesion.close();
        return paises;
      }
    };
    task.setOnSucceeded(e -> {
      List<Pais> paises = task.getValue();
      for (int i = 0; i < paises.size(); i++) {
        cbxPais.getItems().add(paises.get(i).getNombre());
      }
    });
    new Thread(task).start();



    lblCambiarLogin.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ENTER, SPACE -> cambiarALogin();
        default -> {
        }
      }
    });
  }

  @FXML
  public void crearCuenta() throws IOException {
    if (App.inicioRoot == null) {
      App.inicioRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/inicio.fxml"));
    }
    App.scene.setRoot(App.inicioRoot);
    App.primaryStage.setMaximized(true);
    App.inicioRoot = null;
    App.crearCuentaRoot = null;
  }

  @FXML
  private void cambiarALogin() {
    App.scene.setRoot(App.loginRoot);
    App.primaryStage.sizeToScene();
  }

}
