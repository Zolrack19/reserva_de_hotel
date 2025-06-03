package com.example.hotel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.hibernate.Session;

import com.example.hotel.dominio.Cliente;
import com.example.hotel.util.HibernateUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;


/**
  @author Carlos Barrientos <a>https://github.com/Zolrack19/reserva_de_hotel</a>
*/
public class App extends Application {

  /**
  * Programador de tareas asíncronas
  */
  public static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  public static ScheduledFuture<?> future;

  /**
  * Panel principal de la aplicación, similar a JPanel en swing.
  */
  public static Scene scene;
  public static Parent loginRoot;
  public static Parent crearCuentaRoot;
  public static Parent clienteInfo;
  public static Parent inicioRoot;
  public static Parent resultadosRoot;
  public static Parent detallesRoot;

  public static boolean sesionActiva;

  /**
  * Ventana general de la aplicación, similar a JFrame de swing.
  */
  public static Stage primaryStage;

  public static Cliente cliente;
  /**
  * Pila global de navegación.
  */
  public static final Stack<Parent> stackNavegacion = new Stack<>();
  
  /**
  * Puntero de navegación en la pila.
  */
  public static byte puntero = -1;

  /**
  * Configura las instancias y comportamientos necesarios para inicar la aplicación.
  * Tales como: el fxml de incio, añade eventos de mouse y teclado para la navegación.
  */

  @Override
  public void start(Stage stage) throws IOException {
    boolean resultado = iniciarSesion();
    sesionActiva = resultado;
    if (!resultado) {
      FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
      loginRoot = fxmlLoader.load();
      scene = new Scene(loginRoot);
    }
    confInit(stage, resultado);
  }

  private boolean iniciarSesion() throws IOException {
    Session s = HibernateUtil.getSession().openSession();
    String ruta = System.getProperty("user.home") + File.separator + ".hotel" + File.separator + "sesion.txt";
    BufferedReader reader = new BufferedReader(new FileReader(ruta));
    String contenido = reader.readLine();
    reader.close();
    if (contenido == null || contenido.isEmpty()) return false;
    cliente = s.createQuery("from Cliente c where c.email = ?1", Cliente.class)
    .setParameter(1, contenido)
    .uniqueResult();
    s.close();
    if (cliente == null) return false;
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("inicio.fxml"));
    inicioRoot = fxmlLoader.load();
    puntero++;
    stackNavegacion.add(inicioRoot);
    scene = new Scene(inicioRoot);
    return true;
  }

  private void confInit(Stage stage, boolean maxTamanio) {
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
    if (maxTamanio) {
      stage.setMaximized(true);
    } else {
      stage.sizeToScene();
    }
    stage.setTitle("Little View");
    stage.show();
  }

  /**
  * Lógica de la navegación entre páginas de la aplicación, simula una navegación web guardando 
  * las páginas de la aplicación en una pila y ajustando un puntero a la posición del usuario.
  * @param root Página a la que está navegando el usuario, se guarda en la pila.
  */
  public static void navegar(Parent root) {
    if (stackNavegacion.size() >= 20) { // tamaño máximo de la pila
      stackNavegacion.removeFirst();
      if (puntero + 1 == stackNavegacion.size()) {
        puntero--;
      }
    }
    while (stackNavegacion.size() - 1 != puntero) {
      stackNavegacion.removeLast();
    }
    puntero++;
    stackNavegacion.add(root);
    App.scene.setRoot(root);
  }

  /**
  * Cierra todos los recursos globales al cerrar la ventana general.
  */
  @Override
  public void stop() throws Exception {
    super.stop();
    scheduler.shutdownNow();
    String ruta = System.getProperty("user.home") + File.separator + ".hotel" + File.separator + "sesion.txt";
    BufferedWriter writer = new BufferedWriter(new FileWriter(ruta));
    if (sesionActiva) {
      writer.write(cliente.getEmail());
    } else {
      writer.write("");
    }
    writer.close();
  }

   public static void main(String[] args) {
    launch();
  }

}
