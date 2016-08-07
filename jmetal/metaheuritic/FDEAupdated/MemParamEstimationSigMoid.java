package jmetal.metaheuritic.FDEAupdated;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

public class MemParamEstimationSigMoid {
	
	public static double []ObjMean;
	public static double []Objvar;
	public static double []A;
	public static double []APos;
	
	public static double []maxDiff;

	
	double m_n;
	double []m_oldM;
	double []m_newM;
	double []m_oldS;
	double []m_newS;
	double totalSolutionNo;
	
	
	
	
	public void Initialization(SolutionSet solutionSet){
		int numberOfOjectives=solutionSet.get(0).numberOfObjectives();
		
		
		
		
		totalSolutionNo=solutionSet.size();
		
		ObjMean=new double[numberOfOjectives];
		Objvar=new double[numberOfOjectives];
		A=new double[numberOfOjectives];
		APos=new double[numberOfOjectives];
		
		m_n=0;
		m_oldM=new double[numberOfOjectives];;
		m_newM=new double[numberOfOjectives];;
		m_oldS=new double[numberOfOjectives];;
		m_newS=new double[numberOfOjectives];;
		
		maxDiff=new double[numberOfOjectives];;		
		for(int i=0;i<numberOfOjectives;i++){
			maxDiff[i]=-Double.MAX_VALUE;		
		}
		
		
		
		
		for (int i = 0; i < solutionSet.size(); i++) {
			Solution solution = solutionSet.get(i);

			for (int j = i + 1; j < solutionSet.size(); j++) {
				if (i == j)continue;
				
				Solution comPareSolution = solutionSet.get(j);
				pushValue(solution, comPareSolution);

			}
		}
		
		
		
		ObjMean=m_newM;
		
		for(int i=0;i<numberOfOjectives;i++){
			Objvar[i]=Math.sqrt(m_newS[i]/(m_n - 1));
			
			//A[i]=SigMoid.CalculateA(ObjMean[i]-2*Objvar[i],.99, ObjMean[i]);						
			//System.out.println(A[i]+"\t"+ObjMean[i]);			
			
			A[i]=SigMoid.CalculateA(-ObjMean[i]+1.0*Objvar[i],.99,-ObjMean[i]);
			//APos[i]=SigMoid.CalculateA(ObjMean[i]-1.0*Objvar[i],.99,ObjMean[i]);
			APos[i]=SigMoid.CalculateA(-ObjMean[i]-1*Objvar[i],0.99,0);
			
		}
		
		
		//printArray(APos);
		//printArray(Objvar);
		//printArray(ObjMean);
		
		//System.out.println(SigMoid.CalculateA(0, .99, 6));
		
		//System.exit(0);
		 
		//System.out.println("---------------------------------------");
		/*
		printArray(ObjMean);
		printArray(Objvar);
		printArray(pseduoMin);
		printArray(pseduoMax);
		*/
		
		//printArray(ObjMean);
	}
	
	public void printArray(double []value){
		for(int i=0;i<value.length;i++){
			System.out.printf("%.2f\t",value[i]);
		}
		
		System.out.println();
	}
	
	public void pushValue(Solution solution1,Solution solution2){
		
		double []x1=solution1.get_objectives();
		double []x2=solution2.get_objectives();
		
		double []x=new double[solution1.numberOfObjectives()];
		
		
		m_n++;

        // See Knuth TAOCP vol 2, 3rd edition, page 232
        if (m_n == 1)
        {
        	for(int i=0;i<x.length;i++){
        		x[i]=Math.abs(x1[i]-x2[i]);
        		if(x[i]>maxDiff[i])maxDiff[i]=x[i];
        		m_oldM[i] = m_newM[i] = x[i];
                m_oldS[i] = 0.0;
        	}
        }
        else
        {
        	for(int i=0;i<x.length;i++){
        		
        		x[i]=Math.abs(x1[i]-x2[i]);
        		if(x[i]>maxDiff[i])maxDiff[i]=x[i];
	            m_newM[i] = m_oldM[i] + (x[i] - m_oldM[i])/m_n;
	            m_newS[i] = m_oldS[i] + (x[i] - m_oldM[i])*(x[i] - m_newM[i]);
	
	            // set up for next iteration
	            m_oldM[i] = m_newM[i]; 
	            m_oldS[i] = m_newS[i];
        	}
        }
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
			
			
			
			//value = SigMoid.SigMoidValue(diff, APos[i],ObjMean[i]);
			value = SigMoid.SigMoidValue(diff, APos[i],0);
			dominateBy *= value;
			
			if(debug)System.out.print(i+" ---> "+value);
			
			/*System.out.println("---------dominateBy-------------");
			System.out.println(diff);
			System.out.println(value);*/
			
			//value = SigMoid.SigMoidValue(-diff, APos[i],ObjMean[i]);
			value = SigMoid.SigMoidValue(-diff, APos[i],0);
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
			tempMatrix[tmpi][tmpj]=dominateByWR;
			tempMatrix[tmpj][tmpi]=dominatedByWR;
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
