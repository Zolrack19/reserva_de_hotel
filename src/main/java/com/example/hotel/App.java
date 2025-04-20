package com.example.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class App extends Application {

  public static Scene scene;
  public static Parent loginRoot;
  public static Parent crearCuentaRoot;
  public static Parent inicioRoot;
  public static Parent resultadosRoot;
  public static Parent detallesRoot;
  public static Stage primaryStage;

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
    loginRoot = fxmlLoader.load();
    scene = new Scene(loginRoot);
    primaryStage = stage;
    stage.setScene(scene);
    stage.sizeToScene();
    stage.setTitle("Little View");
    stage.show();
  }

  public static void main(String[] args) {
    //Session sesion = HibernateUtil.getSession().openSession();
		//Transaction tx = sesion.beginTransaction();
    
    //Pais c = sesion.get(Pais.class,(short) 2);
    //System.out.println(c.getNombre());
    //tx.commit();
		//sesion.close();
		//System.out.println("exito!!!!!!!!!!");
    launch();
  }

}
