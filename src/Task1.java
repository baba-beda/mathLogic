import expression.Expression;
import expression.Implication;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Task1 {
    public static void main(String[] args) {
        new Task1().solve();
    }

    public void solve() {
        Scanner in;
        try {
            Parser parser = new Parser();

            // here is the name of test-file, but i don't now its exact name
            in = new Scanner(new File("test.in"));

            // number of current statement
            int i = 0;

            HashMap<Expression, Integer> proof = new HashMap<Expression, Integer>();

            HashMap<Expression, Expression> sourcesMP = new HashMap<Expression, Expression>();

            HashMap<Expression, Pair> resultMP = new HashMap<Expression, Pair>();

            boolean correct = true;
            Expression auxExpr = parser.parse("(B->(B->B))");

            while (in.hasNext()) {
                i++;

                String statement = in.next();
                // it's more comfortable to to have all operators of one symbol
                statement = statement.replace("->", ">");

                Expression expr = parser.parse(statement);

                proof.put(expr, i);


                // checking if current statement is an axiom
                int a = parser.isAxiom();

                boolean isAxiom = false;
                boolean isMP = false;

                if (a != 0) {
                    isAxiom = true;
                }

                if (resultMP.containsKey(expr)) {
                    isMP = true;
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
                    System.out.print("Proof is incorrect beginning the statement number " + i);
                    break;
                }
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