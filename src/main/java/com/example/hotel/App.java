package com.example.hotel;

import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import com.example.hotel.dominio.Cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class App extends Application {

  public static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  public static ScheduledFuture<?> future;

  public static Scene scene;
  public static Parent loginRoot;
  public static Parent crearCuentaRoot;
  public static Parent clienteInfo;
  public static Parent inicioRoot;
  public static Parent resultadosRoot;
  public static Parent detallesRoot;
  public static Stage primaryStage;

  public static final byte size = 20;
  public static final Stack<Parent> stackNavegacion = new Stack<>();
  public static byte puntero = -1;

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
    loginRoot = fxmlLoader.load();
    scene = new Scene(loginRoot);
    scene.setOnKeyPressed(event -> {
      if (event.isAltDown() && event.getCode() == KeyCode.LEFT) {
        if (puntero - 1 <= -1)
          return;
        scene.setRoot(stackNavegacion.get(--puntero));
      } else if (event.isAltDown() && event.getCode() == KeyCode.RIGHT) {
        if (puntero + 1 >= stackNavegacion.size())
          return;
        scene.setRoot(stackNavegacion.get(++puntero));
      }
    });
    scene.setOnMouseClicked(e -> {
      if (e.getButton() == MouseButton.BACK) {
        if (puntero - 1 <= -1)
          return;
        scene.setRoot(stackNavegacion.get(--puntero));
      } else if (e.getButton() == MouseButton.FORWARD) {
        if (puntero + 1 >= stackNavegacion.size())
          return;
        scene.setRoot(stackNavegacion.get(++puntero));
      }
    });
    primaryStage = stage;
    stage.setScene(scene);
    stage.sizeToScene();
    stage.setTitle("Little View");
    stage.show();
  }

  public static void navegar(Parent root) {
    if (stackNavegacion.size() >= size) {
      stackNavegacion.removeFirst();
      if (puntero + 1 == stackNavegacion.size()) {
        // stackNavegacion.removeFirst();
        puntero--;
      }
    }
    // puntero++;
    // stackNavegacion.add(root);
    while (stackNavegacion.size() - 1 != puntero) {
      stackNavegacion.removeLast();
    }
    puntero++;
    stackNavegacion.add(root);
    App.scene.setRoot(root);
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    scheduler.shutdownNow();
  }

  public static void main(String[] args) {
    HibernateUtil.getSession();
    launch(); 

    //TODO: cambiar la ruta de las imagenes de hoteles
  }

}
