package org.moeaframework.algorithm.sid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.PopulationIO;
import org.moeaframework.core.Solution;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
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
import org.moeaframework.problem.DTLZ.DTLZ1;
import org.moeaframework.problem.DTLZ.DTLZ2;
import org.moeaframework.problem.DTLZ.DTLZ3;
import org.moeaframework.problem.DTLZ.DTLZ4;
import org.moeaframework.problem.DTLZ.DTLZ;
import org.moeaframework.problem.DTLZ.DTLZ7;
public class RunMoead {
		
		public static void main(String[] args) throws ClassNotFoundException {
			
			String pfPath="C:\\Users\\andy\\Desktop\\MOEAsid\\MOEAFramework-2.1\\pf\\WFG8_2D.pf";
			
			
			/*
			int k=18;
			int l=36;
			int M=19;
			
			WFG8 sidWfg=new WFG8(k,l,M);*/
			
			//NondominatedPopulation refSet=getReferenceSet(new File(pfPath));	
			//Hypervolume moea=new Hypervolume(sidWfg, refSet);
			//System.out.println(moea.evaluate(refSet));
			
			//System.exit(0);

			/*
			
			NondominatedPopulation referenceSet=new NondominatedPopulation();
			
			int Size=512;
			while(Size-->0)
				referenceSet.add(sidWfg.generate());
			
			Hypervolume actual=new Hypervolume(sidWfg, referenceSet);
			*/
			
			//System.out.println(actual.evaluate(referenceSet));
			
			
			int []dimArr={2,3,5,7,10,12,15,20};
			int []wfgno={1,2,3,4,5,6,7,8,9};
			
			double []seedar={0.05,0.10,0.15,0.20,0.25,0.30,0.35,0.40,0.45,0.50,0.55,0.60,0.65,0.70,0.75,0.80,0.85,0.05,0.9,0.95,0.9,0.95,1};
			//double []seedar={0.5};
			//int[] wfgno = { 2, 3, 4, 5, 6, 7, 8, 9 };
			
			
			String directory = "E:\\Thesis lab experiment documents\\abcgenerations\\perfectWFG-DTLZ\\perfectMOEAD\\";
			
			for(int dimno=0;dimno<dimArr.length;dimno++){			 
				
				for(int wfgProblemNo=0;wfgProblemNo<wfgno.length;wfgProblemNo++){
					
					
					int M = dimArr[dimno];							
				    int k;
				    int l;
				    
				    int populationSize = 200;
					
						 if(M==2)populationSize=204;
					else if(M==3)populationSize=204;
					else if(M==5)populationSize=212;
					else if(M==7)populationSize=212;
					else if(M==10)populationSize=220;
					else if(M==12)populationSize=160;
					else if(M==15)populationSize=240;
					else if(M==20)populationSize=212;				
					else System.exit(0);
							
					int GenerationNo = 250;
					
					int k_factor = 2;   // k (# position parameters) = k_factor*( M-1 ) 
					int l_factor = 10;   // l (# distance parameters) = l_factor*2

					if(M==2){
						k_factor=4;				
					}
					else if(M>=3 && M<10){
						k_factor=2;				
					}
					else{
						k_factor=1;
					}
					
					l=l_factor*2;
					k=k_factor*(M-1);
					

					
					
					String resultFile="MOEADwfg"+wfgno[wfgProblemNo]+"_"+String.valueOf(M)+".pf";
					//String resultFile="MOEADDTLZ"+wfgno[wfgProblemNo]+"_"+String.valueOf(M)+".pf";
					
					 for(int seedNo=0;seedNo<seedar.length;seedNo++){
						   //PseudoRandom.setseed(seedar[seedNo]);					    	
					    	String  seedFolder=directory+String.valueOf(seedar[seedNo]);
					    	createDirectory(seedFolder);
					    	String path=seedFolder+"\\"+resultFile;
					    	
					    	System.out.println("SEED->"+seedar[seedNo]);
					    	System.out.println("Problem->"+resultFile);
					    	
						    
					    	//System.out.println(getClassName(wfgno[wfgProblemNo]));
					    
					    	NondominatedPopulation result = new Executor()
					    	.withProblemClass(getClassName(wfgno[wfgProblemNo]),k,l,M)
						//	.withProblemClass(getdtlzclass(wfgno[wfgProblemNo]), M)
							.withAlgorithm("MOEAD")
							.withMaxEvaluations(GenerationNo*populationSize)
							.withProperty("populationSize", populationSize)
							.withProperty("operator", "sbx+pm")
							.withProperty("sbx.rate", 1)
							.withProperty("sbx.distributionIndex", 15.0)
							.withProperty("pm.rate", 1/(k+l))
							.withProperty("pm.distributionIndex", 20.0)		 
							.withProperty("neighborhoodSize",0.05) // (T) as a % of popsize (.05 for 5%,default is 10%)
							.withProperty("eta",0.01) //The maximum number of population slots a solution can replace. replacement neighbourhood size(nr). as a % of popsize. default is 1%
							.run();
					    	
					    	printObjectivesToFile(path, result);
					    
							 //note: min bound of T and nr is 20 and 2
							//System.out.format("Objective1  Objective2%n");
							/*	
							for (Solution solution : result) {
								for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
									System.out.format("%.4f ",solution.getObjective(i));
											
								}
								System.out.println();
							}*/
								
							//System.out.println(moea.evaluate(result));
					    	
					    	
					    	
					    	
					    					
					 }
					
					
					
					 
			 
			
				}
			}
		}
		
		public static  Class<? extends DTLZ> getdtlzclass(int problemNo){
			if(problemNo==1)
				return new DTLZ1(0).getClass();
			else if(problemNo==2)
				return  new DTLZ2(0).getClass();
			else if(problemNo==3)
				return  new DTLZ3(0).getClass();
			else if(problemNo==4)
				return  new DTLZ4(0).getClass();
			else if(problemNo==7)
				return  new DTLZ7(0).getClass();
			return null;
			
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
		
		public static void createDirectory(String seedFolder){
			File theDir = new File(seedFolder);

			  // if the directory does not exist, create it
			if (!theDir.exists()) {
			    System.out.println("creating directory: " + seedFolder);
			    boolean result = theDir.mkdir();  
			     if(result) {    
			       System.out.println("DIR created");  
			     }
			}

		}
		
	  public static void printObjectivesToFile(String path,NondominatedPopulation solutionsList_){
		    try {
		      /* Open the file */
		      FileOutputStream fos   = new FileOutputStream(path)     ;
		      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
		      BufferedWriter bw      = new BufferedWriter(osw)        ;
		                        
		      for (int i = 0; i < solutionsList_.size(); i++) {
		        //if (this.vector[i].getFitness()<1.0) {
  
		    	bw.write(getString(solutionsList_.get(i)));		        
		        bw.newLine();
		        //}
		      }
		      
		      /* Close the file */
		      bw.close();
		    }catch (IOException e) {		      
		      e.printStackTrace();
		    }
		  } // printObjectivesToFile
		
	  
		  public static String getString(Solution solu){
			  String aux="";
			  for (int i = 0; i < solu.getNumberOfObjectives(); i++)
		      aux = aux + solu.getObjective(i) + " ";
			    return aux;
		  }
		
		public static NondominatedPopulation getReferenceSet(File referenceSetFile) {
			NondominatedPopulation referenceSet = new NondominatedPopulation(new ParetoDominanceComparator());
			
			try {
				referenceSet.addAll(PopulationIO.readObjectives(
						referenceSetFile));
			} catch (IOException e) {
				throw new IllegalArgumentException(
						"unable to load reference set", e);
			}
			
			return referenceSet;
		}
	
}
