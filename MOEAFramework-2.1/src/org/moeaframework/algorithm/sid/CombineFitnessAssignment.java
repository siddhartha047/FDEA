package org.moeaframework.algorithm.sid;

import static org.moeaframework.core.FastNondominatedSorting.CROWDING_ATTRIBUTE;

import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;



public class CombineFitnessAssignment {
		double fit_mean;
		double fit_var;
		double fit_A;
		
		
		double crowd_mean;
		double crowd_var;
		double crowd_A;
		
		double maxGeneration;
		
		public CombineFitnessAssignment(double maxmimumGeneration){
			maxGeneration=maxmimumGeneration;
		}
		
		
		public void Combine(Population solutionSet){
			setMeanVariance(solutionSet);
			
			
			for(int i=0;i<solutionSet.size();i++){
				Solution solution=solutionSet.get(i);
				
				double fit=solution.getFitness();
				double crowd=(Double)solution.getAttribute(CROWDING_ATTRIBUTE);
				
				
				double fuzFit=SigMoid.SigMoidValue(fit, fit_A, fit_mean);
				
				double fuzCrowd;
				if(Double.isInfinite(fuzFit))
					fuzCrowd=1;
				else
					fuzCrowd=SigMoid.SigMoidValue(crowd, crowd_A, crowd_mean);
				
				double gamma=.5;
				
				double stGamma=1;
				double endGamma=0.5;
				
				
				double x=0.90;
				fuzCrowd=x+(1-x)*(fuzCrowd);
				
				double comBineFitness=fuzFit*fuzCrowd;
				
				solution.setFitness(comBineFitness);
				
			}
			
			
			
		}		
		
		public void setMeanVariance(Population solutionSet){

			double fitm_n=0;
			double fitm_oldM=0;
			double fitm_newM=0;
			double fitm_oldS=0;
			double fitm_newS=0;
			
			double crowdm_n=0;
			double crowdm_oldM=0;
			double crowdm_newM=0;
			double crowdm_oldS=0;
			double crowdm_newS=0;
			
			
			for(int i=0;i<solutionSet.size();i++){
				Solution solution=solutionSet.get(i);
				
				double fitx=solution.getFitness();
				///////////////
			
				fitm_n++;

	            // See Knuth TAOCP vol 2, 3rd edition, page 232
	            if (fitm_n == 1)
	            {
	                fitm_oldM = fitm_newM = fitx;
	                fitm_oldS = 0.0;
	            }
	            else
	            {
	            	fitm_newM = fitm_oldM + (fitx - fitm_oldM)/fitm_n;
	            	fitm_newS = fitm_oldS + (fitx - fitm_oldM)*(fitx - fitm_newM);
	    
	                // set up for next iteration
	            	fitm_oldM = fitm_newM; 
	            	fitm_oldS = fitm_newS;
	            }
				
				
				
				///////////////////////
	            double crowdx=(Double)solution.getAttribute(CROWDING_ATTRIBUTE);
				
				if(Double.isInfinite(crowdx))continue;
				
				crowdm_n++;

	            // See Knuth TAOCP vol 2, 3rd edition, page 232
	            if (crowdm_n == 1)
	            {
	            	crowdm_oldM = crowdm_newM = crowdx;
	            	crowdm_oldS = 0.0;
	            }
	            else
	            {
	            	crowdm_newM = crowdm_oldM + (crowdx - crowdm_oldM)/crowdm_n;
	            	crowdm_newS = crowdm_oldS + (crowdx - crowdm_oldM)*(crowdx - crowdm_newM);
	    
	                // set up for next iteration
	            	crowdm_oldM = crowdm_newM; 
	            	crowdm_oldS = crowdm_newS;
	            }
				
			}
			
			
			//System.out.println(fit[0]+"\t"+fit[1]);
			//System.out.println(crowd[0]+"\t"+crowd[1]);
			
			fit_mean=fitm_newM; 
			fit_var=Math.sqrt(fitm_newS/(fitm_n - 1));
			
			crowd_mean=crowdm_newM;
			crowd_var=Math.sqrt(crowdm_newS/(crowdm_n - 1));
			
			
			
			
			//if(fit_var==0)
			
			fit_A=SigMoid.CalculateA(fit_mean-2*fit_var,.99,fit_mean);
			crowd_A=SigMoid.CalculateA(crowd_mean+2*crowd_var,.99,crowd_mean);
			
		//	System.out.println(fit_mean+"\t"+fit_var+"\t"+fit_A);
		//	System.out.println(crowd_mean+"\t"+crowd_var+"\t"+crowd_A);
			
		}
		
		
	
	
}
