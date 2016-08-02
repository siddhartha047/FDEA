package jmetal.metaheuritic.FDEAupdated;

import java.util.ArrayList;

public class ShiftDensityBasedSorting extends MinMaxSorting{

	
	
	public void scrowdingDistanceAssignment(ArrayList<ReferencePoint> solutionSet, int nObjs) {
		solutionSet.clear();
		nObjs=2;
		
		ReferencePoint A=new ReferencePoint(new double[]{10,17});
		ReferencePoint B=new ReferencePoint(new double[]{1,18});
		ReferencePoint C=new ReferencePoint(new double[]{11,6});
		ReferencePoint D=new ReferencePoint(new double[]{18,12});
		
		solutionSet.add(A);
		solutionSet.add(B);
		solutionSet.add(C);
		solutionSet.add(D);
		
		
		
		
		
		System.exit(0);
	}
	
	@Override
	public double getEucledianDistance(ReferencePoint X,ReferencePoint Y){
		
		double value=0;
		
		for(int i=0;i<X.numberOfObjectives;i++){
			
			if(Y.getObjective(i)<X.getObjective(i))
				value+= (X.getObjective(i)-Y.getObjective(i))*(X.getObjective(i)-Y.getObjective(i));			
		}
	
		return Math.sqrt(value);
	}
	
	
}
