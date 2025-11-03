package com.tgg;

import java.util.Random;
import java.util.Vector;

/**
 * Created by TGG on 1/11/2025.
 */
public class CluePosition implements Clue {

  /*
   *   1) positional (order of addition):
          IN_POSITION_X: name or descriptor is position X
 *        NOT_IN_POSITION_X: name or descriptor is not position X
 *        IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B: name or descriptor is next to name or descriptor
 *        FIRST_OR_LAST: name or descriptor is either first or last
 *        BEFORE_INGREDIENT_B: name or descriptor is before name or descriptor
  */

  @Override
  public boolean hasConflicts(Solution solution) {
    switch (m_clueType) {
      case IN_POSITION_X:
        return (solution.getIngredient(m_position) != m_ingredientA);

      case NOT_IN_POSITION_X:
        return (solution.getIngredient(m_position) == m_ingredientA);

      case BEFORE_INGREDIENT_B: {
        int posA = solution.findIngredientPosition(m_ingredientA);
        int posB = solution.findIngredientPosition(m_ingredientB);
        return (posA < 0 || posB < 0 || posA >= posB);
      }
      case IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B: {
        int posB = solution.findIngredientPosition(m_ingredientB);
        if (posB < 0 ) return true;
        if (posB >= 1 && solution.getIngredient(posB - 1) == m_ingredientA) return false;
        if (posB < solution.getIngredientsCount() - 1 && solution.getIngredient(posB + 1) == m_ingredientA) return false;
        return true;
      }
      case FIRST_OR_LAST:
        if (solution.getIngredient(0) == m_ingredientA) return false;
        if (solution.getIngredient(solution.getIngredientsCount() - 1) == m_ingredientA) return false;
        return true;
      default:
        throw new IllegalStateException();
    }
  }
  /**
   * Generate a new random positional clue based on the provided solution
   * @param mustBeSatisfied  clue will be satisfied by the solution (newclue.hasConflicts(solution) will return false)
   * @return
   */
  public static CluePosition generateRandomClue(Solution solution, boolean mustBeSatisfied) {
    CluePosition retval;
    ClueType clueType = ClueType.getRandomClueType();
    do {
      int numberOfPositions = solution.getIngredientsCount();
      int posA = s_random.nextInt(numberOfPositions);
      int posB = s_random.nextInt(numberOfPositions);
      if (posB == posA) posB = (posB + 1) % numberOfPositions;
      Ingredient ingredientA = solution.getIngredient(posA);
      Ingredient ingredientB = solution.getIngredient(posB);
      int position = s_random.nextInt(numberOfPositions);

      retval = new CluePosition(clueType, ingredientA, ingredientB, position);
    } while (mustBeSatisfied && retval.hasConflicts(solution));
    return retval;
  }

  public static void setSeedForTesting(int seed) {s_random = new Random(seed);}

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    switch (m_clueType) {
      case IN_POSITION_X:
        builder.append("The ");
        builder.append(m_ingredientA);
        builder.append(" is the ");
        builder.append(ordinal(m_position + 1));
        builder.append(" ingredient added.");
        break;
      case NOT_IN_POSITION_X:
        builder.append("The ");
        builder.append(m_ingredientA);
        builder.append(" is not the ");
        builder.append(ordinal(m_position + 1));
        builder.append(" ingredient added.");
        break;

      case BEFORE_INGREDIENT_B:
        builder.append("The ");
        builder.append(m_ingredientA);
        builder.append(" is added at some point before the ");
        builder.append(m_ingredientB);
        builder.append(".");
        break;
      case IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B: {
        builder.append("The ");
        builder.append(m_ingredientA);
        builder.append(" is added either immediately before, or immediately after, the ");
        builder.append(m_ingredientB);
        builder.append(".");
        break;
      }
      case FIRST_OR_LAST:
        builder.append("The ");
        builder.append(m_ingredientA);
        builder.append(" is added either first or last.");
        break;
      default:
        throw new IllegalStateException();
    }
    return builder.toString();
  }

  /**
   * Convert a number to a human-readable ordinal (first, second, third etc)
   * @param i
   * @return the ordinal name (first, second, third etc)
   */
  private static String ordinal(int i) {
    String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
    switch (i % 100) {
      case 11:
      case 12:
      case 13:
        return i + "th";
      default:
        return i + suffixes[i % 10];
    }
  }

  private CluePosition(ClueType clueType, Ingredient ingredientA, Ingredient ingredientB, int position) {
    m_clueType = clueType;
    m_ingredientA = ingredientA;
    m_ingredientB = ingredientB;
    m_position = position;
  }

  private enum ClueType {
    IN_POSITION_X(1),
    NOT_IN_POSITION_X(1),
    IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B(1),
    FIRST_OR_LAST(1),
    BEFORE_INGREDIENT_B(1);

    public static ClueType getRandomClueType() {
      setupWeightsLookupTable();
      Random random = new Random();
      int j = random.nextInt(m_weightstable.size());
      return m_weightstable.get(j);
    }

    private static void setupWeightsLookupTable() {
      if (m_weightstable != null) return;
      m_weightstable = new Vector<ClueType>();
      for (ClueType clueType : ClueType.values()) {
        for (int k = clueType.m_weight - 1; k >= 0; --k) {
          m_weightstable.add(clueType);
        }
      }
    }

    private ClueType(int weight) { m_weight = weight; }
    private int m_weight;
    private static Vector<ClueType> m_weightstable;
  }

  private ClueType m_clueType;
  private Ingredient m_ingredientA;
  // plus either of the following two:
  private Ingredient m_ingredientB;
  private int m_position;  // 0, 1, 2 etc
  private static Random s_random = new Random();
}
