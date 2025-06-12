package com.example.hotel.singleton;

import java.io.File;
// import java.nio.file.Paths;

import lombok.Getter;
/**
  Enum que alamcena la url de imagenes estáticas, para centralización.
*/
@Getter
public enum Imagenes {

  DB("imagenesdb" + File.separator + "imagenes"),
  ESTRELLA(Imagenes.class.getResource("/com/example/imagenes/star.png").toString());
  
  private final String url;
  
  private Imagenes(String url) {
    this.url = url;
  }


}
