package com.company;

public class Ellipse extends GeometricShape {
    private int a;
    private int b;

    public Ellipse(int a, int b) {
        super("Эллипс");
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
        return 4 * ((Math.PI * a * b) + ((a - b) * (a - b))) / (a + b);
    }

    @Override
    public double area() {
        return Math.PI * a * b;
    }
}
