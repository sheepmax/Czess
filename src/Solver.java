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
        public Variable(ArrayList<Integer> domain) {
            this.domain = domain; this.assignment = null;
        }

        private Variable(ArrayList<Integer> domain, Integer assignment) {
            this.domain = domain; this.assignment = assignment;
        }

        @Override
        protected Object clone() {
            return new Variable(new ArrayList<>(this.domain), this.assignment);
        }
    }

    public static class NotEqualConstraint extends Constraint {
        public NotEqualConstraint(List<Variable> variables) {
            super(variables);
        }

        @Override
        boolean isFeasible(int indexOfTheAssignment, int assignment) {
            for(int i = 0; i < variables.size(); i++){
                if(i != indexOfTheAssignment && variables.get(i).assignment != null &&
                        variables.get(i).assignment == assignment){
                    return false;
                }
            }
            return true;
        }

        // Returns set of variables that were assigned.
        @Override
        Set<Integer> infer(int indexOfTheAssignment) {
            int value = variables.get(indexOfTheAssignment).assignment;
            Set<Integer> newlyAssignedValues = new HashSet<>();

            // Remove the current value from the domain of other variables which are not yet assigned.
            for (int i = 0; i < variables.size(); i++) {
                if(variables.get(i).assignment == null && variables.get(i).domain.contains(value)){
                    var index = variables.get(i).domain.indexOf(value);
                    variables.get(i).domain.remove(index);
                    if(variables.get(i).domain.size() == 1){
                        variables.get(i).assignment = variables.get(i).domain.get(0);
                        newlyAssignedValues.add(i);
                    }
                }
            }
            return newlyAssignedValues;
        }
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

    static abstract class Constraint {
        /**
         * Tries to reduce the domain of the variables associated to this constraint, using inference
         */

        protected List<Variable> variables;

        public Constraint(List<Variable> variables) {
            this.variables = variables;
        }

        abstract boolean isFeasible(int indexOfTheAssignment, int assignment);
        abstract Set<Integer> infer(int indexOfTheAssignment);
    }

    static class allowRepetitionOfZeros extends Constraint{

        public allowRepetitionOfZeros(List<Variable> variables){
            super(variables);

        }

        @Override
        boolean isFeasible(int indexOfTheAssignment, int assignment) {

            if(assignment == 0){
                for(int i = 0; i< indexOfTheAssignment; i++){
                    if(variables.get(i).assignment != null && variables.get(i).assignment != 0){
                        return false;
                    }
                }
                return true;
            }

            for(int i = 0; i< variables.size(); i++){
                if(i < indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment >= assignment){
                    return false;
                }
                if(i > indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment <= assignment){
                    return false;
                }
            }
            return true;
        }

        @Override
        Set<Integer> infer(int indexOfTheAssignment) {
            int value = variables.get(indexOfTheAssignment).assignment;
            Set<Integer> newlyAssignedValues = new HashSet<>();

            // Remove the current value from the domain of other variables which are not yet assigned.
            for (int i = 0; i < variables.size(); i++) {
                if(variables.get(i).assignment == null){
                    var index = variables.get(i).domain.indexOf(value);
                    int length = variables.get(i).domain.size();
                    if(value == 0 && i < indexOfTheAssignment) {
                        variables.get(i).domain = new ArrayList<>();
                        variables.get(i).assignment = 0;
                        continue;
                    }
                    if(value == 0){
                        continue;
                    }
                    //Remove all smaller or all bigger values.
                    if(i < indexOfTheAssignment) {
                        variables.get(i).domain = variables.get(i).domain.subList(0, index);
                    } else if (i > indexOfTheAssignment){
                        variables.get(i).domain = variables.get(i).domain.subList(index+1, length);
                    }
                    if(variables.get(i).domain.size() == 1){
                        if(isFeasible(i, variables.get(i).domain.get(0))){
                            variables.get(i).assignment = variables.get(i).domain.get(0);
                            newlyAssignedValues.add(i);
                        } else {
                            variables.get(i).domain = new ArrayList<>();
                            // Conflict detected, but idk how to write code efficiently. Maybe try catch block?
                        }
                    }
                }
            }
            return newlyAssignedValues;

        }
    }
    static class AllOtherAssignmentsHaveToBeBiggerOrSmallerToAvoidRepetition extends Constraint{

        public AllOtherAssignmentsHaveToBeBiggerOrSmallerToAvoidRepetition(List<Variable> variables){
            super(variables);

        }

        @Override
        boolean isFeasible(int indexOfTheAssignment, int assignment) {

            for(int i = 0; i< variables.size(); i++){
                if(i < indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment >= assignment){
                    return false;
                }
                if(i > indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment <= assignment){
                    return false;
                }
            }
            return true;
        }

        @Override
        Set<Integer> infer(int indexOfTheAssignment) {
            int value = variables.get(indexOfTheAssignment).assignment;
            Set<Integer> newlyAssignedValues = new HashSet<>();

            // Remove the current value from the domain of other variables which are not yet assigned.
            for (int i = 0; i < variables.size(); i++) {
                if(variables.get(i).assignment == null){
                    var index = variables.get(i).domain.indexOf(value);
                    int length = variables.get(i).domain.size();

                    //Remove all smaller or all bigger values.
                    if(i < indexOfTheAssignment) {
                        variables.get(i).domain = variables.get(i).domain.subList(0, index);
                    } else if (i > indexOfTheAssignment){
                        variables.get(i).domain = variables.get(i).domain.subList(index+1, length);
                    }
                    if(variables.get(i).domain.size() == 1){
                        if(isFeasible(i, variables.get(i).domain.get(0))){
                            variables.get(i).assignment = variables.get(i).domain.get(0);
                            newlyAssignedValues.add(i);
                        } else {
                            variables.get(i).domain = new ArrayList<>();
                            // Conflict detected, but idk how to write code efficiently. Maybe try catch block?
                        }
                    }
                }
            }
            return newlyAssignedValues;

        }
    }

    static class AllOtherAssignmentsHaveToBeEqualOrBigger extends Constraint{

        public AllOtherAssignmentsHaveToBeEqualOrBigger(List<Variable> variables){
            super(variables);
        }

        @Override
        boolean isFeasible(int indexOfTheAssignment, int assignment) {

            for(int i = 0; i< variables.size(); i++){
                if(i < indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment > assignment){
                    return false;
                }
                if(i > indexOfTheAssignment && variables.get(i).assignment != null && variables.get(i).assignment < assignment){
                    return false;
                }
            }
            return true;
        }

        @Override
        Set<Integer> infer(int indexOfTheAssignment) {
            int value = variables.get(indexOfTheAssignment).assignment;
            Set<Integer> newlyAssignedValues = new HashSet<>();

            // Remove the current value from the domain of other variables which are not yet assigned.
            for (int i = 0; i < variables.size(); i++) {
                if(variables.get(i).assignment == null){
                    var index = variables.get(i).domain.indexOf(value);
                    int length = variables.get(i).domain.size();

                    //Remove all smaller or all bigger values.
                    if(i < indexOfTheAssignment) {
                        variables.get(i).domain = variables.get(i).domain.subList(0, index+1);
                    } else if (i > indexOfTheAssignment){
                        variables.get(i).domain = variables.get(i).domain.subList(index, length);
                    }
                    if(variables.get(i).domain.size() == 1){
                        if(isFeasible(i, variables.get(i).domain.get(0))){
                            variables.get(i).assignment = variables.get(i).domain.get(0);
                            newlyAssignedValues.add(i);
                        } else {
                            variables.get(i).domain = new ArrayList<>();
                            // Conflict detected, but idk how to write code efficiently. Maybe try catch block?
                        }

                    }
                }
            }
            return newlyAssignedValues;

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
        int stackCount = 0;
        while (!stack.empty()){

            Variable[] previousState = stack.pop();
            stackCount++;
            // Constraint class works on this.variables. So for checking for this
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
                int indexOfVariable = -1;
                for (int i = 0; i< variables.length; i++) {
                    if (variables[i].assignment != null) {
                        continue;
                    }
                    variable = variables[i];
                    indexOfVariable = i;
                    break;
                }

                // This is a solution????
                // // If all variables are assigned, push solution
                if (variable == null) {
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


                if (variable.domain.size() == 0) {
                    break;
                }

                int assignment = variable.domain.get(0);
                variable.domain.remove(0);
                //Check if constraints are satisfied

                boolean allConstraintsAreFeasible = true;
                for(Constraint c : constraints){
                    if(!c.isFeasible(indexOfVariable, assignment)){
                        allConstraintsAreFeasible = false;
                        break;
                    }
                }

                if(!allConstraintsAreFeasible){
                    break;
                }

                // Reduce variable's domain and push onto stack

                stack.push(Arrays.stream(this.variables).map(x -> x.clone()).toArray(Variable[]::new));

                this.variables[indexOfVariable].assignment = assignment;
                //infer on constraints.
                Set<Integer> newlyAssignedVariables = new HashSet<>();
                int indexToEvaluate = indexOfVariable;
                newlyAssignedVariables.add(indexToEvaluate);
                while(!newlyAssignedVariables.isEmpty()){
                    newlyAssignedVariables.remove(indexToEvaluate);
                    for(Constraint c : constraints){
                        Set<Integer> cAssignedThose = c.infer(indexToEvaluate);
                        newlyAssignedVariables.addAll(cAssignedThose);
                    }
                    if(!newlyAssignedVariables.isEmpty()){
                        indexToEvaluate = newlyAssignedVariables.iterator().next();
                    }
                }


            }
        }
    }
}