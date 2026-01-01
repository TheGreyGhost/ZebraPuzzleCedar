package com.tgg;

import java.util.Random;
import java.util.Vector;

/**
 * Created by TGG on 1/01/2026.
 */
public enum Setting {
  CLUE_TYPE_WEIGHT_IN_POSITION_X(1),
  CLUE_TYPE_WEIGHT_NOT_IN_POSITION_X(1),
  CLUE_TYPE_WEIGHT_IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B(1),
  CLUE_TYPE_WEIGHT_TWO_NEXT_TO_EACH_OTHER(0),   // subset of IMMEDIATELY_BEFORE_OR_AFTER_INGREDIENT_B
  CLUE_TYPE_WEIGHT_FIRST_OR_LAST(1),
  CLUE_TYPE_WEIGHT_BEFORE_INGREDIENT_B(1),
  CLUE_TYPE_WEIGHT_TWO_NOT_NEXT_TO_EACH_OTHER(0), // subset of BEFORE_INGREDIENT_B

  CLUE_INGREDIENT_DESCRIPTOR_PERCENT_CHANCE(50),

  // The following weights affect whether the information reveals the plurality of the descriptor
  // eg if EXACT_PERCENT is 20, VAGUE_PERCENT is 30, then:
  //  20% chance of "the berry is added first" or "one of the three berries is added first" as appropriate
  //  20% chance of "one of the berries is added first" ; if there's only one berry it says "the berry" as per EXACT
  //  50% chance of "a berry is first" - does not reveal how many berries there are
  CLUE_INGREDIENT_REVEAL_EXACT_PLURALITY_PERCENT_CHANCE(20),
  CLUE_INGREDIENT_REVEAL_VAGUE_PLURALITY_PERCENT_CHANCE(30),
  ;

  public int getWeight() {return m_weight;}
  private Setting(int weight) { m_weight = weight; }
  private int m_weight;
}
