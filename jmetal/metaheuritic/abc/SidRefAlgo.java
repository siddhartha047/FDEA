package jmetal.metaheuritic.abc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Ranking;

public class SidRefAlgo{
	
	SidRefDistance sidRefDistance=new SidRefDistance();
	ReferenceMemberShipFunction referenceMemberShipFunction=new ReferenceMemberShipFunction();
	
	public SidRefAlgo(){
		
		
	}
	
	public ArrayList<ReferencePoint> GlobalRefPoints=null;
	public ArrayList<ReferencePoint> ActiveRefPoints=null;

	
	public int prevMi(int [] m_i,int currentOb){
		int total=0;
		for(int i=0;i<currentOb;i++){
			total+= m_i[i];
		}
		
		return total;
	}
	
	public double prevWi(double [] m_i,int currentOb){
		double total=0;
		for(int i=0;i<currentOb;i++){
			total+= m_i[i];
		}
		
		return total;
	}
	
	public boolean isAnyoneLimit(double []w_i,double limit){
		for(int i=0;i<w_i.length;i++){
			if(w_i[i]==limit)return true;
		}
		return false;
	}
	
	public void generate(int []m_i, double []w_i,int i, int m,double delta, int numberOfObjectives, int numberOfPoints,ArrayList<ReferencePoint> refPoints,double limit,boolean ignoreLimit){
		
		w_i[i-1]= m*delta;
		m_i[i-1]= m;
		//System.out.println(m+" w_i[i-1]: +"+w_i[i-1]);
		//System.out.println(m+ " m_i[i-1]: +"+m_i[i-1]);
		
		if(i==(numberOfObjectives-1)){
			
			w_i[i]=limit-prevWi(w_i, i);
			m_i[i]=(int)(w_i[i]/delta);

			//for excluding same direction
			if(isAnyoneLimit(w_i,limit) && ignoreLimit)return;
			
			refPoints.add(new ReferencePoint(w_i));
			
			//ReferencePoint.printPoint(w_i);
			
			return;
		}
		

		for(int j=0;j<=(numberOfPoints-prevMi(m_i, i));j++)
		{
			generate(m_i, w_i, i+1, j, delta, numberOfObjectives,numberOfPoints,refPoints,limit,ignoreLimit);
		}
		
		
		
	}
	
	public void Experiment(int ObjectiveNo,int NumberOfDivision,double limit,ArrayList<ReferencePoint> refPoints,boolean igNoreLimit){
		
		int numberOfObjectives=3;
		int numberOfDivision=6;
		
		numberOfObjectives=ObjectiveNo;
		numberOfDivision= NumberOfDivision;
		
		int numberOfPoints=numberOfDivision;
		double delta=(double)(limit/(double)numberOfPoints);
		//System.out.println("delta: "+delta);
		
		int []m_i= new int[numberOfObjectives];
		double []w_i=new double[numberOfObjectives];
		
		
			
		for(int j=0;j<=numberOfPoints;j++)
		{
			generate(m_i, w_i,1, j, delta, numberOfObjectives,numberOfPoints,refPoints,limit,igNoreLimit);
		}

		
		
	}
    
	double getMagnitude(double []A){
		double total=0;
		for(int i=0;i<A.length;i++){
			total += A[i]*A[i];
		}
		
		return Math.sqrt(total);
	}
	
	public double getSimilarity(ReferencePoint refPoint,Solution solution){
		
		double []A = refPoint.getPoint();
		double []B = solution.get_fuzzy_objectives();
		
		double score=0;
		
		for(int i=0;i<A.length;i++){
			score +=A[i]*B[i]; 
		}
		
		return score/(getMagnitude(A)*getMagnitude(B));
	}
	
	
	public ReferencePoint getNearestRefPoint(ArrayList<ReferencePoint> refPoints,Solution solution){
		
		double best = 0;
		ReferencePoint bestRef= null,temporaryRef;
		double score=0;
		
		//System.out.println(refPoints.size());
		
		for(int i=0;i<refPoints.size();i++){
			
			temporaryRef=refPoints.get(i);
			score = getSimilarity(temporaryRef, solution);
			
			if(score > best){
				bestRef = refPoints.get(i);
				best = score; 
			}
			
		}
		
		
		
		if(bestRef==null){
			System.out.println("Some is wrong at caculating cosine similarity");
			System.exit(0);
		}
		
		return bestRef;
	}
	
	public ArrayList<ReferencePoint> ActiveReferencePoint(ArrayList<ReferencePoint> refPoints, SolutionSet solutionSet){
		
		HashMap<ReferencePoint, Integer> activePointsIndex= new HashMap<ReferencePoint,Integer>();
		ArrayList<ReferencePoint> activePoints=new ArrayList<ReferencePoint>();
		
		int index=0;
		
		for(int i=0;i<solutionSet.size();i++){
			
			Solution temp = solutionSet.get(i);
			
			ReferencePoint refPoint = getNearestRefPoint(refPoints,temp);
			
			if(activePointsIndex.containsKey(refPoint)){
				index = activePointsIndex.get(refPoint);
				activePoints.get(index).addSolution(temp);
			}
			else{
				index = activePoints.size();
				activePoints.add(refPoint);
				refPoint.addSolution(temp);
				activePointsIndex.put(refPoint,index);
			}
		}
		
		return activePoints;
		
	}

	
	
	public void takeSolution(
			SolutionSet union,
			SolutionSet population,
			int populationSize,
			ArrayList<ReferencePointSettings> refSettings,
			int maxRefPoint){
		
		
		MinMaxFind(union);
		
		SolutionSet parent=new SolutionSet(union.size());
		SolutionSet offSpring=new SolutionSet(union.size());
		
		for(int i=0;i<union.size();i++){
			if(union.get(i).isParent){
				parent.add(union.get(i));
			}
			else{
				offSpring.add(union.get(i));
			}
		}
		
		Normalize(parent);
		Normalize(offSpring);
		
		if(GlobalRefPoints==null){
			
			GlobalRefPoints=new ArrayList<ReferencePoint>();
			
			for(int i=0;i<refSettings.size();i++){
				Experiment(
						refSettings.get(i).numberOfObjectives,
						refSettings.get(i).numberOfDivision,
						refSettings.get(i).limit,
						GlobalRefPoints,
						refSettings.get(i).includeLimit
					);
				
			}
		}
		
		System.out.println("points\t" + GlobalRefPoints.size());
		
		/*
		for(int i=0;i<GlobalRefPoints.size();i++){
			GlobalRefPoints.get(i).printPoint();
		}*/
		
		if(ActiveRefPoints==null){			
			ActiveRefPoints= ActiveReferencePoint(GlobalRefPoints, parent);
		}
		else{
			for(int i=0;i<ActiveRefPoints.size();i++){
				ActiveRefPoints.get(i).ClearAssociatedSolutions();
			}
		}
		
		/*System.out.println("'''''''Parent Active Points'''''''''''''''''''''''''");
		
		for(int i=0;i<ActiveRefPoints.size();i++){
			
			ActiveRefPoints.get(i).printPoint();
			System.out.println("--->");
			ActiveRefPoints.get(i).makeAndReturnSolutionSet().printSolutionSet();
			System.out.println("---------");
			ActiveRefPoints.get(i).associatedSolutionSet.printFuzzySolutionSet();
			
			System.out.println("#####");
			
		}*/
		
		
		
		ArrayList<ReferencePoint> ChildActivePoints=ActiveReferencePoint(GlobalRefPoints, offSpring);
	
		/*System.out.println("'''''''Child Active Points'''''''''''''''''''''''''");
		
		for(int i=0;i<ChildActivePoints.size();i++){
			
			ChildActivePoints.get(i).printPoint();
			System.out.println("--->");
			ChildActivePoints.get(i).makeAndReturnSolutionSet().printSolutionSet();
			System.out.println("---------");
			ChildActivePoints.get(i).associatedSolutionSet.printFuzzySolutionSet();
			
			System.out.println("#####");
			
		}*/
		
		System.out.println("Parent Active "+ActiveRefPoints.size()+"\t ChildActivePoints "+ChildActivePoints.size());
		
		MakeRefActivePoints(ActiveRefPoints,ChildActivePoints,maxRefPoint);
		
		for(int i=0;i<ActiveRefPoints.size();i++){
			ActiveRefPoints.get(i).ClearAssociatedSolutions();
		}
		
		/*System.out.println("'''''''New Active Points'''''''''''''''''''''''''");
		
		for(int i=0;i<ActiveRefPoints.size();i++){
			
			ActiveRefPoints.get(i).printPoint();
			System.out.println("--->");
			ActiveRefPoints.get(i).makeAndReturnSolutionSet().printSolutionSet();
			System.out.println("---------");
			ActiveRefPoints.get(i).associatedSolutionSet.printFuzzySolutionSet();
			
			System.out.println("#####");
			
		}*/
		
		parent.clear();
		
		ActiveRefPoints= ActiveReferencePoint(ActiveRefPoints, union);		
		System.out.println("Active RefPoints "+ActiveRefPoints.size());
		takeNextGeneration(ActiveRefPoints, union, population, referenceMemberShipFunction, populationSize);
		
		//parent.printSolutionSet();
		
		//System.exit(0);
		
		
	}
	
	public void MakeRefActivePoints(ArrayList<ReferencePoint> ParentActive,ArrayList<ReferencePoint> ChildActive,int RequiredSize){
		
		ArrayList<ReferencePoint> unionRefPoints=new ArrayList<ReferencePoint>();
		
		for(int i=0;i<ChildActive.size();i++){
			ReferencePoint ref=ChildActive.get(i);
			boolean flag=true;
			for(int j=0;j<ParentActive.size();j++){
				if(ref == ParentActive.get(j)){
					flag=false;
					break;
				}
			}
			if(flag){
				unionRefPoints.add(ref);
			}
		}
        
		unionRefPoints.addAll(ParentActive);
		
		ParentActive.clear();
		
		if(unionRefPoints.size()<=RequiredSize){
			ParentActive.addAll(unionRefPoints);
			return;
		}
		
		System.out.println(RequiredSize);		
		System.out.println("Crowding distant "+unionRefPoints.size());
		
		sidRefDistance.crowdingDistanceAssignment(unionRefPoints,unionRefPoints.get(0).numberOfObjectives);
				
		Collections.sort(unionRefPoints,new SidCrowdingComparator());	
		
		for (int k = 0; k < RequiredSize; k++) {
			ParentActive.add(unionRefPoints.get(k));
		}
		
		System.out.println("Taken "+ParentActive.size());
				
	}
	
	public void takeNextGeneration(ArrayList<ReferencePoint> activePoints,SolutionSet solutionSet,SolutionSet population,ReferenceMemberShipFunction refMemberShipFunction,int populationSize) {

		refMemberShipFunction.Initialization(solutionSet);
		
		for(int i=0;i<activePoints.size();i++){
			ReferencePoint activePoint=activePoints.get(i);
			
			if(activePoint.makeAndReturnSolutionSet().size()>1)
			{
				if(!activePoint.isSorted){
					refMemberShipFunction.FitnessAssignment(activePoint.getSolutionSet());
					activePoint.sort();
					activePoint.isSorted=true;
				}
			}
			
		}
		
		int index=0;
		int activePointLength=activePoints.size();
		
		populationSize=population.size()+populationSize;
		int lifetimeExpires=0;
		
		while(population.size()<populationSize){
			
			ReferencePoint activePoint = activePoints.get(index);
			SolutionSet activeSolutionSet = activePoint.getSolutionSet();
			
			for(int i=0;i<activeSolutionSet.size();i++){
				Solution activeSolution=activeSolutionSet.get(i);
				
				
				
				if(activeSolution.lifeTime>0){
					population.add(activeSolution);
					activeSolutionSet.remove(i);
					break;
				}
				else if(!activeSolution.firstSweep){
					population.add(activeSolution);
					activeSolutionSet.remove(i);
					break;
				}
				else{
					if(activeSolution.lifeTime<0){
						lifetimeExpires++;
					}
					activeSolution.firstSweep=false;
				}
			}
			
			index=(index+1)%activePointLength;
		}
		
		System.out.println("LifeTime expires : "+ lifetimeExpires);
		
		/////////////////
	}

	
	double []min;
	double []max;
	
	public void Normalize(SolutionSet solutionSet){
		
		double normalValue=0;
		
		for(int i=0;i<solutionSet.size();i++){
			
			Solution temp = solutionSet.get(i);
			for(int j=0;j<max.length;j++){
				
				normalValue= (temp.getObjective(j)-min[j])/(max[j]-min[j]);
				//normalValue= (temp.getObjective(j))/(max[j]);
				temp.setFuzzy_objective_value(normalValue, j);
			}
			
		}
	}
	
	public void MinMaxFind(SolutionSet solutionSet) {
	
		min = new double[solutionSet.get(0).numberOfObjectives()];
		max = new double[solutionSet.get(0).numberOfObjectives()];
		
		for(int i=0;i<min.length;i++){
			min[i]= Double.MAX_VALUE;
			max[i]= -Double.MAX_VALUE;
		}
		
		
		for(int i=0;i<solutionSet.size();i++){
			Solution temp = solutionSet.get(i);
			double []tempObj = temp.get_objectives();
			
			for(int j=0;j<min.length;j++){
				if(min[j]>tempObj[j])min[j]=tempObj[j];
				if(max[j]<tempObj[j])max[j]=tempObj[j];
			}
		}
	}
}
