import expression.Expression;
import expression.Implication;
import expression.Not;
import expression.Or;
import resources.Axioms;
import resources.Parser;
import resources.Proofs;

import java.io.File;
import java.text.ParseException;
import java.util.*;

/**
 * Created by daria on 15.01.15.
 */
public class Task3 {
    public static void main(String[] args) {
        new Task3().solve();
    }

    void solve() {
        try {
            Parser parser = new Parser();
            Proofs proofs = new Proofs();
            proofs.parseProofs();

            Scanner in = new Scanner(new File("test3.in"));

            Expression statement = parser.parse(in.next());

            int varCount = statement.getVarCount();

            HashMap<String, Integer> variables = statement.getVariablesAux();

            ArrayList<String> variablesAux = new ArrayList<String>();

            for (Map.Entry<String, Integer> entry : variables.entrySet()) {
                variablesAux.add(entry.getKey());
            }

            int counter = pow(varCount);



            for (int i = 0; i < counter; i++) {
                if (!statement.evaluate(i)) {
                    String binary = Integer.toBinaryString(i + (2 << 30));
                    System.out.print("Statement is incorrect with ");
                    for (Map.Entry<String, Integer> entry : variables.entrySet()) {
                        System.out.print(entry.getKey() + "=" + (binary.charAt(binary.length() - 1 - entry.getValue()) == '1' ? "T" : "F") + (entry.getValue() != varCount - 1 ? ", " : ""));
                    }
                    break;
                }
            }

            ArrayList<Expression>[] interProofs = new ArrayList[counter];

            for (int bitMask = 0; bitMask < counter; bitMask++) {
                interProofs[bitMask] = statement.prove(bitMask, proofs);
            }

            for (int i = 1; i < counter; i *= 2) {
                for (int j = 0; j < counter - 1; j += varCount) {
                    ArrayList<Expression> first = new ArrayList<Expression>(interProofs[j]);
                    interProofs[j].clear();
                    interProofs[j] = new ArrayList<Expression>(mergeProofs(statement, first, interProofs[j + i], variablesAux, i, proofs));
                }
            }

            for (Expression e : interProofs[0]) {
                System.out.println(e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int pow(int n) {
        int ans = 1;
        for (int i = 0; i < n; i++) {
            ans *= 2;
        }
        return ans;
    }

    public ArrayList<Expression> mergeProofs(Expression statement, ArrayList<Expression> first, ArrayList<Expression> second, ArrayList<String> variables, int d, Proofs proofs) throws ParseException{
        Parser parser = new Parser();
        ArrayList<Expression> result = new ArrayList<Expression>();
        String binary = Integer.toBinaryString(d + (2 << 30)).substring(1);
        int var = binary.length() - binary.indexOf("1") - 1;
        String variable = variables.get(var);
        Expression asmp = parser.parse(variable);

        HashMap<Expression, Integer> firstAux = new HashMap<Expression, Integer>();
        for (int i = 0; i < first.size(); i++) {
            firstAux.put(first.get(i), i + 1);
        }
        HashMap<Expression, Integer> secondAux = new HashMap<Expression, Integer>();
        for (int i = 0; i < second.size(); i++) {
            secondAux.put(second.get(i), i + 1);
        }

        result.addAll(deduction(new Not(asmp), firstAux, first));
        result.addAll(deduction(asmp, secondAux, second));

        Axioms axioms = new Axioms();

        Expression excludedThirdResult = new Or(asmp, new Not(asmp));
        Expression firstAuxSt = new Implication(asmp, excludedThirdResult);

        result.add(firstAuxSt);
        ArrayList<Expression> contraposition = proofs.getContraposition();
        proofs.changeVariablesInList(contraposition, asmp, excludedThirdResult);
        result.addAll(contraposition);

        Expression firstSt = new Implication(new Not(excludedThirdResult), new Not(asmp));

        result.add(firstSt);

        Expression secondAuxSt = new Implication(new Not(asmp), excludedThirdResult);
        result.add(secondAuxSt);
        contraposition.clear();
        Collections.copy(contraposition, proofs.getContraposition());
        proofs.changeVariablesInList(contraposition, new Not(asmp), excludedThirdResult);
        result.addAll(contraposition);

        Expression secondSt = new Implication(new Not(excludedThirdResult), new Not(new Not(asmp)));

        result.add(secondSt);

        result.add(axioms.createAxiom9(new Not(excludedThirdResult), new Not(asmp)));
        result.add(((Implication) result.get(result.size() - 1)).right);
        result.add(((Implication) result.get(result.size() - 1)).right);
        result.add(axioms.createAxiom10(excludedThirdResult));
        result.add(excludedThirdResult);

        ArrayList<Expression> exclusion = proofs.getExclusion();
        proofs.changeVariablesInList(exclusion, statement, asmp);

        result.addAll(exclusion);

        return result;
    }

    public ArrayList<Expression> deduction(Expression asmp, HashMap<Expression, Integer> sourceProof, ArrayList<Expression> sourceProofAux) {
        Parser parser = new Parser();

        // sourceMP stores all implications divided in two parts (key - alpha, value - beta)
        HashMap<Expression, Expression> sourcesMP = new HashMap<Expression, Expression>();
        // resultMP stores all betas, which are true (so we had alpha in proof)
        HashMap<Expression, Pair> resultMP = new HashMap<Expression, Pair>();

        // proof and proofAux store current completed proof
        HashMap<Expression, Integer> proof = new HashMap<Expression, Integer>();
        ArrayList<Expression> proofAux = new ArrayList<Expression>();


        Axioms axioms = new Axioms();

        int i = 0;
        try {
            for (Expression expr : sourceProofAux) {
                int a = parser.isAxiom(expr);

                if (a > 0) {
                    proof.put(expr, ++i);
                    proofAux.add(expr);

                    proof.put(axioms.createAxiom1(expr, asmp), ++i);
                    proofAux.add(axioms.createAxiom1(expr, asmp));

                    proof.put(new Implication(asmp, expr), ++i);
                    proofAux.add(new Implication(asmp, expr));
                } else if (asmp.equals(expr)) {
                    // if we are looking at assumption, that is not last assumption in list, we can meet some other assumptions, which weren't inspected yet
                    if (!expr.equals(asmp)) {
                        proof.put(expr, ++i);
                        proofAux.add(expr);
                    }

                    Expression resultFinishExpr = new Implication(asmp, expr);
                    Expression axiom1SmallExpr = axioms.createAxiom1(expr, asmp);
                    Expression axiom1BigExpr = axioms.createAxiom1(expr, resultFinishExpr);
                    Expression resultInterExpr = new Implication(axiom1BigExpr, resultFinishExpr);
                    Expression axiom2Expr = new Implication(axiom1SmallExpr, resultInterExpr);

                    proof.put(axiom1SmallExpr, ++i);
                    proofAux.add(axiom1SmallExpr);

                    proof.put(axiom2Expr, ++i);
                    proofAux.add(axiom2Expr);

                    proof.put(resultInterExpr, ++i);
                    proofAux.add(resultInterExpr);

                    proof.put(axiom1BigExpr, ++i);
                    proofAux.add(axiom1BigExpr);

                    proof.put(resultFinishExpr, ++i);
                    proofAux.add(resultFinishExpr);
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

                    proof.put(resultInt, ++i);
                    proofAux.add(resultInt);

                    proof.put(result, ++i);
                    proofAux.add(result);
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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return proofAux;
    }

    public class Pair{
        int first, second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

}
