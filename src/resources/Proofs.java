package resources;

import expression.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by daria on 07.02.15.
 */
public class Proofs {
    private ArrayList<Expression> noNoImpl;
    private ArrayList<Expression> noYesImpl;
    private ArrayList<Expression> yesNoNotImpl;
    private ArrayList<Expression> yesYesImpl;

    private ArrayList<Expression> noNoNotAnd;
    private ArrayList<Expression> noYesNotAnd;
    private ArrayList<Expression> yesNoNotAnd;
    private ArrayList<Expression> yesYesAnd;

    private ArrayList<Expression> noNoNotOr;
    private ArrayList<Expression> noYesOr;
    private ArrayList<Expression> yesNoOr;
    private ArrayList<Expression> yesYesOr;

    private ArrayList<Expression> addDoubleNot;

    private ArrayList<Expression> contraposition;

    private ArrayList<Expression> exclusion;

    public void parseProofs() {
        try {
            Parser parser = new Parser();
            Scanner in = new Scanner(new File("proofs/NoNoImpl.txt"));
            noNoImpl = new ArrayList<Expression>();
            while (in.hasNext()) {
                noNoImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoYesImpl.txt"));
            noYesImpl = new ArrayList<Expression>();
            while (in.hasNext()) {
                noYesImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesNoNotImpl.txt"));
            yesNoNotImpl = new ArrayList<Expression>();
            while (in.hasNext()) {
                yesNoNotImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesYesImpl.txt"));
            yesYesImpl = new ArrayList<Expression>();
            while (in.hasNext()) {
                yesYesImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoNoNotAnd.txt"));
            noNoNotAnd = new ArrayList<Expression>();
            while (in.hasNext()) {
                noNoNotAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoYesNotAnd.txt"));
            noYesNotAnd = new ArrayList<Expression>();
            while (in.hasNext()) {
                noYesNotAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesNoNotAnd.txt"));
            yesNoNotAnd = new ArrayList<Expression>();
            while (in.hasNext()) {
                yesNoNotAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesYesAnd.txt"));
            yesYesAnd = new ArrayList<Expression>();
            while (in.hasNext()) {
                yesYesAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoNoNotOr.txt"));
            noNoNotOr = new ArrayList<Expression>();
            while (in.hasNext()) {
                noNoNotOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoYesOr.txt"));
            noYesOr = new ArrayList<Expression>();
            while (in.hasNext()) {
                noYesOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesNoOr.txt"));
            yesNoOr = new ArrayList<Expression>();
            while (in.hasNext()) {
                yesNoOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesYesOr.txt"));
            yesYesOr = new ArrayList<Expression>();
            while (in.hasNext()) {
                yesYesOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/AddDoubleNot.txt"));
            addDoubleNot = new ArrayList<Expression>();
            while (in.hasNext()) {
                addDoubleNot.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/Contraposition.txt"));
            contraposition = new ArrayList<Expression>();
            while (in.hasNext()) {
                contraposition.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/Exclusion.txt"));
            exclusion = new ArrayList<Expression>();
            while (in.hasNext()) {
                exclusion.add(parser.parse(in.next()));
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public ArrayList<Expression> getNoNoImpl() {
        return noNoImpl;
    }

    public ArrayList<Expression> getNoYesImpl() {
        return noYesImpl;
    }

    public ArrayList<Expression> getYesNoNotImpl() {
        return yesNoNotImpl;
    }

    public ArrayList<Expression> getYesYesImpl() {
        return yesYesImpl;
    }

    public ArrayList<Expression> getNoNoNotAnd() {
        return noNoNotAnd;
    }

    public ArrayList<Expression> getNoYesNotAnd() {
        return noYesNotAnd;
    }

    public ArrayList<Expression> getYesNoNotAnd() {
        return yesNoNotAnd;
    }

    public ArrayList<Expression> getYesYesAnd() {
        return yesYesAnd;
    }

    public ArrayList<Expression> getNoNoNotOr() {
        return noNoNotOr;
    }

    public ArrayList<Expression> getNoYesOr() {
        return noYesOr;
    }

    public ArrayList<Expression> getYesNoOr() {
        return yesNoOr;
    }

    public ArrayList<Expression> getYesYesOr() {
        return yesYesOr;
    }

    public ArrayList<Expression> getAddDoubleNot() {
        return addDoubleNot;
    }

    public ArrayList<Expression> getContraposition() {
        return contraposition;
    }

    public ArrayList<Expression> getExclusion() {
        return exclusion;
    }

    private Expression changeVariables(Expression expression, Expression a, Expression b) {
        if (expression instanceof Implication) {
            if (((Implication) expression).left instanceof Variable) {
                if (((Variable) ((Implication) expression).left).var.equals("A")) {
                    ((Implication) expression).left = a.clone();
                }
                else {
                    ((Implication) expression).left = b.clone();
                }
            }
            else {
                ((Implication) expression).left = changeVariables(((Implication) expression).left, a, b);
            }

            if (((Implication) expression).right instanceof Variable) {
                if (((Variable) ((Implication) expression).right).var.equals("A")) {
                    ((Implication) expression).right = a.clone();
                }
                else {
                    ((Implication) expression).right = b.clone();
                }
            }
            else {
                ((Implication) expression).right = changeVariables(((Implication) expression).right, a, b);
            }
        }
        else if (expression instanceof Or) {
            if (((Or) expression).left instanceof Variable) {
                if (((Variable) ((Or) expression).left).var.equals("A")) {
                    ((Or) expression).left = a.clone();
                }
                else {
                    ((Or) expression).left = b.clone();
                }
            }
            else {
                ((Or) expression).left = changeVariables(((Or) expression).left, a, b);
            }

            if (((Or) expression).right instanceof Variable) {
                if (((Variable) ((Or) expression).right).var.equals("A")) {
                    ((Or) expression).right = a.clone();
                }
                else {
                    ((Or) expression).right = b.clone();
                }
            }
            else {
                ((Or) expression).right = changeVariables(((Or) expression).right, a, b);
            }
        }
        else if (expression instanceof And) {
            if (((And) expression).left instanceof Variable) {
                if (((Variable) ((And) expression).left).var.equals("A")) {
                    ((And) expression).left = a.clone();
                }
                else {
                    ((And) expression).left = b.clone();
                }
            }
            else {
                ((And) expression).left = changeVariables(((And) expression).left, a, b);
            }

            if (((And) expression).right instanceof Variable) {
                if (((Variable) ((And) expression).right).var.equals("A")) {
                    ((And) expression).right = a.clone();
                }
                else {
                    ((And) expression).right = b.clone();
                }
            }
            else {
                ((And) expression).right = changeVariables(((And) expression).right, a, b);
            }
        }
        else if (expression instanceof Not) {
            if (((Not) expression).subExpr instanceof Variable) {
                if (((Variable) ((Not) expression).subExpr).var.equals("A")) {
                    ((Not) expression).subExpr = a.clone();
                }
                else {
                    ((Not) expression).subExpr = b.clone();
                }
            }
            else {
                ((Not) expression).subExpr = changeVariables(((Not) expression).subExpr, a, b);
            }
        }
        else {
            if (((Variable) expression).var.equals("A")) {
                return a;
            }
            else {
                return b;
            }
        }
        return expression;
    }

    private Expression changeVariables(Expression expression, Expression a) {

        if (expression instanceof Implication) {
            if (((Implication) expression).left instanceof Variable) {
                ((Implication) expression).left = a;
            }
            else {
                changeVariables(((Implication) expression).left, a);
            }

            if (((Implication) expression).right instanceof Variable) {
                ((Implication) expression).right = a;
            }
            else {
                changeVariables(((Implication) expression).right, a);
            }
        }
        else if (expression instanceof Or) {
            if (((Implication) expression).left instanceof Variable) {
                ((Implication) expression).left = a;
            }
            else {
                changeVariables(((Implication) expression).left, a);
            }

            if (((Implication) expression).right instanceof Variable) {
                ((Implication) expression).right = a;
            }
            else {
                changeVariables(((Implication) expression).right, a);
            }
        }
        else if (expression instanceof And) {
            if (((Implication) expression).left instanceof Variable) {
                ((Implication) expression).left = a;
            }
            else {
                changeVariables(((Implication) expression).left, a);
            }

            if (((Implication) expression).right instanceof Variable) {
                ((Implication) expression).right = a;
            }
            else {
                changeVariables(((Implication) expression).right, a);
            }
        }
        else if (expression instanceof Not) {
            if (((Not) expression).subExpr instanceof Variable) {
                ((Not) expression).subExpr = a;
            }
            else {
                changeVariables(((Not) expression).subExpr, a);
            }
        }
        else {
            return a;
        }
        return expression;
    }

    public void changeVariablesInList(ArrayList<Expression> list, Expression a, Expression b) {
        ArrayList<Expression> aux = new ArrayList<Expression>();
        for (Expression e : list) {
            aux.add(changeVariables(e, a, b));
        }
        list.clear();
        Iterator<Expression> ite = aux.iterator();
        while (ite.hasNext()) {
            list.add(ite.next().clone());
        }
    }

    public void changeVariablesInList(ArrayList<Expression> list, Expression a) {
        ArrayList<Expression> aux = new ArrayList<Expression>();
        for (Expression e : list) {
            aux.add(changeVariables(e, a));
        }
        list.clear();
        Iterator<Expression> ite = aux.iterator();
        while (ite.hasNext()) {
            list.add(ite.next().clone());
        }
    }
}
