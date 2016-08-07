package jmetal.metaheuritic.FDEAupdated;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import Jama.Matrix;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

public class ClusterMinMaxSamplingMemberUpdate {
	
	MinMaxSorting sidRefDistanceSort = new MinMaxSorting();
	//ShiftDensityBasedSorting sidRefDistanceSort = new ShiftDensityBasedSorting();

	
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
			

			for(int i=0;i<refPoints.size();i++){
				
				temporaryRef=refPoints.get(i);
				score = getSimilarity(temporaryRef, solution);
				
				if(score > best){
					bestRef = refPoints.get(i);
					best = score; 
				}
				
				System.out.println(score);
				
				
				double []A = temporaryRef.getPoint();
				double []B = solution.get_fuzzy_objectives();
				
				System.out.println(A[0]+"\t"+A[1]);
				System.out.println(B[0]+"\t"+B[1]);
				
				double k=0;
				
				for(int p=0;p<A.length;p++){
					k +=A[p]*B[p]; 
				}
				
				System.out.println(k);
				
				
				System.out.println(k/(getMagnitude(A)*getMagnitude(B)));
			}
			
			
			
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
				refPoint.ClearAssociatedSolutions();
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
		
		
		MemParamEstimationSigMoidMembershipUpdate refMemberShipFunction=new MemParamEstimationSigMoidMembershipUpdate();
		
    	SolutionSet solutionSet=new SolutionSet(6);
    	StaticSolutionSet.MakeTwoObjectiveSolution(solutionSet);
    	NormalizeSolutionSetInFuzzy(solutionSet);
    	solutionSet.printSolutionSet();
    	solutionSet.printFuzzySolutionSet();
		
    	
    	ClusterMinMaxSamplingMemberUpdate refPointAlgo=new ClusterMinMaxSamplingMemberUpdate();    	
    	
    	////
		ArrayList<ReferencePoint> refPoints=new ArrayList<ReferencePoint>();	
		refPointAlgo.Experiment(2,2,1.00,refPoints,false);
		
		System.out.println("points\t" + refPoints.size());
		
		for(int i=0;i<refPoints.size();i++){
			refPoints.get(i).printPoint();
		}
		///
		
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
	
	public static void ImpactofReferencePointandFuzzy(){
		String path="D:\\FDEA2016\\Codes\\abcgenerations\\recompileWFG-DTLZ\\FDEA\\backups\\10real\\2\\0.05\\";
		
		MemParamEstimationSigMoidMembershipUpdate refMemberShipFunction=new MemParamEstimationSigMoidMembershipUpdate();
		int populationSize = 8;
		
    	SolutionSet solutionSet=new SolutionSet(populationSize*2);
    	StaticSolutionSet.MakeTwoObjectiveSolution(solutionSet);
    	
    	NormalizeSolutionSetInFuzzy(solutionSet);
    	
    	solutionSet.printSolutionSet();
    	solutionSet.printFuzzySolutionSet();
    	    	
    	solutionSet.printObjectivesToFile(path+"2dlinear1_2.pf");
    	solutionSet.printFuzzySolutionSetToFile(path+"2dlinearfuzzy1_2.pf");
    	
    	ClusterMinMaxSamplingMemberUpdate refPointAlgo=new ClusterMinMaxSamplingMemberUpdate();    	
    	
		ArrayList<ReferencePoint> refPoints=new ArrayList<ReferencePoint>();			
	//	refPointAlgo.Experiment(2,1,1.00,refPoints,false);
		refPoints.add(new ReferencePoint(new double[]{0.5,0.5}));
				
		printReferencePointInfile(refPoints,path+"2referencepoints.ref");
		
		System.out.println("points\t" + refPoints.size());
		
		for(int i=0;i<refPoints.size();i++){
			refPoints.get(i).printPoint();
		}
		///
		
		ArrayList<ReferencePoint> activePoints= refPointAlgo.ActiveReferencePoint(refPoints, solutionSet);
		printReferencePointInfile(activePoints, path+"2activePoints.ref");
		printReferencePointInfile(activePoints, path+"2reducedPoints.ref");
		
		
		
		System.out.println("''''''''''''''''''''''''''''''''");
		
		for(int i=0;i<activePoints.size();i++){
			
			activePoints.get(i).printPoint();
			System.out.println("--->");
			activePoints.get(i).makeAndReturnSolutionSet().printSolutionSet();
			System.out.println("#####");
			
		}
		
		printClusterPointInfile(activePoints, path+"2clusterpoints.refsol");
		printClusterFuzzyPointInfile(activePoints, path+"2clusterfuzzypoints.refsol");
		
		/////////
		
		
		
		SolutionSet population = new SolutionSet(populationSize);
		
		
		takeNextGeneration(activePoints,solutionSet,population,refMemberShipFunction,populationSize);
		
		printMembershipFunction(path+"2dmembership.func",2);
		
		System.out.println("Here");
		solutionSet.printSolutionSet();
		printFitnesstoFile(solutionSet,path+"2dfitnessval.sol");
		System.out.println("Here");
		
		population.printSolutionSet();
		population.printObjectivesToFile(path+"2dlinearsol1_2.pf");
		
	}
	
	static void printMembershipFunction(String path,int obj){
		try {
		      /* Open the file */
		      FileOutputStream fos   = new FileOutputStream(path)     ;
		      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
		      BufferedWriter bw      = new BufferedWriter(osw)        ;
		                        
		      for (int i = 0; i < obj; i++) {
		    	  double mean=MemParamEstimationSigMoidMembershipUpdate.ObjMean[i];
		    	  double var=MemParamEstimationSigMoidMembershipUpdate.Objvar[i];
		    	  double alpha=MemParamEstimationSigMoidMembershipUpdate.APos[i];
		    	  double maxDiff=MemParamEstimationSigMoidMembershipUpdate.maxDiff[i];
		    	  
		    	bw.write(String.valueOf(mean)+"\t"+String.valueOf(var)+"\t"+String.valueOf(alpha)+"\t"+maxDiff);		    			    			    			    			    
		    	bw.newLine();
		      }
		      			      
		      bw.close();
		    }catch (IOException e) {			      
		      e.printStackTrace();
		    }	
	}
	
	static void printFitnesstoFile(SolutionSet solutionsList_,String path){
		try {
		      /* Open the file */
		      FileOutputStream fos   = new FileOutputStream(path)     ;
		      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
		      BufferedWriter bw      = new BufferedWriter(osw)        ;
		                        
		      for (int i = 0; i < solutionsList_.size(); i++) {		        
		    	bw.write(String.valueOf(solutionsList_.get(i).getFitness()));		    			    			    			    			    
		    	bw.newLine();
		      }
		      			      
		      bw.close();
		    }catch (IOException e) {			      
		      e.printStackTrace();
		    }	
	}
	static void printClusterPointInfile(ArrayList<ReferencePoint> solutionsList_,String path){
		try {
		      /* Open the file */
		      FileOutputStream fos   = new FileOutputStream(path)     ;
		      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
		      BufferedWriter bw      = new BufferedWriter(osw)        ;
		                        
		      for (int i = 0; i < solutionsList_.size(); i++) {
		        
		    	bw.write(solutionsList_.get(i).toString());		    	
		    	bw.newLine();		    			    	
		    	SolutionSet solutions=solutionsList_.get(i).getSolutionSet();
		    	
		    	for(int j=0;j<solutions.size();j++){
			        bw.write(solutions.get(j).toString());
			        bw.newLine();
		    	}		 
		    	bw.write("-1 -1");
		    	bw.newLine();
		      }
		      			      
		      bw.close();
		    }catch (IOException e) {			      
		      e.printStackTrace();
		    }		
	}
	
	static void printClusterFuzzyPointInfile(ArrayList<ReferencePoint> solutionsList_,String path){
		try {
		      /* Open the file */
		      FileOutputStream fos   = new FileOutputStream(path)     ;
		      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
		      BufferedWriter bw      = new BufferedWriter(osw)        ;
		                        
		      for (int i = 0; i < solutionsList_.size(); i++) {
		        
		    	bw.write(solutionsList_.get(i).toString());
		    	bw.newLine();		    			    	
		    	SolutionSet solutions=solutionsList_.get(i).getSolutionSet();
		    	
		    	for(int j=0;j<solutions.size();j++){
			        bw.write(solutions.get(j).toFuzzString());
			        bw.newLine();
		    	}		 
		    	bw.write("-1 -1");
		    	bw.newLine();
		      }
		      			      
		      bw.close();
		    }catch (IOException e) {			      
		      e.printStackTrace();
		    }		
	}
	
	static void printReferencePointInfile(ArrayList<ReferencePoint> solutionsList_,String path){
		 
			    try {
			      /* Open the file */
			      FileOutputStream fos   = new FileOutputStream(path)     ;
			      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
			      BufferedWriter bw      = new BufferedWriter(osw)        ;
			                        
			      for (int i = 0; i < solutionsList_.size(); i++) {
			        //if (this.vector[i].getFitness()<1.0) {
			        bw.write(solutionsList_.get(i).toString());
			        bw.newLine();
			        //}
			      }
			      			      
			      bw.close();
			    }catch (IOException e) {			      
			      e.printStackTrace();
			    }		
	}
	
	static void  printReferencePoint(ArrayList<ReferencePoint> activePoints, String Name){
		
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#######"+Name+"################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		
		
		for(int i=0;i<activePoints.size();i++){
			activePoints.get(i).printPoint();
		}
		
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("##################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		
	}
	
	
	public void  takeSolution(SolutionSet solutionSet,SolutionSet population, int remain,ArrayList<ReferencePointSettings> refSettings,MemParamEstimationSigMoidMembershipUpdate refMembershipFunction){
		
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
		*/
		
		
		ArrayList<ReferencePoint> activePoints= ActiveReferencePoint(refPoints, solutionSet);
		
		/**
		 * Reducing active points
		 */
		//
		
		System.out.println("ReferencePoints :"+refPoints.size());
		System.out.println("Actual Active Points : "+activePoints.size());
		
		int RequiredSize = remain/FDEA_Main.onAvg;
		//int RequiredSize = refPoints.get(0).numberOfObjectives;
		
		if(activePoints.size()>RequiredSize){
			
			
			sidRefDistanceSort.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);
			
			//sidRefDistanceCrowd.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);			
			//Collections.sort(activePoints,new SidCrowdingComparator());
			
			
			//printReferencePoint(refPoints,"Global Reference Points");			
			//printReferencePoint(activePoints,"Active Points");

			/*
			for(int i=0;i<activePoints.size();i++){
				System.out.println(activePoints.get(i).crowdingDistance);
			}
			*/
			
			
			while(activePoints.size()>RequiredSize){
				activePoints.remove(activePoints.size()-1);
			}
			
			//printReferencePoint(activePoints,"Active Points After reduction");
			
			
			
			System.out.println("Crowding Active Points : "+activePoints.size());			 
			activePoints= ActiveReferencePoint(activePoints, solutionSet);
			
			System.out.println("After Crowding Active Points : "+activePoints.size());			
			
			
		}
	
		
		//ModifyActivePointsWithClustering(activePoints);		
		//activePoints= ActiveReferencePoint(activePoints, solutionSet);
		//System.out.println("Active Points after Clustering : "+activePoints.size());
	
		
		//printReferencePoint(activePoints,"Active Points After smoothing");
		
		
		sidRefDistanceSort.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);
		
		//sidRefDistanceCrowd.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);						
		//Collections.sort(activePoints,new SidCrowdingComparator());
		
		
		
		
		
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

	public static void takeNextGeneration(ArrayList<ReferencePoint> activePoints,SolutionSet solutionSet,SolutionSet population,MemParamEstimationSigMoidMembershipUpdate refMemberShipFunction,int populationSize) {

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

	public static double evaluateValue(double []direction, double []objectives,double []minVector){
		
		double value=0;
		
		for(int i=0;i<direction.length;i++){
			value+=(objectives[i]-minVector[i])/direction[i];
		}
		
		return value;
	}
	
	public static double[] computeIntercepts(Solution []extremeSolutions,double []zideal,double []zmax) {
		int obj=extremeSolutions.length;
		
		
		
		double intercepts[] = new double[obj];

		double[][] temp = new double[obj][obj];

		for (int i = 0; i < obj; i++) {
			for (int j = 0; j < obj; j++) {
				double val = extremeSolutions[i].getObjective(j) - zideal[j];
				temp[i][j] = val;
			}
		}

		Matrix EX = new Matrix(temp);

		if (EX.rank() == EX.getRowDimension()) {
			double[] u = new double[obj];
			for (int j = 0; j < obj; j++)
				u[j] = 1;

			Matrix UM = new Matrix(u, obj);

			Matrix AL = EX.inverse().times(UM);

			int j = 0;
			for (j = 0; j < obj; j++) {

				double aj = 1.0 / AL.get(j, 0) + zideal[j];

				if ((aj > zideal[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj)))
					intercepts[j] = aj;
				else
					break;
			}
			if (j != obj) {
				for (int k = 0; k < obj; k++)
					intercepts[k] = zmax[k];
			}

		} else {
			for (int k = 0; k < obj; k++)
				intercepts[k] = zmax[k];
		}
		
		return intercepts;
		
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
		/*		
		for(int i=0;i<min.length;i++){
			System.out.printf("%f\t",min[i]);
		}
		System.out.println();
		for(int i=0;i<max.length;i++){
			System.out.printf("%f\t",max[i]);
		}*/
		
		///before
		
		int numberOfObjectives=min.length;
		
		
		//stuff that now i am about to do following NSGAIII er work
		double [][]directionVector=new double[numberOfObjectives][numberOfObjectives];
		Solution []extremeSolution=new Solution[numberOfObjectives];
		double []directionValue=new double[numberOfObjectives];
		
		
		for(int i=0;i<numberOfObjectives;i++){
			for(int j=0;j<numberOfObjectives;j++){
				directionVector[i][j]=0.000001;
			}
		}
		
		for(int i=0;i<numberOfObjectives;i++){
			directionVector[i][i]=1;
			directionValue[i]=Double.MAX_VALUE;
		}
		
		
		
		for(int i=0;i<solutionSet.size();i++){
			
			Solution temp = solutionSet.get(i);
			double []objectives=temp.get_objectives();
			
			for(int dir=0;dir<numberOfObjectives;dir++){
				double value=evaluateValue(directionVector[dir], objectives, min);				
				if(value<directionValue[dir]){
					directionValue[dir]=value;
					extremeSolution[dir]=temp;
				}
			}								
		}
		/*
		System.out.println();
		
		for(int i=0;i<extremeSolution.length;i++){
			for(int dir=0;dir<numberOfObjectives;dir++){				
				System.out.print(extremeSolution[i].getObjective(dir)+" ");
			}
			System.out.println();
		}
		*/
		
		double []intercepts=computeIntercepts(extremeSolution, min, max);
		
		/*
		for(int i=0;i<numberOfObjectives;i++){
			System.out.print(intercepts[i]+"\t");
		}
		System.out.println();
		*/
		
		//System.exit(0);
		
		///before
		
		double normalValue=0;
		
		for(int i=0;i<solutionSet.size();i++){
			
			Solution temp = solutionSet.get(i);
			double totalValue=0;
			for(int j=0;j<max.length;j++){
				
				//normalValue= (temp.getObjective(j)-min[j])/(max[j]-min[j]);
				normalValue= (temp.getObjective(j)-min[j])/(intercepts[j]-min[j]);
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
					//normalValue= (temp.getObjective(j)-min[j])/(max[j]-min[j]);
					normalValue= (temp.getObjective(j)-min[j])/(intercepts[j]-min[j]);				
					temp.setFuzzy_objective_value(normalValue/totalValue, j);
				}
			}
		}
	}
	
	
	public void  takeSolutionGaussian(SolutionSet solutionSet,SolutionSet population, int remain,ArrayList<ReferencePointSettings> refSettings,MemParamEstimationGaussian refMembershipFunction){
		
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
		*/
		
		
		ArrayList<ReferencePoint> activePoints= ActiveReferencePoint(refPoints, solutionSet);
		
		/**
		 * Reducing active points
		 */
		//
		
		System.out.println("ReferencePoints :"+refPoints.size());
		System.out.println("Actual Active Points : "+activePoints.size());
		
		int RequiredSize = solutionSet.size()/FDEA_Main.onAvg;
		//int RequiredSize = refPoints.get(0).numberOfObjectives;
		
		if(activePoints.size()>RequiredSize){
			
			
			sidRefDistanceSort.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);
			
			//sidRefDistanceCrowd.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);			
			//Collections.sort(activePoints,new SidCrowdingComparator());
			
			
			//printReferencePoint(refPoints,"Global Reference Points");			
			//printReferencePoint(activePoints,"Active Points");

			/*
			for(int i=0;i<activePoints.size();i++){
				System.out.println(activePoints.get(i).crowdingDistance);
			}
			*/
			
			
			while(activePoints.size()>RequiredSize){
				activePoints.remove(activePoints.size()-1);
			}
			
			//printReferencePoint(activePoints,"Active Points After reduction");
			
			
			
			System.out.println("Crowding Active Points : "+activePoints.size());			 
			activePoints= ActiveReferencePoint(activePoints, solutionSet);
			
			System.out.println("After Crowding Active Points : "+activePoints.size());			
			
			
		}
	
		
		//ModifyActivePointsWithClustering(activePoints);		
		//activePoints= ActiveReferencePoint(activePoints, solutionSet);
		//System.out.println("Active Points after Clustering : "+activePoints.size());
	
		
		//printReferencePoint(activePoints,"Active Points After smoothing");
		
		
		sidRefDistanceSort.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);
		
		//sidRefDistanceCrowd.crowdingDistanceAssignment(activePoints,refSettings.get(0).numberOfObjectives);						
		//Collections.sort(activePoints,new SidCrowdingComparator());
		
		
		
		
		
		System.out.println("Actual Active point used "+activePoints.size());
		
		takeNextGenerationGaussian(activePoints,solutionSet,population,refMembershipFunction,remain);
		
		
		
	}

	public static void takeNextGenerationGaussian(ArrayList<ReferencePoint> activePoints,SolutionSet solutionSet,SolutionSet population,MemParamEstimationGaussian refMemberShipFunction,int populationSize) {

		refMemberShipFunction.setfmaxfmins(solutionSet);
		
		for(int i=0;i<activePoints.size();i++){
			
			ReferencePoint activePoint=activePoints.get(i);
			
			if(activePoint.getSolutionSet().size()>1)
			{
				if(!activePoint.isSorted){
					refMemberShipFunction.FitnessAssignment(activePoint.getSolutionSet());
					activePoint.reversesort();
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

	
}
