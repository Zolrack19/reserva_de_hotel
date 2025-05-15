package com.example.hotel.service;

import com.example.hotel.App;
import com.example.hotel.dao.ClienteDAO;
import com.example.hotel.dao.PaisDAO;
import com.example.hotel.dominio.Cliente;
import com.example.hotel.dominio.Pais;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class ClienteServicio {

  private static ClienteServicio clienteServicio;
  private final ClienteDAO clienteDAO;
  private final PaisDAO paisDAO;
  
  private ClienteServicio() {
    clienteDAO = new ClienteDAO();
    paisDAO = new PaisDAO();
  }

  public void actualizarCliente(Cliente cliente) {
    clienteDAO.actualizarCliente(cliente);
  }

  public boolean signIn(String email, String contrasena) {
    App.cliente = clienteDAO.getByEmail(email);
    if (App.cliente == null) {
      return false;
    }
    return BCrypt.checkpw(contrasena, App.cliente.getContrasena());
  }

  public void instanciarPais(Pais pais) {
    paisDAO.instanciarDivisa(pais);
  }

  public List<Pais> getPaises() {
    return paisDAO.getPaisesRango(0, 20, true); //cambiar esto por si hay más paises
  }

  public static ClienteServicio getInstancia() {
    if (clienteServicio == null) {
      clienteServicio = new ClienteServicio();
    }
    return clienteServicio;
  }

}