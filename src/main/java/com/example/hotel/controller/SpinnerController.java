package com.example.hotel.controller;

import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class SpinnerController {

  @FXML
  private StackPane spinnerRoot;

  @FXML
  private Circle spinnerCircle;

  private RotateTransition rotate;

  public void initialize() {
    rotate = new RotateTransition(Duration.seconds(3), spinnerCircle);
    rotate.setByAngle(360);
    rotate.setCycleCount(Animation.INDEFINITE);
    rotate.setInterpolator(Interpolator.LINEAR);
    spinnerRoot.setVisible(false); // oculto por defecto
  }

  public void showSpinner() {
    spinnerRoot.setVisible(true);
    rotate.play();
  }

  public void hideSpinner() {
    rotate.stop();
    spinnerRoot.setVisible(false);
  }
}
