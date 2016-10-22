package jmetal.problems.RectangleProblem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

public class RecInstanceI extends Problem{

	  /** 
	   * Constructor.
	   * Creates a default instance of the Rectangle problem.
	   * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
	   */
	  public RecInstanceI(String solutionType) throws ClassNotFoundException {
	    this(solutionType, 2);
	  } // 
	  
	  /** 
	   * Constructor.
	   * Creates a new instance of the Rectangle problem.
	   * @param numberOfVariables Number of variables of the problem 
	   * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
	   */
	  
	  
	  public RecInstanceI(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
	    numberOfVariables_   = numberOfVariables.intValue() ;
	    numberOfObjectives_  = 4                            ;
	    numberOfConstraints_ = 0                            ;
	    problemName_         = "recinstancei"               ;
	        
	    upperLimit_ = new double[numberOfVariables_] ;
	    lowerLimit_ = new double[numberOfVariables_] ;
	       
	    for (int i = 0; i < numberOfVariables_; i++) {
	      //lowerLimit_[i] = -20 	;
	      //upperLimit_[i] = 120  ;
	    	lowerLimit_[i] = -10000.0;
	    	upperLimit_[i] =  10000.0;
	    } // for
	        
	    if (solutionType.compareTo("BinaryReal") == 0)
	    	solutionType_ = new BinaryRealSolutionType(this) ;
	    else if (solutionType.compareTo("Real") == 0)
	    	solutionType_ = new RealSolutionType(this) ;
	    else if (solutionType.compareTo("ArrayReal") == 0)
	    	solutionType_ = new ArrayRealSolutionType(this) ;
	    else {
	    	System.out.println("Error: solution type " + solutionType + " invalid") ;
	    	System.exit(-1) ;
	    }
	  } // Kursawe
	    
	
	@Override
	public void evaluate(Solution solution) throws JMException {
		XReal vars = new XReal(solution) ;
        
		double aux, xi, xj           ; // auxiliary variables
	    double [] fx = new double[numberOfObjectives_] ; // function values     
	    double [] x = new double[numberOfVariables_] ;
	    for (int i = 0 ; i < numberOfVariables_; i++)
	    	x[i] = vars.getValue(i) ;
	    
	    	    	   
	    
	    fx[0]=Math.abs(x[0]-0);
	    fx[1]=Math.abs(x[0]-100);
	    fx[2]=Math.abs(x[1]-0);
	    fx[3]=Math.abs(x[1]-100);
	    
	    
	    for(int i=0;i<numberOfObjectives_;i++){
	    	solution.setObjective(i, fx[i]);
	    }
	    	    
	    
	    
	    
	  } // evaluate

}
