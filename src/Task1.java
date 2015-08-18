import expression.Expression;
import expression.Implication;
import resources.Axioms;
import resources.Parser;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        new Task1().solve();
    }

    public void solve() {
        Scanner in;
        try {
            Parser parser = new Parser();
            Axioms axioms = new Axioms();

            // here is the name of test-file, but i don't know its exact name
            in = new Scanner(new File("good1.in"));

            // number of current statement
            int i = 0;

            // here we store the whole proof
            // key - expression (to easily get its number and check whether proof contains a statement)
            // value - integer (the number of the list of statements called proof)
            HashMap<Expression, Integer> proof = new HashMap<Expression, Integer>();

            // here we store expression that are in the form alpha -> beta
            // key - expression alpha
            // value - expression beta
            HashMap<Expression, Expression> sourcesMP = new HashMap<Expression, Expression>();

            // here we store beta which was proven with MP
            // key - expression beta
            // value - pair of indices of expression alpha and expression alpha->beta in proof
            HashMap<Expression, Pair> resultMP = new HashMap<Expression, Pair>();

            boolean correct = true;

            while (in.hasNext()) {
                i++;

                String statement = in.next();

                // it's more comfortable to to have all operators of one symbol
                statement = statement.replace("->", ">");

                Expression expr = parser.parse(statement);

                // basis that says why current expression is correct
                String basis = "";

                proof.put(expr, i);


                // checking if current statement is an axiom
                // a - number of axiom
                int a = axioms.isAxiom(expr);

                boolean isAxiom = false;
                boolean isMP = false;

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
                    System.out.println(i + ")" + expr.toString() + " " + "Unproved");
                    correct = false;
                    System.out.print("Proof is incorrect from the statement number " + i);
                    break;
                }

                System.out.println(i + ") " + expr.toString() + " " + basis);


            }
            if (correct) {
                System.out.println("Proof is correct");
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