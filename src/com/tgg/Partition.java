package com.tgg;

/**
 * Created by TGG on 30/10/2025.
 */
/*
 * Copyright (C) 2017 COSLING S.A.S.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.SetVar;

/**
 * Simple Choco Solver example involving set variables
 * @author Jean-Guillaume FAGES (cosling)
 * @version choco-solver-4.0.4
 */
public class Partition {

  public static void main(String[] args) {
    // Create the model
    Model model = new Model();

    // partitions a set "universe" into three disjoint sets x, y and z
    // declare a constant set variable (LB = UB = {1, 2, 3, 5, 7, 8})
    SetVar universe = model.setVar("universe", 1, 2, 3, 5, 7, 8);

    // x initial domain
    // integers that MAY belong to A solution
    int[] x_UB = new int[]{1, 3, 2, 8};
    // integers that MUST belong to EVERY solution
    int[] x_LB = new int[]{1};
    // create a set variable x
    SetVar x = model.setVar("x", x_LB, x_UB);
    // same for set variable y and z
    SetVar y = model.setVar("y", new int[]{}, new int[]{2, 6, 7});
    SetVar z = model.setVar("z", new int[]{2}, new int[]{2, 1, 3, 5, 7, 12});

    // partition constraint : union(x,y,z) = universe and all_disjoint(x,y,z)
    model.partition(new SetVar[]{x, y, z}, universe).post();

    // find one partition
    if(model.getSolver().solve()){
      System.out.println("Partition found!");
      System.out.println(universe.getValue()+" = "+x.getValue()+" + "+y.getValue()+" + "+z.getValue());
    }
  }
}