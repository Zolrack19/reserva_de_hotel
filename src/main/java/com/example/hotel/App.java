package com.example.hotel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.hibernate.Session;

import com.example.hotel.dominio.Cliente;
import com.example.hotel.util.HibernateUtil;
import com.example.hotel.util.Rutas;

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
  public static boolean sesionActiva;

  /**
  * Ventana general de la aplicación, similar a JFrame de swing.
  */
  public static Stage primaryStage;

  public static Cliente cliente;
  /**
  * Pila global de navegación.
  */
  private static final Stack<Rutas> stackNavegacion = new Stack<>();
  
  /**
  * Puntero de navegación en la pila.
  */
  private static byte puntero = -1;
  private static HashMap<Rutas, Parent> vistas = new HashMap<>();

  @Override
  public void stop() throws Exception {
    super.stop();
    scheduler.shutdownNow();
    String ruta = "imagenesdb" + File.separator + "sesion.txt";
    BufferedWriter writer = new BufferedWriter(new FileWriter(ruta));
    if (sesionActiva) {
      writer.write(cliente.getEmail());
    } else {
      writer.write("");
    }
    writer.close();
  }

  /**
  * Configura las instancias y comportamientos necesarios para inicar la aplicación.
  * Tales como: el fxml de incio, añade eventos de mouse y teclado para la navegación.
  */
  @Override
  public void start(Stage stage) throws IOException {
    boolean resultado = iniciarSesion();
    sesionActiva = resultado;
    if (!resultado) {
      FXMLLoader fxmlLoader = new FXMLLoader(Rutas.LOGIN.getUrlVista());
      vistas.put(Rutas.LOGIN, fxmlLoader.load());
      scene = new Scene(vistas.get(Rutas.LOGIN));
    }
    confInit(stage, resultado);
  }

  private boolean iniciarSesion() throws IOException {
    Session s = HibernateUtil.getSession().openSession();
    String ruta = "imagenesdb" + File.separator + "sesion.txt";
    BufferedReader reader = new BufferedReader(new FileReader(ruta));
    String contenido = reader.readLine();
    reader.close();
    if (contenido == null || contenido.isEmpty()) return false;
    cliente = s.createQuery("from Cliente c where c.email = ?1", Cliente.class)
    .setParameter(1, contenido)
    .uniqueResult();
    s.close();
    if (cliente == null) return false;
    FXMLLoader fxmlLoader = new FXMLLoader(Rutas.INICIO.getUrlVista());
    vistas.put(Rutas.INICIO, fxmlLoader.load());
    puntero++;
    stackNavegacion.add(Rutas.INICIO);
    scene = new Scene(vistas.get(Rutas.INICIO));
    return true;
  }

  private void confInit(Stage stage, boolean maxTamanio) {
    scene.setOnKeyPressed(e -> {
      if (e.isAltDown() && e.getCode() == KeyCode.LEFT) {
        if (puntero - 1 <= -1) return;
        resetTtl(stackNavegacion.get(puntero));
        puntero--;
        if (stackNavegacion.get(puntero).getTtlTask() != null && !stackNavegacion.get(puntero).getTtlTask().isDone()) {
          stackNavegacion.get(puntero).getTtlTask().cancel(false);
        }
        if (getVista(stackNavegacion.get(puntero)) == null) {
          setVista(stackNavegacion.get(puntero));
        }
        scene.setRoot(getVista(stackNavegacion.get(puntero)));
      } else if (e.isAltDown() && e.getCode() == KeyCode.RIGHT) {
        if (puntero + 1 >= stackNavegacion.size()) return;
        resetTtl(stackNavegacion.get(puntero));
        puntero++;
        if (stackNavegacion.get(puntero).getTtlTask() != null && !stackNavegacion.get(puntero).getTtlTask().isDone()) {
          stackNavegacion.get(puntero).getTtlTask().cancel(false);
        }
        if (getVista(stackNavegacion.get(puntero)) == null) {
          setVista(stackNavegacion.get(puntero));
        }
        scene.setRoot(getVista(stackNavegacion.get(puntero)));
      }
    });
    scene.setOnMouseClicked(e -> {
      if (e.getButton() == MouseButton.BACK) {
        if (puntero - 1 <= -1) return;
        resetTtl(stackNavegacion.get(puntero));
        puntero--;
        if (stackNavegacion.get(puntero).getTtlTask() != null && !stackNavegacion.get(puntero).getTtlTask().isDone()) {
          stackNavegacion.get(puntero).getTtlTask().cancel(false);
        }
        if (getVista(stackNavegacion.get(puntero)) == null) {
          setVista(stackNavegacion.get(puntero));
        }
        scene.setRoot(getVista(stackNavegacion.get(puntero)));
      } else if (e.getButton() == MouseButton.FORWARD) {
        if (puntero + 1 >= stackNavegacion.size()) return;
        resetTtl(stackNavegacion.get(puntero));
        puntero++;
        if (stackNavegacion.get(puntero).getTtlTask() != null && !stackNavegacion.get(puntero).getTtlTask().isDone()) {
          stackNavegacion.get(puntero).getTtlTask().cancel(false);
        }
        if (getVista(stackNavegacion.get(puntero)) == null) {
          setVista(stackNavegacion.get(puntero));
        }
        scene.setRoot(getVista(stackNavegacion.get(puntero)));
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
  * @param ruta Página a la que está navegando el usuario, se guarda en la pila.
  */
  public static void navegar(Rutas ruta) {
    if (ruta.getTtlTask() != null && !ruta.getTtlTask().isDone()) {
      ruta.getTtlTask().cancel(false);
    }
    if (puntero >= 0) {
      resetTtl(stackNavegacion.get(puntero));
    }

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
    stackNavegacion.add(ruta);
    App.scene.setRoot(vistas.get(ruta));

  }



  public static void resetTtl(Rutas ruta) {
    if (ruta.getTtlTask() != null && !ruta.getTtlTask().isDone()) {
      ruta.getTtlTask().cancel(false);
    }
    ruta.setTtlTask(scheduler.schedule(() -> {
      System.out.println("eliminando vista: " + ruta);
      vistas.remove(ruta);
    }, 3, TimeUnit.MINUTES));
  }

  public static Parent setVista(Rutas ruta) {
    try {
      return vistas.put(ruta, FXMLLoader.load(ruta.getUrlVista()));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static Parent setVista(Rutas ruta, Parent nodo) {
    return vistas.put(ruta, nodo);
  }

  public static void removeVista(Rutas ruta) {
    vistas.remove(ruta);
  }
  
  public static Parent getVista(Rutas ruta) {
    return vistas.get(ruta);
  }

  public static void main(String[] args) {
    launch();
  }
}
