package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.HibernateUtil;
import com.example.hotel.dominio.Boleta;

public class BoletaDAO {
  
  public void crearBoleta(Boleta boleta) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(boleta);

    tr.commit();
    session.close();
  }

  public Boleta getById(long id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Boleta boleta = session.get(Boleta.class, id);
    if (init) {
      boleta.getMedioPago();
      boleta.getReserva();
    }
    session.close();
    return boleta;
  }

  public Boleta getPorReserva(long idReserva) {
    Session session = HibernateUtil.getSession().openSession();
    Boleta boleta = session.createQuery("from Boleta b.reserva.id = :id", Boleta.class)
    .setParameter("id", idReserva)
    .uniqueResult();
    session.close();
    return boleta;
  }

  public List<Boleta> getBoletasRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Boleta> boletas = session.createQuery("from Boleta b order by b.id", Boleta.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return boletas;
  }

  public List<Boleta> getBoletasPorMedioPago(short idMedioPago, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Boleta> boletas = session.createQuery("from Boleta b where b.medioPago.id = :id order by b.id", Boleta.class)
    .setParameter("id", idMedioPago)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return boletas;
  }

  public List<Boleta> getBoletasPorCliente(int idCliente, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Boleta> boletas = session.createQuery("from Boleta b where b.reserva.cliente.id = :id order by b.id", Boleta.class)
    .setParameter("id", idCliente)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return boletas;
  }

  public void actualizarBoleta(Boleta boleta) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(boleta);
    
    tr.commit();
    session.close();
  }

  public void eliminarBoleta(Boleta boleta) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(boleta);

    tr.commit();
    session.close();
  }

}
