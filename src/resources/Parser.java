package resources;

import expression.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;

public class Parser {
    // available operators
    private final String OPERATORS = ">|&!";

    // available words
    private final String WORDS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // temporary stack, that holds operators and brackets
    private Stack<String> stackOperations = new Stack<>();

    // stack for holding expressions converted to reversed polish notation (holds current parsed expression)
    private Stack<String> stackRPN = new Stack<>();

    // expression that represents expression-tree of current statement
    Expression expr;


    private boolean isWord(String token) {
        return WORDS.contains(token);
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private byte getPrecedence(String token) {
        return (byte) OPERATORS.indexOf(token);
    }

    private boolean isLeftAssoc(String token) {
        return (token.equals("&") || token.equals("|"));
    }

    private boolean isBinary(String token) {
        return (!token.equals("!"));
    }

    public Expression parse(String expression) throws ParseException {
        // cleaning stacks
        stackOperations.clear();
        stackRPN.clear();

        StringTokenizer stringTokenizer = new StringTokenizer(expression, OPERATORS + "()" + WORDS, true);

        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (isOpenBracket(token)) {
                stackOperations.push(token);
            }
            else if (isCloseBracket(token)) {
                while (!stackOperations.isEmpty() && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.pop();
            }
            else if (isWord(token)) {
                if (WORDS.indexOf(token) >= WORDS.indexOf("0")) {
                    stackRPN.push(stackRPN.pop() + token);
                }
                else {
                    stackRPN.push(token);
                }
            }
            else if (isOperator(token)) {
                while (!stackOperations.isEmpty() && isOperator(stackOperations.lastElement()) &&
                        ((isLeftAssoc(token) && getPrecedence(token) <= getPrecedence(stackOperations.lastElement())) ||
                                (!isLeftAssoc(token) && getPrecedence(token) < getPrecedence(stackOperations.lastElement())))) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.push(token);
            }
        }
        while(!stackOperations.isEmpty()) {
            stackRPN.push(stackOperations.pop());
        }
        expr = (new Expression()).parseExpression(stackRPN);
        return expr;
    }

    public ArrayList<Expression> parseAlpha(String assumption) throws ParseException {
        ArrayList<Expression> alpha = new ArrayList<>();
        String curAss = "";
        int position = assumption.indexOf("|-");
        for (int i = 0; i < position; i++) {
            char token = assumption.charAt(i);
            if (token == ',') {
                alpha.add(parse(curAss));
                curAss = "";
            }
            else {
                curAss += token;
            }
        }
        alpha.add(parse(curAss));
        return alpha;
    }
}