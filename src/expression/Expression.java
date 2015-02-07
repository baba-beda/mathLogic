package expression;

import com.sun.istack.internal.Pool;
import resources.Proofs;

import javax.swing.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by daria on 14.01.15.
 */

public class Expression {
    private int varCount;

    private HashSet<String> variables = new HashSet<String>();
    private HashMap<String, Integer> variablesAux = new HashMap<String, Integer>();
    


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
        variables = this.getVariables();
        int i = 0;
        for (String s : variables) {
            variablesAux.put(s, i);
            i++;
        }
        return variables.size();
    }

    private HashSet<String> getVariables() {
        if (this instanceof Implication) {
            variables.addAll(((Implication) this).left.getVariables());
            variables.addAll(((Implication) this).right.getVariables());
        }
        else if (this instanceof Or) {
            variables.addAll(((Or) this).left.getVariables());
            variables.addAll(((Or) this).right.getVariables());
        }
        else if (this instanceof And) {
            variables.addAll(((And) this).left.getVariables());
            variables.addAll(((And) this).right.getVariables());
        }
        else if (this instanceof Not) {
            variables.addAll(((Not) this).subExpr.getVariables());
        }
        else {
            variables.add(((Variable) this).var);
        }
        return variables;
    }

    public boolean evaluate(int bitMask) {
        if (this instanceof Implication) {
            ((Implication) this).left.setVariablesAux(variablesAux);
            ((Implication) this).right.setVariablesAux(variablesAux);
            return implication(((Implication) this).left.evaluate(bitMask), ((Implication) this).right.evaluate(bitMask));
        }
        if (this instanceof Or) {
            ((Or) this).left.setVariablesAux(variablesAux);
            ((Or) this).right.setVariablesAux(variablesAux);
            return ((Or) this).left.evaluate(bitMask) || ((Or) this).right.evaluate(bitMask);
        }
        if (this instanceof And) {
            ((And) this).left.setVariablesAux(variablesAux);
            ((And) this).right.setVariablesAux(variablesAux);
            return ((And) this).left.evaluate(bitMask) && ((And) this).right.evaluate(bitMask);
        }
        if (this instanceof Not) {
            ((Not) this).subExpr.setVariablesAux(variablesAux);
            return !((Not) this).subExpr.evaluate(bitMask);
        }
        String binary = Integer.toBinaryString(bitMask + (2 << 30));

        return (binary.charAt(binary.length() - variablesAux.get(((Variable) this).var) - 1) == '1');
    }

    boolean implication(boolean left, boolean right) {
        return (!left || right);
    }

    public void setVariablesAux(HashMap<String, Integer> variablesAux) {
        this.variablesAux = variablesAux;
    }

    public HashMap<String, Integer> getVariablesAux() {
        return variablesAux;
    }

    @Override
    public String toString() {
        if (this instanceof Implication) {
            return "(" + ((Implication) this).left.toString() + "->" + ((Implication) this).right.toString() + ")";
        }
        else if (this instanceof Or) {
            return "(" + ((Or) this).left.toString() + "|" + ((Or) this).right.toString() + ")";
        }
        else if (this instanceof And) {
            return "(" + ((And) this).left.toString() + "&" + ((And) this).right.toString() + ")";
        }
        else if (this instanceof Not) {
            return "(" + "!" + ((Not) this).subExpr.toString() + ")";
        }
        return ((Variable) this).var;
    }

    public ArrayList<Expression> prove(int bitMask, Proofs proofs) {
        ArrayList<Expression> proof = new ArrayList<Expression>();

        if (this instanceof Implication) {
            if (((Implication) this).left.evaluate(bitMask) && ((Implication) this).right.evaluate(bitMask)) {
                ArrayList<Expression> yesYesImpl = new ArrayList<Expression>();
                yesYesImpl.addAll(proofs.getYesYesImpl());
                proofs.changeVariablesInList(yesYesImpl, ((Implication) this).left, ((Implication) this).right);
                proof.addAll(((Implication) this).left.prove(bitMask, proofs));
                proof.addAll(((Implication) this).left.prove(bitMask, proofs));
                proof.addAll(yesYesImpl);
                return proof;
            }
            if (!((Implication) this).left.evaluate(bitMask) && ((Implication) this).right.evaluate(bitMask)) {
                ArrayList<Expression> noYesImpl = new ArrayList<Expression>(proofs.getNoYesImpl());
                proofs.changeVariablesInList(noYesImpl, ((Implication) this).left, ((Implication) this).right);
                proof.addAll(new Not(((Implication) this).left).prove(bitMask, proofs));
                proof.addAll(((Implication) this).left.prove(bitMask, proofs));
                proof.addAll(noYesImpl);
                return proof;
            }
            if (!((Implication) this).left.evaluate(bitMask) && !((Implication) this).right.evaluate(bitMask)) {
                ArrayList<Expression> noNoImpl = new ArrayList<Expression>(proofs.getNoNoImpl());
                proofs.changeVariablesInList(noNoImpl, ((Implication) this).left, ((Implication) this).right);
                proof.addAll(new Not(((Implication) this).left).prove(bitMask, proofs));
                proof.addAll(new Not(((Implication) this).left).prove(bitMask, proofs));
                proof.addAll(noNoImpl);
                return proof;
            }
        }

        if (this instanceof And) {
            ArrayList<Expression> yesYesAnd = new ArrayList<Expression>(proofs.getYesYesAnd());
            proofs.changeVariablesInList(yesYesAnd, ((And) this).left, ((And) this).right);
            proof.addAll(((And) this).left.prove(bitMask, proofs));
            proof.addAll(((And) this).right.prove(bitMask, proofs));
            proof.addAll(yesYesAnd);
            return proof;
        }

        if (this instanceof Or) {
            if (((Or) this).left.evaluate(bitMask) && ((Or) this).right.evaluate(bitMask)) {
                ArrayList<Expression> yesYesOr = new ArrayList<Expression>(proofs.getYesYesOr());
                proofs.changeVariablesInList(yesYesOr, ((Or) this).left, ((Or) this).right);
                proof.addAll(((Or) this).left.prove(bitMask, proofs));
                proof.addAll(((Or) this).right.prove(bitMask, proofs));
                proof.addAll(yesYesOr);
                return proof;
            }
            if (!((Or) this).left.evaluate(bitMask) && ((Or) this).right.evaluate(bitMask)) {
                ArrayList<Expression> noYesOr = new ArrayList<Expression>(proofs.getNoYesOr());
                proofs.changeVariablesInList(noYesOr, ((Or) this).left, ((Or) this).right);
                proof.addAll(new Not(((Or) this).left).prove(bitMask, proofs));
                proof.addAll(((Or) this).right.prove(bitMask, proofs));
                proof.addAll(noYesOr);
                return proof;
            }
            if (((Or) this).left.evaluate(bitMask) && !((Or) this).right.evaluate(bitMask)) {
                ArrayList<Expression> yesNoOr = new ArrayList<Expression>(proofs.getYesNoOr());
                proofs.changeVariablesInList(yesNoOr, ((Or) this).left, ((Or) this).right);
                proof.addAll(((Or) this).left.prove(bitMask, proofs));
                proof.addAll(new Not(((Or) this).right).prove(bitMask, proofs));
                proof.addAll(yesNoOr);
                return proof;
            }
        }

        if (this instanceof Not) {
            if (((Not) this).subExpr instanceof Implication) {
                ArrayList<Expression> yesNoNotImpl = new ArrayList<Expression>(proofs.getYesNoNotImpl());
                proofs.changeVariablesInList(yesNoNotImpl, ((Implication) this).left, ((Implication) this).right);
                proof.addAll(((Implication) this).left.prove(bitMask, proofs));
                proof.addAll(new Not(((Implication) this).right).prove(bitMask, proofs));
                proof.addAll(yesNoNotImpl);
            }
            if (((Not) this).subExpr instanceof And) {
                if (!((And) this).left.evaluate(bitMask) && !((And) this).right.evaluate(bitMask)) {
                    ArrayList<Expression> noNoNotAnd = new ArrayList<Expression>(proofs.getNoNoNotAnd());
                    proofs.changeVariablesInList(noNoNotAnd, ((And) this).left, ((And) this).right);
                    proof.addAll(new Not(((And) this).left).prove(bitMask, proofs));
                    proof.addAll(new Not(((And) this).right).prove(bitMask, proofs));
                    proof.addAll(noNoNotAnd);
                    return proof;
                }
                if (!((And) this).left.evaluate(bitMask) && ((And) this).right.evaluate(bitMask)) {
                    ArrayList<Expression> noYesNotAnd = new ArrayList<Expression>(proofs.getNoYesNotAnd());
                    proofs.changeVariablesInList(noYesNotAnd, ((And) this).left, ((And) this).right);
                    proof.addAll(new Not(((And) this).left).prove(bitMask, proofs));
                    proof.addAll(((And) this).right.prove(bitMask, proofs));
                    proof.addAll(noYesNotAnd);
                    return proof;
                }
                if (((And) this).left.evaluate(bitMask) && !((And) this).right.evaluate(bitMask)) {
                    ArrayList<Expression> yesNoNotAnd = new ArrayList<Expression>(proofs.getYesNoNotAnd());
                    proofs.changeVariablesInList(yesNoNotAnd, ((And) this).left, ((And) this).right);

                    proof.addAll(((And) this).left.prove(bitMask, proofs));
                    proof.addAll(new Not(((And) this).right).prove(bitMask, proofs));
                    proof.addAll(yesNoNotAnd);
                    return proof;
                }
            }
            if (((Not) this).subExpr instanceof Or) {
                ArrayList<Expression> noNoNotOr = new ArrayList<Expression>(proofs.getNoNoNotOr());
                proofs.changeVariablesInList(noNoNotOr, ((Or) this).left, ((Or) this).right);
                proof.addAll(new Not(((Or) this).left).prove(bitMask, proofs));
                proof.addAll(new Not(((Or) this).right).prove(bitMask, proofs));
                proof.addAll(noNoNotOr);
            }
            if (((Not) this).subExpr instanceof Not) {
                Expression a = ((Not) ((Not) this).subExpr).subExpr;
                ArrayList<Expression> addDoubleNot = new ArrayList<Expression>(proofs.getAddDoubleNot());
                proofs.changeVariablesInList(addDoubleNot, a);
                proof.addAll(a.prove(bitMask, proofs));
                proof.addAll(addDoubleNot);
            }
        }
        return proof;
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


}
