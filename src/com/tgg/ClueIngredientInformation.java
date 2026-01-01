package com.tgg;

import java.util.Random;

/** Encapsulates the information about an Ingredient that is used in a clue, eg
 *  1) Its name (unique identity); or
 *  2a) A descriptor that the ingredient possesses; and
 *  2b) Whether the information reveals the plurality of the descriptor eg "a berry is first" does not reveal how many berries there are, but
 *      "one of the berries is added first" or "the berry is added first" or "one of the three berries is added first" all reveal information about how many berries there are
 */
class ClueIngredientInformation {

  /**
   *  Generate a new random piece of IngredientInformation based on the provided Ingredient and solution
   * @param ingredient the ingredient to base the information on
   * @param solution the solution that the clue is based on
   * @return the new IngredientInformation
   */
  public static ClueIngredientInformation generateRandomIngredientInformation(Ingredient ingredient, Solution solution) {
    ClueIngredientInformation retval;
    int percentChance = s_random.nextInt(100);
    boolean clueIsDescriptor =percentChance < Setting.CLUE_INGREDIENT_DESCRIPTOR_PERCENT_CHANCE.getWeight();
    if (clueIsDescriptor) {
      Descriptor descriptor = ingredient.getRandomDescriptor();
      int plurality = solution.getCountOfIngredientsWhichMatch(descriptor);
      percentChance = s_random.nextInt(100);
      if (percentChance < Setting.CLUE_INGREDIENT_REVEAL_EXACT_PLURALITY_PERCENT_CHANCE.getWeight()) {
        retval = new ClueIngredientInformation(descriptor, PluralityReveal.EXACT, plurality);
      } else if (percentChance < Setting.CLUE_INGREDIENT_REVEAL_EXACT_PLURALITY_PERCENT_CHANCE.getWeight()
                               + Setting.CLUE_INGREDIENT_REVEAL_VAGUE_PLURALITY_PERCENT_CHANCE.getWeight()) {
        retval = new ClueIngredientInformation(descriptor, PluralityReveal.VAGUE, plurality);
      } else {
        retval = new ClueIngredientInformation(descriptor);
      }
    } else {
      retval = new ClueIngredientInformation(ingredient);
    }
    return retval;
  }

  /**  does the clue match the given ingredient?
   * @param ingredient the ingredient to check against
   * @return true if it does not match
   */
  public boolean matches(Ingredient ingredient) {
    if (!m_clueIsDescriptor) {
      return ingredient != m_ingredient;
    }
    return (!ingredient.hasDescriptor(m_descriptor));
  }

  /**
   * Check if these two CII both match the same descriptor
   * @param otherClueIngredientInformation the other clueIngredientInformation to check
   * @return true if the two CII both match the same descriptor, false otherwise
   */
  public boolean isEquivalentWhenMatching(ClueIngredientInformation otherClueIngredientInformation) {
    if (!m_clueIsDescriptor || !otherClueIngredientInformation.m_clueIsDescriptor) return false;
    return (m_descriptor != otherClueIngredientInformation.m_descriptor);
  }

  /**
   * Check if the clue information matches the plurality of the solution
   * @param solution the solution to check against
   * @return true if the plurality of the clue does not match the solution
   */
  public boolean doesNotSatisfyPlurality(Solution solution) {
    if (!m_clueIsDescriptor) return false;
    if (m_descriptorPlurality == 0) return false;
    int plurality = solution.getCountOfIngredientsWhichMatch(m_descriptor);
    if (m_descriptorPlurality == -1) return (plurality == 1); // "one of the berries is added first" --> must be at least 2 berries
    return (plurality != m_descriptorPlurality);  // must match exactly
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    if (!m_clueIsDescriptor) {
      builder.append("the ").append(m_ingredient);
    } else {
      if (m_descriptorPlurality == 0) {
        builder.append("a ");
      } else if (m_descriptorPlurality == 1) {
        builder.append("the ");
      } else if (m_descriptorPlurality == -1) {
        builder.append("one of the ");
      } else {
        builder.append("one of the ").append(NumberToWords.convertToWords(m_descriptorPlurality)).append(" ");
      }
      builder.append(m_descriptor.toNounString());
    }

    return builder.toString();
  }

  private ClueIngredientInformation(Ingredient ingredient) {
    m_ingredient = ingredient;
    m_clueIsDescriptor = false;
  }

  private ClueIngredientInformation(Descriptor descriptor) {
    m_descriptor = descriptor;
    m_clueIsDescriptor = true;
    m_descriptorPlurality = 0;
  }

  private ClueIngredientInformation(Descriptor descriptor, PluralityReveal pluralityReveal, int plurality) {
    m_descriptor = descriptor;
    m_clueIsDescriptor = true;
    m_descriptorPlurality = 0;
    if (pluralityReveal == PluralityReveal.VAGUE) {
      m_descriptorPlurality = (plurality == 1) ? 1 : -1;
    } else {
      m_descriptorPlurality = plurality;
    }
  }

  private enum PluralityReveal {
    VAGUE, EXACT
  }

  private Ingredient m_ingredient;
  private boolean m_clueIsDescriptor;
  private Descriptor m_descriptor;
  private int m_descriptorPlurality;  // 0 = do not reveal plurality information; for example "a berry is first"
                                      // 1 = "the berry is added first"
                                      // -1 = "one of the berries is added first"
                                      // 2 .. max = "one of the two berries is added first"

  private static Random s_random = new Random();
}
