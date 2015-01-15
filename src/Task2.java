import expression.Expression;
import expression.Implication;

import java.io.File;
import java.util.*;


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
            String alphaStr = parser.parseAlpha(assumption), betaStr = parser.parseBeta(assumption);

            Expression alpha = parser.parse(alphaStr), beta = parser.parse(betaStr);

            System.out.print(alphaStr + "\n" + betaStr + "\n");

            String statement;

            Stack<String> auxStack;

            // number of current expression in full proof
            int i = 0;

            //number of current expression in source proof
            int j = 0;

            HashMap<Expression, Integer> sourceProof = new HashMap<Expression, Integer>();
            HashMap<Expression, Expression> sourcesMP = new HashMap<Expression, Expression>();
            HashMap<Expression, Pair> resultMP = new HashMap<Expression, Pair>();


            ArrayList<Expression> sourceProofAux = new ArrayList<Expression>();

            HashMap<Expression, Integer> proof = new HashMap<Expression, Integer>();
            ArrayList<String> resultProof = new ArrayList<String>();
            ArrayList<String> basis = new ArrayList<String>();



            while (in.hasNext()) {
                j++;
                statement = in.next();
                statement = statement.replace("->", ">");

                Expression expr = parser.parse(statement);
                sourceProof.put(expr, j);
                sourceProofAux.add(expr);

                int a = parser.isAxiom();

                if (a > 0) {
                    proof.put(expr, ++i);
                    resultProof.add(expr.toString());
                    basis.add("axiom " + a);

                    proof.put(new Implication(expr, new Implication(alpha, expr)), ++i);
                    resultProof.add((new Implication(expr, new Implication(alpha, expr))).toString());
                    basis.add("axiom " + 1);

                    proof.put(new Implication(alpha, expr), ++i);
                    resultProof.add((new Implication(alpha, expr)).toString());
                    basis.add("MP " + (i - 2) + ", " + (i - 1));
                }

                else if (alphaStr.contains(statement)) {
                    Expression resultFinishExpr = new Implication(alpha, expr);
                    Expression axiom1SmallExpr = new Implication(expr, resultFinishExpr);
                    Expression axiom1BigExpr = new Implication(expr, new Implication(resultFinishExpr, expr));
                    Expression resultInterExpr = new Implication(axiom1BigExpr, resultFinishExpr);
                    Expression axiom2Expr = new Implication(axiom1SmallExpr, resultInterExpr);

                    proof.put(axiom1SmallExpr, ++i);
                    resultProof.add(axiom1SmallExpr.toString());
                    basis.add("axiom " + 1);

                    proof.put(axiom2Expr, ++i);
                    resultProof.add(axiom2Expr.toString());
                    basis.add("axiom " + 2);

                    proof.put(resultInterExpr, ++i);
                    resultProof.add(resultInterExpr.toString());
                    basis.add("MP " + (i - 2) + ", " + (i - 1));

                    proof.put(axiom1BigExpr, ++i);
                    resultProof.add(axiom1BigExpr.toString());
                    basis.add("axiom " + 1);

                    proof.put(resultFinishExpr, ++i);
                    resultProof.add(resultFinishExpr.toString());
                    basis.add("MP " + (i - 1) + ", " + (i - 2));
                }

                else if (resultMP.containsKey(expr)) {
                    Expression result = new Implication(alpha, expr);
                    Expression deltaJ = sourceProofAux.get(resultMP.get(expr).first - 1);
                    Expression deltaK = new Implication(deltaJ, expr);
                    Expression auxJ = new Implication(alpha, deltaJ);
                    Expression auxK = new Implication(alpha, deltaK);
                    Expression resultInt = new Implication(auxK, result);
                    Expression auxAxiom2 = new Implication(auxJ, resultInt);

                    proof.put(auxAxiom2, ++i);
                    resultProof.add(auxAxiom2.toString());
                    basis.add("axiom " + 2);

                    proof.put(resultInt, ++i);
                    resultProof.add(resultInt.toString());
                    basis.add("MP " + proof.get(auxJ) + ", " + (i - 1));

                    proof.put(result, ++i);
                    resultProof.add(result.toString());
                    basis.add("MP " + proof.get(auxK) + ", " + (i - 1));
                }

                if (sourcesMP.containsKey(expr)) {
                    resultMP.put(sourcesMP.get(expr), new Pair(j, sourceProof.get(new Implication(expr, sourcesMP.get(expr)))));
                }

                if (expr instanceof Implication) {
                    sourcesMP.put(((Implication) expr).left, ((Implication) expr).right);
                    if (sourceProof.containsKey(((Implication) expr).left)) {
                        resultMP.put(((Implication) expr).right, new Pair(sourceProof.get(((Implication) expr).left), j));
                    }
                }
            }


            for (int k = 0; k < resultProof.size(); k++) {
                System.out.println((k + 1) + ") " + basis.get(k) + " " + resultProof.get(k));
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
