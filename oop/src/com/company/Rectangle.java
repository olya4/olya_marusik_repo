package com.company;

public class Rectangle extends GeometricShape {
    private int a;
    private int b;

    public Rectangle(int a, int b) {
        super("Прямоугольник");
        this.a = a;
        this.b = b;
    }

    public Rectangle(String name, int a, int b) {
        super("Квадрат");
        this.a = a;
        this.b = b;
    }

    @Override
    public void scale(int k) {
        a = a * k;
        b = b * k;
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
    }

    @Override
    public double perimeter() {
        return 2 * (a + b);
    }

    @Override
    public double area() {
        return a * b;
    }
}
