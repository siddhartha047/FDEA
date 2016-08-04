package jmetal.metaheuritic.FDEAupdated;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

public class MemParamEstimationSigMoidMembershipUpdate {
	
	public static double []ObjMean;
	public static double []Objvar;
	public static double []A;
	public static double []APos;
	
	public static double []ObjMeanValue;
	public static double []ObjvarValue;
	public static double []maxDiff;
	public static double []minDiff;
	
	double totalSolutionNo;
	
	
	public void calculateMean(SolutionSet solutionSet){
		for(int i=0;i<ObjMeanValue.length;i++)ObjMeanValue[i]=0;
		double size=solutionSet.size();
		for (int i = 0; i < solutionSet.size(); i++) {
			Solution solution = solutionSet.get(i);
			
			for(int j=0;j<ObjMeanValue.length;j++)ObjMeanValue[j]+=solution.getObjective(j);
			
		}
		for(int i=0;i<ObjMeanValue.length;i++)ObjMeanValue[i]=ObjMeanValue[i]/size;
	}
	
	public void calculateVariance(SolutionSet solutionSet){
		for(int i=0;i<ObjvarValue.length;i++)ObjvarValue[i]=0;
		double size=solutionSet.size();
		for (int i = 0; i < solutionSet.size(); i++) {
			Solution solution = solutionSet.get(i);
			
			for(int j=0;j<ObjvarValue.length;j++){
				ObjvarValue[j]+=(solution.getObjective(j)-ObjMeanValue[j])*(solution.getObjective(j)-ObjMeanValue[j]);
			}
			
		}
		for(int i=0;i<ObjvarValue.length;i++)ObjvarValue[i]=Math.sqrt(ObjvarValue[i]/(size-1));
	}
	
	public void calculateDifferenceMean(SolutionSet solutionSet){
		int nobj=solutionSet.get(0).numberOfObjectives();
		double count[]=new double[nobj];
		double minL=0,maxL=0,x,y;
		
		for(int k=0;k<nobj;k++){
			count[k]=0;
			ObjMean[k]=0;
		}
		
		
		
		for (int i = 0; i < solutionSet.size(); i++) {
			Solution solution = solutionSet.get(i);

			for (int j = i + 1; j < solutionSet.size(); j++) {
				if (i == j)continue;
				
				Solution comPareSolution = solutionSet.get(j);
				
				
				for(int k=0;k<nobj;k++){
					minL=ObjMeanValue[k]-2*ObjvarValue[k];
					maxL=ObjMeanValue[k]+2*ObjvarValue[k];
					x=solution.getObjective(k);
					y=comPareSolution.getObjective(k);
					if(Math.abs(x-y)>maxDiff[k])maxDiff[k]=Math.abs(x-y);
					if(x>=minL && x<=maxL && y>=minL && y<=maxL){
						ObjMean[k]+=Math.abs(x-y);
						count[k]++;
					}
				}
				
			}
		}
		
		for(int k=0;k<nobj;k++)ObjMean[k]=ObjMean[k]/count[k];
		
		for(int k=0;k<nobj;k++){
			count[k]=0;
			Objvar[k]=0;
		}
		
		
		
		for (int i = 0; i < solutionSet.size(); i++) {
			Solution solution = solutionSet.get(i);

			for (int j = i + 1; j < solutionSet.size(); j++) {
				if (i == j)continue;
				
				Solution comPareSolution = solutionSet.get(j);
				
				
				for(int k=0;k<nobj;k++){
					minL=ObjMeanValue[k]-2*ObjvarValue[k];
					maxL=ObjMeanValue[k]+2*ObjvarValue[k];
					x=solution.getObjective(k);
					y=comPareSolution.getObjective(k);					
					if(x>=minL && x<=maxL && y>=minL && y<=maxL){
						Objvar[k]+=(Math.abs(x-y)-ObjMean[k])*(Math.abs(x-y)-ObjMean[k]);
						count[k]++;
					}
				}
				
			}
		}
		
		for(int k=0;k<nobj;k++){
			Objvar[k]=Math.sqrt(Objvar[k]/(count[k]-1));
		}
		
	}
	
	public void Initialization(SolutionSet solutionSet){
		int numberOfOjectives=solutionSet.get(0).numberOfObjectives();
		
		ObjMeanValue=new double[numberOfOjectives];
		ObjvarValue=new double[numberOfOjectives];
		maxDiff=new double[numberOfOjectives];;
		minDiff=new double[numberOfOjectives];;
		for(int i=0;i<numberOfOjectives;i++){
			maxDiff[i]=-Double.MAX_VALUE;
			minDiff[i]=Double.MIN_VALUE;
		}
		
		
		calculateMean(solutionSet);
		calculateVariance(solutionSet);
		
		totalSolutionNo=solutionSet.size();
		
		ObjMean=new double[numberOfOjectives];
		Objvar=new double[numberOfOjectives];
		
		calculateDifferenceMean(solutionSet);
		
		A=new double[numberOfOjectives];
		APos=new double[numberOfOjectives];
		
		
		for(int i=0;i<numberOfOjectives;i++){
				
			
			A[i]=SigMoid.CalculateA(-ObjMean[i]+1.0*Objvar[i],.99,-ObjMean[i]);
			//APos[i]=SigMoid.CalculateA(ObjMean[i]-1.0*Objvar[i],.99,ObjMean[i]);
			APos[i]=SigMoid.CalculateA(-ObjMean[i]-1.0*Objvar[i],0.99,0);
			
		}
		
		
	
	}
	
	public void printArray(double []value){
		for(int i=0;i<value.length;i++){
			System.out.printf("%.2f\t",value[i]);
		}
		
		System.out.println();
	}
	
		
	double tempMatrix[][];
	int tmpi;
	int tmpj;
	boolean debug=false;
	
	public void FitnessAssignment(SolutionSet solutionSet) {
		totalSolutionNo=solutionSet.size();		
		for(int i=0;i<solutionSet.size();i++){
			Solution solution = solutionSet.get(i);
			solution.setFitness(0);
		}
		if(debug){
			tempMatrix=new double[(int) totalSolutionNo][(int) totalSolutionNo];
		}
		
		for (int i = 0; i < solutionSet.size(); i++) {
			Solution solution = solutionSet.get(i);

			for (int j = i + 1; j < solutionSet.size(); j++) {
				if (i == j)continue;
				
				if(debug){
					System.out.print("("+i+","+j+") -> ");
					tmpi=i;
					tmpj=j;
				}
				
				
				Solution comPareSolution = solutionSet.get(j);
				CompareSolution(solution, comPareSolution);

			}
		}
		
		if(debug){
			for (int i = 0; i < solutionSet.size(); i++) {			
				for (int j = 0; j < solutionSet.size(); j++) {
					System.out.printf("%.3f\t",tempMatrix[i][j]);
				}
				System.out.println();
			}	
		}
		// solutionSet.printSolutionSet();
	}

	public void CompareSolution(Solution mainSolution, Solution compareSolution) {
		double[] mainObjectives = mainSolution.get_objectives();
		double[] compareObjectives = compareSolution.get_objectives();
		
		double dominateBy=1;
		double dominatedBy=1;
		
		double better=0;
		double equal=0;
		double worse=0;
		
		double value;
		
		for (int i = 0; i < mainObjectives.length; i++) {

			double diff=mainObjectives[i]-compareObjectives[i];
			
			if(diff==0){
				equal++;
				//System.out.println("rare");
			}
			else if(diff<0){
				better++;				
			}
			else{
				worse++;
			}
			
			
			
			value = SigMoid.SigMoidValue(diff, APos[i],ObjMean[i]);
			dominateBy *= value;
			
			if(debug)System.out.print(i+" ---> "+value);
			
			/*System.out.println("---------dominateBy-------------");
			System.out.println(diff);
			System.out.println(value);*/
			
			value = SigMoid.SigMoidValue(-diff, APos[i],ObjMean[i]);
			dominatedBy *= value;
			
			if(debug)System.out.println("\t"+i+" ---> "+value);
			
			
			/*System.out.println("dominatedBy");
			System.out.println(-diff);
			System.out.println(value);
			*/
		}
		/*
		
		if(better+equal==mainObjectives.length && better>worse){
			dominatedBy=0;
			dominateBy=1;
		}
		else if(worse+equal==mainObjectives.length && worse>better){
			dominatedBy=0;
			dominateBy=1;
		}
		*/
		
		if(debug)System.out.println(dominateBy+"\t"+dominatedBy);
		
		
		double dominatedByWR=(dominatedBy)/(dominateBy+dominatedBy);
		double dominateByWR=(dominateBy)/(dominateBy+dominatedBy);
		
		if(debug){
			System.out.println(dominatedByWR+"\t"+dominateByWR);
			tempMatrix[tmpi][tmpj]=dominatedByWR;
			tempMatrix[tmpj][tmpi]=dominateByWR;
		}
		
		//System.exit(0);
		
		//System.out.println(dominatedBy+"-"+dominateBy+"\t"+dominatedByWR+"-"+dominateByWR);
		
		//System.out.println(dominatedByWR+"\t"+dominateByWR);
		
		double mainfit=mainSolution.getFitness();
		mainfit+=dominatedByWR/(totalSolutionNo-1);
		//mainfit+=dominateByWR/(totalSolutionNo-1);
		//mainfit+=dominateByWR;
		
		mainSolution.setFitness(mainfit);
		
		double compareFit=compareSolution.getFitness();
		compareFit+=dominateByWR/(totalSolutionNo-1);
		//compareFit+=dominatedByWR/(totalSolutionNo-1);
		//compareFit+=dominatedByWR;
		  
		compareSolution.setFitness(compareFit);
		
	}
	
	
}
