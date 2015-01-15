package expression;

import java.util.Stack;

/**
 * Created by daria on 14.01.15.
 */
public class Implication extends Expression {
    public Expression left, right;

    Implication(Stack<String> stackRPN) {
        right = parseExpression(stackRPN);
        left = parseExpression(stackRPN);
    }

    public Implication(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

}
