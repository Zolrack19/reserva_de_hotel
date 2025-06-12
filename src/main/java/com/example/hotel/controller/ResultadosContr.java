package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.example.hotel.App;
import com.example.hotel.auxiliar.ConfRepetitiva;
import com.example.hotel.dominio.Categoria;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.service.BusquedaServicio;
import com.example.hotel.singleton.Imagenes;
import com.example.hotel.singleton.Rutas;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
  Clase que se encarga de la plantilla de resultados, plantilla en la que se muestran los
  resultados de búsqueda de hoteles.
  Usa un controlador interno para instanciar las cartas de resultados de hoteles.
  @see TarjetaResultadoContr
*/
public class ResultadosContr implements Initializable {

  @FXML
  private Label lblInicio;
  
  @FXML
  private TextField txtBuscar;
  
  @FXML
  private DatePicker dateInicio;
  
  @FXML
  private DatePicker dateFin;
  
  @FXML
  private VBox vboxFiltros;
  
  @FXML
  private VBox vboxCategoria;
  
  @FXML
  private VBox vboxEstrellas;

  @FXML
  private VBox vboxResultados;

  @FXML
  private Button btnBuscar;

  private List<Hotel> resultados;

  private List<CheckBox> categorias;
  private CheckBox[] estrellas;

  private final Popup popup = new Popup();
  private final ListView<Hotel> sugerencias = new ListView<>();
  
  private boolean activar;
  private boolean hayResultados;

  /**
    Método para  instanciar los elementos gráficos del fxml asociado, se encarga de hacer configurarciones
    como la validación de fechas en los DatePicker y asignar eventos de mouse y teclado, así como crear otros
    componentes gráficos desde código java, ejemplo: RangeSlider.
  */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    categorias = new ArrayList<>();
    estrellas = new CheckBox[5];
    resultados = new ArrayList<>();
    configurarFiltros();
    configurarCalendarios();
    popup.getContent().add(sugerencias);
    ConfRepetitiva.confListaSugerencia(sugerencias, txtBuscar);
     
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
        resultados = BusquedaServicio.buscarHotel(filtrado, dateInicio.getValue(), dateFin.getValue());

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

    // #region eventos para la lista de sugerencia
    sugerencias.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        if (popup.isShowing()) {
          popup.hide();
        }
      } else if (e.getCode() == KeyCode.TAB) {
        e.consume();
        if (e.isShiftDown()) {
          lblInicio.requestFocus();
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

  private void configurarFiltros() {
    List<Categoria> categorias = BusquedaServicio.getCategorias();
    for (int i = 0; i < categorias.size(); i++) {
      CheckBox checkBox = new CheckBox(categorias.get(i).getNombre());
      vboxCategoria.getChildren().add(checkBox);
      this.categorias.add(checkBox);
    }

    byte i = 0;
    for (Node nodo : vboxEstrellas.getChildren()) {
      estrellas[i] = ((CheckBox) nodo);
      i++;
    }

    lblInicio.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          irAInicio();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
  }

  private void configurarCalendarios() {
    ConfRepetitiva.confEstiloCalendario(dateInicio);
    ConfRepetitiva.confEstiloCalendario(dateFin);
    ConfRepetitiva.confCalendarios(dateInicio, dateFin);
  }

  /**
    Implementación de prueba del botón de buscar, solo se encarga de cambiar de plantilla, fxml.
    @throws IOException
  */
  private void crearTarjetas() throws IOException {
    // prueba de lo que sería la generación de las tarjetas de resultados de búsqueda de hoteles
    for (int i = 0; i < resultados.size(); i++) {
      Hotel hotel = resultados.get(i);
      Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl());
      String url = Files.list(carpeta).sorted().map(path -> path.toUri().toString()).findFirst().orElseThrow();
      FXMLLoader loader = new FXMLLoader(Rutas.TARJETA_RESULTADO.getUrlVista());
      Parent card = loader.load();
      TarjetaResultadoContr contr = loader.getController();
      setDataTarjetaCarusel(contr, hotel, url);
      vboxResultados.getChildren().add(card);
    }
  }

  public void inicarTarjetas(ObservableList<Hotel> hoteles) {
    vboxResultados.getChildren().clear();
    resultados.clear();
    resultados.addAll(hoteles);
    try {
      crearTarjetas();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void buscar() {
    if (resultados == null || !hayResultados) return;
    PantallaCarga pantallaDeCarga = PantallaCarga.getPantallaCarga();
    vboxResultados.getChildren().clear();
    pantallaDeCarga.mostrar();
    vboxResultados.getChildren().add(pantallaDeCarga);
  }

  @FXML
  private void irAInicio() {
    if (App.getVista(Rutas.INICIO) == null) {
      App.setVista(Rutas.INICIO);
    }
    App.navegar(Rutas.INICIO);
  }

  private void setDataTarjetaCarusel(TarjetaResultadoContr contr, Hotel hotel, String imagenURL) {
    contr.getRoot().setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        verDetalles(hotel);
      }
    });
    contr.getRoot().setOnMouseClicked(e -> {
      verDetalles(hotel);
    });

    ConfRepetitiva.setBackground(contr.getLblImagen(), imagenURL);
    contr.getLblTitulo().setText(hotel.getNombre());
    contr.getLblDescripcion().setText(hotel.getDescripcion());
    ConfRepetitiva.confEstrellas(contr.getLblEstrellas(), hotel.getEstrellas());
  }

  private void verDetalles(Hotel hotel) {
    if (App.getVista(Rutas.DETALLES_HOTEL) == null) {
      App.setVista(Rutas.DETALLES_HOTEL);
    }
    ((DetallesController) App.getControlador(Rutas.DETALLES_HOTEL)).setData(hotel);
    App.navegar(Rutas.DETALLES_HOTEL);
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 Resultados eliminado por GC\n\n");
  }
}