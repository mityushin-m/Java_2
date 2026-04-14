package com.lab2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CalculatorTest {

    // --- Тесты корректных выражений ---
    @Test
    void testAddition() {
        Calculator calc = new Calculator("2+3");
        assertEquals(5.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testSubtraction() {
        Calculator calc = new Calculator("10-4");
        assertEquals(6.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testMultiplication() {
        Calculator calc = new Calculator("3*5");
        assertEquals(15.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testDivision() {
        Calculator calc = new Calculator("10/4");
        assertEquals(2.5, calc.evaluate(), 1e-9);
    }

    @Test
    void testOperatorPrecedence() {
        Calculator calc = new Calculator("2+3*4");
        assertEquals(14.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testParentheses() {
        Calculator calc = new Calculator("(2+3)*4");
        assertEquals(20.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testUnaryMinus() {
        Calculator calc = new Calculator("-5+3");
        assertEquals(-2.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testComplexExpression() {
        Calculator calc = new Calculator("2*(3+4*(5-2))");
        assertEquals(30.0, calc.evaluate(), 1e-9);
    }

    // --- Тесты функций ---
    @Test
    void testSin() {
        Calculator calc = new Calculator("sin(0)");
        assertEquals(0.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testCos() {
        Calculator calc = new Calculator("cos(0)");
        assertEquals(1.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testTan() {
        Calculator calc = new Calculator("tan(0)");
        assertEquals(0.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testSqrt() {
        Calculator calc = new Calculator("sqrt(16)");
        assertEquals(4.0, calc.evaluate(), 1e-9);
    }

    @Test
    void testCot() {
        // cot(π/4) = 1, но π/4 вычислим как 0.7853981633974483
        Calculator calc = new Calculator("cot(0.7853981633974483)");
        assertEquals(1.0, calc.evaluate(), 1e-6);
    }

    // --- Тесты ошибок (должны выбрасывать исключения) ---
    @Test
    void testDivisionByZero() {
        Calculator calc = new Calculator("5/0");
        assertThrows(RuntimeException.class, calc::evaluate);
    }

    @Test
    void testSqrtNegative() {
        Calculator calc = new Calculator("sqrt(-9)");
        assertThrows(RuntimeException.class, calc::evaluate);
    }

    @Test
    void testCotZero() {
        // cot(0) не определён
        Calculator calc = new Calculator("cot(0)");
        assertThrows(RuntimeException.class, calc::evaluate);
    }

    @Test
    void testMismatchedParentheses() {
        Calculator calc = new Calculator("(2+3");
        assertThrows(RuntimeException.class, calc::evaluate);
    }

    @Test
    void testInvalidCharacter() {
        Calculator calc = new Calculator("2+@3");
        assertThrows(RuntimeException.class, calc::evaluate);
    }

    @Test
    void testUnknownFunction() {
        Calculator calc = new Calculator("unknown(5)");
        assertThrows(RuntimeException.class, calc::evaluate);
    }

    @Test
    void testExtraCharacters() {
        Calculator calc = new Calculator("2+3 abc");
        assertThrows(RuntimeException.class, calc::evaluate);
    }
}