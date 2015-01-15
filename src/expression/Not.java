package expression;

import java.util.Stack;

/**
 * Created by daria on 14.01.15.
 */
public class Not extends Expression {
    public Expression subExpr;

    Not(Stack<String> stackRPN) {
        subExpr = parseExpression(stackRPN);
    }

}
