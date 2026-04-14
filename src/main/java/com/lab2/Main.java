package com.lab2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение (числа, переменные, + - * /, скобки, а так же функции sin(x), cos(x), tan(x),  cot(x), sqrt(x)):");
        String input = scanner.nextLine();

        try {
            Calculator calc = new Calculator(input);
            double result = calc.evaluate();
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}