package com.company;

public abstract class GeometricShape implements ShapeScale, MovingCenter {
    private String name;
    private int x = 0;
    private int y = 0;

    public GeometricShape(String name) {
        this.name = name;
    }

    // расчитать периметр
    public abstract double perimeter();

    // расчитать площадь
    public abstract double area();


    @Override
    public void scale(int k) {
    }

    @Override
    public void move(int x, int y) {
        System.out.println("Новые координаты центра фигуры: x = " + x + ", y = " + y);
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}