package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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

  private final Popup popup = new Popup();
  private final ListView<Hotel> sugerencias = new ListView<>();
  private boolean activar;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarCalendarios();
    llenarCarusel();
    congigurarListaSugerencias();
    popup.getContent().add(sugerencias);
    popup.setAutoHide(true);

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
        List<String> filtrado = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
          if (tokens[i].length() > 3) {
            filtrado.add(tokens[i]);
            if (filtrado.size() > 5) break;
          }
        }
        List<Hotel> filtrar = BusquedaServicio.buscarHotel(filtrado, dateInicio.getValue(), dateFin.getValue());

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
      "-fx-background-color: rgba(255,255,255,0.05);" + // fondo translúcido
      "-fx-border-color: #6c4dc2;" +                    // borde lavanda claro
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

    sugerencias.prefWidthProperty().bind(txtBuscar.widthProperty());
    sugerencias.setPrefHeight(160);
    sugerencias.maxHeight(160);
  }

  private void llenarCarusel() {
    try {
      for (int i = 0; i < 6; i++) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/tarjeta-carusel.fxml"));
        Parent card = loader.load();
        TarjetaCaruselContr contr = loader.getController();
        contr.setData("titulo: " + i, "alguno: " + i + 1);
        hboxCarusel.getChildren().add(card);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void buscarHotel() throws IOException {
    if (App.resultadosRoot == null) {
      App.resultadosRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/resultados.fxml"));
    }
    App.navegar(App.resultadosRoot);
  }
  
  @FXML
  private void irACuenta() throws IOException {
    if (App.clienteInfo == null) {
      App.clienteInfo = FXMLLoader.load(getClass().getResource("/com/example/hotel/cliente-info.fxml"));
    }
    App.navegar(App.clienteInfo);
  }
}