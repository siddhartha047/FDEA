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

public class FDEA_Main_DTLZ {
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

		 //int []dimArr={2,3,5,7,10,12,15,20};
		int[] dimArr = {20};
		 //int []wfgno={1,2,3,4,5,6,7,8,9};
		int[] wfgno = {1,2,3,4};

		double []seedar={0.05,0.1,0.15,0.2,0.25,0.3,0.35,0.4,0.45,0.5,0.55,0.6,0.65,0.7,0.75,0.8,0.85,0.9,0.95,1};
		//double []seedar={0.05,0.1,0.15,0.2,0.25};
		//double []seedar={0.05};
	
		 
		for(int avg=2;avg<=2;avg++){
		 
		for (int dimno = 0; dimno < dimArr.length; dimno++) {

			for (int wfgProblemNo = 0; wfgProblemNo < wfgno.length; wfgProblemNo++) {
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
				read_settings.generation_path=read_settings.generation_path+Integer.toString(avg)+"\\";
				onAvg=avg;
				// settings

				int dim = dimArr[dimno];
				int problemNo = wfgno[wfgProblemNo];

				int M = dim;
				int k;
				int l;
				
				int GenerationNo = 250;

				int populationSize = 200;

				if (M == 2)
					populationSize = 204;
				else if (M == 3)
					populationSize = 204;
				else if (M == 5)
					populationSize = 212;
				else if (M == 7)
					populationSize = 212;
				else if (M == 10){
					//GenerationNo = 250;
					populationSize = 220;
				}
					
				else if (M == 12)
					populationSize = 160;
				else if (M == 15){
					//GenerationNo = 250;
					populationSize = 240;
				}
					
				else if (M == 20)
					populationSize = 212;
				else if (M == 25)
					populationSize = 328;

				else
					System.exit(0);

			



				String problemName = "DTLZ";
				String getProblem = problemName + String.valueOf(problemNo);
				String refFileName = getProblem + "_" + String.valueOf(dim)
						+ ".pf";
				String resultFile = "fdea" + getProblem + "_"
						+ String.valueOf(dim) + ".pf";

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

					 int number_of_decvars=dim+9;
			        
			    	 if(problemNo==1)number_of_decvars=dim+4;
			    	 else if(problemNo==2 ||problemNo==3 || problemNo==4)number_of_decvars=dim+9;
			    	 else if(problemNo==7)number_of_decvars=dim+19;
			    	 else{
			    		 System.out.println("not specified");
			    		 System.exit(0);
			    	 }
			    	 			    	 
			        Object [] params={"Real",number_of_decvars,dim};
			        problem = (new ProblemFactory()).getProblem(getProblem,params);
			        
				} // else
					// System.out.println(problem);

				//algorithm=new FDEAgaussian(problem);
				algorithm = new FDEA(problem);
				//algorithm = new FDEAwithoutReferencePoints(problem);
				//algorithm = new FDEAwithParetoDom(problem);

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
				mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);

				// Selection Operator
				parameters = null;
				// TO DO: done
				if(dim>=5){
					selection = SelectionFactory.getSelectionOperator("SidBinaryTournament", parameters);
				}
				else{
					selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);
				}
				
				

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

					// logger_.info("Variables values have been writen to file VAR");
					// population.printVariablesToFile("VAR"+Double.toString(seedar[i]));
					logger_.info("Objectives values have been writen to file "
							+ seedFolder + "\\" + resultFile);

					population.printObjectivesToFile(resultPfName);

					System.out.println(refFileName + " " + resultFile);

					String resultTxt = seedFolder + "\\" + resultFile + ".txt";

					File file = new File(resultTxt);

					if (indicators != null) {

						double GD = indicators.getGD(population);
						double IGD = indicators.getIGD(population);
						double Spread = indicators.getSpread(population);
						double Epsilon = indicators.getEpsilon(population);

						/*
						 * BufferedWriter output = new BufferedWriter(new
						 * FileWriter(file)); d
						 * output.write("GD "+String.valueOf(GD)+"\n");
						 * output.write("IGD "+String.valueOf(IGD)+"\n");
						 * output.write("Spread "+String.valueOf(Spread)+"\n");
						 * output
						 * .write("Epsilon "+String.valueOf(Epsilon)+"\n");
						 * 
						 * output.close();
						 */

						System.out.println("GD -> " + GD);
						System.out.println("IGD -> " + IGD);
						System.out.println("Spread -> " + Spread);
						System.out.println("Epsilon -> " + Epsilon);

					} // if

				}
				// /

			}// end of of my main loop;
		}
		}//newavg
	} // main		
} // NSGAII_main