package com.tgg;

import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * Created by TGG on 31/10/2025.
 */
public enum Ingredient {
  WHITE_MUSHROOM("White Mushroom"),
  RED_MUSHROOM("Red Mushroom"),
  BROWN_MUSHROOM("Brown Mushroom"),
  HONEY("Honey"),
  DRAGONFRUIT("Dragonfruit");

   public static Ingredient getNext(Ingredient ingredient) {
    int index = ingredient.ordinal();
    int nextIndex = index + 1;
    nextIndex %= Ingredient.values().length;
    return Ingredient.values()[nextIndex];
  }

  public static Ingredient getFirst() {
    return Ingredient.values()[0];
  }
  public static Ingredient getLast() {
    return Ingredient.values()[Ingredient.values().length-1];
  }

  @Override
  public String toString() {
    return m_readableName;
  }

  public static void setSeedForTesting(int seed) {s_random = new Random(seed);}

  public static Ingredient getRandom() {
    return Ingredient.values()[s_random.nextInt(Ingredient.values().length)];
  }

  /*
    Check the vector of ingredients to see if any ingredient appears more than once
    return: true if duplicates found
   */
  public static boolean hasDuplicates(Vector<Ingredient> ingredients) {
    boolean[] foundValues = new boolean[Ingredient.values().length];
    for (Ingredient ingredient : ingredients) {
      if (foundValues[ingredient.ordinal()]) return true;
      foundValues[ingredient.ordinal()] = true;
    }
    return false;
  }

  private Ingredient(String readableName) {
    m_readableName = readableName;
  }

  private String m_readableName;
  private static Random s_random = new Random();
}
