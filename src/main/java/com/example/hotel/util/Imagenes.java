package com.example.hotel.util;

import lombok.Getter;

@Getter
public enum Imagenes {

  ESTRELLA(Imagenes.class.getResource("/com/example/imagenes/star.png").toString());
  
  private final String url;
  
  private Imagenes(String url) {
    this.url = url;
  }


}
