package com.company;

import java.util.Scanner;

public class Program2 {
    public static int binarySearch(int array[], int element, int left, int right) {

        if (left < right){
            // найти середину
            int middle = left + (right - left)/2;

            if (array[middle] > element){
                return binarySearch(array, element, left, middle);
            }
            else if (array[middle] < element){
                return 	binarySearch(array, element, middle + 1, right);
            }
            else return middle;
        }
        return -1;
    }
    public static void main (String [] args){
        Scanner scanner = new Scanner(System.in);
        int element = scanner.nextInt();
        int array[] = { -16, 5, 7, 29, 54, 96, 316, 378, 459};
        int left = 0;
        int right = array.length;
        int result = binarySearch(array, element, left, right);
        System.out.println("index of the search element " + result);

    }
}
