package resources;

import expression.*;

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
}
