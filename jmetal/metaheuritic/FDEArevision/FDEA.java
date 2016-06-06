/** To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.metaheuritic.FDEArevision;

/**
 *
 * @author Sid
 */

import java.util.ArrayList;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/** 
 *   Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class FDEA extends Algorithm {
		

	public FDEA(Problem problem) {
		super (problem) ;
	} 
	
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		
		double lifeTime = 10.00;
		double decrement  = 0.00;
		
		int populationSize;
		int maxEvaluations;
		int evaluations;

		QualityIndicator indicators; // QualityIndicator object
		int requiredEvaluations; // Use in the example of use of the
		// indicators object (see below)

		SolutionSet population;
		SolutionSet offspringPopulation;
		SolutionSet union;

		Operator mutationOperator;
		Operator crossoverOperator;
		Operator selectionOperator;

		//Read the parameters
		populationSize = ((Integer) getInputParameter("populationSize")).intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
		indicators = (QualityIndicator) getInputParameter("indicators");

		//Initialize the variables
		population = new SolutionSet(populationSize);
		//population = generate_initpop(populationSize);
		
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
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			population.add(newSolution);
		} //for       
		
		//population.printSolutionSet();
		//System.exit(0);
		
		ClusterMinMaxSampling refPointAlgo=new ClusterMinMaxSampling();
		MemParamEstimationSigMoid refMembershipFunction=new MemParamEstimationSigMoid();
		

		int GenerationNo=0;
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
					
					parents[0].lifeTime = parents[0].lifeTime - decrement;
					parents[1].lifeTime = parents[1].lifeTime - decrement;
					offSpring[0].lifeTime = lifeTime;
					offSpring[1].lifeTime = lifeTime;
					
					parents[0].firstSweep = true;
					parents[1].firstSweep = true;
					offSpring[0].firstSweep = true;
					offSpring[1].firstSweep = true;
					
					
					//added this two line
					offSpring[0].setFitness(0);
					offSpring[1].setFitness(0);
					
					offspringPopulation.add(offSpring[0]);
					offspringPopulation.add(offSpring[1]);
					evaluations += 2;
				} // if                            
			} // for

			for(int i=0;i<population.size();i++){
				population.get(i).setFitness(0);
			}
			
			// Create the solutionSet union of solutionSet and offSpring
			union = ((SolutionSet) population).union(offspringPopulation);
			
			population.clear();
			
			
			int numberOfObjectives= union.get(0).numberOfObjectives();
			
			ArrayList<ReferencePointSettings> refSettings= new ArrayList<ReferencePointSettings>();
			
			if(numberOfObjectives == 2){				
				refSettings.add(new ReferencePointSettings(numberOfObjectives, 2000, 1.00, false));
			}
			else if(numberOfObjectives == 3){				
				refSettings.add(new ReferencePointSettings(numberOfObjectives, 50, 1.00, false));
			}			
			else if(numberOfObjectives == 5){			
				refSettings.add(new ReferencePointSettings(numberOfObjectives, 25, 1.00, false));
			}			
			else if(numberOfObjectives == 7){
				refSettings.add(new ReferencePointSettings(numberOfObjectives, 13, 1.00, false));			
			}
			else if(numberOfObjectives == 10){				
				refSettings.add(new ReferencePointSettings(numberOfObjectives, 10, 1.00, false));			
			}			
			else if(numberOfObjectives == 12){							
				refSettings.add(new ReferencePointSettings(numberOfObjectives,7, 1.00, false));							
			}			
			else if(numberOfObjectives == 15){
				refSettings.add(new ReferencePointSettings(numberOfObjectives, 7, 1.00, false));							
			}			
			else if(numberOfObjectives == 20){
				refSettings.add(new ReferencePointSettings(numberOfObjectives, 4, 1.00, false));							
			}								
			else{
				System.out.println("Dimension Settings Not Specified");
				System.exit(0);
			}
								
			refPointAlgo.takeSolution(union, population, populationSize, refSettings, refMembershipFunction);

			if(population.size()!=populationSize){
				System.out.println("Failure");
				System.exit(0);					
			}

			
			if (indicators != null){
				double GD=indicators.getGD(population);
		        double IGD=indicators.getIGD(population);
		        double Spread=indicators.getSpread(population);
		        double Epsilon=indicators.getEpsilon(population);		        
		        System.out.println(GD+"\t,\t"+IGD+"\t,\t"+Spread+"\t,\t"+Epsilon);		        		       
			}
			
			System.out.println(GenerationNo);			
			//String path=SidPolarMainDTLZ.currentPath+Integer.toString(GenerationNo);
			//String path=SidPolarMain.currentPath+Integer.toString(GenerationNo);
			//System.out.println(path);			
			//population.printObjectivesToFile(path);
			
			
			
			GenerationNo++;
			
			
		} // while

		// Return as output parameter the required evaluations
		setOutputParameter("evaluations", requiredEvaluations);

		// Return the first non-dominated front
		//Ranking ranking = new Ranking(population);
		//return ranking.getSubfront(0);
		return population;

	}


	private SolutionSet generate_initpop(int populationSize) {
		// TODO Auto-generated method stub
		
		return null;
	} 
}