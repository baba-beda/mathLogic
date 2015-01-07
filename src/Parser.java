import java.text.ParseException;
import java.util.Stack;
import java.util.StringTokenizer;

public class Parser {
    // available operators
    private final String OPERATORS = ">|&!";

    // available words
    private final String WORDS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // temporary stack, that holds operators and brackets
    private Stack<String> stackOperations = new Stack<String>();

    // stack for holding expressions converted to reversed polish notation (holds current parsed expression)
    private Stack<String> stackRPN = new Stack<String>();

    // stack fro holding total parsed expression
    private Stack<String> stackExpression = new Stack<String>();


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

    public void parse(String expression) throws ParseException {
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
                stackRPN.push(token);
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
    }

    public int isAxiom() throws ParseException {
        stackExpression.clear();
        stackExpression.addAll(stackRPN);
        for (int i = 1; i <= 10; i++) {
            if (isAxiom(i)) {
                return i;
            }
        }
        return 0;
    }

    private boolean isAxiom(int number) throws ParseException {
        Stack<String> newStack = new Stack<String>();
        Stack<String> auxStack = new Stack<String>();
        String a, b, c, q;
        switch(number) {
            case 1:
                for (int i = 0; i < stackExpression.size() - 2; i++) {
                    String token = stackExpression.get(i);
                    compileStack(newStack, token);
                }
                try {
                    return (newStack.size() == 3 && newStack.get(0).equals(newStack.get(2))
                            && stackExpression.lastElement().equals(stackExpression.get(stackExpression.size() - 2)) && stackExpression.lastElement().equals(">"));
                } catch (Exception e) {
                    break;
                }

            case 2:
                for (int i = 0; i < stackExpression.size() - 2; i++) {
                    String token = stackExpression.get(i);
                    compileStack(auxStack, token);
                }
                if (auxStack.size() != 3 || !stackExpression.lastElement().equals(">") || !stackExpression.get(stackExpression.size() - 2).equals(">")) {
                    break;
                }
                
                parse(auxStack.get(0));
                for (int i = 0; i < stackRPN.size() - 1; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                if (newStack.size() != 2 || !stackRPN.lastElement().equals(">")) {
                    break;
                }

                parse(auxStack.get(1));
                for (int i = 0; i < stackRPN.size() - 2; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                if (newStack.size() != 5 || !stackRPN.lastElement().equals(">") || !stackRPN.get(stackRPN.size() - 1).equals(">")
                        || !newStack.get(0).equals(newStack.get(2)) || !newStack.get(1).equals(newStack.get(3))) {
                    break;
                }

                a = newStack.get(0);
                b = newStack.get(1);
                c = newStack.get(4);

                parse(auxStack.get(2));
                for (int i = 0; i < stackRPN.size() - 1; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                if (newStack.size() != 7 || !stackRPN.lastElement().equals(">")
                        || !newStack.get(5).equals(a) || !newStack.get(6).equals(c)) {
                    break;
                }

                return true;
                

            case 3:
                for (int i = 0; i < stackExpression.size() - 1; i++) {
                    String token = stackExpression.get(i);
                    compileStack(newStack, token);
                }
                try {
                return (newStack.size() == 2 && stackExpression.lastElement().equals(">")
                        && newStack.firstElement().substring(1, newStack.lastElement().length() + 1).equals(newStack.lastElement()));
                } catch (Exception e) {
                    break;
                }


            case 4:
                for (int i = 0; i < stackExpression.size() - 1; i++) {
                    String token = stackExpression.get(i);
                    compileStack(newStack, token);
                }
                try {
                    return (newStack.size() == 2 && stackExpression.lastElement().equals(">")
                            && newStack.firstElement().substring(newStack.firstElement().length() - newStack.lastElement().length() - 1, newStack.firstElement().length() - 1).equals(newStack.lastElement()));
                } catch (Exception e) {
                    break;
                }

            case 5:
                for (int i = 0; i < stackExpression.size() - 3; i++) {
                    String token = stackExpression.get(i);
                    compileStack(newStack, token);
                }
                try {
                    return (newStack.size() == 4 && stackExpression.lastElement().equals(">") && stackExpression.get(stackExpression.size() - 2).equals(">")
                            && stackExpression.get(stackExpression.size() - 3).equals("&") && newStack.get(0).equals(newStack.get(2))
                            && newStack.get(1).equals(newStack.get(3)));
                } catch (Exception e) {
                    break;
                }

            case 6:
                for (int i = 0; i < stackExpression.size() - 2; i++) {
                    String token = stackExpression.get(i);
                    compileStack(newStack, token);
                }
                try {
                    return (newStack.size() == 3 && stackExpression.get(stackExpression.size() - 2).equals("|") && stackExpression.lastElement().equals(">")
                            && newStack.firstElement().equals(newStack.get(1)));
                } catch (Exception e) {
                    break;
                }

            case 7:
                for (int i = 0; i < stackExpression.size() - 2; i++) {
                    String token = stackExpression.get(i);
                    compileStack(newStack, token);
                }
                try {
                    return (newStack.size() == 3 && stackExpression.get(stackExpression.size() - 2).equals("|") && stackExpression.lastElement().equals(">")
                            && newStack.firstElement().equals(newStack.lastElement()));
                } catch (Exception e) {
                    break;
                }

            case 8:
                for (int i = 0; i < stackExpression.size() - 2; i++) {
                    String token = stackExpression.get(i);
                    compileStack(auxStack, token);
                }
                if (auxStack.size() != 3 || !stackExpression.lastElement().equals(">") || !stackExpression.get(stackExpression.size() - 2).equals(">")) {
                    break;
                }

                parse(auxStack.get(0));
                for (int i = 0; i < stackRPN.size() - 1; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                if (newStack.size() != 2 || !stackRPN.lastElement().equals(">")) {
                    break;
                }

                a = newStack.firstElement();
                q = newStack.lastElement();

                parse(auxStack.get(1));
                for (int i = 0; i < stackRPN.size() - 1; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                if (newStack.size() != 4 || !stackRPN.lastElement().equals(">") || !newStack.lastElement().equals(q)) {
                    break;
                }

                b = newStack.get(2);

                parse(auxStack.get(2));
                for (int i = 0; i < stackRPN.size() - 1; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                try {
                    if (newStack.size() != 6 || !stackRPN.lastElement().equals(">") || !newStack.lastElement().equals(q)
                            || !newStack.get(4).substring(1, a.length() + 1).equals(a) || !newStack.get(4).substring(a.length() + 2, newStack.get(4).length() - 1).equals(b)) {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
                return true;

            case 9:
                for (int i = 0; i < stackExpression.size() - 2; i++) {
                    String token = stackExpression.get(i);
                    compileStack(auxStack, token);
                }
                if (auxStack.size() != 3 || !stackExpression.get(stackExpression.size() - 2).equals(">") || !stackExpression.lastElement().equals(">")) {
                    break;
                }

                parse(auxStack.get(0));
                for (int i = 0; i < stackRPN.size() - 1; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                if (newStack.size() != 2 || !stackRPN.lastElement().equals(">")) {
                    break;
                }

                a = newStack.firstElement();
                b = newStack.lastElement();

                parse(auxStack.get(1));
                for (int i = 0; i < stackRPN.size() - 1; i++) {
                    String token = stackRPN.get(i);
                    compileStack(newStack, token);
                }
                try {
                    if (newStack.size() != 4 || !newStack.get(2).equals(a) || !newStack.get(3).substring(2, b.length() + 2).equals(b)) {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }

                a = "!" + "(" + a + ")";
                parse(auxStack.get(2));
                for (String token : stackRPN) {
                    compileStack(newStack, token);
                }
                if (newStack.size() != 5 || !newStack.lastElement().equals(a)) {
                    break;
                }
                return true;

            case 10:
                for (int i = 0; i < stackExpression.size() - 1; i++) {
                    String token = stackExpression.get(i);
                    compileStack(newStack, token);
                }
                try {
                    return (newStack.size() == 2 && stackExpression.lastElement().equals(">")
                            && newStack.firstElement().substring(4, newStack.firstElement().length() - 2).equals(newStack.lastElement()));
                } catch (Exception e) {
                    break;
                }
        }
        return false;
    }

    private void compileStack(Stack<String> stack, String token) {
        if (isWord(token)) {
            stack.push(token);
        }
        if (isOperator(token)) {
            if (isBinary(token)) {
                String beta = stack.pop(), alpha = stack.pop();
                stack.push("(" + alpha + token + beta + ")");
            }
            else {
                stack.push(token + "(" + stack.pop() + ")");
            }
        }
    }

    public Stack<String> compileStack(boolean flag) {
        if (flag) {
            if (!stackExpression.lastElement().equals(">")) {
                return null;
            }
            Stack<String> result = new Stack<String>();
            for (int i = 0; i < stackExpression.size() - 1; i++) {
                String token = stackExpression.get(i);
                compileStack(result, token);
            }
            return result;
        }
        else {
            if (!stackRPN.lastElement().equals(">")) {
                return null;
            }
            Stack<String> result = new Stack<String>();
            for (int i = 0; i < stackRPN.size() - 1; i++) {
                String token = stackRPN.get(i);
                compileStack(result, token);
            }
            return result;
        }
    }

    public String parseAlpha(String assumption) {
        String alpha = "(";
        int position = assumption.indexOf("|-");
        for (int i = 0; i < position; i++) {
            char token = assumption.charAt(i);
            if (token == ',') {
                alpha += ")|(";
            }
            else {
                alpha += token;
            }
        }
        return alpha + ")";
    }

    public String parseBeta(String assumption) {
        int position = assumption.indexOf("|-");
        return assumption.substring(position + 2);
    }

    public String reParse(String statement) throws ParseException{
        parse(statement);
        Stack<String> auxStack = new Stack<String>();
        for (String token : stackRPN) {
            compileStack(auxStack, token);
        }
        return auxStack.firstElement();
    }
}