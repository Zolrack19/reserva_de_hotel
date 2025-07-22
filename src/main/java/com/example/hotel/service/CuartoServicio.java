package com.example.hotel.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import com.example.hotel.App;
import com.example.hotel.dao.ClienteDAO;
import com.example.hotel.dao.MedioPagoDAO;
import com.example.hotel.dao.ReservaDAO;
import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.MedioPago;
import com.example.hotel.dominio.Reserva;

public class CuartoServicio {
  private final ClienteDAO clienteDAO = new ClienteDAO();
  private final ReservaDAO reservaDAO = new ReservaDAO();
  private final MedioPagoDAO medioPagoDAO = new MedioPagoDAO();
  

  public void reservar(Cuarto cuarto, LocalDate fechaEntrada, LocalDate fechaSalida, BigDecimal precio) {
    App.cliente.setSaldo(App.cliente.getSaldo().subtract(precio).setScale(2, RoundingMode.HALF_UP));
    clienteDAO.actualizarCliente(App.cliente);
    Reserva reserva = new Reserva(App.cliente, cuarto, fechaEntrada, fechaSalida);
    reservaDAO.crearReserva(reserva);
  }

  public List<MedioPago> getMedioPago() {
    return medioPagoDAO.getMedioPagosRango(0, 10);
  }


}
