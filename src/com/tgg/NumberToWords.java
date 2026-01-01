package com.tgg;

/**
 * From https://www.javathinking.com/blog/convert-number-to-words-in-java-using-switch-case/ slightly tweaked
 */
public class NumberToWords {

  // Method to convert a number to words
  public static String convertToWords(int number) {
    if (number == 0) {
      return "zero";
    }

    String prefix = "";

    if (number < 0) {
      number = -number;
      prefix = "negative";
    }

    String current = "";
    int place = 0;

    do {
      int n = number % 1000;
      if (n != 0) {
        String s = convertLessThanOneHundred(n / 100);
        if (!s.isEmpty()) {
          current = s + " hundred " + current;
        }
        s = convertLessThanOneHundred(n % 100);
        if (!s.isEmpty()) {
          current = s + " and " + current;
        }
        switch (place) {
          case 1:
            current = "thousand " + current;
            break;
          case 2:
            current = "million " + current;
            break;
          case 3:
            current = "billion " + current;
            break;
          // You can add more cases for larger numbers if needed
        }
      }
      number /= 1000;
      place++;
    } while (number > 0);

    return (prefix + " " + current).trim();
  }

  /**
   * Convert a number to a human-readable ordinal (first, second, third etc)
   * @param i
   * @return the ordinal name (first, second, third etc)
   */
  public static String convertToOrdinal(int i) {
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

  // Method to convert single - digit or two - digit numbers to words
  private static String convertLessThanOneHundred(int number) {
    String[] tensNames = {
            "", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };
    String[] numNames = {
            "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    };

    if (number < 20) {
      return numNames[number];
    }

    int tens = number / 10;
    int units = number % 10;

    if (units == 0) {
      return tensNames[tens];
    } else {
      return tensNames[tens] + " " + numNames[units];
    }
  }

}
