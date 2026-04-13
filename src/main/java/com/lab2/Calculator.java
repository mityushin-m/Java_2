package com.lab2;

public class Calculator {
    private String expr;
    private int pos;
    private char ch;

    public Calculator(String expr) {
        this.expr = expr.replaceAll("\\s+", "");
        this.pos = 0;
        if (this.expr.length() > 0) this.ch = this.expr.charAt(0);
        else this.ch = '\0';
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
        else if (ch == '-') {
            nextChar();
            return -parseFactor();
        }
        else if(Character.isDigit(ch)) {
            return parseNumber();
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

    private void nextChar() {
        pos++;
        if (pos < expr.length()) ch = expr.charAt(pos);
        else ch = '\0';
    }
}