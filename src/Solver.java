import java.util.*;
import java.util.function.IntFunction;

class Solver {
    static class Variable implements Cloneable{
        static List<Integer> domain;
        BitSet bitDomain;
        boolean assigned;
        // you can add more attributes

        /**
         * Constructs a new variable.
         * @param domain A list of values that the variable can take
         */
        public static void setDomain(List<Integer> domain) {
            Variable.domain = domain;
        }

        public Variable() {
            this.bitDomain = new BitSet(Variable.domain.size());
            this.bitDomain.set(0, Variable.domain.size());
            this.assigned = false;
        }

        private Variable(BitSet bitDomain, boolean assigned) {
            this.bitDomain = bitDomain; this.assigned = assigned;
        }

        public int assignmentValue() {
            return Variable.domain.get(this.bitDomain.nextSetBit(0));
        }

        public void setAssignment(int index) {
            this.bitDomain = new BitSet(Variable.domain.size());
            this.bitDomain.set(index);
            this.assigned = true;
        }

        @Override
        protected Object clone() {
            return new Variable(this.bitDomain, this.assigned);
        }
    }

    static abstract class Constraint {
        /**
         * Tries to reduce the domain of the variables associated to this constraint, using inference
         */

        protected List<Variable> variables;

        public Constraint(List<Variable> variables) {
            this.variables = variables;
        }
        abstract int infer();
    }

    static class TotalOrdering extends Constraint {
        /**
         * Constraint of the type x0 < x1 < x2 < ..., with the possibility for equality, i.e. x0 <= x1 <= x2 <= ...
         */
        boolean withEquality;

        public TotalOrdering (List<Variable> variables, boolean withEquality) {
            super(variables);
            this.withEquality = withEquality;
        }

        @Override
        int infer() {
            int minimum = Integer.MIN_VALUE;
            int hasInferred = 0;

            for (Variable var: this.variables) {
                if (var.assigned && var.assignmentValue() < minimum) {
                    return -1;
                }

                if (var.bitDomain.isEmpty()) {
                    return -1;
                }

                int newMin = (var.assigned) ? var.assignmentValue() :
                        var.domain.get(var.bitDomain.nextSetBit(0));

                if (newMin > minimum) {
                    minimum = this.withEquality ? newMin - 1 : newMin;
                    continue;
                }

                int i = var.bitDomain.nextSetBit(0);
                while (i >= 0 && var.domain.get(i) <= minimum) { i = var.bitDomain.nextSetBit(i+1); }

                if (i == -1) {
                    return -1;
                }
                BitSet changedDomain = (BitSet) var.bitDomain.clone();
                changedDomain.clear(0, i);
                var.bitDomain = changedDomain;

                newMin = var.domain.get(var.bitDomain.nextSetBit(0));
                minimum = this.withEquality ? newMin - 1 : newMin;

                hasInferred = 1;
            }
            return hasInferred;
        }
    }

    static class DifferenceInequality extends Constraint {
        /**
         * Constraint of the type |x0 - x1| =\= constant
         */

        Variable var1, var2;
        int constant;

        public DifferenceInequality (Variable var1, Variable var2, int constant) {
            super(List.of());
            this.var1 = var1; this.var2 = var2;
            this.constant = constant;
        }

        @Override
        int infer() {
            if (var1.assigned && var2.assigned && Math.abs(var1.assignmentValue() - var2.assignmentValue()) == constant) {
                return -1;
            }
            if (var1.assigned && !var2.assigned) {
                int toRemove = var1.assignmentValue() - constant;
                int toRemoveIndex = toRemove;
                var2.bitDomain = (BitSet) var2.bitDomain.clone();

                if (toRemoveIndex > 0 && toRemoveIndex < Variable.domain.size()) {
                    var2.bitDomain.clear(toRemoveIndex);
                }

                toRemove = constant + var1.assignmentValue();
                toRemoveIndex = toRemove - Variable.domain.get(0);

                if (toRemoveIndex < Variable.domain.size()) {
                    var2.bitDomain.clear(toRemoveIndex);
                }
            }

            if (var2.assigned && !var1.assigned) {
                int toRemove = var2.assignmentValue() - constant;
                int toRemoveIndex = toRemove;
                var1.bitDomain = (BitSet) var1.bitDomain.clone();

                if (toRemoveIndex >= 0 && toRemoveIndex < Variable.domain.size()) {
                    var1.bitDomain.clear(toRemoveIndex);
                }

                toRemove = constant + var2.assignmentValue();
                toRemoveIndex = toRemove - Variable.domain.get(0);

                if (toRemoveIndex < Variable.domain.size()) {
                    var1.bitDomain.clear(toRemoveIndex);
                }

            }
            return 0;
        }
    }

    static class NotEqual extends Constraint {
        /**
         * Constraint of the type x0 =/= x1 =/= x2 =/= ...
         */

        public NotEqual(List<Variable> variables) {
            super(variables);
        }

        @Override
        int infer() {
            BitSet toClear = new BitSet(Variable.domain.size());
            //toClear.set(0, Variable.domain.size());

            for (Variable var: this.variables) {
                if (var.assigned) {
                    int toClearIndex = var.bitDomain.nextSetBit(0);
                    if (toClear.get(toClearIndex) == true) {
                        return -1;
                    }
                    toClear.set(toClearIndex);
                }
            }

            if (toClear.isEmpty()) {
                return 0;
            }

            for (Variable var: this.variables) {
                if (!var.assigned) {
                    var.bitDomain = (BitSet) var.bitDomain.clone();
                    var.bitDomain.andNot(toClear);
                }
            }

            return 0;
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

            // Constraint class works on this.variables. So for checking for this
            for (int i = 0; i < this.variables.length; i++) {
                Variable v = this.variables[i];
                Variable prev = previousState[i];
                v.bitDomain = prev.bitDomain; v.assigned = prev.assigned;
            }

            while(true) {
                //infer on constraints .
                boolean isFeasible = true;

                // Do variable selection and assignment
                Variable chosen = null;
                int minimumCardinality = Integer.MAX_VALUE;
                for (Variable var: this.variables) {
                    int cardinality = var.bitDomain.cardinality();

                    if (cardinality == 1 && !var.assigned) {
                        var.setAssignment(var.bitDomain.nextSetBit(0));
                    }


                    else if (cardinality > 1) {
                        if (cardinality < minimumCardinality) {
                            minimumCardinality = cardinality;
                            chosen = var;
                        }
                    }
                }

                for (Constraint c: this.constraints) {
                    int result = c.infer();
                    if (result == -1) { isFeasible = false; break; }
                }

                if (!isFeasible) { break; }

                for (Variable var: this.variables) {
                    if (var.bitDomain.isEmpty()) {
                        isFeasible = false; break;
                    }
                }

                if (!isFeasible) { break; }

                // This is a solution????
                // // If all variables are assigned, push solution
                if (chosen == null) {
                    int[] sol = new int[this.variables.length];
                    for (int i = 0; i < sol.length; i++){
                        sol[i] = Variable.domain.get(this.variables[i].bitDomain.nextSetBit(0));
                    }

                    solutions.add(sol);
                    if (findAllSolutions) {
                        break;
                    }
                    return;
                }

                // Reduce variable's domain and push onto stack

                Variable[] newVariables = new Variable[this.variables.length];

                int assignmentOffset = chosen.bitDomain.nextSetBit(0);
                chosen.bitDomain = (BitSet) chosen.bitDomain.clone();
                chosen.bitDomain.clear(assignmentOffset);

                for (int i = 0; i < this.variables.length; i++) {
                    newVariables[i] = (Variable) this.variables[i].clone();
                }
                stack.push(newVariables);

                chosen.setAssignment(assignmentOffset);
            }
        }
    }
}