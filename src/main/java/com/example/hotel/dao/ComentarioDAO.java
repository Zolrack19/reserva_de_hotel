package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.Comentario;
import com.example.hotel.util.HibernateUtil;

public class ComentarioDAO {
  
  public void crearComentario(Comentario comentario) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(comentario);

    tr.commit();
    session.close();
  }

  public Comentario getById(long id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Comentario comentario = session.get(Comentario.class, id);
    if (init) {
      comentario.getCliente();
      comentario.getHotel();
    }
    session.close();
    return comentario;
  }

  public List<Comentario> getComentariosRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Comentario> Comentarios = session.createQuery("from Comentario c order by c.id", Comentario.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return Comentarios;
  }

  public List<Comentario> getComentariosPorCliente(int idCliente, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Comentario> comentarios = session.createQuery("from Comentario c where c.cliente.id = :id order by c.id", Comentario.class)
    .setParameter("id", idCliente)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return comentarios;
  }

  public List<Comentario> getComentariosPorHotel(long idHotel, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Comentario> comentarios = session.createQuery("from Comentario c where c.hotel.id = :id order by c.id", Comentario.class)
    .setParameter("id", idHotel)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return comentarios;
  }

  public void actualizarComentario(Comentario comentario) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(comentario);
    
    tr.commit();
    session.close();
  }

  public void eliminarComentario(Comentario comentario) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(comentario);

    tr.commit();
    session.close();
  }
}
