package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.RangeSlider;
import com.example.hotel.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ResultadosContr implements Initializable {

  @FXML
  private Button btnAtras;

  @FXML
  private DatePicker dateInicio;
  
  @FXML
  private DatePicker dateFin;
  
  @FXML
  private VBox vboxFiltros;
  
  @FXML
  private Label lblMinValor;
  
  @FXML
  private Label lblMaxValor;

  @FXML
  private VBox vboxPresupuesto;
  
  @FXML
  private VBox vboxCategoria;

  @FXML
  private VBox vboxResultados;

  @FXML
  private Button btnBuscar;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    configurarCalendarios();
    
    lblMinValor.setText("10");
    lblMaxValor.setText("150");

    RangeSlider rangeSlider = new RangeSlider(10, 500, 10, 150);
    rangeSlider.setMajorTickUnit(10);
    rangeSlider.setMinorTickCount(0);
    rangeSlider.setBlockIncrement(10);
    rangeSlider.setSnapToTicks(true);

    rangeSlider.lowValueProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal.doubleValue() > rangeSlider.getHighValue() - 50) {
        rangeSlider.setLowValue(rangeSlider.getHighValue() - 50);
        return;
      }
      lblMinValor.setText(String.valueOf(newVal.intValue()));
    });

    rangeSlider.highValueProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal.doubleValue() < rangeSlider.getLowValue() + 50) {
        rangeSlider.setHighValue(rangeSlider.getLowValue() + 50);
        return;
      }
      if (newVal.intValue() != 500) {
        lblMaxValor.setText(String.valueOf(newVal.intValue()));
      } else {
        lblMaxValor.setText(newVal.intValue() + "+");
      }
    });

    vboxPresupuesto.getChildren().add(rangeSlider);

    try {
      for (int i = 0; i < 5; i++) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/tarjeta-resultado.fxml"));
        Parent card = loader.load();
        TarjetaResultadoContr contr = loader.getController();
        contr.setData("Esto es un título",
          "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n" +
          "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
          "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
          "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
          "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
          "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.quam.Lorem ipsum dolor sit amet consectetur, adipisicing elit. Qui modi nobis laudantium voluptatum ullam adipisci, autem\n"+
          "aliquid ad porro natus magnam repellendus impedit ab dolor? Illo, incidunt ea. Quia, quam.");
        vboxResultados.getChildren().add(card);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void volverInicio() throws IOException {
    App.navegar(App.inicioRoot);
  }

  @FXML
  public void buscar() throws IOException {
    if (App.detallesRoot == null) {
      App.detallesRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/detalles-hotel.fxml"));
    }
    App.navegar(App.detallesRoot);
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

}