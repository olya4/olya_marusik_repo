package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CarsRepositoryFilesImpl implements CarsRepository {
    // имя файла из которого читать
    private String fileName;

    private static Function<String, Car> carMapper = line -> {
        String parsedLine[] = line.split("#"); // разобьет строку из файла на массив строк
        // вернет список
        return new Car(Long.parseLong(parsedLine[0]),
                parsedLine[1],
                parsedLine[2],
                Long.parseLong(parsedLine[3]),
                Long.parseLong(parsedLine[4]));
    };

    public CarsRepositoryFilesImpl(String fileName) {
        this.fileName = fileName;
    }

    // найти машины по цвету или пробегу
    @Override
    public List<Long> findAllByColorOrMileage(String color, Long mileage) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<Long> cars = reader
                    .lines() // возвращает строки
                    .map(carMapper) // список машин
                    .filter(car -> car.getColor().equals(color) || car.getMileage().equals(mileage)) // выбрать машину по заданному цвету или пробегу
                    .map(Car::getNumber) // вернуть номера машин
                    .collect(Collectors.toList()); // преобразовать в список

            reader.close();
            // вернуть список отфильтрованных машин
            return cars;

        } catch (FileNotFoundException e) {
            // неправильный аргумент функции (fileName)
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            // приложение в некорректном состоянии
            throw new IllegalStateException(e);
        }
    }

    // количество уникальных моделей в ценовой политике от 700 до 800
    @Override
    public long countFindUnique(int min, int max) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<Car> cars = reader
                    .lines() // возвращает строки
                    .map(carMapper) // список машин
                    .collect(Collectors.toList()); // преобразовать в список

            // количество подходящих машин
            long total = cars.stream()
                    // выбрать машину по цене
                    .filter(car -> car.getPrice() >= min && car.getPrice() <= max)
                    // уникальные модели
                    .distinct()
                    // количество
                    .count();

            reader.close();
            // вернуть количество
            return total;

        } catch (FileNotFoundException e) {
            // неправильный аргумент функции (fileName)
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            // приложение в некорректном состоянии
            throw new IllegalStateException(e);
        }
    }

    // вывести цвет автомобиля с минимальной стоимостью
    @Override
    public String findMinCost() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<Car> cars = reader
                    .lines() // возвращает строки
                    .map(carMapper) // список машин
                    .collect(Collectors.toList()); // преобразовать в список

            // цвет машины с минимальной стоимостью
            String minCost = cars.stream()
                    .min(Comparator.comparingLong(Car::getPrice)) // вернуть стоимость машин
                    .map(Car::getColor) // вернуть цвет машин
                    .orElse("Нет такой машины");

            reader.close();
            // вернуть цвет машины с минимальной стоимостью
            return minCost;

        } catch (FileNotFoundException e) {
            // неправильный аргумент функции (fileName)
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            // приложение в некорректном состоянии
            throw new IllegalStateException(e);
        }
    }

}