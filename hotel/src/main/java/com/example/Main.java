package com.example;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class Main {
	public static void main(String[] args) {
		try {
			Session sesion = HibernateUtil.getSession().openSession();
			Transaction tx = sesion.beginTransaction();


			tx.commit();
			sesion.close();
			System.out.println("exito!!!!!!!!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}