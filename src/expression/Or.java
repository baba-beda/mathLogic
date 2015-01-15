package expression;

import java.util.Stack;

/**
 * Created by daria on 14.01.15.
 */
public class Or extends Expression {
    public Expression left, right;

    Or(Stack<String> stackRPN) {
        right = parseExpression(stackRPN);
        left = parseExpression(stackRPN);
    }
}
