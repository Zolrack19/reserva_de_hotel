package com.example.hotel.service;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;

import com.example.hotel.HibernateUtil;
import com.example.hotel.dominio.Hotel;

public class BusquedaServicio {
  // len = 106
  private static final StringBuilder query = new StringBuilder("""
    SELECT DISTINCT h.*
    FROM hotel h
    JOIN ciudad c ON h.ciudad_id = c.id
    JOIN pais p ON c.pais_id = p.id
  """);
  
  public static List<Hotel> buscarHotel(List<String> tokens, LocalDate fechaInicio, LocalDate fechaFin) {
    System.out.println(query.length());
    if (tokens.size() == 0) return null;
    Session session = HibernateUtil.getSession().openSession();
    query.delete(109, query.length());
    if (fechaInicio != null) {
      query.append("""
      LEFT JOIN cuarto cuar ON cuar.hotel_id = h.id
      LEFT JOIN reserva r ON r.cuarto_id = cuar.id
      """
      );
      query.append("AND r.fecha_entrada < DATE '").append(fechaFin).append("'\n");
      query.append("AND r.fecha_salida > DATE '").append(fechaInicio).append("'\n");
      query.append("\nWHERE\n r.id IS NULL\n ");
    } else {
      query.append("\nWHERE\n");
    }

    for (int i = 0; i < tokens.size(); i++) {
      String token = tokens.get(i);
      query.append(" AND (\n");
      query.append("h.nombre_normalizado ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.nombre_normalizado ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("p.nombre_normalizado ILIKE unaccent('%").append(token); 
      query.append("%')");
      query.append(")\n");
    }
    
    List<Hotel> hotles =  session.createNativeQuery(query.toString(), Hotel.class).list();
    session.close();
    return hotles;
  }
}
