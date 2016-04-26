package jmetal.metaheuritic.FDEA;
import java.util.Comparator;

import jmetal.core.Solution;

public class HightoLowFitnessComparator implements Comparator<Solution>
{
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