package com.company;


import java.util.List;

public interface CarsRepository {

    // найти машины по цвету или пробегу
    List<Long> findAllByColorOrMileage(String color, Long mileage);

    // количество уникальных моделей в ценовой политике
    long countFindUnique(int min, int max);

    // вывести цвет автомобиля с минимальной стоимостью
    String findMinCost();

}