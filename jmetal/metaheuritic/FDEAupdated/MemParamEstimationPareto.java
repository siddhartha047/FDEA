package jmetal.metaheuritic.FDEAupdated;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

public class MemParamEstimationPareto {
	
	public void CompareSolution(Solution mainSolution, Solution compareSolution) {
		double[] mainObjectives = mainSolution.get_objectives();
		double[] compareObjectives = compareSolution.get_objectives();
				
		double better=0;
		double equal=0;
		double worse=0;
		
		int objNo=mainObjectives.length;
		
		for (int i = 0; i < mainObjectives.length; i++) {

			double diff=mainObjectives[i]-compareObjectives[i];
			
			if(diff==0){
				equal++;
			}
			else if(diff<0){
				better++;				
			}
			else{
				worse++;
			}			
					
		}	
		
		double dominateBy=0;
		double dominatedBy=0;
		
		
		if(better+equal==objNo){
			dominateBy=1;
			dominatedBy=0;
		}
		else if(worse+equal==objNo){
			dominateBy=0;
			dominatedBy=1;
		}
		else{
			dominateBy=0.5;
			dominatedBy=0.5;
		}
		
		double mainfit=mainSolution.getFitness();
		mainfit+=dominateBy;		
		mainSolution.setFitness(mainfit);		
		double compareFit=compareSolution.getFitness();
		compareFit+=dominatedBy;
		compareSolution.setFitness(compareFit);
		
	}
	
	public void FitnessAssignment(SolutionSet solutionSet){
		
		for (int i = 0; i < solutionSet.size(); i++) {
			Solution solution = solutionSet.get(i);

			for (int j = i + 1; j < solutionSet.size(); j++) {
				if (i == j)continue;
								
				
				Solution comPareSolution = solutionSet.get(j);
				CompareSolution(solution, comPareSolution);

			}
		}
		
		
	}
}
