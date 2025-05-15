package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.Divisa;
import com.example.hotel.util.HibernateUtil;

public class DivisaDAO {
   public void crearDivisa(Divisa divisa) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(divisa);

    tr.commit();
    session.close();
  }

  public Divisa getById(short id) {
    Session session = HibernateUtil.getSession().openSession();
    Divisa Divisa = session.get(Divisa.class, id);
    session.close();
    return Divisa;
  }

  public List<Divisa> getDivisasRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Divisa> divisas = session.createQuery("from Divisa d order by d.id", Divisa.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return divisas;
  }

  public void actualizarDivisa(Divisa divisa) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(divisa);
    
    tr.commit();
    session.close();
  }

  public void eliminarDivisa(Divisa divisa) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(divisa);

    tr.commit();
    session.close();
  }
}
