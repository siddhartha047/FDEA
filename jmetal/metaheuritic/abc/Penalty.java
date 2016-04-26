package jmetal.metaheuritic.abc;

import jmetal.core.SolutionSet;
import jmetal.util.comparators.ObjectiveComparator;

public class Penalty {
	
	public static void takeSolutionByPenalizeMethod(SolutionSet population,SolutionSet front,int remain,HowMuchBetterFitness better){
		
		better.Initialization(front);
		
		SolutionSet taken=new SolutionSet(remain);
	//	System.out.println(remain);
		int numberOfObjectives=front.get(0).numberOfObjectives();
		
		//take the best one
		
		
		
		for(int i=0;i<numberOfObjectives;i++){
			if(taken.size()<remain){
				front.sort(new ObjectiveComparator(i));		
				taken.add(front.get(0));
				front.remove(0);
			}
		}
		
		//continuously take and update 
		
		while(taken.size()<remain){
			//updateThePopulation
			better.updateThePopulation(taken,front);
			
		}
		for(int i=0;i<taken.size();i++){
			population.add(taken.get(i));
		}
		
		
		
	}

}
