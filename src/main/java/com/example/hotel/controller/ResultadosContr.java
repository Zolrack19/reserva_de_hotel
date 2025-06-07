package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.hotel.App;
import com.example.hotel.auxiliar.Calendario;
import com.example.hotel.dominio.Categoria;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.service.BusquedaServicio;
import com.example.hotel.util.Imagenes;
import com.example.hotel.util.Rutas;

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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

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
  private List<CheckBox> estrellas;
  /**}
    Método para  instanciar los elementos gráficos del fxml asociado, se encarga de hacer configurarciones
    como la validación de fechas en los DatePicker y asignar eventos de mouse y teclado, así como crear otros
    componentes gráficos desde código java, ejemplo: RangeSlider.
  */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    categorias = new ArrayList<>();
    estrellas = new ArrayList<>();
    lblInicio.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          irAInicio();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
    resultados = new ArrayList<>();
    configurarCalendarios();

    List<Categoria> categorias = BusquedaServicio.getCategorias();
    for (int i = 0; i < categorias.size(); i++) {
      CheckBox checkBox = new CheckBox(categorias.get(i).getNombre());
      vboxCategoria.getChildren().add(checkBox);
      this.categorias.add(checkBox);
    }

    for (Node nodo : vboxEstrellas.getChildren()) {
      estrellas.add((CheckBox) nodo);
    }
  }

  private void configurarCalendarios() {
    Calendario.confEstilo(dateInicio);
    Calendario.confEstilo(dateFin);
    Calendario.confCalendarios(dateInicio, dateFin);
  }

  /**
    Implementación de prueba del botón de buscar, solo se encarga de cambiar de plantilla, fxml.
    @throws IOException
  */
  private void crearTarjetas() {
    // prueba de lo que sería la generación de las tarjetas de resultados de búsqueda de hoteles
    try {
      for (int i = 0; i < resultados.size(); i++) {
        Hotel hotel = resultados.get(i);
        Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl());
        String url = Files.list(carpeta).sorted().map(path -> path.toUri().toString()).findFirst().orElseThrow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotel/tarjeta-resultado.fxml"));
        Parent card = loader.load();
        TarjetaResultadoContr contr = loader.getController();
        contr.setData(hotel, url);
        vboxResultados.getChildren().add(card);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void inicarTarjetas(ObservableList<Hotel> hoteles) {
    vboxResultados.getChildren().clear();
    resultados.clear();
    resultados.addAll(hoteles);
    crearTarjetas();
  }

  @FXML
  public void buscar() throws IOException {
    // if (App.getVista(Rutas.DETALLES_HOTEL) == null) {
    //   App.setVista(Rutas.DETALLES_HOTEL);
    // }
    // App.navegar(Rutas.DETALLES_HOTEL);
  }

  @FXML
  private void irAInicio() throws IOException {
    if (App.getVista(Rutas.INICIO) == null) {
      App.setVista(Rutas.INICIO);
    }
    App.navegar(Rutas.INICIO);
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 Resultados eliminado por GC\n\n");
  }
}