import java.util.*;
import java.util.function.IntFunction;

class Solver {
    static class Variable implements Cloneable{
        List<Integer> domain;
        BitSet bitDomain;
        Integer assignment;
        // you can add more attributes

        /**
         * Constructs a new variable.
         * @param domain A list of values that the variable can take
         */
        public Variable(ArrayList<Integer> domain) {
            this.domain = domain; this.assignment = null;
            this.bitDomain = new BitSet(domain.size());
            this.bitDomain.set(0, domain.size(), true);
        }

        private Variable(List<Integer> domain, Integer assignment, BitSet bitDomain) {
            this.domain = domain; this.assignment = assignment; this.bitDomain = bitDomain;
        }

        @Override
        protected Object clone() {
            return new Variable(this.domain, this.assignment, (BitSet)this.bitDomain.clone());
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

//    public static class NotEqualConstraint extends Constraint {
//        public NotEqualConstraint(List<Variable> variables) {
//            super(variables);
//        }
//
//        @Override
//        boolean isFeasible(int indexOfTheAssignment, int assignment) {
//            for(int i = 0; i < variables.size(); i++){
//                if(i != indexOfTheAssignment && variables.get(i).assignment != null &&
//                        variables.get(i).assignment == assignment){
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        // Returns set of variables that were assigned.
//        @Override
//        Set<Integer> infer(int indexOfTheAssignment) {
//            int value = variables.get(indexOfTheAssignment).assignment;
//            Set<Integer> newlyAssignedValues = new HashSet<>();
//
//            // Remove the current value from the domain of other variables which are not yet assigned.
//            for (int i = 0; i < variables.size(); i++) {
//                if(variables.get(i).assignment == null && variables.get(i).domain.contains(value)){
//                    var index = variables.get(i).domain.indexOf(value);
//                    variables.get(i).domain.remove(index);
//                    if(variables.get(i).domain.size() == 1){
//                        variables.get(i).assignment = variables.get(i).domain.get(0);
//                        newlyAssignedValues.add(i);
//                    }
//                }
//            }
//            return newlyAssignedValues;
//        }
//    }

//    static class allowRepetitionOfNull extends Constraint{
//
//        public allowRepetitionOfNull(List<Variable> variables){
//            super(variables);
//
//        }
//
//        @Override
//        boolean isFeasible(int indexOfTheAssignment, int assignment) {
//
//            if(assignment == -1){
//                for(int i = 0; i< indexOfTheAssignment; i++){
//                    if(variables.get(i).assignment != null && variables.get(i).assignment != -1){
//                        return false;
//                    }
//                }
//                return true;
//            }
//
//            for(int i = 0; i< variables.size(); i++){
//                if(i < indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment >= assignment){
//                    return false;
//                }
//                if(i > indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment <= assignment){
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        Set<Integer> infer(int indexOfTheAssignment) {
//            int value = variables.get(indexOfTheAssignment).assignment;
//            Set<Integer> newlyAssignedValues = new HashSet<>();
//
//            // Remove the current value from the domain of other variables which are not yet assigned.
//            for (int i = 0; i < variables.size(); i++) {
//                if(variables.get(i).assignment == null){
//                    var index = variables.get(i).domain.indexOf(value);
//                    int length = variables.get(i).domain.size();
//                    if(value == -1 && i < indexOfTheAssignment) {
//                        variables.get(i).domain = new ArrayList<>();
//                        variables.get(i).assignment = -1;
//                        continue;
//                    }
//                    if(value == -1){
//                        continue;
//                    }
//                    //Remove all smaller or all bigger values.
//                    if(i < indexOfTheAssignment) {
//                        variables.get(i).domain = variables.get(i).domain.subList(0, index);
//                    } else if (i > indexOfTheAssignment){
//                        variables.get(i).domain = variables.get(i).domain.subList(index+1, length);
//                    }
//                    if(variables.get(i).domain.size() == 1){
//                        if(isFeasible(i, variables.get(i).domain.get(0))){
//                            variables.get(i).assignment = variables.get(i).domain.get(0);
//                            newlyAssignedValues.add(i);
//                        } else {
//                            variables.get(i).domain = new ArrayList<>();
//                            // Conflict detected, but idk how to write code efficiently. Maybe try catch block?
//                        }
//                    }
//                }
//            }
//            return newlyAssignedValues;
//
//        }
//    }
//    static class AllOtherAssignmentsHaveToBeBiggerOrSmallerToAvoidRepetition extends Constraint{
//
//        public AllOtherAssignmentsHaveToBeBiggerOrSmallerToAvoidRepetition(List<Variable> variables){
//            super(variables);
//
//        }
//
//        @Override
//        boolean isFeasible(int indexOfTheAssignment, int assignment) {
//
//            for(int i = 0; i< variables.size(); i++){
//                if(i < indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment >= assignment){
//                    return false;
//                }
//                if(i > indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment <= assignment){
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        Set<Integer> infer(int indexOfTheAssignment) {
//            int value = variables.get(indexOfTheAssignment).assignment;
//            Set<Integer> newlyAssignedValues = new HashSet<>();
//
//            // Remove the current value from the domain of other variables which are not yet assigned.
//            for (int i = 0; i < variables.size(); i++) {
//                if(variables.get(i).assignment == null){
//                    var index = variables.get(i).domain.indexOf(value);
//                    int length = variables.get(i).domain.size();
//
//                    //Remove all smaller or all bigger values.
//                    if(i < indexOfTheAssignment) {
//                        variables.get(i).domain = variables.get(i).domain.subList(0, index);
//                    } else if (i > indexOfTheAssignment){
//                        variables.get(i).domain = variables.get(i).domain.subList(index+1, length);
//                    }
//                    if(variables.get(i).domain.size() == 1){
//                        if(isFeasible(i, variables.get(i).domain.get(0))){
//                            variables.get(i).assignment = variables.get(i).domain.get(0);
//                            newlyAssignedValues.add(i);
//                        } else {
//                            variables.get(i).domain = new ArrayList<>();
//                            // Conflict detected, but idk how to write code efficiently. Maybe try catch block?
//                        }
//                    }
//                }
//            }
//            return newlyAssignedValues;
//
//        }
//    }

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

                int newMin = (var.assignment != null) ? var.assignment : var.domain.get(0);

                if (var.domain.get(0) > minimum) {
                    minimum = this.withEquality ? newMin - 1 : newMin;
                    continue;
                }

                int i = 0;
                while (i < var.domain.size() && var.domain.get(i) <= minimum) { i++; }

                if (i == var.domain.size()) {
                    return -1;
                }

                var.domain = var.domain.subList(i, var.domain.size());

                newMin = var.domain.get(0);
                minimum = this.withEquality ? newMin - 1 : newMin;

                hasInferred = 1;
            }



            return hasInferred;
        }
    }

    static class NotEqual extends Constraint {
        /**
         * Constraint of the type x0 =/= x1 =/= x2 =/= ...
         */

        BitSet hasBeenInferred;
        int numInferred;

        public NotEqual (List<Variable> variables) {
            super(variables);
            this.hasBeenInferred = new BitSet(variables.size());
            this.numInferred = 0;
        }

        @Override
        int infer() {
           int hasInferred = 0;

           if (this.numInferred == this.hasBeenInferred.size()) { return 0; }

           for (Variable var: this.variables) {


               if (var.assignment == null) { continue; }

               for (Variable other: this.variables) {
                   if (var == other) { continue; }

                   int index = other.domain.indexOf(var.assignment);

                   if (index == -1) { continue; }

                   other.domain.remove(index);
                   hasInferred = 1;
               }
           }

           return hasInferred;
        }
    }

//    static class LinearLessThanInequality extends Constraint {
//        List<Integer> form;
//        Integer comparison;
//        public LinearLessThanInequality(List<Variable> variables, List<Integer> form, Integer comparison){
//            super(variables);
//            this.form = form;
//            this.comparison = comparison;
//        }
//        @Override
//        boolean infer() {
//            // Remove the current value from the domain of other variables which are not yet assigned.
//            boolean hasInferred = false;
//            for (int i = 0; i < variables.size(); i++) {
//                Variable pruned = variables.get(i);
//
//                if (pruned.assignment != null) continue;
//
//                int sum = 0;
//                for (int j = 0; j < variables.size(); j++) {
//                    if (i == j) continue;
//
//                    Variable var = variables.get(j);
//
//                    if (var.assignment != null) {
//                        sum += form.get(j) * var.assignment;
//                    }
//
//                    if (var.domain.size() == 0) return false;
//                    int coeff = form.get(j);
//
//                    sum += coeff * var.domain.get(coeff < 0 ? (var.domain.size() - 1) : 0);
//                }
//
//                int threshold = comparison - sum;
//
//
//
//            }
//            return hasInferred;
//        }
//    }

//    static class AllOtherAssignmentsHaveToBeEqualOrBigger extends Constraint{
//
//        public AllOtherAssignmentsHaveToBeEqualOrBigger(List<Variable> variables){
//            super(variables);
//        }
//
//        @Override
//        boolean isFeasible(int indexOfTheAssignment, int assignment) {
//
//            for(int i = 0; i< variables.size(); i++){
//                if(i < indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment > assignment){
//                    return false;
//                }
//                if(i > indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment < assignment){
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        Set<Integer> infer(int indexOfTheAssignment) {
//            int value = variables.get(indexOfTheAssignment).assignment;
//            Set<Integer> newlyAssignedValues = new HashSet<>();
//
//            // Remove the current value from the domain of other variables which are not yet assigned.
//            for (int i = 0; i < variables.size(); i++) {
//                if(variables.get(i).assignment == null){
//                    var index = variables.get(i).domain.indexOf(value);
//                    int length = variables.get(i).domain.size();
//
//                    //Remove all smaller or all bigger values.
//                    if(i < indexOfTheAssignment) {
//                        variables.get(i).domain = variables.get(i).domain.subList(0, index+1);
//                    } else if (i > indexOfTheAssignment){
//                        variables.get(i).domain = variables.get(i).domain.subList(index, length);
//                    }
//                    if(variables.get(i).domain.size() == 1){
//                        if(isFeasible(i, variables.get(i).domain.get(0))){
//                            variables.get(i).assignment = variables.get(i).domain.get(0);
//                            newlyAssignedValues.add(i);
//                        } else {
//                            variables.get(i).domain = new ArrayList<>();
//                            // Conflict detected, but idk how to write code efficiently. Maybe try catch block?
//                        }
//
//                    }
//                }
//            }
//            return newlyAssignedValues;
//
//        }
//    }


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
                v.bitDomain = prev.bitDomain;
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
                        var.assignment = var.domain.get(domainOffset);
                    } else if (cardinality == 0 && var.assignment == null) {
                        isFeasible = false;
                        break;
                    }
                }

                if (!isFeasible) { break; }

                // Assign a variable
                Variable variable = null;
                for (int i = 0; i< variables.length; i++) {
                    if (variables[i].assignment != null) {
                        continue;
                    }
                    variable = variables[i];
                    break;
                }

                // This is a solution????
                // // If all variables are assigned, push solution
                if (variable == null) {
                    // x == -1 means that it is null, empty or whatever.
                    int[] solution = Arrays.stream(this.variables).mapToInt(x -> x.assignment).toArray();

//                    System.out.print("[");
//                    for (int i = 0; i < solution.length; i++) {
//                        System.out.print(solution[i]);
//                        System.out.print(", ");
//                    }
//                    System.out.println("]");

                    solutions.add(solution);
                    if (findAllSolutions) {
                        break;
                    }
                    return;
                }


                if (variable.bitDomain.isEmpty()) {
                    break;
                }

                int domainOffset = variable.bitDomain.nextSetBit(0);
                int assignment = variable.domain.get(domainOffset);
                variable.bitDomain.clear(domainOffset);

                // Reduce variable's domain and push onto stack

                Variable[] newVariables = new Variable[this.variables.length];

                for (int i = 0; i < this.variables.length; i++) {
                    newVariables[i] = (Variable) this.variables[i].clone();
                }
                stack.push(newVariables);

                variable.assignment = assignment;
            }
        }
    }
}