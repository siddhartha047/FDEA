package jmetal.metaheuritic.FDEArevision;

public class ReferencePointSettings {
	public  int numberOfObjectives;
	public  int numberOfDivision;
	public  double limit;
	public  boolean includeLimit;
	
	public ReferencePointSettings(){
		
	}
	
	public ReferencePointSettings(int noOfObjectives,int noOfDivision,double Limit,boolean inLimit){
		numberOfObjectives=noOfObjectives;
		numberOfDivision = noOfDivision;
		limit=Limit;
		includeLimit=inLimit;
	}
	
	
}
