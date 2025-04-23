package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.example.hotel.App;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

public class InicioController implements Initializable {
  @FXML
  private Button btnBuscar;

  @FXML
  private TextField txtBuscar;

  @FXML
  private HBox hboxCarusel;

  private final Popup popup = new Popup();
  private final ListView<String> sugerencias = new ListView<>();
  private final List<String> items = Arrays.asList("Java", "JavaFX", "JavaScript", "JDBC", "JSP");
  private boolean activar;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    llenarCarusel();
    congigurarListaSugerencias();
    popup.getContent().add(sugerencias);
    popup.setAutoHide(true);

    txtBuscar.textProperty().addListener((obx, oldText, newText) -> {
      if (activar) {
        activar = false;
        return;
      }

      if (App.future != null && !App.future.isDone()) {
        App.future.cancel(false);
      }

      App.future = App.scheduler.schedule(() -> {
        List<String> filtrar = items.stream()
            .filter(item -> item.toLowerCase().contains(newText.toLowerCase()))
            .collect(Collectors.toList());

        Platform.runLater(() -> {
          if (newText == null || newText.isEmpty()) {
            popup.hide();
          } else {
            if (filtrar.isEmpty()) {
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
        String selected = sugerencias.getSelectionModel().getSelectedItem();
        if (selected != null) {
          activar = true;
          txtBuscar.setText(selected);
          popup.hide();
        }
      }
    });
    sugerencias.setOnMouseClicked(e -> {
      String selected = sugerencias.getSelectionModel().getSelectedItem();
      if (selected != null) {
        activar = true;
        txtBuscar.setText(selected);
        popup.hide();
      }
    });
    // #endregion
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
        protected void updateItem(String item, boolean empty) {
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
            setText(item);
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
}