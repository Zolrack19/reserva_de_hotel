package com.example.hotel.auxiliar;

import javafx.scene.Parent;
import lombok.Getter;

@Getter
public class Vista {
  private final Parent view; 
  private final Object controller;
  public Vista(Parent view, Object controller) {
    this.view = view;
    this.controller = controller;
  }
}