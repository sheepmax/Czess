import java.util.*;

public class StandardCombinatorics {
    /**
     * Returns a list of all binary strings of length n
     */
    public static List<String> getBinaryStrings(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        List<Integer> domain = List.of(0, 1);
        Solver.Variable.setDomain(domain);

        for (int i = 0; i < n; i++) {
            variables.add(new Solver.Variable());
        }

        // There are no constraints

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> results = solver.findAllSolutions();

        List<String> strings = new ArrayList<>();

        for (int[] result: results) {
            StringBuilder builder = new StringBuilder(result.length);
            for (int i: result) {
                builder.append(i);
            }
            strings.add(builder.toString());
        }

        return strings;
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} without repetitions
     */
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        ArrayList<Integer> domain = new ArrayList<Integer>();
        for(int i = 1; i <= n; i++){
            domain.add(i);
        }

        Solver.Variable.setDomain(domain);

        for(int i = 0; i < k; i++){
            variables.add(new Solver.Variable());
        }

        // TODO: add your constraints
        constraints.add(new Solver.TotalOrdering(variables, false));

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
//        System.out.print("[");
//        for (int i = 161670; i < result.size(); i++) {
//            System.out.print("[");
//            for (int j = 0; j < result.get(i).length; j++) {
//                System.out.print(result.get(i)[j]);
//                System.out.print(", ");
//           }
//            System.out.println("]");
//        }

        return result;
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} with repetitions
     */

    /**
     * (x0 <= x1) <= x2 <= x3 ...
     * (x0, x1) (1, -1)  1 => x0 - x1 < 1
     * x0 {0, 1} 0 - x1 < 1 x1 {0, 1}
     * x0 {1}  - x1 < 0     x1 {1
     */

    public static List<int[]> getCombinationsWithRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add your variables
        ArrayList<Integer> domain = new ArrayList<Integer>();
        for(int i = 1; i <= n; i++){
            domain.add(i);
        }
        Solver.Variable.setDomain(domain);

        for(int i = 0; i < k; i++){
            variables.add(new Solver.Variable());
        }

        // TODO: add your constraints
        constraints.add(new Solver.TotalOrdering(variables, true));

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
        return result;
    }


    /**
     * Returns a list of all subsets in the set {1,...,n}
     */
        public static List<int[]> getSubsets(int n) {
            List<int[]> total = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                List<int[]> partial = getCombinationsWithoutRepetition(n, i);
                total.addAll(partial);
            }
            return total;
        }

    /**
     * Returns a list of all permutations in the set {1,...,n}
     */
    public static List<int[]> getSetPermutations(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add your variables
        ArrayList<Integer> domain = new ArrayList<Integer>();
        for(int i = 1; i <= n; i++){
            domain.add(i);
        }

        Solver.Variable.setDomain(domain);

        for(int i = 0; i < n; i++){
            variables.add(new Solver.Variable());
        }

//        for (Solver.Variable var1: variables) {
//            for (Solver.Variable var2: variables) {
//                if (var1 == var2) { continue; }
//                constraints.add(new Solver.BinaryNotEqual(var1, var2));
//            }
//        }
        constraints.add(new Solver.NotEqual(variables));

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
        return result;
    }
}
