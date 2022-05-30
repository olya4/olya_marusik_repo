package com.company;

import java.util.Objects;
import java.util.StringJoiner;

public class Car {
    // номер автомобиля
    private Long number;
    // модель
    private String model;
    // цвет
    private String color;
    // пробег
    private Long mileage;
    // стоимость
    private Long price;

    public Car(Long number, String model, String color, Long mileage, Long price) {
        this.number = number;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(number, car.number) &&
                Objects.equals(model, car.model) &&
                Objects.equals(color, car.color) &&
                Objects.equals(mileage, car.mileage) &&
                Objects.equals(price, car.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, model, color, mileage, price);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("НОМЕР =" + number)
                .add("МОДЕЛЬ ='" + model + "'")
                .add("ЦВЕТ ='" + color + "'")
                .add("ПРОБЕГ =" + mileage)
                .add("ЦЕНА =" + price)
                .toString();
    }
}
