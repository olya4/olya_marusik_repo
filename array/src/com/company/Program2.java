package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Program2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // ввод количества элементов массива
        int n = scanner.nextInt();
        int array[] = new int[n];

        for (int i = 0; i < array.length; i++) {
            // ввод элемента массива
            array[i] = scanner.nextInt();
        }

        for (int i = 0; i < array.length / 2; i++) {
            // новый элемент равен элементу из массива
            int temp = array[i];
            // элемент массива равен последнему элементу
            array[i] = array[array.length - i - 1];
            // последний элемент равен новому
            array[array.length - i - 1] = temp;
        }
        System.out.println(Arrays.toString(array));
    }
}
