package com.mycalculator.logic;

/**
 * This class represents the logic for a calculator.
 * It provides methods for performing various mathematical operations.
 */
public class CalculatorLogic {
    public Parser parser = new Parser();

    /**
     * Processes the given numbers with the specified action.
     *
     * @param firstNumber the first number
     * @param secondNumber the second number
     * @param action the action to perform
     * @return the result of the operation
     * @throws ArithmeticException if the action is division and the second number is zero
     * @throws IllegalArgumentException if the action is not a valid operator
     */
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

    /**
     * Processes the given number with the specified action.
     *
     * @param firstNumber the number
     * @param action the action to perform
     * @return the result of the operation
     * @throws IllegalArgumentException if the action is not a valid operator
     */
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

    /**
     * Processes the memory with the specified action and current text.
     *
     * @param memory the memory
     * @param action the action to perform
     * @param currentText the current text
     * @return the result of the operation
     * @throws IllegalArgumentException if the action is not a valid operator
     */
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