//  GDE3_main.java
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
package jmetal.metaheuristics.gde3;

import jmetal.core.*;
import jmetal.metaheuritic.abc.SidPolar;
import jmetal.operators.crossover.*;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.*;
import jmetal.problems.*                  ;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.WFG.*;
import jmetal.problems.LZ09.* ;
import jmetal.qualityIndicator.QualityIndicator;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.read_settings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class for configuring and running the GDE3 algorithm
 */
public class CopyOfGDE3_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object
  public static String currentpath;
  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three choices
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName paretoFrontFile
   */
  public static void main(String [] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
    
	   int []dimArr={2,3,5,7,10,13,19};
	   int []wfgno={1,2,3,4,5,6,7,8,9};  
	   
	   double []seedar={0.05,0.1,0.15,0.2,0.25,0.3,0.35,0.4,0.45,0.5,0.55,0.6,0.65,0.7,0.75,0.8,0.85,0.9,0.95,1};
	   
	   //double []seedar={0.5};
	   //double []seedar={0.1,0.3,0.5,0.7,0.9};
		
		/*
		int []dimArr={3};
		int []wfgno={2,3,4,5,6,7,8,9};
		double []seedar={0.1,40.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,0.99};*/
		//double []seedar={0.1,0.3,0.5};
		//double []seedar={0.05,0.1,0.15,0.2,0.25,0.3,0.35,0.4,0.45,0.5,0.55,0.6,0.65,0.7,0.75,0.8,0.85,0.9,0.95,1};
		//double []seedar={0.05,0.15,0.2,0.25,0.3,0.35,0.4,0.45};
		//double []seedar={0.55,0.6,0.65,0.7,0.75,0.8,0.85,0.95,1};
				
		//int []dimArr={4};
		  
		for(int dimno=0;dimno<dimArr.length;dimno++){
		  
		for(int wfgProblemNo=0;wfgProblemNo<wfgno.length;wfgProblemNo++){
			///
			
			Problem   problem   ; // The problem to solve
		    Algorithm algorithm ; // The algorithm to use
		    Operator  crossover ; // Crossover operator
		    Operator  mutation  ; // Mutation operator
		    Operator  selection ; // Selection operator
		    
		    HashMap  parameters ; // Operator parameters
		    
		    QualityIndicator indicators ; // Object to get quality indicators

		    // Logger object and file to store log messages
		    logger_      = Configuration.logger_ ;

		    
		    //double []seedar={.5};
		    
		    read_settings rs=new read_settings();
		    indicators = null;
		    
		    //settings
		    int k=36;
			int l=18;
			int dim=dimArr[dimno];
			int problemNo=wfgno[wfgProblemNo];
			
			String problemName="wfg";    	    	
			String getProblem=problemName+String.valueOf(problemNo);
			String refFileName=getProblem+"_"+String.valueOf(dim)+".pf";    
			String resultFile="GDE3"+getProblem+"_"+String.valueOf(dim)+".pf";
		    
		    
		    
		    if (args.length == 1) {
		      Object [] params = {"Real"};
		      
		      problem = (new ProblemFactory()).getProblem(args[0],params);
		    } // if 
		    else if (args.length == 2) {
		      Object [] params = {"Real"};
		      problem = (new ProblemFactory()).getProblem(args[0],params);
		      indicators = new QualityIndicator(problem, args[1]) ;
		    } // if
		    else { // Default problem

		        // DTLZ settings
		       /* int dtlzwhat=2;
		        int number_of_objective=5;
		        int number_of_decvars=number_of_objective+9;
		        Object [] params={"Real",number_of_decvars,number_of_objective};
		        problem = (new ProblemFactory()).getProblem("DTLZ"+dtlzwhat,params);
		        indicators = new QualityIndicator(problem, read_settings.pf_path+"/DTLZ"+dtlzwhat+"_"+String.valueOf(number_of_objective)+"D.pf") ;
		        System.out.println(read_settings.pf_path+"/DTLZ"+dtlzwhat+"_"+String.valueOf(number_of_objective)+"D.pf");
		       */
		    	
		    	
		    	
		       
		        Object [] params={"Real",k,l,dim}; // for WFG1
		        problem = (new ProblemFactory()).getProblem(getProblem.toUpperCase(),params);
		        //problem = new WFG9("Real",2,4,3);
		        
		        System.out.println("calculating True pareto front");
		        //indicators = new QualityIndicator(problem, read_settings.pf_path+"/"+refFileName) ;
		       // System.out.println("true"+indicators.getTrueParetoFrontHypervolume());
		        

		    } // else
		    
		    algorithm = new GDE3(problem);
		    
		    int M=dim;
		    
			int populationSize = 200;
				
				 if(M==2)populationSize=204;
			else if(M==3)populationSize=204;
			else if(M==5)populationSize=212;
			else if(M==7)populationSize=212;
			else if(M==10)populationSize=220;
			else if(M==13)populationSize=184;
			else if(M==19)populationSize=212;
			else System.exit(0);		
			
			int GenerationNo = 200;
		 		   
		    
		    // Algorithm parameters
		    algorithm.setInputParameter("populationSize",populationSize);
		    algorithm.setInputParameter("maxIterations",GenerationNo);
		    
		    // Crossover operator 
		    parameters = new HashMap() ;
		    parameters.put("CR", 0.5) ;
		    parameters.put("F", 0.5) ;
		    crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover", parameters);                   
		    
		    // Add the operators to the algorithm
		    parameters = null ;
		    selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection", parameters) ;

		    algorithm.addOperator("crossover",crossover);
		    algorithm.addOperator("selection",selection);
		    
		    for(int i=0;i<seedar.length;i++){
			    PseudoRandom.setseed(seedar[i]);
			    
			    //directory creation
		    	
		    	
		    	String  seedFolder=read_settings.generation_path+"\\"+String.valueOf(seedar[i]);
		    	File theDir = new File(seedFolder);

				  // if the directory does not exist, create it
				  if (!theDir.exists()) {
				    System.out.println("creating directory: " + seedFolder);
				    boolean result = theDir.mkdir();  
				     if(result) {    
				       System.out.println("DIR created");  
				     }
				  }
				

				String resultPfName=seedFolder+"\\"+resultFile;
				currentpath=resultPfName;
				    
			    long initTime = System.currentTimeMillis();
			    SolutionSet population = algorithm.execute();
			    long estimatedTime = System.currentTimeMillis() - initTime;
			   // fileHandler_ = new FileHandler("abc_main"+Double.toString(seedar[i])+".log"); 
			   // logger_.addHandler(fileHandler_);
			   // logger_.info("Total execution time: "+estimatedTime + "ms");
			    
			    //logger_.info("Variables values have been writen to file VAR");
			    //population.printVariablesToFile("VAR"+Double.toString(seedar[i]));    
			    //logger_.info("Objectives values have been writen to file "+seedFolder+"\\"+resultFile);
			   
			    population.printObjectivesToFile(resultPfName);
			    
			    System.out.println(refFileName+" "+resultFile);
			    
			    
			    String resultTxt=seedFolder+"\\"+resultFile+".txt";
			    
			    
			    File file= new File(resultTxt);
			    
				if (indicators != null) {
					

				    
					
			        double GD=indicators.getGD(population);
			        double IGD=indicators.getIGD(population);
			        double Spread=indicators.getSpread(population);
			        double Epsilon=indicators.getEpsilon(population);
			        
			        
			        /*
			        BufferedWriter output = new BufferedWriter(new FileWriter(file));
	d				output.write("GD "+String.valueOf(GD)+"\n");
					output.write("IGD "+String.valueOf(IGD)+"\n");
					output.write("Spread "+String.valueOf(Spread)+"\n");
					output.write("Epsilon "+String.valueOf(Epsilon)+"\n");
					
			        output.close();
			        */
			        
			        System.out.println("GD -> "+ GD);
			        System.out.println("IGD -> "+ IGD);
			        System.out.println("Spread -> "+ Spread);
			        System.out.println("Epsilon -> "+ Epsilon);
			        
			        /*
			        
					
							logger_.info("Quality indicators") ;
						//	logger_.info("True Hypervolume: "+ indicators.getTrueParetoFrontHypervolume());
						//	logger_.info("Hypervolume: "+ indicators.getHypervolume(population));
						//  logger_.info("Hypervolume Ratio    : "+ indicators.getHypervolume_Ratio(population)+" *****************");
							logger_.info("GD         : " + indicators.getGD(population)+"+++++++++++++++++++++++++++++");
							logger_.info("IGD        : " + indicators.getIGD(population));
							logger_.info("Spread     : " + indicators.getSpread(population)+"##########################");
							logger_.info("Epsilon    : " + indicators.getEpsilon(population));
							logger_.info("----------------------------------------------");
							
							
							*/
				//      int evaluations = ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
				     // logger_.info("Speed      : " + evaluations/pop + " gen") ;
				    } // if
				
		    }
			///
			
			
			
			
			
			
		}//end of of my main loop;
		}
		  
	
  }
} // GDE3_main
