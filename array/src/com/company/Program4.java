package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Program4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // ввод количества элементов массива
        int n = scanner.nextInt();
        int array[] = new int[n];

        for (int i = 0; i < array.length; i++) {
            // ввод элемента массива
            array[i] = scanner.nextInt();
        }
        System.out.println(Arrays.toString(array));

        int min = array[0];
        int max = array[0];
        int temp;
        int positionOfMin = 0;
        int positionOfMax = 0;

        for (int i = 0; i < array.length; i++) {
            // если элемент меньше min
            if (array[i] < min) {
                // то этот элемент min
                min = array[i];
                // индекс min позиции элемента
                positionOfMin = i;
            }
            // если элемент больше max
            if (max < array[i]) {
                // то этот элемент max
                max = array[i];
                // индекс max позиции элемента
                positionOfMax = i;
            }
        }

        System.out.println(min);
        System.out.println(max);

        temp = array[positionOfMin];
        array[positionOfMin] = array[positionOfMax];
        array[positionOfMax] = temp;

        System.out.println(Arrays.toString(array));
    }
}
