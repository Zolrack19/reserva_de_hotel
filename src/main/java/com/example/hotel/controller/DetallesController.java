package com.example.hotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import com.example.hotel.App;
import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.service.BusquedaServicio;
import com.example.hotel.util.Imagenes;
import com.example.hotel.util.Rutas;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.control.RangeSlider;

/**
 * Controla la plantilla de los detalles de hotel, encargado de mostrar
 * información más específica y es la parte final para ir al formulario de
 * Cuarto de hotel.
 */
public class DetallesController implements Initializable {

  private Hotel hotel;

  @FXML
  private Label lblVolver;
  
  @FXML
  private VBox vboxFotosContainer;

  @FXML
  private Label lblImagen1;

  @FXML
  private Label lblImagen2;

  @FXML
  private Label lblImagen3;

  @FXML
  private Label lblImagen4;

  @FXML
  private Label lblImagen5;

  @FXML
  private Label lblImagen6;

  @FXML
  private Label lblImagen7;
  
  @FXML
  private Label lblNombre;
  
  @FXML
  private Label lblEstrellas;
  
  @FXML
  private Label lblDescripcion;  
  
  @FXML
  private Label lblVerMenos;

  @FXML
  private DatePicker dateInicio;
  
  @FXML
  private DatePicker dateFin;
  
  @FXML
  private Spinner<Integer> spnCapacidad;

  @FXML
  private VBox vboxPresupuesto;

  @FXML
  private Label lblDivisa1;

  @FXML
  private Label lblDivisa2;

  @FXML
  private Label lblMinValor;
  
  @FXML
  private Label lblMaxValor;
  
  @FXML
  private TableView<Cuarto> tblCuartos;

  private Label[] labels;
  private byte estrella;
  private boolean lblOculto;
  private Stage modalCuarto;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblVolver.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          volver();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
    labels = new Label[] { lblImagen1, lblImagen2, lblImagen3, lblImagen4, lblImagen5, lblImagen6, lblImagen7 };
    lblVerMenos.setVisible(false);
    lblVerMenos.setManaged(false);
    lblOculto = true;

    lblVerMenos.setOnMouseClicked(e -> {
      lblDescripcion.setMinHeight(150);
      lblVerMenos.setVisible(false);
      lblVerMenos.setManaged(false);
      lblOculto = true;
    });

    lblDescripcion.setOnMouseClicked(e -> {
      if (lblOculto) {
        lblDescripcion.setMinHeight(Label.USE_PREF_SIZE);
        lblVerMenos.setVisible(true);
        lblVerMenos.setManaged(true);
        lblOculto = false;
      }
    });

    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
    spnCapacidad.setValueFactory(valueFactory);

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

    configurarTabla();
  }


  @SuppressWarnings("unchecked")
  private void configurarTabla() {
    TableColumn<Cuarto, Integer> colId = new TableColumn<>("Id");
    colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colId.setPrefWidth(100);
    colId.setReorderable(false);
    
    TableColumn<Cuarto, String> colfechaEntrada = new TableColumn<>("N° habitación");
    colfechaEntrada.setCellValueFactory(new PropertyValueFactory<>("numero"));
    colfechaEntrada.setPrefWidth(200);
    colfechaEntrada.setReorderable(false);
    
    TableColumn<Cuarto, BigDecimal> colfechaSalida = new TableColumn<>("Precio por noche");
    colfechaSalida.setCellValueFactory(new PropertyValueFactory<>("precioPorNoche"));
    colfechaSalida.setPrefWidth(200);
    colfechaSalida.setReorderable(false);

    TableColumn<Cuarto, Byte> estrellas = new TableColumn<>("Puntuación");
    estrellas.setCellValueFactory(new PropertyValueFactory<>("estrellas"));
    estrellas.setPrefWidth(200);
    estrellas.setReorderable(false);
    
    tblCuartos.getColumns().addAll(colId, colfechaEntrada, colfechaSalida, estrellas);
    tblCuartos.setFixedCellSize(35);
    tblCuartos.prefHeightProperty().bind(
      Bindings.size(tblCuartos.getItems()).multiply(tblCuartos.getFixedCellSize()).add(35)
    );

    Cuarto[] cuartoAnterior = new Cuarto[1];
    tblCuartos.setOnMouseClicked(event -> {
      if (event.getClickCount() == 1) { // o 2 para doble clic
        Cuarto seleccionado = tblCuartos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
          if (modalCuarto == null) {
            crearModal();
          }
          if (cuartoAnterior[0] != seleccionado) {
            ((ModalCuartoContr) App.getControlador(Rutas.MODAL_CUARTO)).setData(hotel, seleccionado);
            cuartoAnterior[0] = seleccionado;
          }
          modalCuarto.show();
        }
      }
    });
  }

  private void crearModal() {
    App.setVista(Rutas.MODAL_CUARTO);
    modalCuarto = new Stage();
    modalCuarto.setScene(new Scene(App.getVista(Rutas.MODAL_CUARTO)));
    modalCuarto.setTitle("Detalles de habitación");
    modalCuarto.initModality(Modality.WINDOW_MODAL);
    modalCuarto.initOwner(App.primaryStage);
    modalCuarto.setResizable(false);
  }


  public void setData(Hotel hotel) {
    if (this.hotel == null || this.hotel != hotel) {
      this.hotel = hotel;
      prepararPlantilla();
    }
  }

  private void prepararPlantilla() {
    tblCuartos.getItems().clear();
    List<Cuarto> cuartos = BusquedaServicio.getCuartos(hotel);
    for (int i = 0; i < cuartos.size(); i++) {
      tblCuartos.getItems().add(cuartos.get(i));
    }

    lblNombre.setText(hotel.getNombre());
    if (estrella != hotel.getEstrellas()) {
      estrella = (byte) hotel.getEstrellas();
      lblEstrellas.setPrefWidth(23.5 * estrella);
      lblEstrellas.setStyle(
        "-fx-background-image: url('" + Imagenes.ESTRELLA.getUrl() + "');" +
        "-fx-background-repeat: repeat-x;" +
        "-fx-background-position: left center;"
      );
    }
    lblDescripcion.setText(hotel.getDescripcion());
    Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl());
    try {
      Object[] urls = Files.list(carpeta).filter(Files::isRegularFile).sorted().limit(7).map(path -> path.toUri().toString()).toArray();
      for (int i = 0; i < urls.length; i++) {
        labels[i].setStyle(
          "-fx-background-image: url('" + urls[i] + "');" +
          "-fx-background-repeat: no-repeat;" +
          "-fx-background-position: center center;" +
          "-fx-background-size: cover;"
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void volver() throws IOException {
    if (App.getVista(Rutas.RESULTADOS) == null) {
      if (App.getVista(Rutas.INICIO) == null) {
        App.setVista(Rutas.INICIO);
      }
      App.navegar(Rutas.INICIO);
    } else {
      App.navegar(Rutas.RESULTADOS);
    }
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 Detalles eliminado por GC\n\n");
  }

}
