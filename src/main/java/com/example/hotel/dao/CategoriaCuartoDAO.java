package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.CategoriaCuarto;
import com.example.hotel.util.HibernateUtil;

/**
  Encargado de proporcionar un crud para la tabla categoria_cuarto en la base de datos,
*/
public class CategoriaCuartoDAO {
  
  public void crearCategoriaCuarto(CategoriaCuarto categoriaCuarto) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    session.save(categoriaCuarto);
    tr.commit();
    session.close();
  }

  public CategoriaCuarto getById(short id) {
    Session session = HibernateUtil.getSession().openSession();
    CategoriaCuarto categoriaCuarto = session.get(CategoriaCuarto.class, id);
    session.close();
    return categoriaCuarto;
  }

  public List<CategoriaCuarto> getCategoriaCuartosRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<CategoriaCuarto> categoriaCuartos = session.createQuery("from CategoriaCuarto c order by c.id", CategoriaCuarto.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return categoriaCuartos;
  }

  public void actualizarCategoriaCuarto(CategoriaCuarto categoriaCuarto) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(categoriaCuarto);
    
    tr.commit();
    session.close();
  }

  public void eliminarCategoriaCuarto(CategoriaCuarto categoriaCuarto) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(categoriaCuarto);

    tr.commit();
    session.close();
  }
}
