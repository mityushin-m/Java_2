package com.lab2;

import java.util.HashMap;
import java.util.Map;


/**
 * Калькулятор арифметических выражений.
 * Поддерживает операции +, -, *, /, унарный минус, скобки,
 * переменные (латинские буквы) и функции: sin, cos, tan, cot, sqrt.
 * 
 * @author mityushin-m
 */
public class Calculator {
    private String expr; //выражение
    private int pos; //текущая позиция в строке
    private char ch; //текущий символ
    private Map<String, Double> variables; //значения переменных

    /**
     * Конструктор, принимающий строку с выражением.
     * Удаляет все пробелы из выражения и инициализирует хранилище переменных.
     * 
     * @param expr строковое представление выражения (например, "2 + 3 * sin(x)")
     */
    public Calculator(String expr) {
        this.expr = expr.replaceAll("\\s+", "");
        this.pos = 0;
        if (this.expr.length() > 0) this.ch = this.expr.charAt(0);
        else this.ch = '\0';
        this.variables = new HashMap<>();
    }

    /**
     * Вычисляет значение выражения.
     * 
     * @return результат вычисления (тип double)
     * @throws RuntimeException если в выражении встречается ошибка (например, непарные скобки,
     *         неизвестный символ, деление на ноль, неопределённая функция и т.д.)
     */
    public double evaluate()  {
        double result = parseExpression();
        // Проверяем, что вся строка разобрана (нет лишних символов)
        if (pos < expr.length()) {
            throw new RuntimeException("Ошибка: лишние символы в конце выражения: " + expr.substring(pos));
        }
        return result;
    }

    /**
     * Разбирает выражение на уровне сложения и вычитания.
     * 
     * @return значение выражения
     * @throws RuntimeException при ошибке в дочерних элементах
     */
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

    /**
     * Разбирает выражение на уровне умножения и деления.
     * 
     * @return значение терма
     * @throws RuntimeException при делении на ноль
     */
    private double parseTerm() {
        double left = parseFactor();
        while (ch == '*' || ch == '/') {
            char op = ch;
            nextChar();
            double right = parseFactor();
            if (op == '*') left *= right;
            else {
            	if (Math.abs(right) < 1e-12) throw new RuntimeException("Ошибка: деление на ноль");
            	left /= right;
            }
        }
        return left;
    }
    
    /**
     * Считывает имя переменной или функции.
     * 
     * @return строковое имя
     */
    private String parseIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < expr.length() && Character.isLetter(ch)) {
            sb.append(ch);
            nextChar();
        }
        return sb.toString();
    }

    /**
     * Разбирает множитель: число, переменную, функцию, выражение в скобках или унарный минус.
     * 
     * @return значение множителя
     * @throws RuntimeException при непарных скобках, неожиданных символах и т.д.
     */
    private double parseFactor() {
        if (ch == '(') {
            nextChar();
            double val = parseExpression();
            if (ch != ')') {
                throw new RuntimeException("Ошибка: ожидалась закрывающая скобка, найдено: " + ch);
            }
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
        	String name = parseIdentifier();
        	if (ch == '(') {
                return parseFunction(name);
                }
        	else return getVariableValue(name);
        }
        throw new RuntimeException("Ошибка: неожиданный символ '" + ch + "' на позиции " + pos);
    }

    /**
     * Считывает число (целое или дробное).
     * 
     * @return значение числа
     * @throws RuntimeException при неверном формате числа
     */
    private double parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < expr.length() && (Character.isDigit(ch) || ch == '.')) {
            sb.append(ch);
            nextChar();
        }
        try {
        return Double.parseDouble(sb.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Ошибка: неверный формат числа '" + sb.toString() + "'");
        }
        
    }
    
    /**
     * Возвращает значение переменной. Если переменная ещё не определена,
     * запрашивает её значение у пользователя через консоль и запоминает.
     * 
     * @param varName имя переменной
     * @return числовое значение переменной
     */
    private double getVariableValue(String varName) {
        
        if (!variables.containsKey(varName)) {
            System.out.print("Введите значение переменной " + varName + ": ");
            try (java.util.Scanner sc = new java.util.Scanner(System.in)){
                if (sc.hasNextDouble()) {
                	double val = sc.nextDouble();
                	variables.put(varName, val);
                } else {
                    throw new RuntimeException("Ошибка: введите число для переменной " + varName);
                }
            }
        }
        return variables.get(varName);
    }
    
    /**
     * Вычисляет значение функции. Поддерживаются: sin, cos, tan, cot, sqrt.
     * 
     * @param name имя функции
     * @return результат вычисления
     * @throws RuntimeException если функция неизвестна или аргумент недопустим
     */
    private double parseFunction(String name) {
    	// текущий символ должен быть '('
        if (ch != '(') {
            throw new RuntimeException("Ошибка: после имени функции '" + name + "' ожидалась '('");
        }
        nextChar(); // пропускаем '('
        double arg = parseExpression();
        if (ch != ')') {
            throw new RuntimeException("Ошибка: ожидалась закрывающая скобка после аргумента функции " + name);
        }
        nextChar(); // пропускаем ')'
        switch (name) {
            case "sin": return Math.sin(arg);
            case "cos": return Math.cos(arg);
            case "tan": return Math.tan(arg);
            case "cot": 
                double sin = Math.sin(arg);
                if (Math.abs(sin) < 1e-12) {
                	throw new RuntimeException("Ошибка: котангенс не определён (sin(" + arg + ") = 0)");
                }
                return Math.cos(arg) / sin;
            case "sqrt": 
            	if (arg < 0) {
            		throw new RuntimeException("Ошибка: квадратный корень из отрицательного числа " + arg);
            	}
            	return Math.sqrt(arg);
            default: throw new RuntimeException("Ошибка: неизвестная функция '" + name + "'");
        }
    }

    /**
     * Переход к следующему символу в строке выражения.
     */
    private void nextChar() {
        pos++;
        if (pos < expr.length()) ch = expr.charAt(pos);
        else ch = '\0';
    }
}