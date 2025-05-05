package com.example.hotel.service;

import com.example.hotel.App;
import com.example.hotel.dao.ClienteDAO;
import com.example.hotel.dominio.Cliente;
import org.mindrot.jbcrypt.BCrypt;

public class ClienteServicio {
  private final ClienteDAO clienteDAO = new ClienteDAO();

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

}