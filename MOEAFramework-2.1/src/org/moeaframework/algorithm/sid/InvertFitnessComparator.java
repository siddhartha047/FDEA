package org.moeaframework.algorithm.sid;

import java.util.Comparator;

import org.moeaframework.core.Solution;



public class InvertFitnessComparator implements Comparator<Solution>
{
	
	public InvertFitnessComparator() {
		super();
	}

	
    @Override
    public int compare(Solution x, Solution y)
    {
        if (x.getFitness() < y.getFitness()){
            return 1;
        }
        else if (x.getFitness() > y.getFitness())
        {
            return -1;
        }
        return 0;
    }
    
}