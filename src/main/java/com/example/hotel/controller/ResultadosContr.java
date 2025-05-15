package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.RangeSlider;
import com.example.hotel.App;
import com.example.hotel.dominio.Categoria;
import com.example.hotel.service.BusquedaServicio;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
  Clase que se encarga de la plantilla de resultados, plantilla en la que se muestran los
  resultados de búsqueda de hoteles.
  Usa un controlador interno para instanciar las cartas de resultados de hoteles.
  @see TarjetaResultadoContr
*/
public class ResultadosContr implements Initializable {

  @FXML
  private DatePicker dateInicio;
  
  @FXML
  private DatePicker dateFin;
  
  @FXML
  private VBox vboxFiltros;
  
  @FXML
  private Label lblDivisa1;
  
  @FXML
  private Label lblDivisa2;
  
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

  /**
    Método para  instanciar los elementos gráficos del fxml asociado, se encarga de hacer configurarciones
    como la validación de fechas en los DatePicker y asignar eventos de mouse y teclado, así como crear otros
    componentes gráficos desde código java, ejemplo: RangeSlider.
  */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarCalendarios();
    lblDivisa1.setText(App.cliente.getPais().getDivisa().getSimbolo());
    lblDivisa2.setText(App.cliente.getPais().getDivisa().getSimbolo());
    lblMinValor.setText("10");
    lblMaxValor.setText("150");

    List<Categoria> categorias = BusquedaServicio.getCategorias();
    for (int i = 0; i < categorias.size(); i++) {
      CheckBox checkBox = new CheckBox(categorias.get(i).getNombre());
      vboxCategoria.getChildren().add(checkBox);
    }

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
      lblMinValor.setText(String.format("%,d", newVal.intValue()));
    });

    rangeSlider.highValueProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal.doubleValue() < rangeSlider.getLowValue() + 50) {
        rangeSlider.setHighValue(rangeSlider.getLowValue() + 50);
        return;
      }
      if (newVal.intValue() != 500) {
        lblMaxValor.setText(String.format("%,d", newVal.intValue()));
      } else {
        lblMaxValor.setText(String.format("%,d%s", newVal.intValue(), "+"));
      }
    });

    vboxPresupuesto.getChildren().add(rangeSlider);
    
    // prueba de lo que sería la generación de las tarjetas de resultados de búsqueda de hoteles
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
  private void volver() throws IOException {
    App.navegar(App.inicioRoot);
  }

  /**
    Implementación de prueba del botón de buscar, solo se encarga de cambiar de plantilla, fxml.
    @throws IOException
  */
  @FXML
  public void buscar() throws IOException {
    if (App.detallesRoot == null) {
      App.detallesRoot = FXMLLoader.load(getClass().getResource("/com/example/hotel/detalles-hotel.fxml"));
    }
    App.navegar(App.detallesRoot);
  }

  private void configurarCalendarios() {
    // Se deshabilitan las fechas que sean menores a las de la fecha actual.
    // Esto para ambos DatePicker
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

    // Evento, si se selecciona una fecha de inicio y no hay una fecha de fin, se le asigna 
    // una fecha del día siguiente en fecha de fin
    dateInicio.showingProperty().addListener((obs, wasShowing, isShowing) -> {
      if (!isShowing) {
        if (dateFin.getValue() == null && dateInicio.getValue() != null) {
          dateFin.setValue(dateInicio.getValue().plusDays(1));
        }
      }
    });

    // No se permite seleccionar una fecha de fin sin antes haber una fecha de inicio seleccionado
    dateFin.showingProperty().addListener((obs, wasShowing, isShowing) -> {
      if (!isShowing) {
        if (dateInicio.getValue() == null) {
          dateFin.setValue(null);
        }
      }
    });

    // Evento para que la selección de fecha de inicio no sea mayor que la fecha de fin
    dateInicio.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (dateFin.getValue() != null && newVal != null && (newVal.isAfter(dateFin.getValue()) || newVal.isEqual(dateFin.getValue()))) {
        dateFin.setValue(newVal.plusDays(1));
      }
    });
  
    // Evento para que la selección de fecha de fin no sea menor que la fecha de inicio
    dateFin.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (dateInicio.getValue() != null && newVal != null && (newVal.isBefore(dateInicio.getValue()) || newVal.isEqual(dateInicio.getValue()))) {
        dateInicio.setValue(newVal);
        dateFin.setValue(newVal.plusDays(1));
      }
    });
  }

}