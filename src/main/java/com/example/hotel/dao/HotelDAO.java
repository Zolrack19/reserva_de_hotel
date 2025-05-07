package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.HibernateUtil;
import com.example.hotel.dominio.Hotel;

public class HotelDAO {

  public void crearHotel(Hotel hotel) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(hotel);

    tr.commit();
    session.close();
  }
  
  public Hotel getById(long id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Hotel hotel = session.get(Hotel.class, id);
    if (init) {
      hotel.getCategoria();
      hotel.getCiudad();
    }
    session.close();
    return hotel;
  }
  
  // public List<Hotel> getByNombre(String nombre) {
  //   Session session = HibernateUtil.getSession().openSession();
  //  List<Hotel> hoteles = session.createQuery("from")

  //   session.close();
  //   return hoteles;
  // }


  public List<Hotel> getHotelesRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Hotel> hoteles = session.createQuery("from Hotel h order by h.id", Hotel.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return hoteles;
  }

  public List<Hotel> getHotelesPorCiudad(int idCiudad, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Hotel> hoteles = session.createQuery("from Hotel h where h.ciudad.id = :id order by h.id", Hotel.class)
    .setParameter("id", idCiudad)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return hoteles;
  }

  public List<Hotel> getHotelesPorCategoria(short idCategoria, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Hotel> Hotels = session.createQuery("from Hotel h where h.categoria.id = :id order by h.id", Hotel.class)
    .setParameter("id", idCategoria)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return Hotels;
  }

  public void actualizarHotel(Hotel hotel) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(hotel);
    
    tr.commit();
    session.close();
  }

  public void eliminarHotel(Hotel hotel) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(hotel);

    tr.commit();
    session.close();
  }
}
