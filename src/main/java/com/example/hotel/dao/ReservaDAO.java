package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.Reserva;
import com.example.hotel.singleton.HibernateUtil;

/**
  Clase para realizar crud para la tabla reserva en la base de datos.
*/
public class ReservaDAO {
  
  public void crearReserva(Reserva reserva) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(reserva);

    tr.commit();
    session.close();
  }

  public Reserva getById(long id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Reserva reserva = session.get(Reserva.class, id);
    if (init) {
      reserva.getBoleta();
      reserva.getCliente();
      reserva.getCuarto();
    }
    session.close();
    return reserva;
  }

  public Reserva getPorBoleta(long idBoleta) {
    Session session = HibernateUtil.getSession().openSession();
    Reserva reserva = session.createQuery("from Reserva r r.boleta.id = :id", Reserva.class)
    .setParameter("id", idBoleta)
    .uniqueResult();
    session.close();
    return reserva;
  }
  
  public Reserva getPorCliente(int idCliente) {
    Session session = HibernateUtil.getSession().openSession();
    Reserva reserva = session.createQuery("from Reserva r r.cliente.id = :id", Reserva.class)
    .setParameter("id", idCliente)
    .uniqueResult();
    session.close();
    return reserva;
  }

  public List<Reserva> getReservasRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Reserva> reservas = session.createQuery("from Reserva r order by r.id", Reserva.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return reservas;
  }

  public List<Reserva> getReservasPorCliente(int idCliente, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Reserva> reservas = session.createQuery("from Reserva r where r.cliente.id = :id order by r.id", Reserva.class)
    .setParameter("id", idCliente)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return reservas;
  }

  public List<Reserva> getReservasPorCuarto(long idCuarto, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Reserva> reservas = session.createQuery("from Reserva r where r.cuarto.id = :id order by r.id", Reserva.class)
    .setParameter("id", idCuarto)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return reservas;
  }

  public void actualizarReserva(Reserva reserva) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(reserva);
    
    tr.commit();
    session.close();
  }

  public void eliminarReserva(Reserva reserva) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(reserva);

    tr.commit();
    session.close();
  }
}
