package com.tgg;

import java.util.*;


/**
 * Created by TGG on 31/10/2025.
 */
public enum Ingredient {
  WHITE_BERRY("White Berry", "White Berries", Set.of(Descriptor.BERRY, Descriptor.WHITE)),
  RED_BERRY("Red Berry", "Red Berries", Set.of(Descriptor.BERRY, Descriptor.RED)),
  BROWN_BERRY("Brown Berry", "Brown Berries", Set.of(Descriptor.BERRY, Descriptor.BROWN)),
  YELLOW_BERRY("Yellow Berry", "Yellow Berries", Set.of(Descriptor.BERRY, Descriptor.YELLOW)),
  GREEN_BERRY("Green Berry", "Green Berries", Set.of(Descriptor.BERRY, Descriptor.GREEN)),
  BLUE_BERRY("Blue Berry", "Blue Berries", Set.of(Descriptor.BERRY, Descriptor.BLUE)),
  WHITE_MUSHROOM("White Mushroom", "White Mushrooms", Set.of(Descriptor.MUSHROOM, Descriptor.WHITE)),
  RED_MUSHROOM("Red Mushroom", "Red Mushrooms", Set.of(Descriptor.MUSHROOM, Descriptor.RED)),
  BROWN_MUSHROOM("Brown Mushroom", "Brown Mushrooms", Set.of(Descriptor.MUSHROOM, Descriptor.BROWN)),
  GARLIC("Clove of Garlic", "Cloves of Garlic", Set.of(Descriptor.WHITE)),
  HONEY("Jar of Honey", "Jars of Honey", Set.of(Descriptor.BOTTLED, Descriptor.YELLOW)),
  DRAGONFRUIT("Dragon Fruit", "Dragon Fruit", Set.of( Descriptor.FRUIT, Descriptor.PINK)),
  RED_FLOWER("Red Flower", "Red Flowers", Set.of(Descriptor.FLOWER, Descriptor.RED)),
  BLUE_FLOWER("Blue Flower", "Red Flowers", Set.of(Descriptor.FLOWER, Descriptor.BLUE)),
  YELLOW_FLOWER("Yellow Flower", "Red Flowers", Set.of(Descriptor.FLOWER, Descriptor.YELLOW)),
  WHITE_FLOWER("White Flower", "Red Flowers", Set.of(Descriptor.FLOWER, Descriptor.WHITE)),
  POMEGRANATE("Pomegranate", "Pomegranates", Set.of(Descriptor.FRUIT, Descriptor.RED)),
  RED_ONION("Red Onion", "Red Onions", Set.of(Descriptor.ONION, Descriptor.RED)),
  BROWN_ONION("Brown Onion", "Brown Onions", Set.of(Descriptor.ONION, Descriptor.BROWN)),
  WHITE_ONION("White Onion", "White Onions", Set.of(Descriptor.ONION, Descriptor.WHITE)),
  CINNAMON("Jar of Cinnamon", "Jars of Cinnamon", Set.of(Descriptor.BOTTLED, Descriptor.BROWN)),
  SALT("Jar of Salt", "Jars of Salt", Set.of(Descriptor.BOTTLED, Descriptor.WHITE)),
  SPINACH("Bunch of Spinach", "Bunches of Spinach", Set.of(Descriptor.GREEN)),
  ROCKET("Bunch of Rocket", "Bunches of Rocket", Set.of(Descriptor.GREEN)),
  PARSELEY("Bunch of Parseley", "Bunches of Parseley", Set.of(Descriptor.GREEN)),
  PICKLED_ONIONS("Jar of Pickled Onions", "Jars of Pickled Onions", Set.of(Descriptor.BOTTLED, Descriptor.WHITE)),
  GHERKINS("Jar of Gherkins", "Jars of Gherkins", Set.of(Descriptor.BOTTLED, Descriptor.GREEN)),
  PICKLED_CAPSICUM("Jar of Preserved Capsicum", "Jars of Preserved Capsicum", Set.of(Descriptor.BOTTLED, Descriptor.RED));

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

  /**
   * Returns true if this ingredient has the given descriptor
   * @param descriptorToMatch the descriptor to check for
   * @return true if it has the descriptor, false otherwise
   */
  public boolean hasDescriptor(Descriptor descriptorToMatch) {
     return m_descriptors.contains(descriptorToMatch);
  }

  public Descriptor getRandomDescriptor() {
    int i = s_random.nextInt(m_descriptors.size());
    for (Descriptor descriptor : m_descriptors) {
      if (--i < 0) return descriptor;
    }
    throw new AssertionError("Did something wrong...");
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

  private Ingredient(String readableName, String readableNamePlural, Set<Descriptor> descriptors) {
    m_readableName = readableName;
    m_readableNamePlural = readableNamePlural;
    if (Descriptor.checkForMutexViolations(descriptors)) throw new AssertionError("Invalid Mutex set");
    m_descriptors = descriptors;
  }

  private String m_readableName;
  private String m_readableNamePlural;
  private Set<Descriptor> m_descriptors;
  private static Random s_random = new Random();
}
