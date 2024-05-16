package com.mycalculator.logic;

public class CalculatorLogic {

    public double processNumbers(double firstNumber, double secondNumber, char action) {
        return switch (action) {
            case '+' -> firstNumber + secondNumber;
            case '-' -> firstNumber - secondNumber;
            case '*' -> firstNumber * secondNumber;
            case '/' -> {
                if (secondNumber != 0)
                    yield firstNumber / secondNumber;
                else
                    throw new ArithmeticException("Cannot divide by zero");
            }
            case '%' -> (int) firstNumber % (int) secondNumber;
            default -> throw new IllegalArgumentException("Invalid operator: " + action);
        };
    }

    public double processNumbers(double firstNumber, String action) {
        return switch (action) {
            case "1/x" -> {
                if (firstNumber != 0)
                    yield 1 / firstNumber;
                else
                    yield 0;
            }
            case "x²" -> firstNumber * firstNumber;
            case "√x" -> Math.sqrt(firstNumber);
            case "Sin" -> Math.sin(firstNumber);
            case "Cos" -> Math.cos(firstNumber);
            case "Tg" -> Math.tan(firstNumber);
            case "Ctg" -> 1 / Math.tan(firstNumber);
            default -> throw new IllegalArgumentException("Invalid operator: " + action);
        };
    }

    public double processMemory(double memory, String action, String currentText) {
        double currentNumber = Double.parseDouble(currentText);
        return switch (action) {
            case "MS" -> currentNumber;
            case "M+" -> memory + currentNumber;
            case "M-" -> memory - currentNumber;
            default -> throw new IllegalArgumentException("Invalid operator: " + action);
        };
    }
}