package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.example.hotel.App;
import com.example.hotel.auxiliar.Calendario;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.service.BusquedaServicio;
import com.example.hotel.util.Imagenes;
import com.example.hotel.util.Rutas;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

/**
* Clase de inicio a la aplicación, una vez que se inicia sesión o se crea una cuenta
* esta clase instancia componentes de la página de bienvenida.
* Implementa Initializable para instancar los componentes gráficos.
* @see Initializable
*/
public class InicioController implements Initializable {


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

  private ScheduledFuture<?> ttlTask;
  
  // Ventana emergente que aparece cuando hay resultados de búsqueda.
  private final Popup popup = new Popup();

  //  Lista sincronizada con popup para mostrar los resultados sugeridos.
  private final ListView<Hotel> sugerencias = new ListView<>();

  
  // Boolean auxiliar para cancelar evento de teclado en txtBuscar.
  private boolean activar;
  private boolean hayResultados;
  private ResultadosContr resultadosContr;

  /**
    Implementación del método de Initializable.
    Encarga de dar configuración inicial a elementos gráficos como DatePicker, TextField, Popup, etc.
    También hace una búsqueda inicial en la base de datos para llenar un carusel de hoteles.
  */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarCalendarios();
    llenarCarusel();
    configurarListaSugerencias();
    popup.getContent().add(sugerencias);

    lblAvatar.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          irACuenta();
        } catch (Exception ex) {
          ex.printStackTrace();
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
          ex.printStackTrace();
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
    Calendario.confEstilo(dateInicio);
    Calendario.confEstilo(dateFin);
    Calendario.confCalendarios(dateInicio, dateFin);
  }


  private void configurarListaSugerencias() {
    sugerencias.setStyle(
      "-fx-background-color: rgba(255,255,255,0.05);" +
      "-fx-border-color: #6c4dc2;" +
      "-fx-border-width: 1;" +
      "-fx-background-radius: 8;" +
      "-fx-border-radius: 8;" +
      "-fx-padding: 4;" +
      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0.0, 0, 2);"
    );
    sugerencias.setCellFactory(lv -> {
      return new ListCell<>() {
        @Override
        protected void updateItem(Hotel item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(null);
            setStyle(
              "-fx-background-color: rgba(255, 255, 255, 0.08);" +
              "-fx-font-size: 13px;" +
              "-fx-padding: 6 12;" +
              "-fx-background-radius: 4;"
            );
          } else {
            setText(item.getNombre());
            setTextFill(Color.WHITE);
            
            if (isSelected()) {
              setStyle(
                """
                -fx-background-color: rgba(255, 255, 255, 0.3);
                -fx-font-size: 14px; 
                -fx-padding: 6 12; 
                -fx-background-radius: 4
                """
              );
            } else {
              setStyle(
                """
                  -fx-background-color: rgba(255, 255, 255, 0.08);
                  -fx-font-size: 14px;
                  -fx-padding: 6 12;
                  -fx-background-radius: 4;
                """
              );
            }
            
            setOnMouseEntered(e -> setStyle(
              "-fx-background-color: rgba(255, 255, 255, 0.2);" +
              "-fx-font-size: 14px;" +
              "-fx-padding: 6 12;" +
              "-fx-background-radius: 4;"
            ));
            setOnMouseExited(e -> setStyle(
              "-fx-background-color: rgba(255, 255, 255, 0.08);" +
              "-fx-font-size: 14px;" +
              "-fx-padding: 6 12;" +
              "-fx-background-radius: 4;"
            ));
          }
        }
      };
    });

    sugerencias.prefWidthProperty().bind(txtBuscar.widthProperty()); //vincula la anchura con la del txtBuscar
    sugerencias.setPrefHeight(160);
    sugerencias.maxHeight(160);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/tarjeta-carusel.fxml"));
        Parent card = loader.load();
        TarjetaCaruselContr contr = loader.getController();
        contr.setData(hotel, url);
        hboxCarusel.getChildren().add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  /**
  * Versión de preuba, evento que cambia de plantilla fxml.
  * @throws IOException
  */
  @FXML
  private void buscarHotel() throws IOException {
    if (sugerencias.getItems().isEmpty()) return;
    if (App.getVista(Rutas.RESULTADOS) == null) {
      FXMLLoader loader = new FXMLLoader(Rutas.RESULTADOS.getUrlVista());
      Parent parent = loader.load();
      resultadosContr = loader.getController();
      App.setVista(Rutas.RESULTADOS, parent);
    }

    resultadosContr.inicarTarjetas(sugerencias.getItems());
    App.navegar(ttlTask, Rutas.RESULTADOS);
  }
  
  /**
  * Cambia a la plantilla de cuenta en donde se muestran los detalles de usuario.
  * @see ClienteInfoContr
  * @throws IOException
  */
  @FXML
  private void irACuenta() throws IOException {
    if (App.getVista(Rutas.CLIENTE_INFO) == null) {
      // App.clienteInfo = FXMLLoader.load(getClass().getResource("/com/example/hotel/cliente-info.fxml"));
      App.setVista(Rutas.CLIENTE_INFO);
    }
    App.navegar(ttlTask, Rutas.CLIENTE_INFO);
  }
}