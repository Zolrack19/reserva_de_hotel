package com.example.hotel.service;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;

import com.example.hotel.dao.CategoriaDAO;
import com.example.hotel.dao.CuartoDAO;
import com.example.hotel.dominio.Categoria;
import com.example.hotel.dominio.Cuarto;
import com.example.hotel.dominio.Hotel;
import com.example.hotel.singleton.HibernateUtil;

/**
  Servicio que se encarga de hacer consultas personalizadas y generales en la base de datos
*/
public class BusquedaServicio {
  private static final CategoriaDAO categoriaDao = new CategoriaDAO();
  // len = 106
  private static final StringBuilder query = new StringBuilder();
  
  /**
    Método para generar un query complejo y personalizado, se encarga de buscar hoteles
    según las opciones de búsqueda marcadas por el usuario.
    @param tokens lista de String que son tratados como tokens de búsqueda
    @param fechaInicio fecha de inicio de reserva
    @param fechaFin fecha de fin de reserva
    @return
  */
  public static List<Hotel> buscarHotel(String[] tokens, LocalDate fechaInicio, LocalDate fechaFin) {
    if (tokens[0] == null) return null;
    query.append("""
      SELECT DISTINCT h.*
      FROM hotel h
      JOIN ciudad c ON h.ciudad_id = c.id
      JOIN pais p ON c.pais_id = p.id 
    """);
    Session session = HibernateUtil.getSession().openSession();
    // query.delete(109, query.length());
    if (fechaInicio != null) {
      query.append("""
      WHERE EXISTS (
        SELECT 1 
        FROM cuarto cuar
        WHERE cuar.hotel_id = h.id
          AND NOT EXISTS (
            SELECT 1
            FROM reserva r
            WHERE r.cuarto_id = cuar.id
      """
      );
      query.append("AND r.fecha_entrada < DATE '").append(fechaFin).append("'\n");
      query.append("AND r.fecha_salida > DATE '").append(fechaInicio).append("'\n");
      query.append(")\n) \nAND\n ");
    } else {
      query.append("\nWHERE\n");
    }
    // query.append("h.categoria_id IN (1,2,3,7) AND h.estrellas IN (1,3,4,5,2) AND");
    int i = 0;
    while (tokens[i] != null) {
      String token = tokens[i];
      if (i != 0) {
        query.append(" OR ");
      }
      query.append("(\n");
      query.append("h.nombre_normalizado ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.nombre_normalizado ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("p.nombre_normalizado ILIKE unaccent('%").append(token); 
      query.append("%')");
      query.append(")\n");
      i++;
    }
    
    List<Hotel> hotles =  session.createNativeQuery(query.toString(), Hotel.class).list();
    session.close();
    query.setLength(0);
    return hotles;
  }

  /**
    Método para obtener hoteles aleatoriamente.
    @param cantidad cantidad de hoteles a obtener
  */
  public static List<Hotel> hotelesAlazar(int cantidad) {
    Session session = HibernateUtil.getSession().openSession();
    List<Hotel> hoteles =  session.createNativeQuery("select * from hoteles_aleatorios(:cantidad)", Hotel.class)
    .setParameter("cantidad", cantidad)
    .list();
    for (Hotel hotel : hoteles) {
      hotel.getCiudad();
      hotel.getCiudad().getPais();
    }
    session.close();
    return hoteles; 
  }

  public static List<Categoria> getCategorias() {
    return categoriaDao.getCategoriasRango(0, 20); // cambianr si hay más
  }

  public static List<Cuarto> getCuartos(Hotel hotel, LocalDate fechaEntrada, LocalDate fechaSalida) {
    if (fechaEntrada == null || fechaSalida == null) return null;
    Session s = HibernateUtil.getSession().openSession();
    query.append("""
    SELECT c.*
    FROM cuarto c
    WHERE c.hotel_id =""").append(hotel.getId()).append("\n");
    query.append("""
    AND NOT EXISTS (
      SELECT 1
      FROM reserva r
      WHERE r.cuarto_id = c.id
        AND r.fecha_entrada <= '""").append(fechaEntrada).append(""" 
        '
        AND r.fecha_salida >= '""").append(fechaSalida).append("""
        '
    )"""
    );
    List<Cuarto> cuartos = s.createNativeQuery(query.toString(), Cuarto.class).list();
    query.setLength(0);
    s.close();
    return cuartos;
  }
}
