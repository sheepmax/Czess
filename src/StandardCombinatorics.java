import java.util.*;

public class StandardCombinatorics {
    /**
     * Returns a list of all binary strings of length n
     */
    public static List<String> getBinaryStrings(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            variables.add(new Solver.Variable(List.of(0, 1)));
        }

        // There are no constraints

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        return new ArrayList<>();
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} without repetitions
     */
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add variables

        // TODO: add your constraints

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
        return new ArrayList<>();
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} with repetitions
     */
    public static List<int[]> getCombinationsWithRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add your variables

        // TODO: add your constraints

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
        return new ArrayList<>();
    }

    /**
     * Returns a list of all subsets in the set {1,...,n}
     */
    public static List<int[]> getSubsets(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add your variables

        // TODO: add your constraints

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
        return new ArrayList<>();
    }

    /**
     * Returns a list of all permutations in the set {1,...,n}
     */
    public static List<int[]> getSetPermutations(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add your variables

        // TODO: add your constraints

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
        return new ArrayList<>();
    }
}
