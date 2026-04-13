package com.lab2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение (числа, + - * /, скобки):");
        String input = scanner.nextLine();

        try {
            Calculator calc = new Calculator(input);
            double result = calc.evaluate();
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}