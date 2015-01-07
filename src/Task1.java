import java.io.*;
import java.util.ArrayList;
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

            // arraylist that holds all statements of the proof
            ArrayList<String> proof = new ArrayList<String>();
            // stack that holds expression divided into alpha and beta, for checking it on MP
            Stack<String> auxStack = new Stack<String>();
            // arraylist that holds statements, which were found to be result of MP
            ArrayList<String> resultMP = new ArrayList<String>();
            // arraylist that holds number of statement alpha->beta and number of statement alpha
            ArrayList<Pair> resultMPSources = new ArrayList<Pair>();
            // number of current statement
            int i = 0;

            boolean correct = true;

            while (in.hasNext()) {
                i++;
                if (auxStack != null) {
                    auxStack.clear();
                }
                String statement = in.next();
                // it's more comfortable to to have all operators of one symbol
                statement = statement.replace("->", ">");
                proof.add(statement);
                parser.parse(statement);
                // checking if current statement is an axiom
                int a = parser.isAxiom();

                boolean isAxiom = false;
                boolean isMP = false;

                if (a != 0) {
                    isAxiom = true;
                }

                // maybe we can get statement (beta), which is result of another statement (alpha) and this (alpha -> beta)
                auxStack = parser.compileStack(true);
                if (auxStack !=  null && proof.contains(auxStack.firstElement())) {
                    resultMP.add(auxStack.lastElement());
                    resultMPSources.add(new Pair(proof.indexOf(auxStack.firstElement()) + 1, proof.indexOf(statement) + 1));
                }
            
                if (resultMP.contains(statement)) {
                    isMP = true;
                }
                
                // that means that nothing prove current statement and it's incorrect
                if (!isAxiom && !isMP) {
                    System.out.print("Incorrect proof from line " + i);
                    correct = false;
                    break;
                }
            
                // maybe we have in proof statement (alpha->beta), so current statement is alpha, and we can get beta 
                for (int j = 0; j < proof.size() - 1; j++) {
                    parser.parse(proof.get(j));
                    auxStack = parser.compileStack(false);
                    if (auxStack != null && auxStack.firstElement().equals(statement)) {
                        resultMP.add(auxStack.lastElement());
                        resultMPSources.add(new Pair(proof.indexOf(statement) + 1, j + 1));
                    }
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