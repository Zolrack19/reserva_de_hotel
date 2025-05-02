package com.example.hotel.service;

import com.example.hotel.dao.ClienteDAO;
import com.example.hotel.dominio.Cliente;

public class LoginServicio {
  private final ClienteDAO clienteDAO = new ClienteDAO();
  
  public boolean signIn(String email, String contrasena) {
    Cliente cliente = clienteDAO.getByEmailContrasena(email, contrasena);
    return cliente != null;
  }

}