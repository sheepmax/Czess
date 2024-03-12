import java.util.*;
import java.util.function.IntFunction;

class Solver {
    static class Variable implements Cloneable{
        List<Integer> domain;
        Integer assignment;
        // you can add more attributes

        /**
         * Constructs a new variable.
         * @param domain A list of values that the variable can take
         */
        public Variable(List<Integer> domain) {
            this.domain = domain; this.assignment = null;
        }

        private Variable(List<Integer> domain, Integer assignment) {
            this.domain = domain; this.assignment = assignment;
        }

        @Override
        protected Object clone() {
            return new Variable(new ArrayList<>(this.domain), this.assignment);
        }
    }

    static abstract class Constraint {
        /**
         * Tries to reduce the domain of the variables associated to this constraint, using inference
         */
        abstract boolean isFeasible();
        abstract void infer(/* you can add params */);
    }

    // Example implementation of the Constraint interface.
    // It enforces that for given variable X, it holds that 5 < X < 10.
    //
    // This particular constraint will most likely not be very useful to you...
    // Remove it and design a few constraints that *can* help you!
    static abstract class BetweenFiveAndTenConstraint {
        Variable var;

        public BetweenFiveAndTenConstraint(Variable var) {
            this.var = var;
        }

        void infer() {
            List<Integer> newDomain = new LinkedList<>();

            for (Integer x : this.var.domain) {
                if (5 < x && x < 10)
                    newDomain.add(x);
            }

            this.var.domain = newDomain;
        }
    }

    Variable[] variables;
    Constraint[] constraints;
    List<int[]> solutions;
    // you can add more attributes

    /**
     * Constructs a solver.
     * @param variables The variables in the problem
     * @param constraints The constraints applied to the variables
     */
    public Solver(Variable[] variables, Constraint[] constraints) {
        this.variables = variables;
        this.constraints = constraints;

        solutions = new LinkedList<>();
    }

    /**
     * Searches for one solution that satisfies the constraints.
     * @return The solution if it exists, else null
     */
    int[] findOneSolution() {
        solve(false);

        return !solutions.isEmpty() ? solutions.get(0) : null;
    }

    /**
     * Searches for all solutions that satisfy the constraints.
     * @return The solution if it exists, else null
     */
    List<int[]> findAllSolutions() {
        solve(true);

        return solutions;
    }

    /**
     * Main method for solving the problem.
     * @param findAllSolutions Whether the solver should return just one solution, or all solutions
     */
    void solve(boolean findAllSolutions) {
        // here you can do any preprocessing you might want to do before diving into the search

        search(findAllSolutions /* you can add more params */);
    }

    /**
     * Solves the problem using search and inference.
     */
    void search(boolean findAllSolutions /* you can add more params */) {
        Stack<Variable[]> stack = new Stack<>();
        stack.push(this.variables);

        while (!stack.empty()){

            Variable[] previousState = stack.pop();

            for (int i = 0; i < this.variables.length; i++) {
                Variable v = this.variables[i];
                Variable prev = previousState[i];
                v.assignment = prev.assignment; v.domain = prev.domain;
            }

            while(true) {
                // TODO: Some sort of inference
                // Now we've reached a fixed point

                // Assign a variable
                Variable variable = null;
                for (Variable var: this.variables) {
                    if (var.assignment != null) {
                        continue;
                    }
                    variable = var;
                }

                // This is a solution????
                // // If all variables are assigned, push solution
                if (variable == null) {
                    int[] solution = Arrays.stream(this.variables).mapToInt(x -> x.assignment).toArray();

                    System.out.print("[");
                    for (int i = 0; i < solution.length; i++) {
                        System.out.print(solution[i]);
                        System.out.print(", ");
                    }
                    System.out.println("]");

                    solutions.add(solution);
                    if (findAllSolutions) {
                        break;
                    }
                    return;
                }

                if (variable.domain.size() == 0) {
                    break;
                }

                variable.assignment = variable.domain.get(0);

                // Check whether constraints are still satisfied, if not backtrack
                // TODO

                // Reduce variable's domain and push onto stack
                variable.domain = variable.domain.subList(1, variable.domain.size());
                stack.push(Arrays.stream(this.variables).map(x -> x.clone()).toArray(Variable[]::new));
            }
        }



        // TODO: implement search using search and inference
    }
}