package com.web.kpp.entity;

public class TriangleIdentification {
  private boolean isEquilateral;
  private boolean isIsosceles;
  private boolean isRectangular;
  private int counter;

  public TriangleIdentification() {}

  public TriangleIdentification(boolean isEquilateral, boolean isIsosceles, boolean isRectangular, int counter) {
    this.isEquilateral = isEquilateral;
    this.isIsosceles = isIsosceles;
    this.isRectangular = isRectangular;
    this.counter = counter;
  }

  public boolean getIsEquilateral() {
    return isEquilateral;
  }

  public boolean getIsIsosceles() {
    return isIsosceles;
  }

  public boolean getIsRectangular() {
    return isRectangular;
  }

  public int getCounter() {
    return counter;
  }

  public void setIsEquilateral(boolean isEquilateral) {
    this.isEquilateral = isEquilateral;
  }

  public void setIsIsosceles(boolean isIsosceles) {
    this.isIsosceles = isIsosceles;
  }

  public void setIsRectangular(boolean isRectangular) {
    this.isRectangular = isRectangular;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }
}
