package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.Pais;
import com.example.hotel.util.HibernateUtil;

/**
  Clase para realizar crud para la tabla pais en la base de datos.
*/
public class PaisDAO {

  public void crearPais(Pais pais) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(pais);

    tr.commit();
    session.close();
  }
  
  public Pais getById(short id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Pais pais = session.get(Pais.class, id);
    if (init) {
      pais.getDivisa();
    }
    session.close();
    return pais;
  }

  public List<Pais> getPaisesRango(int inicio, int fin, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    List<Pais> paises;
    if (init) {
      paises = session.createQuery("from Pais p JOIN FETCH p.divisa order by p.id", Pais.class)
      .setFirstResult(inicio)
      .setMaxResults(fin)
      .list();
    } else {
      paises = session.createQuery("from Pais p order by p.id", Pais.class)
      .setFirstResult(inicio)
      .setMaxResults(fin)
      .list();
    }
    session.close();
    return paises;
  }

  public List<Pais> getPaisesPorDivisa(short idDivisa, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Pais> paises = session.createQuery("from Pais p where p.divisa.id = :id order by p.id", Pais.class)
    .setParameter("id", idDivisa)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return paises;
  }

  public void actualizarPais(Pais pais) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.update(pais);

    tr.commit();
    session.close();
  }

  public void eliminarPais(Pais pais) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.delete(pais);

    tr.commit();
    session.close();
  }

  public void instanciarDivisa(Pais pais) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    pais = (Pais) session.merge(pais);
    tr.commit();
    session.close();
  }

}
