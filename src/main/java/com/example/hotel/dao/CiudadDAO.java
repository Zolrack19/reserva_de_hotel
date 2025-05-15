package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.Ciudad;
import com.example.hotel.util.HibernateUtil;

public class CiudadDAO {

  public void crearCiudad(Ciudad ciudad) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(ciudad);

    tr.commit();
    session.close();
  }

  public Ciudad getdById(int id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Ciudad ciudad = session.get(Ciudad.class, id);
    if (init) {
      ciudad.getPais();
    }
    session.close();
    return ciudad;
  }

  public List<Ciudad> getCiudadesRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Ciudad> ciudades = session.createQuery("from Ciudad c order by c.id", Ciudad.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return ciudades;
  }

  public List<Ciudad> getCiudadesPorPais(short idPais, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Ciudad> ciudades = session.createQuery("from Ciudad c where c.pais.id = :id order by c.id", Ciudad.class)
    .setParameter("id", idPais)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return ciudades;
  }
  
  public void actualizarCiudad(Ciudad Ciudad) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(Ciudad);
    
    tr.commit();
    session.close();
  }

  public void eliminarCiudad(Ciudad Ciudad) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(Ciudad);

    tr.commit();
    session.close();
  }
}
