package jmetal.metaheuritic.FDEAupdated;

import java.util.ArrayList;
import java.util.Collections;

public class CrowdingShiftDensityBasedSorting extends MinMaxSorting{

	
	
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
		
		ArrayList<ReferencePoint> front = new ArrayList<ReferencePoint>();
		front.addAll(solutionSet);
		
		solutionSet.clear();
		
		
		
			
		
		for (int i = 0; i < nObjs; i++) {
			
			Collections.sort(front, new ReferencePointComparator(i));

			if(!solutionSet.contains(front.get(0))){
				solutionSet.add(front.get(0));
			}
			if(!solutionSet.contains(front.get(front.size()-1))){
				solutionSet.add(front.get(front.size() - 1));
			}			
		}
		
		if(!front.removeAll(solutionSet))
		{
			System.out.println("Something wrong in sid ref distance sort removing solution");
			System.exit(0);
		}
		
		
		
		
		System.exit(0);
	}
	
	
	
	
	
	
}
