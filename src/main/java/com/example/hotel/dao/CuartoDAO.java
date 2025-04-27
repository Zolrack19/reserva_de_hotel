package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.HibernateUtil;
import com.example.hotel.dominio.Cuarto;

public class CuartoDAO {

  public void crearCuarto(Cuarto cuarto) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(cuarto);

    tr.commit();
    session.close();
  }

  public Cuarto getById(long id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Cuarto cuarto = session.get(Cuarto.class, id);
    if (init) {
      cuarto.getCategoriaCuarto();
      cuarto.getHotel();
    }
    session.close();
    return cuarto;
  }

  public List<Cuarto> getCuartosRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Cuarto> Cuartos = session.createQuery("from Cuarto b order by b.id", Cuarto.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return Cuartos;
  }

  public List<Cuarto> getCuartosPorHotel(int idHotel, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Cuarto> Cuartos = session.createQuery("from Cuarto c where c.hotel.id = :id order by c.id", Cuarto.class)
    .setParameter("id", idHotel)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return Cuartos;
  }

  public List<Cuarto> getCuartosPorCategoriaCuartos(short idCategoriaCuarto, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Cuarto> Cuartos = session.createQuery("from Cuarto c where c.categoriaCuarto.id = :id order by c.id", Cuarto.class)
    .setParameter("id", idCategoriaCuarto)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return Cuartos;
  }

  public void actualizarCuarto(Cuarto cuarto) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(cuarto);
    
    tr.commit();
    session.close();
  }

  public void eliminarCuarto(Cuarto cuarto) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(cuarto);

    tr.commit();
    session.close();
  }
}
