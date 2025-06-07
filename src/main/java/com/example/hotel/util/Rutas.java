package com.example.hotel.util;

import java.net.URL;

import lombok.Getter;

@Getter
public enum Rutas {
  INICIO((byte) 1,Rutas.class.getResource("/com/example/hotel/inicio.fxml")),
  LOGIN((byte) 2,Rutas.class.getResource("/com/example/hotel/login.fxml")),
  CREAR_CUENTA((byte) 3,Rutas.class.getResource("/com/example/hotel/crear-cuenta.fxml")),
  CLIENTE_INFO((byte) 4,Rutas.class.getResource("/com/example/hotel/cliente-info.fxml")),
  DETALLES_HOTEL((byte) 5,Rutas.class.getResource("/com/example/hotel/detalles-hotel.fxml")),
  FORM_RESERVA((byte) 6,Rutas.class.getResource("/com/example/hotel/formulario-reserva.fxml")),
  MODAL_CUARTO((byte) 7,Rutas.class.getResource("/com/example/hotel/modal-cuarto.fxml")),
  MODAL_EDICION((byte) 8,Rutas.class.getResource("/com/example/hotel/modal-edicion.fxml")),
  RESULTADOS((byte) 9,Rutas.class.getResource("/com/example/hotel/resultados.fxml")),
  SPINNER((byte) 10,Rutas.class.getResource("/com/example/hotel/spinner.fxml")),
  TARJETA_CARUSEL((byte) 11,Rutas.class.getResource("/com/example/hotel/tarjeta-carusel.fxml")),
  TARJETA_RESULTADO((byte) 12,Rutas.class.getResource("/com/example/hotel/tarjeta-resultado.fxml")),
  CODIGO_VERIF((byte) 13,Rutas.class.getResource("/com/example/hotel/codigo-veri.fxml"))
  ;

  private byte id;
  private URL urlVista;

  private Rutas(byte id, URL urlVista) {
    this.id = id;
    this.urlVista = urlVista;
  }
}
