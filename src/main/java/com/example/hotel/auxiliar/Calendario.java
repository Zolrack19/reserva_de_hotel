package com.example.hotel.auxiliar;

import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

public class Calendario {
  
  public static void confEstilo(DatePicker datePicker) {
    datePicker.setDayCellFactory(picker -> new DateCell() {
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

}
