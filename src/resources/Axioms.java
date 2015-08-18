package resources;

import expression.*;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by daria on 06.02.15.
 */
public class Axioms {
    public Expression createAxiom1(Expression alpha, Expression beta) {
        return new Implication(alpha, new Implication(beta, alpha));
    }

    public Expression createAxiom2(Expression alpha, Expression beta, Expression gamma) {
        return new Implication(new Implication(alpha, beta), new Implication(new Implication(alpha, new Implication(beta, gamma)), new Implication(alpha, gamma)));
    }

    public Expression createAxiom3(Expression alpha, Expression beta) {
        return new Implication(new And(alpha, beta), alpha);
    }

    public Expression createAxiom4(Expression alpha, Expression beta) {
        return new Implication(new And(alpha, beta), beta);
    }

    public Expression createAxiom5(Expression alpha, Expression beta) {
        return new Implication(alpha, new Implication(beta, new And(alpha, beta)));
    }

    public Expression createAxiom6(Expression alpha, Expression beta) {
        return new Implication(alpha, new Or(alpha, beta));
    }

    public Expression createAxiom7(Expression alpha, Expression beta) {
        return new Implication(beta, new Or(alpha, beta));
    }

    public Expression createAxiom8(Expression alpha, Expression beta, Expression gamma) {
        return new Implication(new Implication(alpha, gamma), new Implication(new Implication(beta, gamma), new Implication(new Or(alpha, beta), gamma)));
    }

    public Expression createAxiom9(Expression alpha, Expression beta) {
        return new Implication(new Implication(alpha, beta), new Implication(new Implication(alpha, new Not(beta)), new Not(alpha)));
    }

    public Expression createAxiom10(Expression alpha) {
        return new Implication(new Not(new Not(alpha)), alpha);
    }

    public int isAxiom(Expression expression) throws ParseException {
        for (int i = 1; i <= 10; i++) {
            if (isAxiom(i, expression)) {
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

}
