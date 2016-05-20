/* Copyright 2009-2014 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.problem.WFG.WFG;
import org.moeaframework.problem.WFG.WFG1;
import org.moeaframework.problem.WFG.WFG2;
import org.moeaframework.problem.WFG.WFG3;
import org.moeaframework.problem.WFG.WFG4;
import org.moeaframework.problem.WFG.WFG5;
import org.moeaframework.problem.WFG.WFG6;
import org.moeaframework.problem.WFG.WFG7;
import org.moeaframework.problem.WFG.WFG8;
import org.moeaframework.problem.WFG.WFG9;

/**
 * Demonstrates using an Executor to solve the UF1 test problem with NSGA-II,
 * one of the most widely-used multiobjective evolutionary algorithms.
 */
public class Example1 {

	public static void main(String[] args) {
		//configure and run this experiment
		/*NondominatedPopulation result = new Executor()
				.withProblem("UF1")
				.withAlgorithm("NSGAII")
				.withMaxEvaluations(10000)
				.distributeOnAllCores()
				.run();*/
		
		int []wfgno={2,3,4,5,6,7,8,9};
		int []dimArr={10,13,19};
		int k=18;
		int l=36;
		int M=dimArr[0];
		int populationSize=250;
    	int GenerationNo=200;
    	
		NondominatedPopulation result = new Executor()
		.withProblemClass(getClassName(wfgno[0]),k,l,M)
		.withAlgorithm("HypE")
		.withMaxEvaluations(GenerationNo*populationSize)
		.withProperty("populationSize", populationSize)
		.withProperty("seed", 1)
		.withProperty("tournament",2)
		.withProperty("bound", 1000)
		.withProperty("mating", 1)
		.withProperty("sbx.rate", 1)
		.withProperty("sbx.distributionIndex", 15.0)
		.withProperty("pm.rate", 1/(k+l))
		.withProperty("pm.distributionIndex", 20.0)		
		.withProperty("nrOfSamples",500*M)
		.distributeOnAllCores()
		.run();

//display the results
		
		//display the results
		for (Solution solution : result) {
			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				System.out.format("%.4f ",solution.getObjective(i));
						
			}
			System.out.println();
		}
		System.out.println(result.size());
		
		
	}
	public static  Class<? extends WFG> getClassName(int problemNo){
		if(problemNo==1)
				return new WFG1(0,0,0).getClass();
		else if(problemNo==2)
			return new WFG2(0,0,0).getClass();
		else if(problemNo==3)
			return new WFG3(0,0,0).getClass();
		else if(problemNo==4)
			return new WFG4(0,0,0).getClass();
		else if(problemNo==5)
			return new WFG5(0,0,0).getClass();
		else if(problemNo==6)
			return new WFG6(0,0,0).getClass();
		else if(problemNo==7)
			return new WFG7(0,0,0).getClass();
		else if(problemNo==8)
			return new WFG8(0,0,0).getClass();		
		else if(problemNo==9)
			return new WFG9(0,0,0).getClass();
		return null;
		
	}
}


