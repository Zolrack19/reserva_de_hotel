package com.example.hotel.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.example.hotel.auxiliar.ConfRepetitiva;
import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.util.Imagenes;
import com.example.hotel.util.Rutas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ModalCuartoContr implements Initializable {
  
  @FXML
  private HBox principal;

  @FXML
  private HBox hboxImagenCuarto;
  
  @FXML
  private Button btnIzquierda;
  
  @FXML
  private Button btnDerecha;
  
  @FXML
  private GridPane grdImagenes;
  
  @FXML
  private Label lblTituloCuarto;
  
  @FXML
  private Label lblDescripcionCuarto;

  @FXML
  private Button btnForm;

  private Label[] labels;
  private Label lblActual;
  private Object[] urls;
  private byte puntero;
  private boolean enForm;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    labels = new Label[15];
    btnDerecha.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        clickDerecho();
      }
    });
    btnIzquierda.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        clickIzquierdo();
      }
    });
    btnForm.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        try {
          irFormulario();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
  }

  public void setData(Hotel hotel, Cuarto cuarto) {
    puntero = 0;
    lblTituloCuarto.setText(cuarto.getNombre());
    lblDescripcionCuarto.setText(cuarto.getDescripcion());

    Path carpeta = Paths.get(Imagenes.DB.getUrl(), hotel.getImagenUrl(), cuarto.getImagenUrl());
    try {
      urls = Files.list(carpeta).filter(Files::isRegularFile).sorted().limit(15).map(path -> path.toUri().toString()).toArray();
      ConfRepetitiva.setBackground(hboxImagenCuarto, urls[0].toString());
      byte i = 0;
      for (; i < urls.length; i++) {
        if (labels[i] == null) {
          labels[i] = new Label();
          labels[i].setFocusTraversable(true);
          labels[i].getStyleClass().add("lbl-imagen-cuarto");
          labels[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
          grdImagenes.add(labels[i], i%5, i/5);
          final byte p = i;
          labels[i].setOnMouseClicked(e -> { lblClick(p); });
          labels[i].setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
              lblClick(p);
            }
          });
        }
        ConfRepetitiva.setBackground(labels[i], urls[i].toString());
      }
      while (labels[i] != null) {
        grdImagenes.getChildren().remove(labels[i]);
        labels[i] = null;
        i++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (lblActual != null) {
      lblActual.getStyleClass().remove("lbl-imagen-cuarto");
    }
    lblActual = labels[0];
    lblActual.getStyleClass().add("lbl-imagen-cuarto");
  }

  private void lblClick(byte p) {
    if (lblActual == labels[p]) return;
    lblActual.getStyleClass().remove("lbl-imagen-cuarto");
    puntero = p;
    lblActual = labels[p];
    lblActual.getStyleClass().add("lbl-imagen-cuarto");
    ConfRepetitiva.setBackground(hboxImagenCuarto, urls[p].toString());
  }

  public void resetModal(Scene scene, Parent root) {
    if (!enForm) return;
    scene.setRoot(root);
    enForm = false;
  }

  @FXML
  private void clickIzquierdo() {
    if (puntero == 0) return;
    lblActual.getStyleClass().remove("lbl-imagen-cuarto");
    lblActual = labels[--puntero];
    lblActual.getStyleClass().add("lbl-imagen-cuarto");
    ConfRepetitiva.setBackground(hboxImagenCuarto, urls[puntero].toString());
  }

  @FXML
  private void clickDerecho() {
    if (labels[puntero + 1] == null) return;
    lblActual.getStyleClass().remove("lbl-imagen-cuarto");
    lblActual = labels[++puntero];
    lblActual.getStyleClass().add("lbl-imagen-cuarto");
    ConfRepetitiva.setBackground(hboxImagenCuarto, urls[puntero].toString());
  }

  @FXML
  private void irFormulario() throws IOException {
    FXMLLoader loader = new FXMLLoader(Rutas.FORM_RESERVA.getUrlVista());
    Parent parent = loader.load();
    enForm = true;
    principal.getScene().setRoot(parent);
  }

  @Override
  protected void finalize() {
    System.out.println("\n\n🧹 ModalCuarto eliminado por GC\n\n");
  }
}
