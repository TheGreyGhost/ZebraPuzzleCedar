package com.tgg;

import java.util.Random;
import java.util.Vector;

/**
 * Created by TGG on 1/11/2025.
 */
public class CluePosition implements Clue {

  /*
   *   positional (order of addition):
          IN_POSITION_X: name or descriptor is position X
 *        NOT_IN_POSITION_X: name or descriptor is not position X
 *        IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B: name or descriptor is next to name or descriptor
 *        FIRST_OR_LAST: name or descriptor is either first or last
 *        BEFORE_INGREDIENT_B: name or descriptor is before name or descriptor
 *        TWO_NEXT_TO_EACH_OTHER
 *        TWO_NOT_NEXT_TO_EACH_OTHER
  */

  @Override
  public boolean hasConflicts(Solution solution) {
    switch (m_clueType) {
      case IN_POSITION_X: {
        if (!m_ingredientInformationA.matches(solution.getIngredient(m_position))) return true;
        if (m_ingredientInformationA.doesNotSatisfyPlurality(solution)) return true;
        return false;
      }

      case NOT_IN_POSITION_X: {
        if (m_ingredientInformationA.matches(solution.getIngredient(m_position))) return true;
        if (m_ingredientInformationA.doesNotSatisfyPlurality(solution)) return true;
        return false;
      }

      case BEFORE_INGREDIENT_B: {
        if (m_ingredientInformationA.doesNotSatisfyPlurality(solution)) return true;
        if (m_ingredientInformationB.doesNotSatisfyPlurality(solution)) return true;

        int posA = solution.findFirstMatchingIngredientPosition(m_ingredientInformationA);
        int posB = solution.findLastMatchingIngredientPosition(m_ingredientInformationB);
        return (posA < 0 || posB < 0 || posA >= posB);
      }

      case IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B: {
        int posB = solution.findFirstMatchingIngredientPosition(m_ingredientInformationB);
        while (posB >= 0 ) {
          if (posB >= 1 && m_ingredientInformationA.matches(solution.getIngredient(posB - 1))) return false;
          if (posB < solution.getIngredientsCount() - 1 &&
                     m_ingredientInformationB.matches(solution.getIngredient(posB + 1))
             )
            return false;
          posB = solution.findNextMatchingIngredientPosition(m_ingredientInformationB, posB);
        }
        return true;
      }

      case FIRST_OR_LAST: {
        if (m_ingredientInformationA.matches(solution.getIngredient(0))) return false;
        if (m_ingredientInformationA.matches(solution.getIngredient(solution.getIngredientsCount() - 1))) return false;
        return true;
      }

      case TWO_NEXT_TO_EACH_OTHER: {
        int posA = solution.findFirstMatchingIngredientPosition(m_ingredientInformationA);
        int lastPosToCheck = solution.getIngredientsCount() - 2;
        while (posA >= 0 && posA <= lastPosToCheck) {
          if (m_ingredientInformationA.matches(solution.getIngredient(posA + 1))) return false;
          posA = solution.findNextMatchingIngredientPosition(m_ingredientInformationA, posA);
        }
        return true;
      }

      case TWO_NOT_NEXT_TO_EACH_OTHER: {
        int posA = solution.findFirstMatchingIngredientPosition(m_ingredientInformationA);
        int lastPosToCheck = solution.getIngredientsCount() - 2;
        while (posA >= 0 && posA <= lastPosToCheck) {
          if (m_ingredientInformationA.matches(solution.getIngredient(posA + 1))) return true;
          posA = solution.findNextMatchingIngredientPosition(m_ingredientInformationA, posA);
        }
        return false;
      }

      default:
        throw new IllegalStateException();
    }
  }
  /**
   * Generate a new random positional clue based on the provided solution
   * @param mustBeSatisfied if true, then the clue will be satisfied by the solution (newclue.hasConflicts(solution) will return false)
   * @return
   */
  public static CluePosition generateRandomClue(Solution solution, boolean mustBeSatisfied) {
    CluePosition retval;
    ClueType clueType = ClueType.getRandomClueType();
    int clueTypeResetCounter = 0;
    final int FAILED_ATTEMPTS_BEFORE_CLUE_TYPE_RESET = 10;
    do {
      if (++clueTypeResetCounter >= FAILED_ATTEMPTS_BEFORE_CLUE_TYPE_RESET) {
        clueType = ClueType.getRandomClueType();
        clueTypeResetCounter = 0;
      }

      int numberOfPositions = solution.getIngredientsCount();
      int posA = s_random.nextInt(numberOfPositions);
      int posB = s_random.nextInt(numberOfPositions);
      if (posB == posA) posB = (posB + 1) % numberOfPositions;

      // fix positions to match the clue types if necessary
      if (clueType == ClueType.IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B) {
        if (posA == 0) {
          posB = 1;
        } else if (posA == numberOfPositions-1) {
          posB = posA - 1;
        } else {
          posB = posA + (s_random.nextBoolean() ? 1 : -1);
        }
      }
      if (clueType == ClueType.BEFORE_INGREDIENT_B) {
        if (posA > posB) {
          int temp = posA;
          posA = posB;
          posB = temp;
        }
      }

      Ingredient ingredientA = solution.getIngredient(posA);
      Ingredient ingredientB = solution.getIngredient(posB);
      int position = s_random.nextInt(numberOfPositions);

      ClueIngredientInformation clueIngredientInformationA =
              ClueIngredientInformation.generateRandomIngredientInformation(ingredientA, solution);
      ClueIngredientInformation clueIngredientInformationB =
              ClueIngredientInformation.generateRandomIngredientInformation(ingredientB, solution);

      ClueType finalClueType = clueType;
      // might need to convert this clue to a different type
      if (clueType == ClueType.IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B) {   // convert to two adjacent
        if (clueIngredientInformationA.isEquivalentWhenMatching(clueIngredientInformationB)) {
          finalClueType = ClueType.TWO_NEXT_TO_EACH_OTHER;
        }
      } else if (clueType == ClueType.BEFORE_INGREDIENT_B) { // convert to "two non-adjacent" or "adjacent" as appropriate
        if (clueIngredientInformationA.isEquivalentWhenMatching(clueIngredientInformationB)) {
          if (posA == posB - 1 || posA == posB +1 ) {
            finalClueType = ClueType.TWO_NEXT_TO_EACH_OTHER;
          } else {
            finalClueType = ClueType.TWO_NOT_NEXT_TO_EACH_OTHER;
          }
        }
      }

      retval = new CluePosition(clueType, clueIngredientInformationA, clueIngredientInformationB, position);
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
        builder.append(NumberToWords.ordinal(m_position + 1));
        builder.append(" ingredient added.");
        break;
      case NOT_IN_POSITION_X:
        builder.append("The ");
        builder.append(m_ingredientA);
        builder.append(" is not the ");
        builder.append(NumberToWords.ordinal(m_position + 1));
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

  private CluePosition(ClueType clueType,
                       ClueIngredientInformation clueIngredientInformationA,
                       ClueIngredientInformation clueIngredientInformationB, int position) {
    m_clueType = clueType;
    m_ingredientInformationA = clueIngredientInformationA;
    m_ingredientInformationB = clueIngredientInformationB;
    m_position = position;
  }

  private enum ClueType {
    IN_POSITION_X(Setting.CLUE_TYPE_WEIGHT_IN_POSITION_X),
    NOT_IN_POSITION_X(Setting.CLUE_TYPE_WEIGHT_NOT_IN_POSITION_X),
    IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B(Setting.CLUE_TYPE_WEIGHT_IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B),
    TWO_NEXT_TO_EACH_OTHER(Setting.CLUE_TYPE_WEIGHT_TWO_NEXT_TO_EACH_OTHER),   // subset of IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B
    FIRST_OR_LAST(Setting.CLUE_TYPE_WEIGHT_FIRST_OR_LAST),
    BEFORE_INGREDIENT_B(Setting.CLUE_TYPE_WEIGHT_BEFORE_INGREDIENT_B),
    TWO_NOT_NEXT_TO_EACH_OTHER(Setting.CLUE_TYPE_WEIGHT_TWO_NOT_NEXT_TO_EACH_OTHER),   // subset of BEFORE_INGREDIENT_B
    ;

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

    private ClueType(Setting setting) { m_weight = setting.getWeight(); }
    private int m_weight;
    private static Vector<ClueType> m_weightstable;
  }

  private ClueType m_clueType;

  private ClueIngredientInformation m_ingredientInformationA;
  // plus either of the following two groupings:
  private ClueIngredientInformation m_ingredientInformationB;
  // or
  private int m_position;  // 0, 1, 2 etc

  private static Random s_random = new Random();
}
