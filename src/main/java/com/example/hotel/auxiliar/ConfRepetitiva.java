package com.example.hotel.auxiliar;

import java.time.LocalDate;

import com.example.hotel.dominio.Hotel;
import com.example.hotel.singleton.Imagenes;

import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class ConfRepetitiva {
  
  public static void confListaSugerencia(ListView<Hotel> sugerencias, TextField txtBuscar) {
    sugerencias.setStyle(
      "-fx-background-color: rgba(27, 4, 78, 0.86);" +
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

    sugerencias.prefWidthProperty().bind(txtBuscar.widthProperty());
    sugerencias.setPrefHeight(160);
    sugerencias.maxHeight(160);
  }

    public static void confEstiloCalendario(DatePicker datePicker) {
    datePicker.setDayCellFactory(picker -> new DateCell() {
      @Override
      public void updateItem(LocalDate date, boolean empty) {
          super.updateItem(date, empty);
        if (empty) return;
      
        if (date.isBefore(LocalDate.now())) {
          setDisable(true);
          setStyle("-fx-background-color: #EEEEEE;");
          // setStyle("-fx-background-color:rgba(16, 0, 61, 0.6); -fx-border-color: rgba(24, 22, 129, 0.6);");
        }
      }
    });
  }

  public static void confCalendarios(DatePicker dateInicio, DatePicker dateFin) {
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

  public static void setBackground(Node nodo, String url) {
    nodo.setStyle(
      "-fx-background-image: url('" + url + "');" +
      "-fx-background-repeat: no-repeat;" +
      "-fx-background-position: center center;" +
      "-fx-background-size: cover;"
    );
  }

  public static void confEstrellas(Label lblEstrellas, int cantidad) {
    lblEstrellas.setPrefWidth(23.5 * cantidad);
    lblEstrellas.setStyle(
      "-fx-background-image: url('" + Imagenes.ESTRELLA.getUrl() + "');" +
      "-fx-background-repeat: repeat-x;" +
      "-fx-background-position: left center;"
    );
  }
}
