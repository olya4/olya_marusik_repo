package com.company;

public class Main {

    public static void main(String[] args) {
        Range range = new Range(3);
        range.scale(2);
        range.perimeter();
        range.area();
        System.out.println(range.getName());
        System.out.println(range.perimeter());
        System.out.println(range.area());
        range.move(2, 3);
        System.out.println();

        Square square = new Square(3);
        square.scale(2);
        square.perimeter();
        square.area();
        System.out.println(square.getName());
        System.out.println(square.perimeter());
        System.out.println(square.area());
        square.move(9, 8);
        System.out.println();

        Rectangle rectangle = new Rectangle(3, 4);
        rectangle.scale(2);
        rectangle.perimeter();
        rectangle.area();
        System.out.println(rectangle.getName());
        System.out.println(rectangle.perimeter());
        System.out.println(rectangle.area());
        rectangle.move(5, 6);
        System.out.println();

        Ellipse ellipse = new Ellipse(3, 4);
        ellipse.scale(2);
        ellipse.perimeter();
        ellipse.area();
        System.out.println(ellipse.getName());
        System.out.println(ellipse.perimeter());
        System.out.println(ellipse.area());
        ellipse.move(7, 4);
        System.out.println();
    }
}
