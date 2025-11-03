package com.tgg.tests;

import com.tgg.*;

/**
 * Created by TGG on 3/11/2025.
 */
public class Tester {

  public static boolean testCluePosition() {
    boolean testfail = false;
    CluePosition.setSeedForTesting(2323);
    Solution solution = new Solution(5, 3);
    solution.addNextIngredient(Ingredient.DRAGONFRUIT,1 );
    solution.addNextIngredient(Ingredient.RED_MUSHROOM,1 );
    solution.addNextIngredient(Ingredient.HONEY,1 );
    solution.addNextIngredient(Ingredient.WHITE_MUSHROOM,1 );
    solution.addNextIngredient(Ingredient.BROWN_MUSHROOM,1 );
    System.out.println(solution);
    for (int i = 0; i < 100; ++i) {
      Clue clue = CluePosition.generateRandomClue(solution, true);
      if (clue.hasConflicts(solution)) {
        testfail = true;
        System.out.println("fail3");
      }
      System.out.println(clue);
    }
    return !testfail;
  }

  public static boolean testSolution() {
    boolean testfail = false;
    Solution solution = Solution.getFirstCombinationOfIngredients(5, 3);
    boolean success = false;
    do {
      System.out.println(solution);
      success = solution.nextUniqueCombinationOfIngredients();
    } while (success);

    Solution solution1 = Solution.getFirstCombinationOfIngredients(5, 3);
    Solution solution2 = new Solution(solution1);
    solution2.nextUniqueCombinationOfIngredients();
    if (solution1.equals(solution2)) {
      System.out.println("fail1");
      testfail = true;
    }

    for (int i = 0; i < solution.getIngredientsCount(); ++i) {
      if (solution1.findIngredientPosition(solution1.getIngredient(i)) != i ) {
        System.out.println("fail2");
        testfail = true;
      }
    }
    return !testfail;
  }

  public static boolean testPuzzle() {
    Puzzle puzzle = Puzzle.generatePuzzle(5, 1);
    System.out.println(puzzle);
    return true;
  }

}
