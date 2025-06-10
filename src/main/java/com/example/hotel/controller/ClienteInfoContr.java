package com.example.hotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.example.hotel.App;
import com.example.hotel.dominio.Pais;
import com.example.hotel.dominio.Reserva;
import com.example.hotel.service.ClienteServicio;
import com.example.hotel.util.Rutas;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
  Controlador para la plantilla de información de cuenta, se encarga de mostrar los atributos del usuario
  y da la posibilidad de actualizar sus valores, haciendo validaciones de entrada respectivas para cada campo.
  @see ModalEdicionContr
*/
public class ClienteInfoContr implements Initializable {

  @FXML
  private Label lblVolver;

  @FXML
  private Label lblNombre;
  @FXML
  private Label lblEditarNombre;

  @FXML
  private Label lblApellido;
  @FXML
  private Label lblEditarApellido;

  @FXML
  private Label lblEmail;

  @FXML
  private Label lblEditarContra;

  @FXML
  private Label lblDivisa;
  @FXML
  private Label lblSaldo;
  @FXML
  private Label lblEditarSaldo;

  @FXML
  private Label lblPrefijoTelefonico;

  @FXML
  private Label lblTelefono;
  @FXML
  private Label lblEditarTelefono;

  @FXML
  private Label lblPais;
  
  @FXML
  private ComboBox<Pais> cbxPais;

  @FXML
  private CheckBox chxSesionActiva;

  @FXML
  private Button btnEliminarCuenta;
  
  @FXML
  private Button btnGuardar;
  
  @FXML
  private Button btnCancelar;
  
  @FXML
  private TableView<Reserva> tblReservasH;

  private Stage modal;
  private ModalEdicionContr modalController;
  private final ClienteServicio clienteServicio = ClienteServicio.getInstancia();
  private ScheduledFuture<?> ttlTask;

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
    
    chxSesionActiva.setSelected(App.sesionActiva);
    chxSesionActiva.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
      if (isNowSelected) {
        App.sesionActiva = true;
      } else {
        App.sesionActiva = false;
      }
    });
    cbxPais.getItems().addAll(clienteServicio.getPaises());
    cbxPais.setValue(App.cliente.getPais());
    lblNombre.setText(App.cliente.getNombre());
    lblApellido.setText(App.cliente.getApellido());
    lblEmail.setText(App.cliente.getEmail());
    lblDivisa.setText(App.cliente.getPais().getDivisa().getSimbolo());
    lblSaldo.setText(App.cliente.getSaldo() != null ? App.cliente.getSaldo().toString() : "0.00");
    lblPrefijoTelefonico.setText(App.cliente.getPais().getPrefijoTelefonico());
    lblTelefono.setText(App.cliente.getTelefono() != null ? App.cliente.getTelefono() : "");

    cbxPais.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == oldVal || newVal == App.cliente.getPais()) return;
      lblDivisa.setText(newVal.getDivisa().getSimbolo());
      lblPrefijoTelefonico.setText(newVal.getPrefijoTelefonico());
      lblTelefono.setText("");
      btnGuardar.setVisible(true);
      btnGuardar.setManaged(true);
      btnCancelar.setVisible(true);
      btnCancelar.setManaged(true);
      if (modal == null) crearModal();
      modalController.setISO(App.cliente.getPais().getCodigoISO());
    });
  
    btnCancelar.setVisible(false);
    btnCancelar.setManaged(false);
    btnCancelar.setOnMouseClicked(e -> {
      lblNombre.setText(App.cliente.getNombre());
      lblApellido.setText(App.cliente.getApellido());
      lblDivisa.setText(App.cliente.getPais().getDivisa().getSimbolo());
      lblSaldo.setText(App.cliente.getSaldo().toString());
      lblPrefijoTelefonico.setText(App.cliente.getPais().getPrefijoTelefonico());
      cbxPais.setValue(App.cliente.getPais());
      lblTelefono.setText(App.cliente.getTelefono());
      btnCancelar.setVisible(false);
      btnCancelar.setManaged(false);
      btnGuardar.setVisible(false);
      btnGuardar.setManaged(false);
      modalController.setGuardar(true);
    });
    
    btnGuardar.setVisible(false);
    btnGuardar.setManaged(false);
    btnGuardar.setOnMouseClicked(e -> {
      App.cliente.setNombre(lblNombre.getText());
      App.cliente.setApellido(lblApellido.getText());
      App.cliente.setSaldo(new BigDecimal(lblSaldo.getText()));
      App.cliente.setTelefono(lblTelefono.getText());
      App.cliente.setPais(cbxPais.getValue());
      clienteServicio.actualizarCliente(App.cliente);
      btnGuardar.setVisible(false);
      btnGuardar.setManaged(false);
      modalController.setGuardar(true);
    });
    lblEditarNombre.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) crearModal();
      modalController.prepararModal("Nombre", lblNombre, 150);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });

    lblEditarApellido.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) crearModal();
      modalController.prepararModal("Apellidos", lblApellido, 150);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });    

    lblEditarContra.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) crearModal();
      modalController.prepararModal("Contraseña actual", null, 50);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });
    
    lblEditarSaldo.setOnKeyPressed(e -> {
    if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
    if (modal == null) crearModal();
      modalController.setIsSaldo(true);
      modalController.prepararModal("Saldo", lblSaldo, 11);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });
    
    lblEditarTelefono.setOnKeyPressed(e -> {
      if (e.getCode() != KeyCode.SPACE && e.getCode() != KeyCode.ENTER) return;
      if (modal == null) crearModal();
      modalController.setIsTelefono(true);
      modalController.prepararModal("Número de teléfono", lblTelefono, 25);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });

    lblEditarNombre.setOnMouseClicked(e -> {
      if (modal == null) crearModal();
      modalController.prepararModal("Nombre", lblNombre, 150);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });
   
    lblEditarApellido.setOnMouseClicked(e -> {
      if (modal == null) crearModal();
      modalController.prepararModal("Apellidos", lblApellido, 150);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });
   
    lblEditarContra.setOnMouseClicked(e -> {
      if (modal == null) crearModal();
      modalController.prepararModal("Contraseña actual", null, 50);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });
   
    lblEditarSaldo.setOnMouseClicked(e -> {
      if (modal == null) crearModal();
      modalController.setIsSaldo(true);
      modalController.prepararModal("Saldo", lblSaldo, 11);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });
    
    lblEditarTelefono.setOnMouseClicked(e -> {
      if (modal == null) crearModal();
      modalController.setIsTelefono(true);
      modalController.prepararModal("Número de teléfono", lblTelefono, 20);
      if (ttlTask != null && !ttlTask.isDone()) {
        ttlTask.cancel(false);
      }
      modal.show();
    });
    configurarTabla();
  }

  @SuppressWarnings("unchecked")
  private void configurarTabla() {
    TableColumn<Reserva, Integer> colId = new TableColumn<>("Id");
    colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colId.setPrefWidth(100);
    colId.setReorderable(false);

    TableColumn<Reserva, LocalDate> colBoleta = new TableColumn<>("Código de boleta");
    colBoleta.setCellValueFactory(new PropertyValueFactory<>("boleta"));
    colBoleta.setPrefWidth(200);
    colBoleta.setReorderable(false);
    
    TableColumn<Reserva, LocalDate> colfechaEntrada = new TableColumn<>("Fecha de Entrada");
    colfechaEntrada.setCellValueFactory(new PropertyValueFactory<>("fechaEntrada"));
    colfechaEntrada.setPrefWidth(200);
    colfechaEntrada.setReorderable(false);
    
    TableColumn<Reserva, LocalDate> colfechaSalida = new TableColumn<>("Fecha de Salida");
    colfechaSalida.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));
    colfechaSalida.setPrefWidth(200);
    colfechaSalida.setReorderable(false);

    tblReservasH.getColumns().addAll(colId, colBoleta, colfechaEntrada, colfechaSalida);
    tblReservasH.setFixedCellSize(35);
    tblReservasH.prefHeightProperty().bind(
      Bindings.size(tblReservasH.getItems()).multiply(tblReservasH.getFixedCellSize()).add(35)
    );

    tblReservasH.getItems().add(new Reserva(1, null, null, null, LocalDate.now(), LocalDate.now().plusDays(4)));
    tblReservasH.getItems().add(new Reserva(2, null, null, null, LocalDate.now(), LocalDate.now().plusDays(4)));
    tblReservasH.getItems().add(new Reserva(3, null, null, null, LocalDate.now(), LocalDate.now().plusDays(4)));
    tblReservasH.getItems().add(new Reserva(4, null, null, null, LocalDate.now(), LocalDate.now().plusDays(4)));
  }

  private void crearModal() {
    try {
      modal = new Stage();
      FXMLLoader loader = new FXMLLoader(Rutas.MODAL_EDICION.getUrlVista());
      Parent parent = loader.load();
      modalController = loader.getController();
      modalController.setBotones(btnGuardar, btnCancelar);
      modalController.setISO(App.cliente.getPais().getCodigoISO());
      modal.setOnHidden(e -> {
        modalController.reiniciar();
        resetTtl();
      });
      modalController.setModal(modal);
      Scene scene = new Scene(parent);
      scene.setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.ESCAPE) {
          modalController.reiniciar();
          modal.close();
        }
      });
      modal.setScene(scene);
      modal.setTitle("Cambiar atributo");
      modal.initModality(Modality.WINDOW_MODAL);
      modal.initOwner(App.primaryStage);
      modal.setResizable(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void resetTtl() {
    if (ttlTask != null && !ttlTask.isDone()) {
      ttlTask.cancel(false);
    }
    ttlTask = App.scheduler.schedule(() -> {
      modal = null;
      modalController = null;
    }, 1, TimeUnit.MINUTES);
  }

  @FXML
  private void volver() throws IOException {
    if (App.getVista(Rutas.INICIO) == null) {
      App.setVista(Rutas.INICIO);
    }
    App.navegar(Rutas.INICIO);
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("\n\n🧹 ClienteInfo eliminado por GC\n\n");
  }
}