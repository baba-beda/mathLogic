import expression.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

public class Parser {
    // available operators
    private final String OPERATORS = ">|&!";

    // available words
    private final String WORDS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // temporary stack, that holds operators and brackets
    private Stack<String> stackOperations = new Stack<String>();

    // stack for holding expressions converted to reversed polish notation (holds current parsed expression)
    private Stack<String> stackRPN = new Stack<String>();

    // stack fro holding total parsed expression
    private Stack<String> stackExpression = new Stack<String>();

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

    public int isAxiom(Expression expression) throws ParseException {
        for (int i = 1; i <= 10; i++) {
            if (isAxiom(i, expression)) {
                return i;
            }
        }
        return 0;
    }

    public int isAxiom() throws ParseException {
        for (int i = 1; i <= 10; i++) {
            if (isAxiom(i, expr)) {
                return i;
            }
        }
        return 0;
    }

    private boolean isAxiom(int number, Expression expr) throws ParseException {
        switch(number) {
            case 1:
                return (expr instanceof Implication && ((Implication) expr).right instanceof Implication && ((Implication) expr).left.equals(((Implication) ((Implication) expr).right).right));

            case 2:
                if (!(expr instanceof Implication) || !(((Implication) expr).left instanceof  Implication) || !(((Implication) expr).right instanceof Implication)
                        || !(((Implication) ((Implication) expr).right).left instanceof Implication) || !(((Implication) ((Implication) expr).right).right instanceof  Implication)
                        || !(((Implication) ((Implication) ((Implication) expr).right).left).right instanceof Implication)) {
                    break;
                }
                return (((Implication) ((Implication) expr).left).left.equals(((Implication) ((Implication) ((Implication) expr).right).left).left) && ((Implication) ((Implication) expr).left).right.equals(((Implication) ((Implication) ((Implication) ((Implication) expr).right).left).right).left)
                && ((Implication) ((Implication) expr).left).left.equals(((Implication) ((Implication) ((Implication) expr).right).right).left)
                && ((Implication) ((Implication) ((Implication) ((Implication) expr).right).left).right).right.equals(((Implication) ((Implication) ((Implication) expr).right).right).right));

            case 3:
                return (expr instanceof Implication && ((Implication) expr).left instanceof And && ((Implication) expr).right.equals(((And) ((Implication) expr).left).left));

            case 4:
                return (expr instanceof Implication && ((Implication) expr).left instanceof And && ((Implication) expr).right.equals(((And) ((Implication) expr).left).right));

            case 5:
                return (expr instanceof Implication && ((Implication) expr).right instanceof Implication && ((Implication) ((Implication) expr).right).right instanceof And
                && ((Implication) expr).left.equals(((And) ((Implication) ((Implication) expr).right).right).left) && ((Implication) ((Implication) expr).right).left.equals(((And) ((Implication) ((Implication) expr).right).right).right));

            case 6:
                return (expr instanceof Implication && ((Implication) expr).right instanceof Or && ((Implication) expr).left.equals(((Or) ((Implication) expr).right).left));

            case 7:
                return (expr instanceof Implication && ((Implication) expr).right instanceof Or && ((Implication) expr).left.equals(((Or) ((Implication) expr).right).right));

            case 8:
                if (!(expr instanceof Implication) || !(((Implication) expr).left instanceof Implication) || !(((Implication) expr).right instanceof Implication) || !(((Implication) ((Implication) expr).right).left instanceof Implication)
                        || !(((Implication) ((Implication) expr).right).right instanceof Implication) || !(((Implication) ((Implication) ((Implication) expr).right).right).left instanceof Or)) {
                    break;
                }
                return (((Implication) ((Implication) expr).left).left.equals(((Or) ((Implication) ((Implication) ((Implication) expr).right).right).left).left)
                && ((Implication) ((Implication) ((Implication) expr).right).left).left.equals(((Or) ((Implication) ((Implication) ((Implication) expr).right).right).left).right)
                && ((Implication) ((Implication) expr).left).right.equals(((Implication) ((Implication) ((Implication) expr).right).left).right) && ((Implication) ((Implication) expr).left).right.equals(((Implication) ((Implication) ((Implication) expr).right).right).right));

            case 9:
                if (!(expr instanceof Implication) || !(((Implication) expr).right instanceof  Implication) || !(((Implication) expr).left instanceof Implication) || !(((Implication) ((Implication) expr).right).right instanceof Not)
                        || !(((Implication) ((Implication) expr).right).left instanceof Implication) || !(((Implication) ((Implication) ((Implication) expr).right).left).right instanceof Not)) {
                    break;
                }
                return (((Implication) ((Implication) expr).left).left.equals(((Implication) ((Implication) ((Implication) expr).right).left).left) && ((Implication) ((Implication) expr).left).left.equals(((Not) ((Implication) ((Implication) expr).right).right).subExpr)
                && ((Implication) ((Implication) expr).left).right.equals(((Not) ((Implication) ((Implication) ((Implication) expr).right).left).right).subExpr));

            case 10:
                return (expr instanceof Implication && ((Implication) expr).left instanceof Not && ((Not) ((Implication) expr).left).subExpr instanceof Not && ((Implication) expr).right.equals(((Not) ((Not) ((Implication) expr).left).subExpr).subExpr));
        }
        return false;
    }

    public ArrayList<Expression> parseAlpha(String assumption) throws ParseException {
        ArrayList<Expression> alpha = new ArrayList<Expression>();
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