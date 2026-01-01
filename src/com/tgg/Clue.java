package com.tgg;

/**
 * Created by TGG on 31/10/2025.
 * A clue
 */
public interface Clue {
  /**
   * Check this clue against the solution.
   * @param solution
   * @return true if the solution does not satisfy this clue
   */
  public boolean hasConflicts(Solution solution);
}
