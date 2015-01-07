import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


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
            String alpha = parser.parseAlpha(assumption), beta = parser.parseBeta(assumption);
            alpha = parser.reParse(alpha);

            System.out.print(alpha + "\n" + beta + "\n");

            String statement;

            ArrayList<String> proof = new ArrayList<String>();
            ArrayList<String> reasons = new ArrayList<String>();

            ArrayList<String> sourceProof = new ArrayList<String>();

            // arraylist that holds statements, which were found to be result of MP
            ArrayList<String> resultMP = new ArrayList<String>();
            // arraylist that holds number of statement alpha->beta and number of statement alpha
            ArrayList<Pair> resultMPSources = new ArrayList<Pair>();

            Stack<String> auxStack;
            int i = 0;

            while (in.hasNext()) {
                statement = in.next();
                statement = statement.replace("->", ">");

                statement = parser.reParse(statement);

                sourceProof.add(statement);
                int a = parser.isAxiom();

                auxStack = parser.compileStack(true);
                if (auxStack !=  null && sourceProof.contains(auxStack.firstElement())) {
                    resultMP.add(auxStack.lastElement());
                    resultMPSources.add(new Pair(sourceProof.indexOf(auxStack.firstElement()), sourceProof.indexOf(statement)));
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

                if (a != 0) {
                    i += 3;
                    reasons.add("axiom " + a);
                    proof.add(statement);
                    reasons.add("axiom 1");
                    proof.add(parser.reParse(statement + ">" + alpha + ">" + statement));
                    reasons.add("MP " + (i - 2) + ", " + (i - 1));
                    proof.add(parser.reParse(alpha + ">" + statement));
                }

                else if (alpha.indexOf(statement) > 0) {
                    i += 5;
                    String ax1 = "(" + statement + ">(" + statement + ">" + statement + "))";
                    String ax1_1 = "(" + statement + ">((" + statement + ">" + statement + ")>" + statement + "))";
                    String res = "(" + statement + ">" + statement + ")";
                    reasons.add("axiom 1");
                    proof.add(ax1);
                    reasons.add("axiom 2");
                    proof.add(parser.reParse(ax1 + ">" + ax1_1 + ">" + res));
                    reasons.add("MP " + (i - 4) + ", " + (i - 3));
                    proof.add(parser.reParse(ax1_1 + ">" + res));
                    reasons.add("axiom 1");
                    proof.add(ax1_1);
                    reasons.add("MP " + (i - 1) + ", " + (i - 2));
                    proof.add(res);
                }

                else if (resultMP.contains(statement)) {
                    i += 3;
                    reasons.add("axiom 2");
                    proof.add("(" + alpha + ">" + sourceProof.get(resultMPSources.get(resultMP.indexOf(statement)).first) + ")>(("
                            + alpha + ">" + sourceProof.get((resultMPSources.get(resultMP.indexOf(statement)).second)) + ")>("
                    + alpha + ">" + statement + "))");

                    String st1 = parser.reParse(alpha + ">" + sourceProof.get(resultMPSources.get(resultMP.indexOf(statement)).first));
                    int index1 = proof.indexOf(st1);
                    String st2 = parser.reParse(alpha + ">" + sourceProof.get((resultMPSources.get(resultMP.indexOf(statement)).second)));
                    int index2 = proof.indexOf(st2);

                    reasons.add("MP " + (index1 + 1) + ", " + (i - 2));

                    proof.add("((" + alpha + ">" + sourceProof.get((resultMPSources.get(resultMP.indexOf(statement)).second)) + ")>("
                            + alpha + ">" + statement + "))");
                    reasons.add("MP " + (index2 + 1) + ", " + (i - 1));
                    proof.add("(" + alpha + ">" + statement + ")");
                }
            }

            for (int j = 0; j < proof.size(); j++) {
                System.out.println((j + 1) + ": " + reasons.get(j) + " " + proof.get(j));
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
