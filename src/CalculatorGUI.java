import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class CalculatorGUI extends JFrame implements ActionListener {

    private final CalculatorLogic calculatorLogic = new CalculatorLogic();

    private JLabel mainText;
    private JLabel additionalText;
    private final String[] buttonLabels = {
            "MS", "MR", "M+", "M-",
            "%", "CE", "C", "Clear",
            "Sin", "Cos", "Tg", "Ctg",
            "1/x", "x²", "√x", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
    };

    private double num1;
    private char operator;
    private boolean calculated = false;
    private double memory = 0;

    public CalculatorGUI() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setSize(430, 700);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));

        JLabel mainLabel = new JLabel("Calculator", SwingConstants.LEFT);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (Character.isDigit(command.charAt(0)) && command.length() == 1) {
            if (Objects.equals(mainText.getText(), "0") || calculated)
                mainText.setText("");
            mainText.setText(mainText.getText() + command);
            calculated = false;
        } else {
            switch (command) {
                case ".":
                    if (!mainText.getText().contains("."))
                        mainText.setText(mainText.getText() + command);
                    break;
                case "CE":
                case "C":
                    mainText.setText("0");
                    if (command.equals("C")) {
                        additionalText.setText("");
                    }
                    break;
                case "Clear":
                    mainText.setText(mainText.getText().substring(0, mainText.getText().length() - 1));
                    break;
                case "MS":
                case "M+":
                case "M-":
                    if (!mainText.getText().isEmpty())
                        memory = calculatorLogic.processMemory(memory, command, mainText.getText());
                    break;
                case "MR":
                    if (memory != 0)
                        mainText.setText(String.valueOf(memory));
                    break;
                default:
                    if (command.charAt(0) == '=') {
                        if (!additionalText.getText().isEmpty() && !mainText.getText().isEmpty()) {
                            double num2 = Double.parseDouble(mainText.getText());
                            double result = calculatorLogic.processNumbers(num1, num2, operator);
                            mainText.setText(Double.toString(result));
                            additionalText.setText("");
                            calculated = true;
                        }
                    } else if (command.length() > 1) {
                        if (!mainText.getText().isEmpty()) {
                            num1 = Double.parseDouble(mainText.getText());
                            double result = calculatorLogic.processNumbers(num1, command);
                            mainText.setText(String.valueOf(result));
                            additionalText.setText("");
                            calculated = true;
                        }
                    } else {
                        if (!mainText.getText().isEmpty()) {
                            operator = command.charAt(0);
                            num1 = Double.parseDouble(mainText.getText());
                            additionalText.setText(mainText.getText() + " " + operator);
                            mainText.setText("");
                        }
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new CalculatorGUI().setVisible(true));
    }
}