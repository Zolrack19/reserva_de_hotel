package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.example.hotel.App;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.service.BusquedaServicio;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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
  private TextField txtBuscar;
  
  @FXML
  private DatePicker dateInicio;
  
  @FXML
  private DatePicker dateFin;
  
  @FXML
  private Button btnBuscar;

  @FXML
  private HBox hboxCarusel;

  /*
    Ventana emergente que aparece cuando hay resultados de búsqueda.
  */
  private final Popup popup = new Popup();
  /*
    Lista sincronizada con popup para mostrar los resultados sugeridos.
  */
  private final ListView<Hotel> sugerencias = new ListView<>();

  /*
    Boolean auxiliar para cancelar evento de teclado en txtBuscar.
  */
  private boolean activar;

  /*
    Implementación del método de Initializable.
    Encarga de dar configuración inicial a elementos gráficos como DatePicker, TextField, Popup, etc.
    También hace una búsqueda inicial en la base de datos para llenar un carusel de hoteles.
  */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarCalendarios();
    llenarCarusel();
    congigurarListaSugerencias();
    popup.getContent().add(sugerencias);
    popup.setAutoHide(true);

    // Evento de teclado en el que se programa una tarea asíncrona con un delay de 400 milisegundos para hacer búsquedas en la base de datos.
    txtBuscar.textProperty().addListener((obx, oldText, newText) -> {
       // Si el usuario activa este evento antes de 400 milisegundos, la tarea se cancela y se programa una nueva
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
        List<String> filtrado = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
          if (tokens[i].length() > 3) {
            filtrado.add(tokens[i]);
            if (filtrado.size() > 5) break;
          }
        }
        List<Hotel> filtrar = BusquedaServicio.buscarHotel(filtrado, dateInicio.getValue(), dateFin.getValue());

        // Actualiza la interfaz del usuario para mostrar los resultados en el popup
        Platform.runLater(() -> {
          if (newText == null || newText.isEmpty()) {
            popup.hide();
          } else {
            if (filtrar == null || filtrar.isEmpty()) {
              popup.hide();
            } else {
              sugerencias.setItems(FXCollections.observableArrayList(filtrar));
              if (!popup.isShowing()) {
                popup.show(txtBuscar,
                txtBuscar.localToScreen(0, txtBuscar.getHeight()).getX(),
                txtBuscar.localToScreen(0, txtBuscar.getHeight()).getY());
              }
            }
          }
        });
      }, 400, TimeUnit.MILLISECONDS);

    });

    // #region eventos para btnBuscar
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
      if (e.getCode() == KeyCode.ENTER) {
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
    dateInicio.setDayCellFactory(picker -> new DateCell() {
      @Override
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        if (empty) return;

        if (date.isBefore(LocalDate.now())) {
          setDisable(true);
          setStyle("-fx-background-color: #EEEEEE;");
        }
      }
    });
    
    dateFin.setDayCellFactory(picker -> new DateCell() {
      @Override
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        if (date.isBefore(LocalDate.now())) {
          setDisable(true);
          setStyle("-fx-background-color: #EEEEEE;");
        }
      }
    });

    dateInicio.showingProperty().addListener((obs, wasShowing, isShowing) -> {
      if (!isShowing) {
        if (dateFin.getValue() == null && dateInicio.getValue() != null) {
          dateFin.setValue(dateInicio.getValue().plusDays(1));
        }
      }
    });

    dateFin.showingProperty().addListener((obs, wasShowing, isShowing) -> {
      if (!isShowing) {
        if (dateInicio.getValue() == null) {
          dateFin.setValue(null);
        }
      }
    });

    dateInicio.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (dateFin.getValue() != null && newVal != null && (newVal.isAfter(dateFin.getValue()) || newVal.isEqual(dateFin.getValue()))) {
        dateFin.setValue(newVal.plusDays(1));
      }
    });
  
    dateFin.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (dateInicio.getValue() != null && newVal != null && (newVal.isBefore(dateInicio.getValue()) || newVal.isEqual(dateInicio.getValue()))) {
        dateInicio.setValue(newVal);
        dateFin.setValue(newVal.plusDays(1));
      }
    });
  }


  private void congigurarListaSugerencias() {
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
            setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.08);" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 6 12;" +
                "-fx-background-radius: 4;"
            );
            
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
      Path carpeta = Paths.get(hotel.getImagenUrl());
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
    if (App.resultadosRoot == null) {
      App.resultadosRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/resultados.fxml"));
    }
    App.navegar(App.resultadosRoot);
  }
  
  /**
  * Cambia a la plantilla de cuenta en donde se muestran los detalles de usuario.
  * @see ClienteInfoContr
  * @throws IOException
  */
  @FXML
  private void irACuenta() throws IOException {
    if (App.clienteInfo == null) {
      App.clienteInfo = FXMLLoader.load(getClass().getResource("/com/example/hotel/cliente-info.fxml"));
    }
    App.navegar(App.clienteInfo);
  }
}