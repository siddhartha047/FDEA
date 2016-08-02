/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.metaheuritic.FDEAupdated;

/**
 *
 * @author Sid
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.read_settings;

public class FDEA_Main_Rectangle_Problem {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	public static String currentPath;
	public static int onAvg=2;

	/**
	 * @param args
	 *            Command line arguments.
	 * @throws JMException
	 * @throws IOException
	 * @throws SecurityException
	 *             Usage: three options -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName
	 *             paretoFrontFile
	 */

	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {
		

		//double []seedar={0.05,0.1,0.15,0.2,0.25,0.3,0.35,0.4,0.45,0.5,0.55,0.6,0.65,0.7,0.75,0.8,0.85,0.9,0.95,1};
		double[] seedar = {0.5};		
		 
		for(int avg=2;avg<=2;avg++){
		 
			Problem problem; // The problem to solve
			Algorithm algorithm; // The algorithm to use
			Operator crossover; // Crossover operator
			Operator mutation; // Mutation operator
			Operator selection; // Selection operator
	
			HashMap parameters; // Operator parameters
	
			QualityIndicator indicators; // Object to get quality indicators
	
			// Logger object and file to store log messages
			logger_ = Configuration.logger_;
	
			// double []seedar={.5};
	
			read_settings rs = new read_settings();
			indicators = null;
			read_settings.generation_path=read_settings.generation_path+Integer.toString(avg)+"\\";
			onAvg=avg;
			// settings			
	
			int M = 4;
			
			int populationSize = 150;
			int GenerationNo = 250;
	
		
			String problemName = "RecInstanceI";			
			String resultFile = "RecInstanceI_4.pf";									
						    	 			    	 
		    Object [] params={"Real",2};
		    problem = (new ProblemFactory()).getProblem(problemName,params);
		        			
			algorithm = new FDEA(problem);
						
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", populationSize);
			algorithm.setInputParameter("maxEvaluations", populationSize * GenerationNo);
	
			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 1.0);
			parameters.put("distributionIndex", 30.0);
			crossover = CrossoverFactory.getCrossoverOperator(
					"SBXCrossover", parameters);
	
			parameters = new HashMap();
			parameters.put("probability",
					1.0 / problem.getNumberOfVariables());
			parameters.put("distributionIndex", 20.0);
			mutation = MutationFactory.getMutationOperator(
					"PolynomialMutation", parameters);
	
			// Selection Operator
			parameters = null;
			// TO DO: done
			selection = SelectionFactory.getSelectionOperator(
					"BinaryTournament", parameters);
	
			// Add the operators to the algorithm
			algorithm.addOperator("crossover", crossover);
			algorithm.addOperator("mutation", mutation);
			algorithm.addOperator("selection", selection);
	
			// Add the indicator object to the algorithm
			algorithm.setInputParameter("indicators", indicators);
			algorithm.setInputParameter("generations", true); // populate
																// generations
																// in file
			// Execute the Algorithm
			// Result messages
			for (int i = 0; i < seedar.length; i++) {
				PseudoRandom.setseed(seedar[i]);
				
	
				// directory creation
	
				String seedFolder = read_settings.generation_path + "\\"
						+ String.valueOf(seedar[i]);
				File theDir = new File(seedFolder);
	
				// if the directory does not exist, create it
				if (!theDir.exists()) {
					System.out.println("creating directory: " + seedFolder);
					boolean result = theDir.mkdir();
					if (result) {
						System.out.println("DIR created");
					}
				}
	
				String resultPfName = seedFolder + "\\" + resultFile;
				currentPath = resultPfName;
	
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				long estimatedTime = System.currentTimeMillis() - initTime;
				fileHandler_ = new FileHandler("abc_main"
						+ Double.toString(seedar[i]) + ".log");
				logger_.addHandler(fileHandler_);
				logger_.info("Total execution time: " + estimatedTime
						+ "ms");
	
				logger_.info("Variables values have been writen to file VAR");
				population.printVariablesToFile(resultPfName+"var");
				
				logger_.info("Objectives values have been writen to file "
						+ seedFolder + "\\" + resultFile);
	
				population.printObjectivesToFile(resultPfName);				

			}
			
		}//newavg
	} // main		
} // NSGAII_main