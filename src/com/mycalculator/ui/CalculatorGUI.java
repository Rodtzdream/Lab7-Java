package com.mycalculator.ui;

import com.mycalculator.logic.CalculatorLogic;
import com.mycalculator.logic.InterpreterException;
import com.mycalculator.logic.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * This class represents the GUI for a calculator.
 * It extends JFrame and implements ActionListener to handle button clicks.
 */
public class CalculatorGUI extends JFrame implements ActionListener {

    private final CalculatorLogic calculatorLogic = new CalculatorLogic();
    String expression = "0";

    private JLabel mainText;
    private final String[] buttonLabels = {
            "MS", "MR", "M+", "M-",
            "(", ")", "C", "Clear",
            "Sin", "Cos", "Tg", "Ctg",
            "%", "^", "√", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
    };

    private double memory = 0;

    /**
     * Constructs a new CalculatorGUI.
     * Sets the title, default close operation, and initializes the components.
     */
    public CalculatorGUI() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setSize(350, 700);
        setLocationRelativeTo(null);
    }

    /**
     * Initializes the components of the calculator GUI.
     * This includes the main label, additional text label, main text label, and buttons.
     */
    private void initComponents() {
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        JLabel mainLabel = new JLabel("Calculator", SwingConstants.LEFT);
        mainLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        mainLabel.setForeground(Color.WHITE);
        mainLabel.setBackground(new Color(35, 35, 35));
        mainLabel.setOpaque(true);
        mainLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(mainLabel);

        mainText = new JLabel("0", SwingConstants.RIGHT);
        mainText.setFont(new Font("Arial", Font.PLAIN, 40));
        mainText.setForeground(Color.WHITE);
        mainText.setBackground(new Color(35, 35, 35));
        mainText.setOpaque(true);
        mainText.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(mainText);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(8, 4));
        JButton[] buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFocusable(false);
            if (i < 4)
                buttons[i].setBackground(new Color(35, 35, 35));
            else if (i < 16 || (i + 1) % 4 == 0)
                buttons[i].setBackground(new Color(50, 50, 50));
            else
                buttons[i].setBackground(new Color(60, 60, 60));
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            buttons[i].setPreferredSize(new Dimension(70, 60));
            buttons[i].setMargin(new Insets(5, 5, 5, 5));
            buttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }
        buttons[buttonLabels.length - 1].setBackground(new Color(35, 180, 240));
        buttons[buttonLabels.length - 1].setForeground(new Color(50, 50, 50));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the action events from the buttons.
     * This includes digit buttons, operation buttons, and memory buttons.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (Character.isDigit(command.charAt(0)) && command.length() == 1) {
            if (Objects.equals(mainText.getText(), "0")) {
                mainText.setText("");
                expression = "";
            }
            mainText.setText(mainText.getText() + command);
            expression += command;
        } else {
            switch (command) {
                case ".":
                    mainText.setText(mainText.getText() + command);
                    expression += command;
                    break;
                case "C":
                    mainText.setText("0");
                    expression = "0";
                    break;
                case "Clear":
                    if (!mainText.getText().isEmpty()) {
                        mainText.setText(mainText.getText().substring(0, mainText.getText().length() - 1));
                        expression = expression.substring(0, expression.length() - 1);
                    }
                    break;
                case "MS":
                case "M+":
                case "M-":
                    if (!mainText.getText().isEmpty())
                        memory = calculatorLogic.processMemory(memory, command, mainText.getText());
                    break;
                case "MR":
                    if (memory != 0) {
                        mainText.setText(String.valueOf(memory));
                        expression = String.valueOf(memory);
                    }
                    break;
                case "+/-":
                    if (!mainText.getText().isEmpty()) {
                        if (mainText.getText().charAt(0) == '-') {
                            mainText.setText(mainText.getText().substring(1));
                            expression = expression.substring(1);
                        } else {
                            mainText.setText("-" + mainText.getText());
                            expression = "-" + expression;
                        }
                    }
                    break;
                case "√":
                case "Sin":
                case "Cos":
                case "Tg":
                case "Ctg":
                    if (!mainText.getText().isEmpty()) {
                        double result = 0;
                        try {
                            result = calculatorLogic.processNumbers((calculatorLogic.parser.evaluate(expression)), command);
                        } catch (InterpreterException ex) {
                            throw new RuntimeException(ex);
                        }
                        mainText.setText(Double.toString(result));
                        expression = String.valueOf(result);
                    }
                    break;
                case "(":
                case ")": {
                    mainText.setText(mainText.getText() + command);
                    expression += command;
                    break;
                }
                case "%":
                case "^":
                    if (!mainText.getText().isEmpty()) {
                        mainText.setText(mainText.getText() + command);
                        expression += command;
                    }
                    break;
                default:
                    if (command.charAt(0) == '=') {
                        if (!mainText.getText().isEmpty()) {
                            double result = 0;
                            try {
                                result = calculatorLogic.parser.evaluate(expression);
                            } catch (InterpreterException ex) {
                                mainText.setText("Error");
                                throw new RuntimeException(ex);
                            }
                            mainText.setText(Double.toString(result));
                            expression = String.valueOf(result);
                        }
                    } else {
                        if (!mainText.getText().isEmpty()) {
                            mainText.setText(mainText.getText() + command);
                            expression += command;
                        }
                    }
                    break;
            }
        }
    }

    /**
     * The main method that launches the calculator GUI.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) throws com.mycalculator.logic.InterpreterException {
        EventQueue.invokeLater(() -> new CalculatorGUI().setVisible(true));
    }
}