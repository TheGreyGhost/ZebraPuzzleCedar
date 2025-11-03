package com.tgg;

import java.util.Vector;

/**
 * Created by TGG on 31/10/2025.
 *
 * Basic structure:
 * 1) Up to 'N' ingredients needed to be added in a particular order
 * 2) The amount of each ingredient can range from 1 to 'M' units, they are not mutually exclusive
 * 3) There is a large pool of ingredients, each ingredient has two characteristics:
 *    a) A Name: Mushroom, Honey, Flower, Dragonfruit, etc.  All ingredients have a name
 *    b) Descriptors: one or more descriptors such as Red, White, Black, Fruit, Liquid, Plant, etc
 *
 * There are various clues of different forms:
 *   1) positional (order of addition):
 *       '=': name or descriptor is position X
 *       '!=': name or descriptor is not position X
 *       'neighbor': name or descriptor is next to name or descriptor
 *       'ends': name or descriptor is either first or last
 *       'before': name or descriptor is before name or descriptor
 *
 *   2) the number of ingredients in a particular category
 *     '=': number of ingredient in category A equals number in category B
 *     '!=': number of ingredient in category A does not equal number in category B
 *     '<': number of ingredient in category A is less than number in category B
 *
 *   3) the amount to add of a particular ingredient
 *     '=': amount of A equals amount of B
 *     '!=': amount of A != amount of B
 *     '<': amount of A < amount of B
 *     A and B can be either: position, ingredient, or category
 *
 */
public class Puzzle {

  enum SolutionCount {NONE, EXACTLY_ONE, MORE_THAN_ONE};

  public static Puzzle generatePuzzle(int numberOfIngredients, int maxIngredientQuantity) {

    Vector<Clue> clues = new Vector<Clue>();
    Solution targetSolution = Solution.getRandomSolution(numberOfIngredients, maxIngredientQuantity);
    Solution firstValidSolution = Solution.getFirstCombinationOfIngredients(numberOfIngredients, maxIngredientQuantity);
    do {
      SolutionCount solutionCount = countNumberOfSolutions(firstValidSolution, clues);
      switch (solutionCount) {
        case EXACTLY_ONE:
          return new Puzzle(firstValidSolution, clues);
        case MORE_THAN_ONE:
          clues.add(ClueFactory.generateClue(targetSolution, true));
          break;
        case NONE:
          throw new IllegalStateException();  // should never be true since we only add clues which satisfy the solution
      }
    } while (true);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Solution:");
    builder.append(m_solution); builder.append("\n");
    builder.append("Clues:");  builder.append("\n");
    for (Clue clue: m_clues) {
      builder.append(clue); builder.append("\n");
    }
    return builder.toString();
  }

  /*  Starting from the firstValidSolution, check whether there are any solutions which can satisfy all of the clues
      returns true if at least one is found.  firstValidSolution will be updated to the first valid solution found
   */
  public static SolutionCount countNumberOfSolutions(Solution firstValidSolution, Vector<Clue> clues) {

    if (!solutionSatisfiesClues(firstValidSolution, clues)) {
      boolean found = findNextSolutionThatSatisifesClues(firstValidSolution, clues);
      if (!found) return SolutionCount.NONE;
    }

    Solution nextCheckSolution = new Solution(firstValidSolution);
    boolean found = nextCheckSolution.nextUniqueCombinationOfIngredients();
    if (!found) return SolutionCount.EXACTLY_ONE;
    found = findNextSolutionThatSatisifesClues(nextCheckSolution, clues);
    if (!found) return SolutionCount.EXACTLY_ONE;

    return SolutionCount.MORE_THAN_ONE;
  }

  /* Check the solution against the clues to see if the solution satisfies all the clues
   */
  public static boolean solutionSatisfiesClues(Solution solution, Vector<Clue> clues) {
    for (Clue clue : clues) {
      if (clue.hasConflicts(solution)) return false;
    }
    return true;
  }

  /*  Finds the next solution combination that satisfies the clues
      Returns false if no solution could be found
   */
  public static boolean findNextSolutionThatSatisifesClues(Solution solution, Vector<Clue> clues) {
    while (!solutionSatisfiesClues(solution, clues)) {
      boolean success = solution.nextUniqueCombinationOfIngredients();
      if (!success) return false;
    }
    return true;
  }

  private Puzzle(Solution solution, Vector<Clue> clues) {
    m_solution = solution;
    m_clues = clues;
  }

  private Solution m_solution;
  private Vector<Clue> m_clues;

}
