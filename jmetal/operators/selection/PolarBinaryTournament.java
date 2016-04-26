//  BinaryTournament.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.operators.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuritic.abc.FuzzyFitnessComparator;
import jmetal.metaheuritic.abc.InvertFitnessComparator;
import jmetal.metaheuritic.abc.SidModifiedDominanceComparator;
import jmetal.util.comparators.*;
import jmetal.util.PseudoRandom;

/**
 * This class implements an binary tournament selection operator
 */
public class PolarBinaryTournament extends Selection {

  /**
   * Stores the <code>Comparator</code> used to compare two
   * solutions
   */
  private Comparator dominance_comparator_;
  
  
  

  /**
   * Constructor
   * Creates a new Binary tournament operator using a BinaryTournamentComparator
   */
  
  private int a_[];
  
  /**
   *  index_ stores the actual index for selection
   */
  private int index_ = 0;
  
  
  public PolarBinaryTournament(HashMap<String, Object> parameters){
  	super(parameters) ;
  	
  	if ((parameters != null) && (parameters.get("comparator") != null))
  	{
  		dominance_comparator_ = (Comparator) parameters.get("comparator") ;
  	}
  		
  	else
  	{
      //comparator_ = new BinaryTournamentComparator();
  		System.out.println("combined comparator");
  		dominance_comparator_ = new DominanceComparator();  	
  		
  	}
  		
  } // BinaryTournament
  
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet
  * @return the selected solution
  */
  public Object execute(Object object){
	    /**
	     * this is normaly birary tournament
	    */ 
	  
	  	/*
		SolutionSet solutionSet = (SolutionSet) object;
		Solution solution1, solution2;
		
		
		
		
		
		solution1 = solutionSet.get(PseudoRandom.randInt(0,
				solutionSet.size() - 1));
		solution2 = solutionSet.get(PseudoRandom.randInt(0,
				solutionSet.size() - 1));
		
		
		int flag = dominance_comparator_.compare(solution1, solution2);
		if (flag == 1){
		      return solution1;
		}
		else if (flag == -1){
		      return solution2;
		}
		else{
			if (PseudoRandom.randDouble() < 0.5)
				return solution1;
			else
				return solution2;
		}
		
		
	  */
	   
	    SolutionSet solutionSet = (SolutionSet) object;
		Solution solution1, solution2;
		
		
		
		
		
		solution1 = solutionSet.get(PseudoRandom.randInt(0,
				solutionSet.size() - 1));
		solution2 = solutionSet.get(PseudoRandom.randInt(0,
				solutionSet.size() - 1));
		
		
		int flag = dominance_comparator_.compare(solution1, solution2);
		if (flag == 1){
		      return solution1;
		}
		else if (flag == -1){
		      return solution2;
		}
		else{
			if (PseudoRandom.randDouble() < 0.5)
				return solution1;
			else
				return solution2;
		}
		
		
	  
	  
		
  } // execute
} // BinaryTournament
