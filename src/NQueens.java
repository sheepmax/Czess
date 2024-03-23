import java.util.*;

public class NQueens {
    public static int getNQueenHelper(int n, BitSet domainFirst) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        List<Integer> domain = new ArrayList<>();

        for (int i = 0; i < n; i++) { domain.add(i); }
        Solver.Variable.setDomain(domain);

        for (int i = 0; i < n; i++) { variables.add(new Solver.Variable()); }

        variables.get(0).bitDomain = domainFirst;

        constraints.add(new Solver.NotEqual(variables));

        for (int i = 0; i < (n - 1); i++) {
            for (int j = i + 1; j < n; j++) {
                constraints.add(new Solver.DifferenceInequality(
                        variables.get(i),
                        variables.get(j),
                        j - i
                ));
            }
        }

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        return result.size();
    }
    /**
     * Returns the number of N-Queen solutions
     */
    public static int getNQueenSolutions(int n) {
        int total = 0;

        BitSet domainFirst = new BitSet(n);
        domainFirst.set(0, n / 2);

        total += getNQueenHelper(n, domainFirst) * 2;

        if ((n % 2) == 1) {
            domainFirst.clear();
            domainFirst.set(n / 2);
            total += getNQueenHelper(n, domainFirst);
        }

        return total;
    }
}
