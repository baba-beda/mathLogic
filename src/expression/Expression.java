package expression;

import com.sun.istack.internal.Pool;

import java.util.Stack;

/**
 * Created by daria on 14.01.15.
 */

public class Expression {
    int varCount;

    public Expression() {
        varCount = 0;
    }

    public Expression parseExpression(Stack<String> stackRPN) {
        String token = stackRPN.pop();
        if (token.equals(">")) {
            return new Implication(stackRPN);
        }
        if (token.equals("|")) {
            return new Or(stackRPN);
        }
        if (token.equals("&")) {
            return new And(stackRPN);
        }
        if (token.equals("!")) {
            return new Not(stackRPN);
        }
        return new Variable(token);
    }

    public int getVarCount() {
        if (this instanceof Implication) {
            varCount = Math.max(Math.max(((Implication) this).left.getVarCount(), ((Implication) this).right.getVarCount()), varCount);
        }
        else if (this instanceof Or) {
            varCount = Math.max(Math.max(((Or) this).left.getVarCount(), ((Or) this).right.getVarCount()), varCount);
        }
        else if (this instanceof And) {
            varCount = Math.max(Math.max(((And) this).left.getVarCount(), ((And) this).right.getVarCount()), varCount);
        }
        else if (this instanceof Not) {
            varCount = Math.max(((Not) this).subExpr.getVarCount(), varCount);
        }
        else {
            varCount = Math.max(((Variable) this).var.charAt(0) - 'A' + 1, varCount);
        }
        return varCount;
    }


    public boolean evaluate(int bitMask) {
        if (this instanceof Implication) {
            return implication(((Implication) this).left.evaluate(bitMask), ((Implication) this).right.evaluate(bitMask));
        }
        if (this instanceof Or) {
            return ((Or) this).left.evaluate(bitMask) || ((Or) this).right.evaluate(bitMask);
        }
        if (this instanceof And) {
            return ((And) this).left.evaluate(bitMask) && ((And) this).right.evaluate(bitMask);
        }
        if (this instanceof Not) {
            return !((Not) this).subExpr.evaluate(bitMask);
        }
        String binary = Integer.toBinaryString(bitMask + (2 << 30));
        return (binary.charAt(binary.length() - (((Variable) this).var.charAt(0) - 'A') - 1) == '1');
    }

    boolean implication(boolean left, boolean right) {
        return (!left || right);
    }



    @Override
    public boolean equals(Object other) {
        if (this instanceof Implication && other instanceof Implication) {
            return (((Implication) this).left.equals(((Implication) other).left) && ((Implication) this).right.equals(((Implication) other).right));
        }
        if (this instanceof Or && other instanceof Or) {
            return (((Or) this).left.equals(((Or) other).left) && ((Or) this).right.equals(((Or) other).right));
        }
        if (this instanceof And && other instanceof And) {
            return (((And) this).left.equals(((And) other).left) && ((And) this).right.equals(((And) other).right));
        }
        if (this instanceof Not && other instanceof Not) {
            return (((Not) this).subExpr.equals(((Not) other).subExpr));
        }
        return (this instanceof Variable && other instanceof Variable && ((Variable) this).var.equals(((Variable) other).var));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (this instanceof Implication) {
            hash = (hash + ">".hashCode() + ((Implication) this).left.hashCode() + ((Implication) this).right.hashCode()) % 1000000007;
        }
        else if (this instanceof Or) {
            hash = (hash + "|".hashCode() + ((Or) this).left.hashCode() + ((Or) this).right.hashCode()) % 1000000007;
        }
        else if (this instanceof And) {
            hash = (hash + "&".hashCode() + ((And) this).left.hashCode() + ((And) this).right.hashCode()) % 1000000007;
        }
        else if (this instanceof Not) {
            hash = (hash + "!".hashCode() + ((Not) this).subExpr.hashCode()) % 1000000007;
        }
        else {
            hash = (hash + ((Variable) this).var.hashCode()) % 1000000007;
        }
        return hash;
    }

    @Override
    public String toString() {
        String statement = "";
        if (this instanceof Implication) {
            return statement = "(" + ((Implication) this).left.toString() + "->" + ((Implication) this).right.toString() + ")";
        }
        else if (this instanceof Or) {
            return statement = "(" + ((Or) this).left.toString() + "|" + ((Or) this).right.toString() + ")";
        }
        else if (this instanceof And) {
            return statement = "(" + ((And) this).left.toString() + "&" + ((And) this).right.toString() + ")";
        }
        else if (this instanceof Not) {
            return statement = "(" + "!" + ((Not) this).subExpr.toString() + ")";
        }
        return ((Variable) this).var;
    }


}
