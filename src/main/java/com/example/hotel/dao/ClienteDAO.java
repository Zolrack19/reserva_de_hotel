package com.example.hotel.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.hotel.dominio.Cliente;
import com.example.hotel.singleton.HibernateUtil;

/**
  Clase para realizar crud para la tabla cliente en la base de datos.
*/
public class ClienteDAO {
  
    public void crearCliente(Cliente cliente) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();

    session.save(cliente);

    tr.commit();
    session.close();
  }

  public Cliente getById(int id, boolean init) {
    Session session = HibernateUtil.getSession().openSession();
    Cliente cliente = session.get(Cliente.class, id);
    if (init) {
      cliente.getPais();
    } 
    session.close();
    return cliente;
  }

  public Cliente getByEmail(String email) {
    Session session = HibernateUtil.getSession().openSession();
    Cliente cliente = session.createQuery("from Cliente c where c.email = :email", Cliente.class)
    .setParameter("email", email)
    .uniqueResult();
    session.close();
    return cliente;
  }

  public boolean existeByEmail(String email) {
    Session session = HibernateUtil.getSession().openSession();
    long count = session.createQuery(
    "SELECT COUNT(c) FROM Cliente c WHERE c.email = :email", Long.class)
    .setParameter("email", email)
    .getSingleResult();
    session.close();
    return count > 0;
  }


  public List<Cliente> getClientesRango(int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Cliente> Clientes = session.createQuery("from Cliente c order by c.id", Cliente.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return Clientes;
  }

  public List<Cliente> getClientesPorPais(int idPais, int inicio, int fin) {
    Session session = HibernateUtil.getSession().openSession();
    List<Cliente> Clientes = session.createQuery("from Cliente c where c.pais.id = :id order by c.id", Cliente.class)
    .setParameter("id", idPais)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    session.close();
    return Clientes;
  }

  public void actualizarCliente(Cliente cliente) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.update(cliente);
    
    tr.commit();
    session.close();
  }

  public void eliminarCliente(Cliente cliente) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction tr = session.beginTransaction();
    
    session.delete(cliente);

    tr.commit();
    session.close();
  }

}
