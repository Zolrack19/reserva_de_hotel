package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.MedioPago;
import com.example.hotel.singleton.HibernateUtil;

/**
  Clase para realizar crud para la tabla medio_pago en la base de datos.
*/
public class MedioPagoDAO {
  
  public void crearMedioPago(MedioPago medioPago) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(medioPago);

    tr.commit();
    session.close();
  }

  public MedioPago getById(short id) {
    Session session = HibernateUtil.getSession().openSession();
    MedioPago medioPago = session.get(MedioPago.class, id);
    session.close();
    return medioPago;
  }

  public List<MedioPago> getMedioPagosRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<MedioPago> medioPagos = session.createQuery("from MedioPago m order by m.id", MedioPago.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return medioPagos;
  }

  public void actualizarMedioPago(MedioPago medioPago) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(medioPago);
    
    tr.commit();
    session.close();
  }

  public void eliminarMedioPago(MedioPago medioPago) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(medioPago);

    tr.commit();
    session.close();
  }
}
