package com.tgg;

import java.util.Random;
import java.util.Vector;

/**
 * Created by TGG on 1/11/2025.
 */
public class ClueFactory {

  public static Clue generateClue(Solution solution, boolean mustBeSatisfied) {
    ClueType clueType = ClueType.getRandomClueType();
    switch (clueType) {
      case CLUE_AMOUNT:
      case CLUE_POSITION:
        return CluePosition.generateRandomClue(solution, mustBeSatisfied);
      case CLUE_CATEGORY_AMOUNT:
      default:
        throw new IllegalStateException();
    }
  }

  private enum ClueType {
    CLUE_POSITION(1),
    CLUE_AMOUNT(0),
    CLUE_CATEGORY_AMOUNT(0);

    public static ClueFactory.ClueType getRandomClueType() {
      setupWeightsLookupTable();
      Random random = new Random();
      int j = random.nextInt(m_weightstable.size());
      return m_weightstable.get(j);
    }

    private static void setupWeightsLookupTable() {
      if (m_weightstable != null) return;
      m_weightstable = new Vector<ClueFactory.ClueType>();
      for (ClueFactory.ClueType clueType : ClueFactory.ClueType.values()) {
        for (int k = clueType.m_weight - 1; k >= 0; --k) {
          m_weightstable.add(clueType);
        }
      }
    }

    private ClueType(int weight) { m_weight = weight; }
    private int m_weight;
    private static Vector<ClueFactory.ClueType> m_weightstable;
  }


}
