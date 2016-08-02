package jmetal.metaheuritic.FDEAupdated;

import java.util.ArrayList;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

public class ReferencePoint {
	public int numberOfObjectives;
	
	public double []point;
	public double crowdingDistance; 
	
	public ReferencePoint(int noOfObjective){
		numberOfObjectives=noOfObjective;
		
		point=new double[numberOfObjectives];
		
	}
	
	ArrayList<Solution> associatedSolution=new ArrayList<Solution>();
	public SolutionSet associatedSolutionSet;
	
	public ReferencePoint(double[] refPoint){
		
		numberOfObjectives=refPoint.length;
		point=new double[refPoint.length];
		
		for(int i=0;i<refPoint.length;i++){
			point[i]=refPoint[i];
		}
		
		
	}
	
	public static void printPoint(double [] refPoint){
		for(int i=0;i<refPoint.length;i++){
			System.out.print(refPoint[i]+"\t");
		}
		System.out.println();
	}
	
	public void printPoint(){
		for(int i=0;i<point.length;i++){
			System.out.print(point[i]+"\t");
		}
		System.out.println();
	}
	
	public double[] getPoint(){
		return point;
	}
	
	public void addSolution(Solution solution){
		associatedSolution.add(solution);
	}
	
	public ArrayList<Solution> getAssociatedSolutionAsList(){
		return associatedSolution;
	}
	
	public SolutionSet makeAndReturnSolutionSet(){
		associatedSolutionSet=new SolutionSet(associatedSolution.size());
		for(int i=0;i<associatedSolution.size();i++){
			associatedSolutionSet.add(associatedSolution.get(i));
		}
		
		return associatedSolutionSet;
	}
	
	
	public SolutionSet getSolutionSet(){
		return associatedSolutionSet;
	}
	
	boolean isSorted=false;
	
	/**
	 * Sorting will be fitness value from lower to higher
	 */
	
	public void sort(){
		//print will be lower to higher
		associatedSolutionSet.sort(new InvertFitnessComparator());
	}
	
		//value sorted form high to low
	public void reversesort(){
		associatedSolutionSet.sort(new HightoLowFitnessComparator());
	}
	
	public void setCrowdingDistance(double crdDistance){
		crowdingDistance=crdDistance;
	}
	
	public double getCrowdingDistance(){
		return crowdingDistance;
	}
	
	public double getObjective(int i){
		return point[i];
	}
	
	public void ClearAssociatedSolutions(){
		associatedSolution.clear();
		associatedSolutionSet=null;
		
	}
	
	public String toString() {
	    String aux="";
	    for (int i = 0; i < numberOfObjectives; i++)
	      aux = aux + this.getObjective(i) + " ";

	    return aux;
	  } // toString
	
}
