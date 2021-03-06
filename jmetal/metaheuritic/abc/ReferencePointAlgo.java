package jmetal.metaheuritic.abc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

public class ReferencePointAlgo {
	
	SidRefDistance sidRefDistance = new SidRefDistance();
	
	

	
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
		
		if(getMagnitude(B)==0)return 1;
		
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
		
		for(int i=0;i<refPoints.size();i++){
			refPoints.get(i).ClearAssociatedSolutions();
		}
		
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
		
		for(int i=0;i<activePoints.size();i++){
			activePoints.get(i).makeAndReturnSolutionSet();
		}
		
		
		return activePoints;
		
	}

	public static void SimpleExperiment(){
		/*
		int populationSize = ((Integer) getInputParameter("populationSize")).intValue();
    	int maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
    	  */
		
		
		ReferenceMemberShipFunction refMemberShipFunction=new ReferenceMemberShipFunction();
		
    	SolutionSet solutionSet=new SolutionSet(6);
    	StaticSolutionSet.MakeTwoObjectiveSolution(solutionSet);
    	NormalizeSolutionSetInFuzzy(solutionSet);
    	solutionSet.printSolutionSet();
    	solutionSet.printFuzzySolutionSet();
		
    	
    	ReferencePointAlgo refPointAlgo=new ReferencePointAlgo();    	
    	
    	////
		ArrayList<ReferencePoint> refPoints=new ArrayList<ReferencePoint>();	
		refPointAlgo.Experiment(3,4,1.00,refPoints,false);
		
		System.out.println("points\t" + refPoints.size());
		
		for(int i=0;i<refPoints.size();i++){
			refPoints.get(i).printPoint();
		}
		///
		
		System.exit(0);
		
		ArrayList<ReferencePoint> activePoints= refPointAlgo.ActiveReferencePoint(refPoints, solutionSet);
		
		System.out.println("''''''''''''''''''''''''''''''''");
		
		for(int i=0;i<activePoints.size();i++){
			
			activePoints.get(i).printPoint();
			System.out.println("--->");
			activePoints.get(i).makeAndReturnSolutionSet().printSolutionSet();
			System.out.println("#####");
			
		}
		
		/////////
		
		int populationSize = 3;
		
		SolutionSet population = new SolutionSet(populationSize);
		
		
		takeNextGeneration(activePoints,solutionSet,population,refMemberShipFunction,populationSize);
		
		population.printSolutionSet();
		
	}
	
	
	public void  takeSolution(SolutionSet solutionSet,SolutionSet population, int remain,ArrayList<ReferencePointSettings> refSettings,ReferenceMemberShipFunction refMembershipFunction){
		
		NormalizeSolutionSetInFuzzy(solutionSet);
		
		
		ArrayList<ReferencePoint> refPoints=new ArrayList<ReferencePoint>();	
		
		
		for(int i=0;i<refSettings.size();i++){
			Experiment(
					refSettings.get(i).numberOfObjectives,
					refSettings.get(i).numberOfDivision,
					refSettings.get(i).limit,
					refPoints,
					refSettings.get(i).includeLimit
				);
			
		}
		
		//GenerateRandomly(refPoints,refSettings.get(0).numberOfObjectives,refSettings.get(0).numberOfDivision);
		
		/*
		System.out.println("------------->>");
		for(int i=0;i<refPoints.size();i++){
			refPoints.get(i).printPoint();
		}
		
		System.exit(0);
		*/
		
		ArrayList<ReferencePoint> activePoints= ActiveReferencePoint(refPoints, solutionSet);
		
		/**
		 * Reducing active points
		 */
		//
		
		System.out.println("ReferencePoints :"+refPoints.size());
		System.out.println("Actual Active Points : "+activePoints.size());
		
		int RequiredSize = solutionSet.size()/2;
		
		if(activePoints.size()>RequiredSize){
			sidRefDistance.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);
			
			Collections.sort(activePoints,new SidCrowdingComparator());	
			
			/* 
			for(int i=0;i<activePoints.size();i++){
				System.out.println(activePoints.get(i).crowdingDistance);
			}
			*/
			
			while(activePoints.size()>RequiredSize){
				activePoints.remove(activePoints.size()-1);
			}
			
			
			System.out.println("Crowding Active Points : "+activePoints.size());			 
			activePoints= ActiveReferencePoint(activePoints, solutionSet);
			
			System.out.println("After Crowding Active Points : "+activePoints.size());			
			
			/*
			ModifyActivePointsWithClustering(activePoints);
			
			activePoints= ActiveReferencePoint(activePoints, solutionSet);
			System.out.println("Active Points after Clustering : "+activePoints.size());
			
			*/
			
		}
	
		
		sidRefDistance.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);		
		Collections.sort(activePoints,new SidCrowdingComparator());	
		
		System.out.println("Actual Active point used "+activePoints.size());
		
		takeNextGeneration(activePoints,solutionSet,population,refMembershipFunction,remain);
		
		
		
	}
	
	public void ModifyActivePointsWithClustering(ArrayList<ReferencePoint> activePoints){
		
		int noOfObjectives=activePoints.get(0).numberOfObjectives;
		
		for(int i=0;i<activePoints.size();i++){
			ReferencePoint ref = activePoints.get(i);
			
			SolutionSet sSet=ref.getSolutionSet();
			
			double []total=new double[noOfObjectives];
			for(int j=0;j<total.length;j++)total[j]=0;
			
			for(int j=0;j<sSet.size();j++){
				
				Solution solution=sSet.get(j);
				
				for(int k=0;k<noOfObjectives;k++){
					total[k]+=solution.get_Fuzzy_objective_value(k);
				}									
			}
			
			
			double size=sSet.size();
			
			for(int j=0;j<ref.numberOfObjectives;j++){
				ref.point[j]=(ref.point[j]+total[j])/(size+1);
			}
			
		}
		
		
		return;
	}
	
	public static void GenerateRandomly(ArrayList<ReferencePoint> refPoints,int numberOfObjectives, int size) {
		Random rand= new Random();
		int count=0;
		
		
		while(count<size){
			
			double []arr=new double[numberOfObjectives];
			double total=0;
			
			for(int i=0;i<numberOfObjectives;i++){
				arr[i]=rand.nextDouble();
				total+=arr[i];
			}
			
			for(int i=0;i<numberOfObjectives;i++){
				arr[i]=arr[i]/total;
			}
			
			refPoints.add(new ReferencePoint(arr));
			
			count++;
		}
		
		
	}

	public static void takeNextGeneration(ArrayList<ReferencePoint> activePoints,SolutionSet solutionSet,SolutionSet population,ReferenceMemberShipFunction refMemberShipFunction,int populationSize) {

		refMemberShipFunction.Initialization(solutionSet);
		
		for(int i=0;i<activePoints.size();i++){
			ReferencePoint activePoint=activePoints.get(i);
			
			if(activePoint.getSolutionSet().size()>1)
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

	public static  void NormalizeSolutionSetInFuzzy(SolutionSet solutionSet) {
		double []min = new double[solutionSet.get(0).numberOfObjectives()];
		double []max = new double[solutionSet.get(0).numberOfObjectives()];
		
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
		
		/*for(int i=0;i<min.length;i++){
			System.out.printf("%f\t",min[i]);
		}
		System.out.println();
		for(int i=0;i<max.length;i++){
			System.out.printf("%f\t",max[i]);
		}*/
		
		double normalValue=0;
		
		for(int i=0;i<solutionSet.size();i++){
			
			Solution temp = solutionSet.get(i);
			double totalValue=0;
			for(int j=0;j<max.length;j++){
				
				normalValue= (temp.getObjective(j)-min[j])/(max[j]-min[j]);
				totalValue+=normalValue;
				//normalValue= (temp.getObjective(j))/(max[j]);
				//temp.setFuzzy_objective_value(normalValue, j);
			}
			//sid: modification to relative change
						
			
			if(totalValue==0){
				for(int j=0;j<max.length;j++){										
					temp.setFuzzy_objective_value(1/(double)max.length, j);
				}
			}
			else{
				for(int j=0;j<max.length;j++){
					
					normalValue= (temp.getObjective(j)-min[j])/(max[j]-min[j]);				
					temp.setFuzzy_objective_value(normalValue/totalValue, j);
				}
			}
		}
	}

	
}
