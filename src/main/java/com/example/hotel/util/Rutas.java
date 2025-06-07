package com.example.hotel.util;

import java.net.URL;
import java.util.concurrent.ScheduledFuture;

import lombok.Getter;

@Getter
public enum Rutas {
  INICIO(Rutas.class.getResource("/com/example/hotel/inicio.fxml")),
  LOGIN(Rutas.class.getResource("/com/example/hotel/login.fxml")),
  CREAR_CUENTA(Rutas.class.getResource("/com/example/hotel/crear-cuenta.fxml")),
  CLIENTE_INFO(Rutas.class.getResource("/com/example/hotel/cliente-info.fxml")),
  DETALLES_HOTEL(Rutas.class.getResource("/com/example/hotel/detalles-hotel.fxml")),
  FORM_RESERVA(Rutas.class.getResource("/com/example/hotel/formulario-reserva.fxml")),
  MODAL_CUARTO(Rutas.class.getResource("/com/example/hotel/modal-cuarto.fxml")),
  MODAL_EDICION(Rutas.class.getResource("/com/example/hotel/modal-edicion.fxml")),
  RESULTADOS(Rutas.class.getResource("/com/example/hotel/resultados.fxml")),
  SPINNER(Rutas.class.getResource("/com/example/hotel/spinner.fxml")),
  TARJETA_CARUSEL(Rutas.class.getResource("/com/example/hotel/tarjeta-carusel.fxml")),
  TARJETA_RESULTADO(Rutas.class.getResource("/com/example/hotel/tarjeta-resultado.fxml")),
  CODIGO_VERIF(Rutas.class.getResource("/com/example/hotel/codigo-veri.fxml"))
  ;

  private URL urlVista;
  private ScheduledFuture<?> ttlTask;
  
  private Rutas(URL urlVista) {
    this.urlVista = urlVista;
  }

  public void setTtlTask(ScheduledFuture<?> ttlTask) {
    this.ttlTask = ttlTask;
  }

  public void dropTtlTask() {
    this.ttlTask = null;
  }
}
