package com.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.Dominio.Boleta;
import com.example.Dominio.Categoria;
import com.example.Dominio.CategoriaCuarto;
import com.example.Dominio.Ciudad;
import com.example.Dominio.Cliente;
import com.example.Dominio.Comentario;
import com.example.Dominio.Cuarto;
import com.example.Dominio.Divisa;
import com.example.Dominio.Hotel;
import com.example.Dominio.MedioPago;
import com.example.Dominio.Pais;
import com.example.Dominio.Reserva;

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
