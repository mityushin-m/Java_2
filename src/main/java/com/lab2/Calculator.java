package com.lab2;

import java.util.HashMap;
import java.util.Map;

public class Calculator {
    private String expr;
    private int pos;
    private char ch;
    private Map<String, Double> variables;

    public Calculator(String expr) {
        this.expr = expr.replaceAll("\\s+", "");
        this.pos = 0;
        if (this.expr.length() > 0) this.ch = this.expr.charAt(0);
        else this.ch = '\0';
        this.variables = new HashMap<>();
    }

    public double evaluate()  {
        double result = parseExpression();
        return result;
    }

    private double parseExpression()  {
        double left = parseTerm();
        while (ch == '+' || ch == '-') {
            char op = ch;
            nextChar();
            double right = parseTerm();
            if (op == '+') left += right;
            else left -= right;
        }
        return left;
    }

    private double parseTerm() {
        double left = parseFactor();
        while (ch == '*' || ch == '/') {
            char op = ch;
            nextChar();
            double right = parseFactor();
            if (op == '*') left *= right;
        }
        return left;
    }

    private double parseFactor() {
        if (ch == '(') {
            nextChar();
            double val = parseExpression();
            nextChar();
            return val;
        }
        if (ch == '-') {
            nextChar();
            return -parseFactor();
        }
        if(Character.isDigit(ch)) {
            return parseNumber();
        }
        if (Character.isLetter(ch)) {
            return parseVariable();
        }
        return 0;
    }

    private double parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < expr.length() && (Character.isDigit(ch) || ch == '.')) {
            sb.append(ch);
            nextChar();
        }
        
        return Double.parseDouble(sb.toString());
        
    }
    
    private double parseVariable() {
        StringBuilder sb = new StringBuilder();
        while (pos < expr.length() && Character.isLetter(ch)) {
            sb.append(ch);
            nextChar();
        }
        String varName = sb.toString();
        if (!variables.containsKey(varName)) {
            System.out.print("Введите значение переменной " + varName + ": ");
            java.util.Scanner sc = new java.util.Scanner(System.in);
            double val = sc.nextDouble();
            variables.put(varName, val);
        }
        return variables.get(varName);
    }

    private void nextChar() {
        pos++;
        if (pos < expr.length()) ch = expr.charAt(pos);
        else ch = '\0';
    }
}