package com.company;

import java.util.Scanner;

public class Program1 {
    public static boolean isPowOf2(int n) {
        if (n == 1 || n % 2 == 0) {

            if (n == 1) {
                return true;
            } else {
                boolean result = isPowOf2(n / 2);
                return result;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        boolean result = isPowOf2(n);
        System.out.println(result);
    }
}
