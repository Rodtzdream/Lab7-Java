package com.mycalculator.logic;

import java.util.Arrays;

public class Parser {

    private static final String EOP = "\0";
    private static final int DELIMITER = 1;
    private static final int VARIABLE = 2;
    private static final int NUMBER = 3;
    private static final int NONE = 0;
    private static final int LE = -1;  // Dummy value, change according to actual value.
    private static final int GE = -1;  // Dummy value, change according to actual value.
    private static final int NE = -1;  // Dummy value, change according to actual value.
    private static final int EOL = -1; // Dummy value, change according to actual value.
    private static final int COMMAND = -1; // Dummy value, change according to actual value.
    private static final int QUOTEDSTR = -1; // Dummy value, change according to actual value.
    private static final int UNKNCOM = -1; // Dummy value, change according to actual value.
    private static final int SYNTAX = 0; // Dummy value, change according to actual value.
    private static final int NOEXP = 0; // Dummy value, change according to actual value.
    private static final int DIVBYZERO = 0; // Dummy value, change according to actual value.
    private static final int UNBALPARENS = 0; // Dummy value, change according to actual value.
    private static final int MISSINGQUOTE = 0; // Dummy value, change according to actual value.
    private static final int NUMVARS = 26;

    private String expressionPointer;
    private String token;
    private int tokenType;
    private double[] vars = new double[NUMVARS];
    private int progIdx;
    private char[] prog;
    private int tokType;
    private int kwToken;

    public Parser() {
        expressionPointer = null;
        token = "";
        tokenType = 0;
        Arrays.fill(vars, 0.0);
    }

    public double evaluate(String exp) throws InterpreterException {
        double result = 0.0;
        prog = exp.toCharArray();
        getToken();

        if (token.equals(EOP)) {
            handleErr(NOEXP);  // немає виразу
        }

        // починаємо аналіз виразу
        result = evalExp1();
        putBack();
        return result;
    }

    private double evalExp1() throws InterpreterException {
        double l_temp, r_temp, result;
        char op;

        result = evalExp2();

        if (token.equals(EOP)) { // якщо досягнуто кінець програми
            return result; // виходимо з методу
        }

        op = token.charAt(0);

        if (isRelop(op)) {
            l_temp = result;
            getToken();
            r_temp = evalExp1();
            switch (op) { // виконати операції порівняння
                case '<':
                    result = (l_temp < r_temp) ? 1.0 : 0.0;
                    break;
                case '>':
                    result = (l_temp > r_temp) ? 1.0 : 0.0;
                    break;
                case '=':
                    result = (l_temp == r_temp) ? 1.0 : 0.0;
                    break;
                case (char) NE:
                    result = (l_temp != r_temp) ? 1.0 : 0.0;
                    break;
            }
        }
        return result;
    }

    private double evalExp2() throws InterpreterException {
        char op;
        double result;
        double partialResult;

        result = evalExp3();

        while ((op = token.charAt(0)) == '+' || op == '-') {
            getToken();
            partialResult = evalExp3();
            switch (op) {
                case '-':
                    result -= partialResult;
                    break;
                case '+':
                    result += partialResult;
                    break;
            }
        }
        return result;
    }

    private double evalExp3() throws InterpreterException {
        char op;
        double result;
        double partialResult;

        result = evalExp4();

        while ((op = token.charAt(0)) == '*' || op == '/' || op == '%') {
            getToken();
            partialResult = evalExp4();
            switch (op) {
                case '*':
                    result *= partialResult;
                    break;
                case '/':
                    if (partialResult == 0.0) {
                        handleErr(DIVBYZERO);
                    }
                    result /= partialResult;
                    break;
                case '%':
                    if (partialResult == 0.0) {
                        handleErr(DIVBYZERO);
                    }
                    result %= partialResult;
                    break;
            }
        }
        return result;
    }

    private double evalExp4() throws InterpreterException {
        double result;
        double partialResult;
        double ex;
        int t;

        result = evalExp5();

        if (token.equals("^")) {
            getToken();
            partialResult = evalExp4();
            ex = result;
            if (partialResult == 0.0) {
                result = 1.0;
            } else {
                for (t = (int) partialResult - 1; t > 0; t--) {
                    result *= ex;
                }
            }
        }
        return result;
    }

    private double evalExp5() throws InterpreterException {
        double result;
        String op = "";

        if ((tokType == DELIMITER) && (token.equals("+") || token.equals("-"))) {
            op = token;
            getToken();
        }
        result = evalExp6();
        if (op.equals("-")) {
            result = -result;
        }
        return result;
    }

    private double evalExp6() throws InterpreterException {
        double result;

        if (token.equals("(")) {
            getToken();
            result = evalExp2();
            if (!token.equals(")")) {
                handleErr(UNBALPARENS);
            }
            getToken();
        } else {
            result = atom();
        }
        return result;
    }

    private double atom() throws InterpreterException {
        double result = 0.0;

        switch (tokType) {
            case NUMBER:
                try {
                    result = Double.parseDouble(token);
                } catch (NumberFormatException exc) {
                    handleErr(SYNTAX);
                }
                getToken();
                break;
            case VARIABLE:
                result = findVar(token);
                getToken();
                break;
            default:
                handleErr(SYNTAX);
                break;
        }
        return result;
    }

    private double findVar(String vname) throws InterpreterException {
        if (!Character.isLetter(vname.charAt(0))) {
            handleErr(SYNTAX);
            return 0.0;
        }
        return vars[Character.toUpperCase(vname.charAt(0)) - 'A'];
    }

    private void putBack() {
        if (EOP.equals(token)) {
            return;
        }
        for (int i = 0; i < token.length(); i++) {
            progIdx--;
        }
    }

    private void getToken() throws InterpreterException {
        char ch;
        tokType = NONE;
        token = "";
        kwToken = UNKNCOM;

        if (progIdx == prog.length) { // чи не досягнуто кінець програми?
            token = EOP;
            return;
        }

        while (progIdx < prog.length && isSpaceOrTab(prog[progIdx])) {
            progIdx++;
        }

        if (progIdx == prog.length) {
            token = EOP;
            tokType = DELIMITER;
            return;
        }

        if (prog[progIdx] == '\r') { // обробка символу '\r'
            progIdx += 2;
            kwToken = EOL;
            token = "\r\n";
            return;
        }

        ch = prog[progIdx];
        if (ch == '<' || ch == '>') {
            if (progIdx + 1 == prog.length) {
                handleErr(SYNTAX);
            }

            switch (ch) {
                case '<':
                    if (prog[progIdx + 1] == '>') {
                        progIdx += 2;
                        token = String.valueOf(NE);
                    } else if (prog[progIdx + 1] == '=') {
                        progIdx += 2;
                        token = String.valueOf(LE);
                    } else {
                        progIdx++;
                        token = "<";
                    }
                    break;
                case '>':
                    if (prog[progIdx + 1] == '=') {
                        progIdx += 2;
                        token = String.valueOf(GE);
                    } else {
                        progIdx++;
                        token = ">";
                    }
                    break;
            }
            tokType = DELIMITER;
            return;
        }

        if (isDelim(prog[progIdx])) { // Оператор
            token += prog[progIdx];
            progIdx++;
            tokType = DELIMITER;
        } else if (Character.isLetter(prog[progIdx])) { // ключове слово
            while (!isDelim(prog[progIdx])) {
                token += prog[progIdx];
                progIdx++;
                if (progIdx >= prog.length) {
                    break;
                }
            }

            kwToken = lookUp(token);
            if (kwToken == UNKNCOM) {
                tokType = VARIABLE;
            } else {
                tokType = COMMAND;
            }
        } else if (Character.isDigit(prog[progIdx])) { // число
            while (!isDelim(prog[progIdx])) {
                token += prog[progIdx];
                progIdx++;
                if (progIdx >= prog.length) {
                    break;
                }
            }
            tokType = NUMBER;
        } else if (prog[progIdx] == '"') { // стрічка в лапках
            progIdx++;
            ch = prog[progIdx];
            while (ch != '"' && ch != '\r') {
                token += ch;
                progIdx++;
                ch = prog[progIdx];
            }
            if (ch == '\r') {
                handleErr(MISSINGQUOTE);
            }
            progIdx++;
            tokType = QUOTEDSTR;
        } else { // невідомий символ
            token = EOP;
            return;
        }
    }

    private boolean isDelim(char c) {
        return (" \r,;<>+-/*%^=()".indexOf(c) != -1);
    }

    private boolean isSpaceOrTab(char c) {
        return (c == ' ' || c == '\t');
    }

    private boolean isRelop(char c) {
        return ("<>=".indexOf(c) != -1); // Dummy implementation, adjust as needed.
    }

    private int lookUp(String token) {
        // Dummy implementation, replace with actual lookup logic.
        return UNKNCOM;
    }

    private void handleErr(int error) throws InterpreterException {
        throw new InterpreterException("Error: " + error);
    }
}