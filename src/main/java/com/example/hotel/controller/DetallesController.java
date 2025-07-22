package com.example.hotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.example.hotel.App;
import com.example.hotel.auxiliar.ConfRepetitiva;
import com.example.hotel.dominio.Comentario;
import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.service.BusquedaServicio;
import com.example.hotel.service.ComentarioServicio;
import com.example.hotel.singleton.Imagenes;
import com.example.hotel.singleton.Rutas;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


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
  private VBox vboxComentarios;
  
  @FXML
  private TextField txtComentario;
  
  @FXML
  private Button btnSend;
  
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
  private Button btnBuscar;

  
  @FXML
  private TableView<Cuarto> tblCuartos;

  private Label[] labels;
  private byte estrella;
  private boolean lblOculto;
  private Stage modalCuarto;
  private String imagen;

  private LocalDate fechaEntrada;
  private LocalDate fechaSalida;


  private Cuarto[] cuartoAnterior;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ConfRepetitiva.confEstiloCalendario(dateInicio);
    ConfRepetitiva.confEstiloCalendario(dateFin);
    ConfRepetitiva.confCalendarios(dateInicio, dateFin);

    btnBuscar.setOnMouseClicked(e -> {
      buscar();
    });
    btnBuscar.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        buscar();
      }
    });
    
    btnSend.setOnMouseClicked(e -> {
      String comentario = txtComentario.getText().trim();
      if (comentario.isEmpty()) return;
      nuevoComentario(comentario);
    });
    btnSend.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        String comentario = txtComentario.getText().trim();
        if (comentario.isEmpty()) return;
        nuevoComentario(comentario);
      }
    });


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

    configurarTabla();
  }

  private void nuevoComentario(String comentario) {
    try {
      ComentarioServicio.crearComentario(hotel, App.cliente, comentario);
      Parent parent = crearComentario(App.cliente.getApellido(), comentario);
      vboxComentarios.getChildren().add(parent);
      txtComentario.setText("");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private Parent crearComentario(String usuario, String contenido) throws IOException {
    FXMLLoader loader = new FXMLLoader(Rutas.COMENTARIO.getUrlVista());
    Parent card = loader.load();
    ComentarioContr contr = loader.getController();
    contr.init(usuario, contenido);
    return card;
  }

  @SuppressWarnings("unchecked")
  private void configurarTabla() {
    TableColumn<Cuarto, Integer> colId = new TableColumn<>("Id");
    colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colId.setPrefWidth(100);
    colId.setReorderable(false);

    TableColumn<Cuarto, String> colNombre = new TableColumn<>("Habitación");
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    colNombre.setPrefWidth(200);
    colNombre.setReorderable(false);
    
    TableColumn<Cuarto, String> colNHabitacion = new TableColumn<>("N° habitación");
    colNHabitacion.setCellValueFactory(new PropertyValueFactory<>("numero"));
    colNHabitacion.setPrefWidth(200);
    colNHabitacion.setReorderable(false);
    
    TableColumn<Cuarto, BigDecimal> colPrecio = new TableColumn<>("Precio por noche");
    colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioPorNoche"));
    colPrecio.setPrefWidth(200);
    colPrecio.setReorderable(false);

    TableColumn<Cuarto, Byte> colCapacidad = new TableColumn<>("Capacidad");
    colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
    colCapacidad.setPrefWidth(200);
    colCapacidad.setReorderable(false);
    
    tblCuartos.getColumns().addAll(colId, colNombre, colNHabitacion, colPrecio, colCapacidad);
    tblCuartos.setFixedCellSize(35);
    tblCuartos.prefHeightProperty().bind(
      Bindings.size(tblCuartos.getItems()).multiply(tblCuartos.getFixedCellSize()).add(35)
    );

    cuartoAnterior = new Cuarto[1];
    tblCuartos.setOnMouseClicked(event -> {
      if (event.getClickCount() == 1) {
        Cuarto seleccionado = tblCuartos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
          if (modalCuarto == null) {
            crearModal();
          }
          if (cuartoAnterior[0] != seleccionado) {
            if (dateInicio.getValue() == null || dateFin.getValue() == null) {
              return;
            }
            ((ModalCuartoContr) App.getControlador(Rutas.MODAL_CUARTO)).setData(hotel, seleccionado, imagen, dateInicio.getValue(), dateFin.getValue());
            cuartoAnterior[0] = seleccionado;
          }
          modalCuarto.show();
        }
      }
    });
  }

  public void nose() {
    tblCuartos.getItems().remove(cuartoAnterior[0]);
    modalCuarto.close();
    cuartoAnterior[0] = null;
  }

  private void crearModal() {
    App.setVista(Rutas.MODAL_CUARTO);
    modalCuarto = new Stage();
    modalCuarto.setScene(new Scene(App.getVista(Rutas.MODAL_CUARTO)));
    modalCuarto.setOnHidden(e -> {
      ((ModalCuartoContr) App.getControlador(Rutas.MODAL_CUARTO)).resetModal(modalCuarto.getScene(), App.getVista(Rutas.MODAL_CUARTO));
    });
    modalCuarto.setTitle("Detalles de habitación");
    modalCuarto.initModality(Modality.WINDOW_MODAL);
    modalCuarto.initOwner(App.primaryStage);
    modalCuarto.setResizable(false);
  }


  public void setData(Hotel hotel, LocalDate fechaEntrada, LocalDate fechaSalida) {
    if (this.hotel == null || this.hotel != hotel) {
      this.hotel = hotel;
      this.fechaEntrada = fechaEntrada;
      this.fechaSalida = fechaSalida;
      dateInicio.setValue(fechaEntrada);
      dateFin.setValue(fechaSalida);
      prepararPlantilla();
    }
  }

  private void prepararPlantilla() {
    tblCuartos.getItems().clear();

    vboxComentarios.getChildren().clear();
    App.scheduler.schedule(() -> {
      Platform.runLater(() -> {
        try {
        List<Comentario> comentarios = ComentarioServicio.getComentariosByHotel(hotel.getId());
          for (Comentario comentario : comentarios) {
            Parent card = crearComentario(comentario.getCliente().getApellido(), comentario.getTexto());
            vboxComentarios.getChildren().add(card);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }, 0, TimeUnit.SECONDS);


    lblNombre.setText(hotel.getNombre());
    if (estrella != hotel.getEstrellas()) {
      estrella = hotel.getEstrellas();
      ConfRepetitiva.confEstrellas(lblEstrellas, hotel.getEstrellas());
    }
    lblDescripcion.setText(hotel.getDescripcion());
    Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl());
    try {
      Object[] urls = Files.list(carpeta).filter(Files::isRegularFile).sorted().limit(7).map(path -> path.toUri().toString()).toArray();
      imagen = urls[0].toString();
      for (int i = 0; i < urls.length; i++) {
        ConfRepetitiva.setBackground(labels[i], urls[i].toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  
  public void buscar() {
    if (dateInicio.getValue() == null || dateFin.getValue() == null) return;
    fechaEntrada = dateInicio.getValue();
    fechaSalida = dateFin.getValue();
    List<Cuarto> cuartos = BusquedaServicio.getCuartos(hotel, fechaEntrada, fechaSalida);
    if (cuartos == null) return;
    tblCuartos.getItems().clear();
    for (int i = 0; i < cuartos.size(); i++) {
      tblCuartos.getItems().add(cuartos.get(i));
    }
  }


  @FXML
  public void volver() {
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
