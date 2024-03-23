import java.util.*;

public class Sudoku {
    /**
     * Returns the filled in sudoku grid.
     *
     * @param grid the partially filled in grid. unfilled positions are -1.
     * @return the fully filled sudoku grid.
     */
    public static int[][] solve(int[][] grid) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();


        int nrows = grid.length;
        int ncols = nrows;

        // TODO: add your variables
        List<Integer> domain = new ArrayList<>();

        for (int i = 0; i < ncols; i++) {
            domain.add(i + 1);
        }
        Solver.Variable.setDomain(domain);

        for (int[] row: grid) {
            for (int square: row) {
                variables.add(new Solver.Variable());
            }
        }

        // Set domains properly, because this solver is silly
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                if (grid[i][j] == -1) continue;
                BitSet newDomain = new BitSet(9);
                newDomain.set(grid[i][j] - 1);
                variables.get(i * ncols + j).bitDomain = newDomain;
                variables.get(i * ncols + j).assigned = true;
            }
        }

        // Create constraints for rows, columns and squares

        // Rows
        for (int i = 0; i < nrows; i++) {
            constraints.add(new Solver.NotEqual(variables.subList(i * ncols, i * ncols + ncols)));
        }

        // Columns
        for (int i = 0; i < ncols; i++) {
            List<Solver.Variable> column = new ArrayList<>();
            for (int j = 0; j < nrows; j++) {
                column.add(variables.get(j * ncols + i));
            }
            constraints.add(new Solver.NotEqual(column));
        }

        // Sqaures
        int squareSize = (int)Math.sqrt(ncols);
        for (int squareX = 0; squareX < ncols; squareX += squareSize) {
            for (int squareY = 0; squareY < nrows; squareY += squareSize) {
                List<Solver.Variable> square = new ArrayList<>();
                for (int i = 0; i < squareSize; i++) {
                    for (int j = 0; j < squareSize; j++) {
                        square.add(variables.get(squareX  + j + (squareY + i) * ncols));
                    }
                }
                constraints.add(new Solver.NotEqual(square));
            }
        }

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        int[] result = solver.findOneSolution();

        int[][] resultGrid = new int[nrows][ncols];

        System.out.println();
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                resultGrid[i][j] = result[i * ncols + j];
            }
        }

        return resultGrid;
    }
}
