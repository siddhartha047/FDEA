/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.metaheuritic.abc;

/**
 *
 * @author andy
 */
import jmetal.core.*;
import jmetal.operators.crossover.*;
import jmetal.operators.mutation.*;
import jmetal.operators.selection.*;
import jmetal.problems.*;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.WFG.*;
import jmetal.problems.LZ09.*;

import jmetal.util.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.metaheuristics.gde3.GDE3;
import jmetal.metaheuristics.nsgaII.NSGAII;

import jmetal.qualityIndicator.QualityIndicator;

public class zhenan_mainDTLZ {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	public static String currentPath;

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

		// int []dimArr={2,4,7,10};
		// int []wfgno={2,3,4,5,6,7,8,9};
		// double []seedar={.1,.5,.9};

		int[] dimArr = { 25};
		int[] dtlzNo = { 1,2,3,4,7};
		//double[] seedar = { 0.5 };
		// double []seedar={0.1,0.3,0.5,0.7,0.9};

		double []seedar={0.05,0.1,0.15,0.2,0.25,0.3,0.35,0.4,0.45,0.5,0.55,0.6,0.65,0.7,0.75,0.8,0.85,0.9,0.95,1};
		// double []seedar={0.05,0.15,0.2,0.25,0.3,0.35,0.4,0.45};
		// double []seedar={0.55,0.6,0.65,0.7,0.75,0.8,0.85,0.95,1};

		// int []dimArr={4};

		for (int dimno = 0; dimno < dimArr.length; dimno++) {

			for (int wfgProblemNo = 0; wfgProblemNo < dtlzNo.length; wfgProblemNo++) {
				// /

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

				// settings

				int dim = dimArr[dimno];
				int problemNo = dtlzNo[wfgProblemNo];

				String problemName = "DTLZ";
				String getProblem = problemName + String.valueOf(problemNo);
				String refFileName = getProblem + "_" + String.valueOf(dim)
						+ ".pf";
				String resultFile = "zhenan" + getProblem + "_"
						+ String.valueOf(dim) + ".pf";
				String paretoFile = getProblem + "_" + String.valueOf(dim)
						+ "D.pf";

				System.out.println(paretoFile);

				if (args.length == 1) {
					Object[] params = { "Real" };

					problem = (new ProblemFactory())
							.getProblem(args[0], params);
				} // if
				else if (args.length == 2) {
					Object[] params = { "Real" };
					problem = (new ProblemFactory())
							.getProblem(args[0], params);
					indicators = new QualityIndicator(problem, args[1]);
				} // if
				else { // Default problem

					// DTLZ settings

					int number_of_decvars = dim + 9;

					if (problemNo == 1)
						number_of_decvars = dim + 4;
					else if (problemNo == 2 || problemNo == 3 || problemNo == 4)
						number_of_decvars = dim + 9;
					else if (problemNo == 7)
						number_of_decvars = dim + 19;
					else {
						System.out.println("not specified");
						System.exit(0);
					}

					Object[] params = { "Real", number_of_decvars, dim };
					problem = (new ProblemFactory()).getProblem(getProblem,
							params);

					// indicators = new QualityIndicator(problem,
					// read_settings.pf_path+"\\"+paretoFile);
					// System.out.println(read_settings.pf_path+"/DTLZ"+dtlzwhat+"_"+String.valueOf(number_of_objective)+"D.pf");

					/*
					 * 
					 * Object [] params={"Real",k,l,dim}; // for WFG1 problem =
					 * (new
					 * ProblemFactory()).getProblem(getProblem.toUpperCase()
					 * ,params); //problem = new WFG9("Real",2,4,3);
					 * 
					 * System.out.println("calculating True pareto front");
					 * //indicators = new QualityIndicator(problem,
					 * read_settings.pf_path+"/"+refFileName) ; //
					 * System.out.println
					 * ("true"+indicators.getTrueParetoFrontHypervolume());
					 */

				} // else
					// System.out.println(problem);
					// algorithm = new abc(problem);
					// algorithm = new SidNaheed(problem,4);
				algorithm = new zhenan(problem);
				// algorithm = new sidfinal(problem);
				// algorithm = new sid(problem);
				// algorithm = new NSGAII(problem);

				// Algorithm parameters

			    int M=dim;
			    
				int populationSize = 200;
					
				if(M==25)populationSize=328;
				
				else System.exit(0);		
				
				int GenerationNo = 250;
			 		   
			    algorithm.setInputParameter("populationSize",populationSize);
			    algorithm.setInputParameter("maxEvaluations",populationSize*GenerationNo);

				// Mutation and Crossover for Real codification
				parameters = new HashMap();
				parameters.put("probability", 1.0);
				parameters.put("distributionIndex", 15.0);
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

				// indicators.getHypervolume(population);
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
					currentPath=resultPfName;
					System.out.println(refFileName + " " + resultFile);

					long initTime = System.currentTimeMillis();
					SolutionSet population = algorithm.execute();
					long estimatedTime = System.currentTimeMillis() - initTime;
					fileHandler_ = new FileHandler("abc_main"
							+ Double.toString(seedar[i]) + ".log");
					logger_.addHandler(fileHandler_);
					logger_.info("Total execution time: " + estimatedTime
							+ "ms");

					// logger_.info("Variables values have been writen to file VAR");
					// population.printVariablesToFile("VAR"+Double.toString(seedar[i]));
					logger_.info("Objectives values have been writen to file "
							+ seedFolder + "\\" + resultFile);

					population.printObjectivesToFile(resultPfName);
					String resultTxt = seedFolder + "\\" + resultFile + ".txt";

					File file = new File(resultTxt);

					

				}
			}
		}

	} // main
} // NSGAII_main