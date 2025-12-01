package com.tgg;

import java.util.Random;
import java.util.Vector;

/**
 *    Descriptors: characteristics of an ingredient such as Red, White, Black, Fruit, Liquid, Plant, etc
 *    Descriptors are grouped into mutually exclusive sets (eg Colour, TypeOfObject, etc) and the type of descriptor:
 *      adjective (eg red), noun (eg berry)
 *
 * Created by TGG on 31/10/2025.
 */
public enum Descriptor {
  RED("red", MutexSet.COLOUR),
  BROWN("brown", MutexSet.COLOUR),
  YELLOW("yellow", MutexSet.COLOUR),
  WHITE("white", MutexSet.COLOUR),
  PINK("pink", MutexSet.COLOUR),
  GREEN("green", MutexSet.COLOUR),
  BLUE("blue", MutexSet.COLOUR),

  BERRY("berry", MutexSet.TYPEOFOBJECT),
  FLOWER("flower", MutexSet.TYPEOFOBJECT),
  MUSHROOM("mushroom", MutexSet.TYPEOFOBJECT),
  FRUIT("fruit", MutexSet.TYPEOFOBJECT),
  ONION("onion", MutexSet.TYPEOFOBJECT),
  BOTTLED("bottled ingredient", MutexSet.TYPEOFOBJECT);

  public enum MutexSet {
    COLOUR(MutexSetType.ADJECTIVE), TYPEOFOBJECT(MutexSetType.NOUN);

    public enum MutexSetType {
      ADJECTIVE, NOUN
    }

    private MutexSet(MutexSetType mutexSetType) {
      m_mutexsettype = mutexSetType;
    }
    private MutexSetType m_mutexsettype;
  }

  @Override
  public String toString() {
    return m_readableName;
  }

  public static void setSeedForTesting(int seed) {s_random = new Random(seed);}

  public static Descriptor getRandom() {
    return Descriptor.values()[s_random.nextInt(Descriptor.values().length)];
  }

    /*
    Check the vector of ingredients to count the number of ingredients which match the given descriptor
    return: true if duplicates found
   */
  public static int countMatchingDescriptors(Vector<Ingredient> ingredients, Descriptor descriptorToCount) {
    int retval = 0;
    for (Ingredient ingredient : ingredients) {
      if (ingredient.hasDescriptor(descriptorToCount)) ++retval;
    }
    return retval;
  }

//  /*
//    Check the vector of ingredients to see if any ingredient appears more than once
//    return: true if duplicates found
//   */
//  public static boolean hasDuplicates(Vector<Descriptor> ingredients) {
//    boolean[] foundValues = new boolean[Descriptor.values().length];
//    for (Descriptor ingredient : ingredients) {
//      if (foundValues[ingredient.ordinal()]) return true;
//      foundValues[ingredient.ordinal()] = true;
//    }
//    return false;
//  }

  private Descriptor(String readableName, MutexSet mutexSet) {
    m_readableName = readableName;
    m_mutexset = mutexSet;
  }

  private String m_readableName;
  private MutexSet m_mutexset;
  private static Random s_random = new Random();
}
