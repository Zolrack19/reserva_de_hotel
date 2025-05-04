package com.example.hotel.service;

import com.example.hotel.dao.ClienteDAO;
import com.example.hotel.dominio.Cliente;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServicio {
  private final ClienteDAO clienteDAO = new ClienteDAO();
  
  public boolean signIn(String email, String contrasena) {
    Cliente cliente = clienteDAO.getByEmail(email);
    if (cliente == null) {
      return false;
    }
    return BCrypt.checkpw(contrasena, cliente.getContrasena());
  }

}