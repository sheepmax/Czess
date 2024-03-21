import java.util.*;
import java.util.function.IntFunction;

class Solver {
    static class Variable implements Cloneable{
        List<Integer> domain;
        BitSet bitDomain;
        Integer assignment;
        int assignmentOffset;
        // you can add more attributes

        /**
         * Constructs a new variable.
         * @param domain A list of values that the variable can take
         */
        public Variable(ArrayList<Integer> domain) {
            this.domain = domain; this.assignment = null;
            this.assignmentOffset = -1;
            this.bitDomain = new BitSet(domain.size());
            this.bitDomain.set(0, domain.size(), true);
        }

        private Variable(List<Integer> domain, Integer assignment, int assignmentOffset, BitSet bitDomain) {
            this.domain = domain; this.assignment = assignment;
            this.bitDomain = bitDomain; this.assignmentOffset = assignmentOffset;
        }




        public void setAssignment(int index) {
            this.assignmentOffset = index;
            this.assignment = this.domain.get(index);
        }

        @Override
        protected Object clone() {
            return new Variable(this.domain, this.assignment, this.assignmentOffset, this.bitDomain);
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
                if (var.assignment != null && var.assignment < minimum) {
                    return -1;
                }

                int newMin = (var.assignment != null) ? var.assignment : var.domain.get(var.bitDomain.nextSetBit(0));

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

    static class BinaryNotEqual extends Constraint {
        /**
         * Constraint of the type x0 =/= x1 =/= x2 =/= ...
         */

        public BinaryNotEqual (Variable var1, Variable var2) {
            super(List.of(var1, var2));
        }

        @Override
        int infer() {
           Variable var1, var2;
           var1 = this.variables.get(0);
           var2 = this.variables.get(1);

           if ((var1.assignment == null && var2.assignment == null) ||
                   (var1.assignment != null && var2.assignment != null)) {
               return 0;
           }

           if (var1.assignment != null) {
               BitSet changedDomain = (BitSet) var2.bitDomain.clone();
               changedDomain.clear(var1.assignmentOffset);
               var2.bitDomain = changedDomain;
           } else {
               BitSet changedDomain = (BitSet) var1.bitDomain.clone();
               changedDomain.clear(var2.assignmentOffset);
               var1.bitDomain = changedDomain;
           }

//           for (int i = 0; i < this.variables.size(); i++) {
//               Variable var = this.variables.get(i);
//
//               if (var.assignment == null) { continue; }
//
//               for (Variable other: this.variables) {
//                   if (var == other) { continue; }
//                   if (other.bitDomain.get(var.assignmentOffset)) {
//                       other.bitDomain.clear(var.assignmentOffset);
//                       hasInferred = 1;
//                   }
//               }
//           }

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
                v.assignment = prev.assignment; v.domain = prev.domain;
                v.bitDomain = prev.bitDomain; v.assignmentOffset = prev.assignmentOffset;
            }

            while(true) {
                //infer on constraints .
                int hasInferred = 1;
                boolean isFeasible = true;
                while(hasInferred > 0){
                    hasInferred = 0;
                    for (Constraint c: this.constraints) {
                        int result = c.infer();
                        if (result == -1) { isFeasible = false; break; }
                        hasInferred += result;
                    }
                }

                if (!isFeasible) { break; }

                for (Variable var: this.variables) {
                    int cardinality = var.bitDomain.cardinality();

                    if (cardinality == 1 && var.assignment == null) {
                        int domainOffset = var.bitDomain.nextSetBit(0);
                        var.setAssignment(domainOffset);
                    } else if (cardinality == 0 && var.assignment == null) {
                        isFeasible = false;
                        break;
                    }
                }

                if (!isFeasible) { break; }

                // Make a new decision.
                Variable variable = null;
                int varIdx = -1;
                for (int i = 0; i< variables.length; i++) {
                    if (variables[i].assignment != null) {
                        continue;
                    }
                    variable = variables[i];
                    varIdx = i;
                    break;
                }

                // This is a solution????
                // // If all variables are assigned, push solution
                if (variable == null) {
                    // x == -1 means that it is null, empty or whatever.
//                    int[] solution = Arrays.stream(this.variables).mapToInt(x -> x.assignment).toArray();
                    int[] sol = new int[this.variables.length];
                    for (int i = 0; i < sol.length; i++){
                        sol[i] = this.variables[i].assignment;
                    }

//                    System.out.print("[");
//                    for (int i = 0; i < solution.length; i++) {
//                        System.out.print(solution[i]);
//                        System.out.print(", ");
//                    }
//                    System.out.println("]");

                    solutions.add(sol);
                    if (findAllSolutions) {
                        break;
                    }
                    return;
                }


                if (variable.bitDomain.isEmpty()) {
                    break;
                }


                // Reduce variable's domain and push onto stack

                Variable[] newVariables = new Variable[this.variables.length];

                for (int i = 0; i < this.variables.length; i++) {
                    newVariables[i] = (Variable) this.variables[i].clone();
                }
                newVariables[varIdx].bitDomain = (BitSet) this.variables[varIdx].bitDomain.clone();
                int domainOffset = newVariables[varIdx].bitDomain.nextSetBit(0);
                newVariables[varIdx].bitDomain.clear(domainOffset);
                stack.push(newVariables);
                variable.setAssignment(domainOffset);


            }
        }
    }
}