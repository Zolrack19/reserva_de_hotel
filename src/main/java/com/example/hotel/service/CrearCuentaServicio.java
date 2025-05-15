package com.example.hotel.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;

import org.mindrot.jbcrypt.BCrypt;

import com.example.hotel.App;
import com.example.hotel.dao.ClienteDAO;
import com.example.hotel.dao.PaisDAO;
import com.example.hotel.dominio.Cliente;
import com.example.hotel.dominio.Pais;

public class CrearCuentaServicio {
  
  private InternetAddress address;
  private MimeMessage message;
  private final ClienteDAO clienteDAO = new ClienteDAO();
  private final PaisDAO paisDAO = new PaisDAO();
  private String texto = "";

  public CrearCuentaServicio() {
    address = new InternetAddress();
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587"); 
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getInstance(properties, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new javax.mail.PasswordAuthentication("carlosmbl1902@gmail.com", "ufrd izhp wiym bggz"); 
      }
    });
    message = new MimeMessage(session);
    try {
      message.setFrom(new InternetAddress("carlosmbl1902@gmail.com"));
      message.setSubject("Código de comprobación");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void evnviarComprobante(String email) {
    try {
      this.address.setAddress(email);
      generarCodigo();
      message.setContent(
        "<h2 style='color:rgb(71, 47, 102);'>Little-View le da la bienvenida!</h2>" +
        "<p style='font-size: 16px;'>Código de verificación: <line style='font-size: 20px'><strong>"+ texto +"</strong></line></p>" +
        "<p>Gracias por registrarte en nuestra plataforma.</p>",
        "text/html; charset=utf-8");
      message.setRecipient(Message.RecipientType.TO, this.address);
      Transport.send(message);
      System.out.println("mensaje envíado correctamente");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean verificarCodigo(String codigo) {
    return codigo.equals(texto);
  }

  private void generarCodigo() {
    texto = "";
    for (int i = 0; i < 6; i++) {
      texto += (byte) Math.floor(Math.random() * 10);
    }
  }

  public void crearCliente(Pais pais, String nombre, String apellido, String email, String contrasena) {
    App.cliente = new Cliente();
    App.cliente.setNombre(nombre);
    App.cliente.setApellido(apellido);
    App.cliente.setEmail(email);
    App.cliente.setContrasena(BCrypt.hashpw(contrasena, BCrypt.gensalt()));
    paisDAO.instanciarDivisa(pais);
    App.cliente.setPais(pais);
    clienteDAO.crearCliente(App.cliente);
  }

  public boolean emailRepetido(String email) {
    return clienteDAO.existeByEmail(email);
  }

  public List<Pais> getPaises() {
    return paisDAO.getPaisesRango(0, 20, false); //cambiar esto por si hay más paises
  }

}