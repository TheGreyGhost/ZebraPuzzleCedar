package com.tgg;
import com.tgg.tests.Tester;
import org.junit.Test;

import java.lang.String;

/*
Generate a zebra puzzle for Alycia's game

Basic structure:
1) Up to 'N' ingredients needed to be added in order
2) The amount of each ingredient can range from 1 to 3, they are not mutually exclusive
3) There is a large pool of ingredients, each ingredient has two characteristics:
   a) A Name: Mushroom, Honey, Flower, Dragonfruit, etc.  All ingredients have a name
   b) Descriptors: one or more descriptors such as Red, White, Black, Fruit, Liquid, Plant, etc
   Descriptors are grouped into mutually exclusive sets (eg Colour, TypeOfObject, etc) and the type of descriptor:
     adjective (eg red), noun (eg berry)

There are various clues of different forms:
  1) positional (order of addition):
      '=': name or descriptor is position X
      '!=': name or descriptor is not position X
      'neighbor': name or descriptor is next to name or descriptor
      'ends': name or descriptor is either first or last
      'before': name or descriptor is before name or descriptor

  2) the number of ingredients in a particular category of descriptor
    '=': number of ingredient in category A equals number in category B
    '!=': number of ingredient in category A does not equal number in category B
    '<': number of ingredient in category A is less than number in category B

  3) the amount to add of a particular ingredient
    '=': amount of A equals amount of B
    '!=': amount of A != amount of B
    '<': amount of A < amount of B
    A and B can be either: position, ingredient, or category

  The algorithm for solving is
  1) Generate a target solution
  1) Randomly add a clue which is satisfied by the target solution.  Try to solve and keep iterating (adding clues) until a single unique solution exists
  2) Attempt to remove redundant clues

variables:
first ingredient ID
second ingredient ID
third ingredient ID

first ingredient quantity
second ingredient quantity
third ingredient quantity

Search strategy:
increment ingredient in order, testing against each clues to see if they're eliminated

Keep track of the one which hasn't been eliminated

Later: Randomise order of testing

class is Clue

The 4th ingredient is red
The 4th ingredient is a flower
The red ingredient is next to the blue ingredient
A/The red ingredient is next to a berry
There are x red ingredients
The 4th ingredient is in a jar




 */



public class Main {

    public static void main(String[] args) {
      boolean allTestsSucceeded;

      allTestsSucceeded = Tester.testSolution();
      allTestsSucceeded = allTestsSucceeded && Tester.testCluePosition();
      allTestsSucceeded = allTestsSucceeded && Tester.testPuzzle();
    }
}
