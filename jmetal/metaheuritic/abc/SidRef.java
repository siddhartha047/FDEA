/** To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.metaheuritic.abc;

/**
 *
 * @author andy
 */

import java.util.ArrayList;
import java.util.Iterator;

import org.omg.CORBA.UnionMember;

import jmetal.core.*;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.*;
import jmetal.util.comparators.CrowdingDistanceComparator;
import jmetal.util.comparators.FitnessComparator;
import jmetal.util.comparators.ObjectiveComparator;

/** 
 *   Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class SidRef extends Algorithm {
	
	

	public SidRef(Problem problem) {
		super (problem) ;
	} 
	
	public void SimpleExperiment(){
				
		
    	SolutionSet parent=new SolutionSet(6);    	    	
    	StaticSolutionSet.MakeTwoObjectiveSolution(parent);
    	
    	SolutionSet offspring=new SolutionSet(6);    	    	
    	StaticSolutionSet.MakeTwoObjectiveSolutionChild(offspring);
    	
    	
    	SidRefAlgo refPointAlgo=new SidRefAlgo();    	
    	
    	ArrayList<ReferencePointSettings> refSettings= new ArrayList<ReferencePointSettings>();
		
		
		refSettings.add(new ReferencePointSettings(2,10, 1.00, false));
    	    	
    	refPointAlgo.takeSolution(parent, offspring, 6, refSettings,6);
    	
    	//parent.printSolutionSet();
    	//parent.printFuzzySolutionSet();
    	
    	//parent.printSolutionSet();
    
	}
	


	public SolutionSet execute() throws JMException, ClassNotFoundException {
		
		double lifeTime = 10.00;
		double decrement  = 0.00;
		
		
		//SimpleExperiment();
		//System.exit(0);
		
		
		int populationSize;
		int maxEvaluations;
		int evaluations;

		QualityIndicator indicators; // QualityIndicator object
		int requiredEvaluations; // Use in the example of use of the
		// indicators object (see below)

		SolutionSet population;
		SolutionSet offspringPopulation;
		SolutionSet union;
		SolutionSet parent;
		

		Operator mutationOperator;
		Operator crossoverOperator;
		Operator selectionOperator;
	
		//Read the parameters
		populationSize = ((Integer) getInputParameter("populationSize")).intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
		indicators = (QualityIndicator) getInputParameter("indicators");

		//Initialize the variables
		population = new SolutionSet(populationSize);

		evaluations = 0;
		requiredEvaluations = 0;

		//Read the operators
		mutationOperator = operators_.get("mutation");
		crossoverOperator = operators_.get("crossover");
		selectionOperator = operators_.get("selection");

		// Create the initial solutionSet
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);
			//forpolar:
			newSolution.lifeTime = lifeTime ;
			//
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			population.add(newSolution);
		} //for       
		
		SidRefAlgo refPointAlgo=new SidRefAlgo();
		
		int GenerationNo=0;
		int maxGenerationNo=maxEvaluations/populationSize;
		
		// Generations 
		int CombinedUsedTimes=0;
		
		while (evaluations < maxEvaluations) {
			
			// Create the offSpring solutionSet      
			offspringPopulation = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];
			for (int i = 0; i < (populationSize / 2); i++) {
				if (evaluations < maxEvaluations) {
					//obtain parents
					parents[0] = (Solution) selectionOperator.execute(population);
					parents[1] = (Solution) selectionOperator.execute(population);
								
					Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
					mutationOperator.execute(offSpring[0]);
					mutationOperator.execute(offSpring[1]);
					problem_.evaluate(offSpring[0]);
					problem_.evaluateConstraints(offSpring[0]);
					problem_.evaluate(offSpring[1]);
					problem_.evaluateConstraints(offSpring[1]);
					
					
					
					offSpring[0].lifeTime = lifeTime;
					offSpring[1].lifeTime = lifeTime;
					
					parents[0].firstSweep = true;
					parents[1].firstSweep = true;
					offSpring[0].firstSweep = true;
					offSpring[1].firstSweep = true;
					
					
					//added this two line
					offSpring[0].setFitness(0);
					offSpring[1].setFitness(0);
				
					offSpring[0].isParent=false;
					offSpring[1].isParent=false;
				
					
					offspringPopulation.add(offSpring[0]);
					offspringPopulation.add(offSpring[1]);
					evaluations += 2;
				} // if                            
			} // for

			for(int i=0;i<population.size();i++){
				population.get(i).setFitness(0);
				population.get(i).lifeTime = population.get(i).lifeTime - decrement;
				population.get(i).isParent=true;
			}
			
			/////////////////////////			
			union = ((SolutionSet) population).union(offspringPopulation);
			
			
			Ranking ranking = new Ranking(union);
			
			int remain = populationSize;
			int index = 0;
			
			SolutionSet front = null;
			population.clear();

			front = ranking.getSubfront(index);

			while ((remain > 0) && (remain >= front.size())) {
				 
				for (int k = 0; k < front.size(); k++) {
					population.add(front.get(k));					
				} 				
				
				remain = remain - front.size();

				// Obtain the next front
				index++;
				if (remain > 0) {
					front = ranking.getSubfront(index);
				}
			}

			// Remain is less than front(index).size, insert only the best one
			if (remain > 0) { // front contains individuals to insert
				
				int maxRefPoint = 300;
				
				
				int numberOfObjectives= front.get(0).numberOfObjectives();
				
				ArrayList<ReferencePointSettings> refSettings= new ArrayList<ReferencePointSettings>();
				
				if(numberOfObjectives == 10){
					refSettings.add(new ReferencePointSettings(numberOfObjectives, 5, 1.00, false));
				}
				else if(numberOfObjectives == 13){
					refSettings.add(new ReferencePointSettings(numberOfObjectives, 5, 1.00, false));
				}
				else if(numberOfObjectives == 19){
					refSettings.add(new ReferencePointSettings(numberOfObjectives, 5, 1.00, false));	
				}
				else{
					System.out.println("not defined yet");
					System.exit(0);
				}
				
				refPointAlgo.takeSolution(front,population, remain, refSettings,maxRefPoint);
				
				if(population.size()!=populationSize){
					System.out.println("Failure");
					System.exit(0);					
				}

				
				
			}

			
			
			
			
			
			////////////////////////
			
			
			
			
					
			if (indicators != null){
				double GD=indicators.getGD(population);
		        double IGD=indicators.getIGD(population);
		        double Spread=indicators.getSpread(population);
		        double Epsilon=indicators.getEpsilon(population);
		        
		        System.out.println(GD+"\t,\t"+Spread+"\t,\t"+Epsilon);
			}
			
			System.out.println(GenerationNo);
			
			String path="E:\\Thesis lab experiment Documents\\sidCleanJmetal\\jmetal\\abcGenerations\\InspectionFitness\\generation"+GenerationNo;			
			population.printObjectivesToFile(path,true );
			
			GenerationNo++;
			
			
		} // while

		// Return as output parameter the required evaluations
		setOutputParameter("evaluations", requiredEvaluations);

		// Return the first non-dominated front
		Ranking ranking = new Ranking(population);
		return ranking.getSubfront(0);

	}


	private SolutionSet generate_initpop(int populationSize) {
		// TODO Auto-generated method stub
		
		return null;
	} 
}