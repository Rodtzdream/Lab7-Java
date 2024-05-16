import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class CalculatorGUI extends JFrame implements ActionListener {

    private JLabel mainLabel;
    private JLabel mainText;
    private JLabel additionalText;
    private JButton[] buttons;
    private String[] buttonLabels = {
            "MS", "MR", "M+", "M-",
            "%", "CE", "C", "Clear",
            "Sin", "Cos", "Tg", "Ctg",
            "1/x", "x²", "√x", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
    };

    private double num1, num2;
    private char operator;
    private boolean calculated = false;
    private double memory = 0;

    public CalculatorGUI() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setSize(430, 750);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));

        mainLabel = new JLabel("Calculator", SwingConstants.LEFT);
        mainLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        mainLabel.setForeground(Color.WHITE);
        mainLabel.setBackground(new Color(35, 35, 35));
        mainLabel.setOpaque(true);
        mainLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(mainLabel);

        additionalText = new JLabel("", SwingConstants.RIGHT);
        additionalText.setFont(new Font("Arial", Font.PLAIN, 20));
        additionalText.setForeground(new Color(150, 150, 150));
        additionalText.setBackground(new Color(35, 35, 35));
        additionalText.setOpaque(true);
        additionalText.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(additionalText);

        mainText = new JLabel("0", SwingConstants.RIGHT);
        mainText.setFont(new Font("Arial", Font.PLAIN, 40));
        mainText.setForeground(Color.WHITE);
        mainText.setBackground(new Color(35, 35, 35));
        mainText.setOpaque(true);
        mainText.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(mainText);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(8, 4));
        buttons = new JButton[buttonLabels.length];
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (Character.isDigit(command.charAt(0)) && command.length() == 1) {
            if (Objects.equals(mainText.getText(), "0") || calculated)
                mainText.setText("");
            mainText.setText(mainText.getText() + command);
            calculated = false;
        } else if (command.equals(".")) {
            if (!mainText.getText().contains("."))
                mainText.setText(mainText.getText() + command);
        } else if (command.equals("CE")) {
            mainText.setText("0");
        } else if (command.equals("C")) {
            mainText.setText("0");
            additionalText.setText("");
        } else if (command.equals("Clear")) {
            mainText.setText(mainText.getText().substring(0, mainText.getText().length() - 1));
        } else if (command.charAt(0) == '=') {
            if (!additionalText.getText().isEmpty() && !mainText.getText().isEmpty()) {
                num2 = Double.parseDouble(mainText.getText());
                double result = processNumbers(num1, num2, operator);
                mainText.setText(Double.toString(result));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("1/x")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                if (num1 != 0)
                    mainText.setText(String.valueOf(1 / num1));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("x²")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                mainText.setText(String.valueOf(num1 * num1));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("√x")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                mainText.setText(String.valueOf(Math.sqrt(num1)));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("+/-")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                additionalText.setText("");
                mainText.setText(String.valueOf(-num1));
            }
        } else if (command.equals("Sin")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                mainText.setText(String.valueOf(Math.sin(num1)));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("Cos")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                mainText.setText(String.valueOf(Math.cos(num1)));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("Tg")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                mainText.setText(String.valueOf(Math.tan(num1)));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("Ctg")) {
            if (!mainText.getText().isEmpty()) {
                num1 = Double.parseDouble(mainText.getText());
                mainText.setText(String.valueOf(1 / Math.tan(num1)));
                additionalText.setText("");
                calculated = true;
            }
        } else if (command.equals("MS")) {
            memory = Double.parseDouble(mainText.getText());
        } else if (command.equals("MR")) {
            if (memory != 0)
                mainText.setText(String.valueOf(memory));
        } else if (command.equals("M+")) {
            memory += Double.parseDouble(mainText.getText());
        } else if (command.equals("M-")) {
            memory -= Double.parseDouble(mainText.getText());
        } else {
            operator = command.charAt(0);
            num1 = Double.parseDouble(mainText.getText());
            additionalText.setText(mainText.getText() + " " + operator);
            mainText.setText("");
        }
    }

    public double processNumbers(double firstNumber, double secondNumber, char action) {
        switch (action) {
            case '+':
                return firstNumber + secondNumber;
            case '-':
                return firstNumber - secondNumber;
            case '*':
                return firstNumber * secondNumber;
            case '/':
                if (secondNumber != 0)
                    return firstNumber / secondNumber;
                else
                    throw new ArithmeticException("Cannot divide by zero");
            case '%':
                return (int) firstNumber % (int) secondNumber;
            default:
                throw new IllegalArgumentException("Invalid operator: " + action);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new CalculatorGUI().setVisible(true));
    }
}