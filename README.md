# Assignment 2: Generic Constraint Solver

---

## Overview

This is a template for Assignment 2 of *CSE3300 Algorithms for NP-Hard Problems*. It serves as a starting point for creating a generic solver, which is supposed to be able to find solutions to a variety of problems, by using just one specific solving algorithm and simply changing the variables and constraints that are given to it.

You are not obliged to use this template; you can also create your own solver from scratch, if you prefer.

For more information about the assignment, please refer to [Brightspace](https://brightspace.tudelft.nl/d2l/le/content/595351/viewContent/3272137/View) or [WebLab](https://weblab.tudelft.nl/cse3300/2023-2024/assignment/127273/info)

---

## Files

- `Solver.java` contains a template for how your `Solver` *could* look. You are allowed to change **everything** within the `Solver` class.
- `StandardCombinatorics.java`, `NQueens.java` and `Sudoku.java` contain methods where you can create models for each problem, let your `Solver` solve them, and construct an output from the found solutions. You are allowed to change the bodies of the methods, but **do not change the method signatures**. This will cause the spec tests to break.
- `StandardCombinatoricsTest.java`, `NQueensTest.java` and `SudokuTest.java` contain basic test suites for verifying your solution. You shouldn't have to change anything in these files, but are allowed to do so if you please.

---

## Submission

Snippets of your code should be copied and pasted into the specified assignments on [WebLab](https://weblab.tudelft.nl/cse3300/2023-2024/assignment/127273/info). \
This includes your entire `Solver` class, and the contents of the methods that model and solve the problems (e.g. `getBinaryStrings`, `getNQueenSolutions`, etc.).

Remember: **all your WebLab submissions *must* use the same `Solver` class!** We will verify manually that this is the case.

---

Happy solving! :)