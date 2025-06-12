package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.Categoria;
import com.example.hotel.singleton.HibernateUtil;

/**
  Clase para realizar crud para la tabla categoria en la base de datos.
*/
public class CategoriaDAO {
  
  public void crearCategoria(Categoria categoria) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(categoria);

    tr.commit();
    session.close();
  }

  public Categoria getById(short id) {
    Session session = HibernateUtil.getSession().openSession();
    Categoria categoria = session.get(Categoria.class, id);
    session.close();
    return categoria;
  }

  public List<Categoria> getCategoriasRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Categoria> categorias = session.createQuery("from Categoria c order by c.id", Categoria.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return categorias;
  }

  public void actualizarCategoria(Categoria Categoria) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(Categoria);
    
    tr.commit();
    session.close();
  }

  public void eliminarCategoria(Categoria Categoria) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(Categoria);

    tr.commit();
    session.close();
  }
}
