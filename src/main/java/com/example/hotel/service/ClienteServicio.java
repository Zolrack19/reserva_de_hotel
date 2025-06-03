package com.example.hotel.service;

import com.example.hotel.App;
import com.example.hotel.dao.ClienteDAO;
import com.example.hotel.dao.PaisDAO;
import com.example.hotel.dominio.Cliente;
import com.example.hotel.dominio.Pais;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

/**
  Servicio para hacer operaciones relacionadas a los clientes en la base de datos.
*/
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

  /**
    Método para comprobar los datos de usuario.
    @param email email de usuario.
    @param contrasena contraseña de usuario.
    @return
  */
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

  /**
    Método para obtener una lista de paises.
  */
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