package com.tgg;

import java.util.Random;
import java.util.Vector;

/**
 * Created by TGG on 31/10/2025.
 *
 * Holds the solution to the puzzle
 *
 */
public class Solution {

  public Solution(int numberOfIngredients, int maxIngredientQuantity) {
    m_numberOfIngredients = numberOfIngredients;
    m_maxIngredientQuantity = maxIngredientQuantity;
    m_ingredientsAdditionOrder = new Vector<Ingredient>(m_numberOfIngredients);
    m_ingredientQuantities = new Vector<Integer>(m_numberOfIngredients);
  }

  /**
   * Deep Copy constructor
   */
  public Solution(Solution source) {
    m_numberOfIngredients = source.m_numberOfIngredients;
    m_maxIngredientQuantity = source.m_maxIngredientQuantity;
    m_ingredientsAdditionOrder = new Vector<Ingredient>(source.m_ingredientsAdditionOrder);
    m_ingredientQuantities = new Vector<Integer>(source.m_ingredientQuantities);
  }

  /* return true if the two solutions are the same
     */
  public boolean equals(Solution rhs) {
    return m_ingredientsAdditionOrder.equals(rhs.m_ingredientsAdditionOrder) &&
           m_ingredientQuantities.equals(rhs.m_ingredientQuantities);
  }

  public void setIngredient(int index, Ingredient ingredient, int quantity) {
    m_ingredientsAdditionOrder.set(index, ingredient);
    if (quantity < 1 || quantity > m_maxIngredientQuantity) throw new IllegalArgumentException();
    m_ingredientQuantities.set(index, quantity);
  }

  /**Get the ingredient which needs to be added as the nth addition
   *
   * @param additionOrderIndex 0 = first ingredient to be added, 1 = second ingredient, etc
   */
  public Ingredient getIngredient(int additionOrderIndex) {
    return m_ingredientsAdditionOrder.get(additionOrderIndex);
  }

  /**Returns the position of the nominated ingredient, or -1 if it doesn't exist
   *
   * @param ingredientToFind
   * @return position of the ingredient: 0 = added first, 1 = added second, etc.  -1 = not in Solution
   */
  public int findIngredientPosition(Ingredient ingredientToFind) {

    for (int i = 0; i < m_numberOfIngredients; ++i) {
      if (m_ingredientsAdditionOrder.get(i) == ingredientToFind) return i;
    }
    return -1;
  }

//  /**
//   * Find the position of the first matching ingredient
//   * @param descriptor descriptor to match
//   * @return position of the first matching ingredient 0 = added first, 1 = added second, etc.  -1 = not in Solution
//   */
//  public int findFirstMatchingIngredientPosition(Descriptor descriptor) {
//    int retval = 0;
//    for (Ingredient ingredient : m_ingredientsAdditionOrder) {
//      if (ingredient.hasDescriptor(descriptor)) return retval;
//      ++retval;
//    }
//    return -1;
//  }
//
//  /**
//   * Find the position of the last matching ingredient
//   * @param descriptor descriptor to match
//   * @return position of the last matching ingredient 0 = added first, 1 = added second, etc.  -1 = not in Solution
//   */
//  public int findLastMatchingIngredientPosition(Descriptor descriptor) {
//    int retval = m_ingredientsAdditionOrder.size();
//    while (retval > 0) {
//      if (m_ingredientsAdditionOrder.get(retval).hasDescriptor(descriptor)) return retval;
//      --retval;
//    }
//    return -1;
//  }

  /**
   * Find the position of the first ingredient which matches the clue information
   * @param cii the ClueIngredientInformation that the ingredient must match
   * @return position of the first matching ingredient 0 = added first, 1 = added second, etc.  -1 = not in Solution
   */
  public int findFirstMatchingIngredientPosition(ClueIngredientInformation cii) {
    int retval = 0;
    for (Ingredient ingredient : m_ingredientsAdditionOrder) {
      if (cii.matches(ingredient)) return retval;
      ++retval;
    }
    return -1;
  }

  /**
   * Find the position of the last ingredient which matches the clue information
   * @param cii the ClueIngredientInformation that the ingredient must match
   * @return position of the last matching ingredient 0 = added first, 1 = added second, etc.  -1 = not in Solution
   */
  public int findLastMatchingIngredientPosition(ClueIngredientInformation cii) {
    int retval = m_ingredientsAdditionOrder.size();
    while (retval > 0) {
      if (cii.matches(m_ingredientsAdditionOrder.get(retval))) return retval;
      --retval;
    }
    return -1;
  }

  /**
   * Given the position of a matching ingredient, find the position of the next ingredient which matches the clue information
   * @param cii the ClueIngredientInformation that the ingredient must match
   * @return position of the next matching ingredient 0 = added first, 1 = added second, etc.  -1 = no more matches in Solution
   */
  public int findNextMatchingIngredientPosition(ClueIngredientInformation cii, int previousPositionFound) {
    if (!cii.matches(m_ingredientsAdditionOrder.get(previousPositionFound))) {
      throw new IllegalArgumentException("previousPositionFound incorrect");
    }
    int retval = previousPositionFound+1;
    while (retval < m_ingredientsAdditionOrder.size()) {
      if (cii.matches(m_ingredientsAdditionOrder.get(retval))) return retval;
      ++retval;
    }
    return -1;
  }

  public int getIngredientsCount() { return m_numberOfIngredients;}

  public void addNextIngredient(Ingredient ingredient, int quantity) {
    m_ingredientsAdditionOrder.add(ingredient);
    if (quantity < 1 || quantity > m_maxIngredientQuantity) throw new IllegalArgumentException();
    m_ingredientQuantities.add(quantity);
  }

  /* Get the first combination of ingredients, for use with nextUniqueCombinationOfIngredients to iterate through the
     entire solution space
   */
  public static Solution getFirstCombinationOfIngredients(int numberOfIngredients, int maxIngredientQuantity) {
    Solution retval = new Solution(numberOfIngredients, maxIngredientQuantity);
    Ingredient ingredient = Ingredient.getFirst();
    for (int i = 1; i <= numberOfIngredients; ++i) {
      retval.addNextIngredient(ingredient, 1);
      ingredient = Ingredient.getNext(ingredient);
    }
    return retval;
  }

  /**Get a random solution with unique ingredients
   *
   * @param numberOfIngredients
   * @param maxIngredientQuantity
   * @return the random solution
   */
  public static Solution getRandomSolution(int numberOfIngredients, int maxIngredientQuantity) {
    Solution retval = new Solution(numberOfIngredients, maxIngredientQuantity);
    for (int i = 0; i < numberOfIngredients; ++i) {
      retval.addNextIngredient(Ingredient.getRandom(), s_random.nextInt(maxIngredientQuantity)+1);
    }
    if (Ingredient.hasDuplicates(retval.m_ingredientsAdditionOrder)) {
      boolean success = retval.nextUniqueCombinationOfIngredients();
      if (!success) retval = getFirstCombinationOfIngredients(numberOfIngredients, maxIngredientQuantity);
    }
    return retval;
  }

  /**
   * Return the number of ingredients which match the given descriptor
   * @param descriptor
   * @return the number of ingredients which have a matching descriptor
   */
  public int getCountOfIngredientsWhichMatch(Descriptor descriptor) {
    int retval = 0;
    for (Ingredient ingredient : m_ingredientsAdditionOrder) {
      if (ingredient.hasDescriptor(descriptor)) ++retval;
    }
    return retval;
  }


  public static void setSeedForTesting(int seed) {s_random = new Random(seed);}

  @Override
  public String toString() {
    StringBuilder retval = new StringBuilder();
    String separator = "";
    for (int i = 0; i < m_ingredientsAdditionOrder.size(); ++i) {
      retval.append(separator);
      retval.append(m_ingredientsAdditionOrder.get(i).toString());
      retval.append("(");
      retval.append(m_ingredientQuantities.get(i));
      retval.append(")");
      separator = ", ";
    }
    return retval.toString();
  }

  /* Iterate to the next combination of ingredients (ignores quantities)
   *  - used during generation
   *  - will skip over combinations where one ingredient is added multiple times
   *
   * @return false if there are no further combinations (we've reached the end)
   */
  public boolean nextUniqueCombinationOfIngredients() {

    do {
      boolean success = nextCombinationDuplicatesAllowed();
      if (!success) return false;
    } while (Ingredient.hasDuplicates(m_ingredientsAdditionOrder));
    return true;
  }

  public boolean nextCombinationDuplicatesAllowed() {
    if (m_ingredientsAdditionOrder.size() != m_numberOfIngredients) throw new IllegalStateException();
    if (m_ingredientsAdditionOrder.size() != m_numberOfIngredients) throw new IllegalStateException();

    int i;
    for (i = m_ingredientsAdditionOrder.size() -1 ; i >=0 ; --i) {
      if (m_ingredientsAdditionOrder.get(i) != Ingredient.getLast()) {
        m_ingredientsAdditionOrder.set(i, Ingredient.getNext(m_ingredientsAdditionOrder.get(i)));
        return true;
      }
      m_ingredientsAdditionOrder.set(i, Ingredient.getNext(m_ingredientsAdditionOrder.get(i)));
    }
    return false;
  }

  private int m_numberOfIngredients = 3;
  private int m_maxIngredientQuantity = 2;

  private Vector<Ingredient> m_ingredientsAdditionOrder;
  private Vector<Integer> m_ingredientQuantities;
  private static Random s_random = new Random();
}
