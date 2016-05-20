package org.moeaframework.algorithm.sid;

import java.io.File;
import java.io.IOException;





import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.PopulationIO;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.indicator.Hypervolume;
import org.moeaframework.problem.WFG.WFG8;

public class SidMain {
	
	public static void main(String[] args) {
		
		
		/*
		NondominatedPopulation result = new Executor()
				.withProblemClass(sidWfg.getClass(),18,14,2)
				.withAlgorithm("Sid")
				.withMaxEvaluations(10000)
				.withProperty("populationSize", 250)
				.withProperty("sbx.rate", 0.9)
				.withProperty("sbx.distributionIndex", 15.0)
				.withProperty("pm.rate", 0.1)
				.withProperty("pm.distributionIndex", 20.0)
				.distributeOnAllCores()
				.run();
		
		//display the results
		System.out.println("Running sid");
		
		
		
		
		for (Solution solution : result) {
			
			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				System.out.format("%.4f\t",solution.getObjective(i));
			}
			System.out.println();
			
		}
		
		System.out.println("Done");
		*/
		
		
		
		
		////
		// setup the instrumenter to record the generational distance metric
		
		
		String pfPath="C:\\Users\\andy\\Desktop\\MOEAsid\\MOEAFramework-2.1\\pf\\WFG8.2D.pf2";
		
		
		/*
				Instrumenter instrumenter = new Instrumenter()
						.withProblemClass(sidWfg.getClass(),18,14,2)
						.withReferenceSet(new File(pfPath))
						.withFrequency(100)
						.attachElapsedTimeCollector()
						.attachGenerationalDistanceCollector()
						.attachHypervolumeCollector();
				*/
		
		
		WFG8 sidWfg=new WFG8(1,10,2);
		
		NondominatedPopulation referenceSet=new NondominatedPopulation();
		
		int Size=512;
		while(Size-->0)
			referenceSet.add(sidWfg.generate());
		
		
		
		
		//NondominatedPopulation referenceSet=getReferenceSet(new File(pfPath));
		Hypervolume actual=new Hypervolume(sidWfg, referenceSet);
		
		
		
		System.out.println(actual.evaluate(referenceSet));
		
		
		System.exit(0);

		/*
		Instrumenter instrumenter = new Instrumenter()
				.withProblemClass(sidWfg.getClass(),18,14,2)
				.withReferenceSet(new File(pfPath))
				.withFrequency(100)
				.attachElapsedTimeCollector()
				.attachGenerationalDistanceCollector()
				.attachHypervolumeCollector();
		*/
				// use the executor to run the algorithm with the instrumenter
				 NondominatedPopulation result = new Executor()
				.withProblemClass(sidWfg.getClass(),1,10,2)
				.withAlgorithm("NSGAII")
				.withMaxEvaluations(300*250)
				.withProperty("populationSize", 250)
				.withProperty("sbx.rate", 0.9)
				.withProperty("sbx.distributionIndex", 15.0)
				.withProperty("pm.rate", 0.1)
				.withProperty("pm.distributionIndex", 20.0)				
				.run();
						/*				
				Accumulator accumulator = instrumenter.getLastAccumulator();
				
				System.out.format("  NFE    Time      Generational Distance Hypervolume%n");
				
				for (int i=0; i<accumulator.size("NFE"); i++) {
					System.out.format("%5d    %-8.4f   %-8.4f %-8.4f%n ",
							accumulator.get("NFE", i),
							accumulator.get("Elapsed Time", i),
							accumulator.get("GenerationalDistance", i),
							accumulator.get("Hypervolume", i));
				}
*/
		
				
				System.out.println(actual.evaluate(result));
		///
		
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