import expression.Expression;

import java.io.File;
import java.util.Scanner;

/**
 * Created by daria on 15.01.15.
 */
public class Task3 {
    public static void main(String[] args) {
        new Task3().solve();

    }

    void solve() {
        try {
            Scanner in = new Scanner(new File("test3.in"));

            Parser parser = new Parser();
            Expression statement = parser.parse(in.next());

            int varCount = statement.getVarCount();

            for (int i = 0; i < pow(varCount); i++) {
                if (!statement.evaluate(i)) {
                    String binary = Integer.toBinaryString(i + (2 << 30));
                    System.out.print("Statement is incorrect with ");
                    for (int j = 0; j < varCount; j++) {
                        System.out.print(((char) ('A' + j)) + "=" + (binary.charAt(binary.length() - 1 - j) == '1' ? "T" : "F") + (j != varCount - 1 ? ", " : ""));
                    }
                    break;
                }
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

}
