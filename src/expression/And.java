package expression;

import java.util.Stack;

/**
 * Created by daria on 14.01.15.
 */
public class And extends Expression {
    public Expression left, right;

    And(Stack<String> stackRPN) {
        right = parseExpression(stackRPN);
        left = parseExpression(stackRPN);
    }

    public And(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}
