package com.example.hotel.service;

import java.util.List;

import com.example.hotel.dao.ComentarioDAO;
import com.example.hotel.dominio.Cliente;
import com.example.hotel.dominio.Comentario;
import com.example.hotel.dominio.Hotel;

public class ComentarioServicio {
  private static final ComentarioDAO comentarioDAO = new ComentarioDAO();
  
  public static void crearComentario(Hotel hotel, Cliente cliente, String texto) {
    Comentario comentario = new Comentario();
    comentario.setCliente(cliente);
    comentario.setHotel(hotel);
    comentario.setTexto(texto);
    comentarioDAO.crearComentario(comentario);
  }

  public static List<Comentario> getComentariosByHotel(long hotelId) {
    return comentarioDAO.getComentariosPorHotel(hotelId, 0, 20); // 20 primeros comentarios como mucho
  }

}
