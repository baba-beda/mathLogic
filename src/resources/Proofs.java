package resources;

import expression.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    private ArrayList<Expression> selfImpl;

    public void parseProofs() {
        try {
            Parser parser = new Parser();
            Scanner in = new Scanner(new File("proofs/NoNoImpl.txt"));
            noNoImpl = new ArrayList<>();
            while (in.hasNext()) {
                noNoImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoYesImpl.txt"));
            noYesImpl = new ArrayList<>();
            while (in.hasNext()) {
                noYesImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesNoNotImpl.txt"));
            yesNoNotImpl = new ArrayList<>();
            while (in.hasNext()) {
                yesNoNotImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesYesImpl.txt"));
            yesYesImpl = new ArrayList<>();
            while (in.hasNext()) {
                yesYesImpl.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoNoNotAnd.txt"));
            noNoNotAnd = new ArrayList<>();
            while (in.hasNext()) {
                noNoNotAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoYesNotAnd.txt"));
            noYesNotAnd = new ArrayList<>();
            while (in.hasNext()) {
                noYesNotAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesNoNotAnd.txt"));
            yesNoNotAnd = new ArrayList<>();
            while (in.hasNext()) {
                yesNoNotAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesYesAnd.txt"));
            yesYesAnd = new ArrayList<>();
            while (in.hasNext()) {
                yesYesAnd.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoNoNotOr.txt"));
            noNoNotOr = new ArrayList<>();
            while (in.hasNext()) {
                noNoNotOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/NoYesOr.txt"));
            noYesOr = new ArrayList<>();
            while (in.hasNext()) {
                noYesOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesNoOr.txt"));
            yesNoOr = new ArrayList<>();
            while (in.hasNext()) {
                yesNoOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/YesYesOr.txt"));
            yesYesOr = new ArrayList<>();
            while (in.hasNext()) {
                yesYesOr.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/AddDoubleNot.txt"));
            addDoubleNot = new ArrayList<>();
            while (in.hasNext()) {
                addDoubleNot.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/Contraposition.txt"));
            contraposition = new ArrayList<>();
            while (in.hasNext()) {
                contraposition.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/Exclusion.txt"));
            exclusion = new ArrayList<>();
            while (in.hasNext()) {
                exclusion.add(parser.parse(in.next()));
            }

            in = new Scanner(new File("proofs/SelfImpl.in"));
            selfImpl = new ArrayList<>();
            while (in.hasNext()) {
                selfImpl.add(parser.parse(in.next()));
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
                } else {
                    ((Implication) expression).left = b.clone();
                }
            } else {
                ((Implication) expression).left = changeVariables(((Implication) expression).left, a, b);
            }

            if (((Implication) expression).right instanceof Variable) {
                if (((Variable) ((Implication) expression).right).var.equals("A")) {
                    ((Implication) expression).right = a.clone();
                } else {
                    ((Implication) expression).right = b.clone();
                }
            } else {
                ((Implication) expression).right = changeVariables(((Implication) expression).right, a, b);
            }
        } else if (expression instanceof Or) {
            if (((Or) expression).left instanceof Variable) {
                if (((Variable) ((Or) expression).left).var.equals("A")) {
                    ((Or) expression).left = a.clone();
                } else {
                    ((Or) expression).left = b.clone();
                }
            } else {
                ((Or) expression).left = changeVariables(((Or) expression).left, a, b);
            }

            if (((Or) expression).right instanceof Variable) {
                if (((Variable) ((Or) expression).right).var.equals("A")) {
                    ((Or) expression).right = a.clone();
                } else {
                    ((Or) expression).right = b.clone();
                }
            } else {
                ((Or) expression).right = changeVariables(((Or) expression).right, a, b);
            }
        } else if (expression instanceof And) {
            if (((And) expression).left instanceof Variable) {
                if (((Variable) ((And) expression).left).var.equals("A")) {
                    ((And) expression).left = a.clone();
                } else {
                    ((And) expression).left = b.clone();
                }
            } else {
                ((And) expression).left = changeVariables(((And) expression).left, a, b);
            }

            if (((And) expression).right instanceof Variable) {
                if (((Variable) ((And) expression).right).var.equals("A")) {
                    ((And) expression).right = a.clone();
                } else {
                    ((And) expression).right = b.clone();
                }
            } else {
                ((And) expression).right = changeVariables(((And) expression).right, a, b);
            }
        } else if (expression instanceof Not) {
            if (((Not) expression).subExpr instanceof Variable) {
                if (((Variable) ((Not) expression).subExpr).var.equals("A")) {
                    ((Not) expression).subExpr = a.clone();
                } else {
                    ((Not) expression).subExpr = b.clone();
                }
            } else {
                ((Not) expression).subExpr = changeVariables(((Not) expression).subExpr, a, b);
            }
        } else {
            if (((Variable) expression).var.equals("A")) {
                return a;
            } else {
                return b;
            }
        }
        return expression;
    }

    private Expression changeVariables(Expression expression, Expression a) {

        if (expression instanceof Implication) {
            if (((Implication) expression).left instanceof Variable) {
                ((Implication) expression).left = a;
            } else {
                changeVariables(((Implication) expression).left, a);
            }

            if (((Implication) expression).right instanceof Variable) {
                ((Implication) expression).right = a;
            } else {
                changeVariables(((Implication) expression).right, a);
            }
        } else if (expression instanceof Or) {
            if (((Or) expression).left instanceof Variable) {
                ((Or) expression).left = a;
            } else {
                changeVariables(((Or) expression).left, a);
            }

            if (((Or) expression).right instanceof Variable) {
                ((Or) expression).right = a;
            } else {
                changeVariables(((Or) expression).right, a);
            }
        } else if (expression instanceof And) {
            if (((And) expression).left instanceof Variable) {
                ((And) expression).left = a;
            } else {
                changeVariables(((And) expression).left, a);
            }

            if (((And) expression).right instanceof Variable) {
                ((And) expression).right = a;
            } else {
                changeVariables(((And) expression).right, a);
            }
        } else if (expression instanceof Not) {
            if (((Not) expression).subExpr instanceof Variable) {
                ((Not) expression).subExpr = a;
            } else {
                changeVariables(((Not) expression).subExpr, a);
            }
        } else {
            return a;
        }
        return expression;
    }

    public void changeVariablesInList(ArrayList<Expression> list, Expression a, Expression b) {
        ArrayList<Expression> aux = list.stream().
                map(e -> changeVariables(e, a, b)).
                collect(Collectors.toCollection(ArrayList::new));
        list.clear();
        list.addAll(aux.stream().
                map(Expression::clone).
                collect(Collectors.toList()));
    }

    public void changeVariablesInList(ArrayList<Expression> list, Expression a) {
        ArrayList<Expression> aux = list.stream().
                map(e -> changeVariables(e, a)).
                collect(Collectors.toCollection(ArrayList::new));
        list.clear();
        list.addAll(aux.stream().
                map(Expression::clone).
                collect(Collectors.toList()));
    }
}
