package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.hotel.App;
import com.example.hotel.auxiliar.ConfRepetitiva;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.service.BusquedaServicio;
import com.example.hotel.singleton.Imagenes;
import com.example.hotel.singleton.Rutas;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;

/**
  Clase de inicio a la aplicación, una vez que se inicia sesión o se crea una cuenta
  esta clase instancia componentes de la página de bienvenida.
  Implementa Initializable para instancar los componentes gráficos.
  @see Initializable
*/
public class InicioController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(InicioController.class);

  @FXML
  private HBox principal; 
  
  @FXML
  private Label lblAvatar;
  
  @FXML
  private TextField txtBuscar;
  
  @FXML
  private DatePicker dateInicio;
  
  @FXML
  private DatePicker dateFin;
  

  @FXML
  private HBox hboxCarusel;

  // Ventana emergente que aparece cuando hay resultados de búsqueda.
  private final Popup popup = new Popup();

  //  Lista sincronizada con popup para mostrar los resultados sugeridos.
  private final ListView<Hotel> sugerencias = new ListView<>();

  
  // Boolean auxiliar para cancelar evento de teclado en txtBuscar.
  private boolean activar;
  private boolean hayResultados;

  /**
    Implementación del método de Initializable.
    Encarga de dar configuración inicial a elementos gráficos como DatePicker, TextField, Popup, etc.
    También hace una búsqueda inicial en la base de datos para llenar un carusel de hoteles.
  */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarCalendarios();
    llenarCarusel();
    popup.getContent().add(sugerencias);
    ConfRepetitiva.confListaSugerencia(sugerencias, txtBuscar);

    lblAvatar.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          irACuenta();
        } catch (Exception ex) {
          log.error("Error al navegar a la interfaz de cuenta de usuario", ex);
        }
      }
    });

    String[] filtrado = new String[5];
    txtBuscar.textProperty().addListener((obx, oldText, newText) -> {
      if (App.future != null && !App.future.isDone()) {
        App.future.cancel(false);
      }

      App.future = App.scheduler.schedule(() -> {
        if (activar) {
          activar = false;
          return;
        }
        if (newText.isEmpty()) return;
        
        String tokens[] = newText.split(" ");
        int j = 0;
        for (int i = 0; i < tokens.length; i++) {
          if (tokens[i].length() > 3) {
            filtrado[j] = tokens[i];
            j++;
            if (j >= 5) break;
          }
        }
        while (j < 5) {
          filtrado[j] = null;
          j++;
        }
        List<Hotel> resultados = BusquedaServicio.buscarHotel(filtrado, dateInicio.getValue(), dateFin.getValue());

        // Actualiza la interfaz del usuario para mostrar los resultados en el popup
        Platform.runLater(() -> {
          if (newText == null || newText.isEmpty() || resultados == null || resultados.isEmpty()) {
            popup.hide();
            hayResultados = false;
            sugerencias.getItems().clear();
          } else {
            hayResultados = true;
            sugerencias.setItems(FXCollections.observableArrayList(resultados));
            if (!popup.isShowing()) {
              popup.show(txtBuscar,
              txtBuscar.localToScreen(0, txtBuscar.getHeight()).getX(),
              txtBuscar.localToScreen(0, txtBuscar.getHeight()).getY());
            }
          }
        });
      }, 400, TimeUnit.MILLISECONDS);
    });

    txtBuscar.setOnMouseClicked(e -> {
      if (!popup.isShowing() && hayResultados) {
        popup.show(txtBuscar,
        txtBuscar.localToScreen(0, txtBuscar.getHeight()).getX(),
        txtBuscar.localToScreen(0, txtBuscar.getHeight()).getY());
      }
    });

    txtBuscar.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        if (!hayResultados) return;
        popup.show(txtBuscar,
        txtBuscar.localToScreen(0, txtBuscar.getHeight()).getX(),
        txtBuscar.localToScreen(0, txtBuscar.getHeight()).getY());
      } else {
        popup.hide();
      }
    });

    // #region evento para btnBuscar
    txtBuscar.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        try {
          buscarHotel();
        } catch (Exception ex) {
          log.error("Error al navegar a la UI de resultados de búsqueda", ex);
        }
      }
    });
    // #endregion

    // #region eventos para la lista de sugerencia
    sugerencias.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        if (popup.isShowing()) {
          popup.hide();
        }
      } else if (e.getCode() == KeyCode.TAB) {
        e.consume();
        if (e.isShiftDown()) {
          lblAvatar.requestFocus();
        } else {
          dateInicio.requestFocus();
        }
        popup.hide();
      } else if (e.getCode() == KeyCode.ENTER) {
        Hotel selected = sugerencias.getSelectionModel().getSelectedItem();
        if (selected != null) {
          activar = true;
          txtBuscar.setText(selected.getNombre());
          popup.hide();
        }
      }
    });
    sugerencias.setOnMouseClicked(e -> {
      Hotel selected = sugerencias.getSelectionModel().getSelectedItem();
      if (selected != null) {
        activar = true;
        txtBuscar.setText(selected.getNombre());
        popup.hide();
      }
    });
    // #endregion
  }

  private void configurarCalendarios() {
    ConfRepetitiva.confEstiloCalendario(dateInicio);
    ConfRepetitiva.confEstiloCalendario(dateFin);
    ConfRepetitiva.confCalendarios(dateInicio, dateFin);
  }


  /**
    Carga tarjetas de hoteles al azar en el componente hboxCarusel.
    @see TarjetaCaruselContr
  */
  private void llenarCarusel() {
    List<Hotel> hoteles = BusquedaServicio.hotelesAlazar(6);
    for (int i = 0; i < hoteles.size(); i++) {
      Hotel hotel = hoteles.get(i);
      Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl());
      try {
        String url = Files.list(carpeta).sorted().map(path -> path.toUri().toString()).findFirst().orElseThrow();
        FXMLLoader loader = new FXMLLoader(Rutas.TARJETA_CARUSEL.getUrlVista());
        Parent card = loader.load();
        TarjetaCaruselContr contr = loader.getController();
        setData(contr, hotel, url);
        hboxCarusel.getChildren().add(card);
      } catch (IOException e) {
        log.error("Error al crear tarjetas de carusel", e);
      }
    }
  }

  private void setData(TarjetaCaruselContr contr, Hotel hotel, String imagenURL) {
    contr.getRoot().setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        verDetalles(hotel);
      }
    });
    contr.getRoot().setOnMouseClicked(e -> {
      verDetalles(hotel);
    });

    ConfRepetitiva.setBackground(contr.getLblImagen(), imagenURL);
    contr.getLblTitulo().setText(hotel.getNombre());
    contr.getLblPais().setText(hotel.getCiudad().getNombre() + ", " + hotel.getCiudad().getPais());
    ConfRepetitiva.confEstrellas(contr.getLblEstrellas(), hotel.getEstrellas());
  }

  private void verDetalles(Hotel hotel) {
    if (App.getVista(Rutas.DETALLES_HOTEL) == null) {
      App.setVista(Rutas.DETALLES_HOTEL);
    }
    ((DetallesController) App.getControlador(Rutas.DETALLES_HOTEL)).setData(hotel, null, null); 
    App.navegar(Rutas.DETALLES_HOTEL);
  }

  /**
  * Versión de preuba, evento que cambia de plantilla fxml.
  */
  @FXML
  private void buscarHotel() {
    if (sugerencias.getItems().isEmpty()) return;
    if (App.getVista(Rutas.RESULTADOS) == null) {
      App.setVista(Rutas.RESULTADOS);
    }
    ((ResultadosContr) App.getControlador(Rutas.RESULTADOS)).inicarTarjetas(sugerencias.getItems());
    App.navegar(Rutas.RESULTADOS);
  }
  
  /**
  * Cambia a la plantilla de cuenta en donde se muestran los detalles de usuario.
  * @see ClienteInfoContr
  */
  @FXML
  private void irACuenta() {
    if (App.getVista(Rutas.CLIENTE_INFO) == null) {
      App.setVista(Rutas.CLIENTE_INFO);
    }
    App.navegar(Rutas.CLIENTE_INFO);
    ((ClienteInfoContr) App.getControlador(Rutas.CLIENTE_INFO)).llenarTabla();
  }
  
  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 Inicio eliminado por GC\n\n");
  }
}