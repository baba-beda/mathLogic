import expression.Expression;
import expression.Implication;
import resources.Axioms;
import resources.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Created by daria on 07.01.15.
 */
public class Task2 {
    public static void main(String[] args) {
        new Task2().solve();
    }

    void solve() {
        Scanner in;
        try {
            Parser parser = new Parser();

            in = new Scanner(new File("tsk2.in"));
            String assumption = in.next().replace("->", ">");

            ArrayList<Expression> alpha = parser.parseAlpha(assumption);

            Collections.reverse(alpha);


            // number of current expression in source proof
            int j = 0;


            // hashMap for fast operations, and arrayList for saving natural order;
            // both of them contain current proof, which we should complete to correct
            HashMap<Expression, Integer> sourceProof = new HashMap<Expression, Integer>();
            ArrayList<Expression> sourceProofAux = new ArrayList<Expression>();

            // sourceMP stores all implications divided in two parts (key - alpha, value - beta)
            HashMap<Expression, Expression> sourcesMP = new HashMap<Expression, Expression>();
            // resultMP stores all betas, which are true (so we had alpha in proof)
            HashMap<Expression, Pair> resultMP = new HashMap<Expression, Pair>();

            // proof and proofAux store current completed proof
            HashMap<Expression, Integer> proof = new HashMap<Expression, Integer>();
            ArrayList<Expression> proofAux = new ArrayList<Expression>();

            // arrayList of statements, which prove that current expression is true
            ArrayList<String> basis = new ArrayList<String>();

            Axioms axioms = new Axioms();

            while (in.hasNext()) {
                j++;
                String statement = in.next().replace("->", ">");

                Expression expr = parser.parse(statement);
                sourceProof.put(expr, j);
                sourceProofAux.add(expr);
            }

            // sequentially complete proof with all assumptions
            for (Expression asmp : alpha) {
                int i = 0;

                // basis is interesting only after last iteration (absolutely completes proof)
                basis.clear();
                for (Expression expr : sourceProofAux) {
                    int a = parser.isAxiom(expr);

                    if (a > 0) {
                        proof.put(expr, ++i);
                        proofAux.add(expr);
                        basis.add("axiom " + a);

                        proof.put(axioms.createAxiom1(expr, asmp), ++i);
                        proofAux.add(axioms.createAxiom1(expr, asmp));
                        basis.add("axiom " + 1);

                        proof.put(new Implication(asmp, expr), ++i);
                        proofAux.add(new Implication(asmp, expr));
                        basis.add("M.P. " + (i - 2) + ", " + (i - 1));
                    } else if (alpha.contains(expr)) {

                        // if we are looking at assumption, that is not last assumption in list, we can meet some other assumptions, which weren't inspected yet
                        if (!expr.equals(asmp)) {
                            proof.put(expr, ++i);
                            proofAux.add(expr);
                            basis.add("assumption");
                        }

                        Expression resultFinishExpr = new Implication(asmp, expr);
                        Expression axiom1SmallExpr = axioms.createAxiom1(expr, asmp);
                        Expression axiom1BigExpr = axioms.createAxiom1(expr, resultFinishExpr);
                        Expression resultInterExpr = new Implication(axiom1BigExpr, resultFinishExpr);
                        Expression axiom2Expr = new Implication(axiom1SmallExpr, resultInterExpr);

                        proof.put(axiom1SmallExpr, ++i);
                        proofAux.add(axiom1SmallExpr);
                        basis.add("axiom " + 1);

                        proof.put(axiom2Expr, ++i);
                        proofAux.add(axiom2Expr);
                        basis.add("axiom " + 2);

                        proof.put(resultInterExpr, ++i);
                        proofAux.add(resultInterExpr);
                        basis.add("M.P. " + (i - 2) + ", " + (i - 1));

                        proof.put(axiom1BigExpr, ++i);
                        proofAux.add(axiom1BigExpr);
                        basis.add("axiom " + 1);

                        proof.put(resultFinishExpr, ++i);
                        proofAux.add(resultFinishExpr);
                        basis.add("M.P. " + (i - 1) + ", " + (i - 2));
                    } else if (resultMP.containsKey(expr)) {
                        Expression result = new Implication(asmp, expr);
                        Expression deltaJ = sourceProofAux.get(resultMP.get(expr).first - 1);
                        Expression deltaK = new Implication(deltaJ, expr);
                        Expression auxJ = new Implication(asmp, deltaJ);
                        Expression auxK = new Implication(asmp, deltaK);
                        Expression resultInt = new Implication(auxK, result);
                        Expression auxAxiom2 = new Implication(auxJ, resultInt);

                        proof.put(auxAxiom2, ++i);
                        proofAux.add(auxAxiom2);
                        basis.add("axiom " + 2);

                        proof.put(resultInt, ++i);
                        proofAux.add(resultInt);
                        basis.add("M.P. " + proof.get(auxJ) + ", " + (i - 1));

                        proof.put(result, ++i);
                        proofAux.add(result);
                        basis.add("M.P. " + proof.get(auxK) + ", " + (i - 1));
                    } else if (alpha.contains(expr)) {
                        proof.put(expr, ++i);
                        proofAux.add(expr);
                    }


                    if (sourcesMP.containsKey(expr)) {
                        resultMP.put(sourcesMP.get(expr), new Pair(sourceProof.get(expr), sourceProof.get(new Implication(expr, sourcesMP.get(expr)))));
                    }

                    if (expr instanceof Implication) {
                        sourcesMP.put(((Implication) expr).left, ((Implication) expr).right);
                        if (sourceProof.containsKey(((Implication) expr).left) && sourceProof.get(((Implication) expr).left) < sourceProof.get(expr)) {
                            resultMP.put(((Implication) expr).right, new Pair(sourceProof.get(((Implication) expr).left), sourceProof.get(expr)));
                        }
                    }
                }

                // current proof is new source proof, and we should continue completing it
                sourceProof = new HashMap<Expression, Integer>(proof);
                proof.clear();
                sourceProofAux = new ArrayList<Expression>(proofAux);
                proofAux.clear();
                resultMP.clear();
                sourcesMP.clear();
            }


            for (int k = 0; k < sourceProofAux.size(); k++) {
                System.out.println((k + 1) + ") " + sourceProofAux.get(k).toString() + " " + basis.get(k));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class Pair {
        int first, second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}
