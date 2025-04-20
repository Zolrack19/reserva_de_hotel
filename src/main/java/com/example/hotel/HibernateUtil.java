package com.example.hotel;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.hotel.dominio.Boleta;
import com.example.hotel.dominio.Categoria;
import com.example.hotel.dominio.CategoriaCuarto;
import com.example.hotel.dominio.Ciudad;
import com.example.hotel.dominio.Cliente;
import com.example.hotel.dominio.Comentario;
import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.Divisa;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.dominio.MedioPago;
import com.example.hotel.dominio.Pais;
import com.example.hotel.dominio.Reserva;

public class HibernateUtil {
  private static final SessionFactory session;

  static {
    try {
      Configuration conf = new Configuration();
      conf.setProperty("hibernate.validator.apply_to_ddl", "false");
      conf.setProperty("hibernate.validator.autoregister_listeners", "false");
      conf.addAnnotatedClass(Cliente.class);
      conf.addAnnotatedClass(Categoria.class);
      conf.addAnnotatedClass(CategoriaCuarto.class);
      conf.addAnnotatedClass(Hotel.class);
      conf.addAnnotatedClass(Cuarto.class);
      conf.addAnnotatedClass(Divisa.class);
      conf.addAnnotatedClass(Boleta.class);
      conf.addAnnotatedClass(MedioPago.class);
      conf.addAnnotatedClass(Pais.class);
      conf.addAnnotatedClass(Ciudad.class);
      conf.addAnnotatedClass(Comentario.class);
      conf.addAnnotatedClass(Reserva.class);

      session = conf.buildSessionFactory();
    } catch (Exception ex) {
      System.err.println("Error en inciar sesión con la base de dato " + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSession() {
    return session;
  }
}
