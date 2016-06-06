package jmetal.metaheuritic.FDEArevision;

import java.util.ArrayList;
import java.util.Collections;

import jmetal.core.*;
import jmetal.util.comparators.ObjectiveComparator;

/**
 * This class implements some utilities for calculating distances
 */
public class MinMaxSorting {

	public MinMaxSorting() {

	}
	
	/**
	 * Return reference points in sorted order such that each reference points is distant than another
	 * 
	 * @param solutionSet
	 * @param nObjs
	 */
	
	

	public void crowdingDistanceAssignment(ArrayList<ReferencePoint> solutionSet, int nObjs) {
		
		
		ArrayList<ReferencePoint> front = new ArrayList<ReferencePoint>();
		front.addAll(solutionSet);
		
		solutionSet.clear();
		
		
		
		for(int i=0;i<front.size();i++){
			front.get(i).crowdingDistance=Double.MAX_VALUE;
		}
			
		
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
		
		
		double maxDistance=-Double.MAX_VALUE;
		int bestIndex=-1;	
		
		for(int i=0;i<front.size();i++){
			
			ReferencePoint point = front.get(i);
			
			double minDistance= getMinDistance(solutionSet,point);
			
			point.crowdingDistance = minDistance;
			
			if(maxDistance<minDistance){
				maxDistance = minDistance;
				bestIndex = i;
			}
			
		}
		
		ReferencePoint prevBest=null;
		
		if(!front.isEmpty()){
			solutionSet.add(front.get(bestIndex));
			prevBest=front.remove(bestIndex);
		}
		
		
		
		
		while(!front.isEmpty()){
			
			maxDistance=-Double.MAX_VALUE;
			bestIndex=-1;						
			
			if(front.size()==1){
				solutionSet.add(front.get(0));
				front.clear();
				break;
			}
			
			for(int i=0;i<front.size();i++){
				
				ReferencePoint point = front.get(i);
				
				double minDistance= getEucledianDistance(prevBest,point);
				
				if(point.crowdingDistance > minDistance){
					point.crowdingDistance = minDistance;
				}
				
				if(maxDistance<point.crowdingDistance){
					maxDistance = point.crowdingDistance;
					bestIndex = i;
				}
				
			}
			
			solutionSet.add(front.get(bestIndex));
			prevBest=front.remove(bestIndex);
		}
	
	} 
	
	public double getMinDistance(ArrayList<ReferencePoint> solutionSet, ReferencePoint point){
		double minDistance=Double.MAX_VALUE;
		
		for(int i=0;i<solutionSet.size();i++){
			double distance= getEucledianDistance(solutionSet.get(i),point);
			
			if(distance<minDistance)minDistance=distance;
		}
		
		
		return minDistance;
	}
	
	public double getEucledianDistance(ReferencePoint X,ReferencePoint Y){
		
		double value=0;
		
		for(int i=0;i<X.numberOfObjectives;i++){
			
			value+= (X.getObjective(i)-Y.getObjective(i))*(X.getObjective(i)-Y.getObjective(i));			
		}
	
		return Math.sqrt(value);
	}
	
	/*
	 
	 
	public void crowdingDistanceAssignment(ArrayList<ReferencePoint> solutionSet, int nObjs) {
		
		ArrayList<ReferencePoint> front = new ArrayList<ReferencePoint>();
		front.addAll(solutionSet);
		
		solutionSet.clear();
		
		

		for (int i = 0; i < nObjs; i++) {
			
			Collections.sort(front, new SidRefPointComparator(i));

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
		
		
		while(!front.isEmpty()){
			
			double maxDistance=-Double.MAX_VALUE;
			int bestIndex=-1;						
			
			if(front.size()==1){
				solutionSet.add(front.get(0));
				front.clear();
				break;
			}
			
			for(int i=0;i<front.size();i++){
				
				ReferencePoint point = front.get(i);
				
				double minDistance= getMinDistance(solutionSet,point);
				
				if(maxDistance<minDistance){
					maxDistance = minDistance;
					bestIndex = i;
				}
				
			}
			
			solutionSet.add(front.get(bestIndex));
			front.remove(bestIndex);
		}
	
	} 
	
	public double getMinDistance(ArrayList<ReferencePoint> solutionSet, ReferencePoint point){
		double minDistance=Double.MAX_VALUE;
		
		for(int i=0;i<solutionSet.size();i++){
			double distance= getEucledianDistance(solutionSet.get(i),point);
			
			if(distance<minDistance)minDistance=distance;
		}
		
		
		return minDistance;
	}
	
	public double getEucledianDistance(ReferencePoint X,ReferencePoint Y){
		
		double value=0;
		
		for(int i=0;i<X.numberOfObjectives;i++){
			
			value+= (X.getObjective(i)-Y.getObjective(i))*(X.getObjective(i)-Y.getObjective(i));			
		}
	
		return Math.sqrt(value);
	} 
	 */
	
	
} 

