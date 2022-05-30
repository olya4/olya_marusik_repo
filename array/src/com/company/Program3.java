package com.company;

import java.util.Scanner;

public class Program3 {
    public static void main (String [] args){
        Scanner scanner = new Scanner (System.in);
        // ввод количества элементов массива
        int n = scanner.nextInt();
        int array[] = new int [n];
        int arraySum = 0;
        double arrayAverage = 0;

        for (int i = 0; i < array.length; i++){
            // ввод элемента массива
            array [i] = scanner.nextInt();
            // сумма всех элементов
            arraySum += array[i];
        }
        arrayAverage = arraySum / n;
        System.out.println(arrayAverage);
    }
}
