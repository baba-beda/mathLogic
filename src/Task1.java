import expression.Expression;
import expression.Implication;
import resources.Axioms;
import resources.Parser;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        new Task1().solve();
    }

    public void solve() {
        try (Scanner in = new Scanner(new File("proofs" + File.separator + "SelfImpl.txt"))) {
            Parser parser = new Parser();

            // here we store the whole proof
            // key - expression (to easily get its number and check whether proof contains a statement)
            // value - integer (the number of the list of statements called proof)
            HashMap<Expression, Integer> proof = new HashMap<>();
            ArrayList<Expression> proofAux = new ArrayList<>();

            int i = 1;

            while (in.hasNext()) {
                String statement = in.next();

                // it's more comfortable to to have all operators of one symbol
                statement = statement.replace("->", ">");

                Expression expr = parser.parse(statement);
                proof.put(expr, i);
                proofAux.add(expr);

                i++;
            }

            giveBasis(proof, proofAux).forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> giveBasis(HashMap<Expression, Integer> proof, ArrayList<Expression> proofAux) throws ParseException {
        // here we store expression that are in the form alpha -> beta
        // key - expression alpha
        // value - expression beta
        HashMap<Expression, Expression> sourcesMP = new HashMap<>();

        // here we store beta which was proven with MP
        // key - expression beta
        // value - pair of indices of expression alpha and expression alpha->beta in proof
        HashMap<Expression, Pair> resultMP = new HashMap<>();

        ArrayList<String> proofWithBasis = new ArrayList<>();
        Axioms axioms = new Axioms();

        int i = 1;
        boolean correct = true;
        for (Expression expr : proofAux) {
            // checking if current statement is an axiom
            // a - number of axiom
            int a = axioms.isAxiom(expr);

            boolean isAxiom = false;
            boolean isMP = false;

            String basis = "";
            if (a != 0) {
                isAxiom = true;
                basis = "axiom " + a;
            }


            if (resultMP.containsKey(expr)) {
                isMP = true;
                basis = "M.P. " + resultMP.get(expr).first + ", " + resultMP.get(expr).second;
            }

            if (sourcesMP.containsKey(expr)) {
                resultMP.put(sourcesMP.get(expr), new Pair(i, proof.get(new Implication(expr, sourcesMP.get(expr)))));
            }

            if (expr instanceof Implication) {
                sourcesMP.put(((Implication) expr).left, ((Implication) expr).right);
                if (proof.containsKey(((Implication) expr).left)) {
                    resultMP.put(((Implication) expr).right, new Pair(proof.get(((Implication) expr).left), i));
                }
            }


            if (!isAxiom && !isMP) {
                correct = false;
                proofWithBasis.add(i + ") " + expr.toString() + " " + "Unproved");
                proofWithBasis.add("Proof is incorrect from the statement number " + i);
                break;
            }

            proofWithBasis.add(i + ") " + expr.toString() + " " + basis);
            i++;
        }

        if (correct) {
            proofWithBasis.add("Proof is correct");
        }
        return proofWithBasis;
    }

    class Pair {
        int first, second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}