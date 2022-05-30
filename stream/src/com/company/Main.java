package com.company;

public class Main {

    public static void main(String[] args) {
        CarsRepository carsRepository = new CarsRepositoryFilesImpl("car.txt");
        System.out.println(carsRepository.findAllByColorOrMileage("черный", 0L));
        System.out.println(carsRepository.countFindUnique(700,800));
        System.out.println(carsRepository.findMinCost());
    }
}
