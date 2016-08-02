package jmetal.problems.RectangleProblem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

public class ParetoBoxProblem extends Problem{

	  /** 
	   * Constructor.
	   * Creates a default instance of the Rectangle problem.
	   * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
	   */
	  public ParetoBoxProblem(String solutionType) throws ClassNotFoundException {
	    this(solutionType, 2);
	  } // 
	  
	  /** 
	   * Constructor.
	   * Creates a new instance of the Rectangle problem.
	   * @param numberOfVariables Number of variables of the problem 
	   * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
	   */
	  
	  
	  public ParetoBoxProblem(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
	    numberOfVariables_   = numberOfVariables.intValue() ;
	    numberOfObjectives_  = 10                            ;
	    numberOfConstraints_ = 0                            ;
	    problemName_         = "recinstancei"               ;
	        
	    upperLimit_ = new double[numberOfVariables_] ;
	    lowerLimit_ = new double[numberOfVariables_] ;
	       
	    for (int i = 0; i < numberOfVariables_; i++) {
	      //lowerLimit_[i] = -20 	;
	      //upperLimit_[i] = 120  ;
	    	lowerLimit_[i] = -10000;
	    	upperLimit_[i] = 10000;
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
	    double [] obj = new double[numberOfObjectives_] ; // function values     
	    double [] xreal = new double[numberOfVariables_] ;
	    for (int i = 0 ; i < numberOfVariables_; i++)
	    	xreal[i] = vars.getValue(i) ;
	    
	    
	    	  
		obj[0] = Math.sqrt((xreal[0] - 50)*(xreal[0] - 50) + (xreal[1] - 88)*(xreal[1] - 88));
		obj[1] = Math.sqrt((xreal[0] - 70)*(xreal[0] - 70) + (xreal[1] - 80)*(xreal[1] - 80));
		obj[2] = Math.sqrt((xreal[0] - 83)*(xreal[0] - 83) + (xreal[1] - 62)*(xreal[1] - 62));
		obj[3] = Math.sqrt((xreal[0] - 83)*(xreal[0] - 83) + (xreal[1] - 38)*(xreal[1] - 38));
		obj[4] = Math.sqrt((xreal[0] - 70)*(xreal[0] - 70) + (xreal[1] - 20)*(xreal[1] - 20));
		obj[5] = Math.sqrt((xreal[0] - 50)*(xreal[0] - 50) + (xreal[1] - 12)*(xreal[1] - 12));
		obj[6] = Math.sqrt((xreal[0] - 30)*(xreal[0] - 30) + (xreal[1] - 20)*(xreal[1] - 20));
		obj[7] = Math.sqrt((xreal[0] - 17)*(xreal[0] - 17) + (xreal[1] - 38)*(xreal[1] - 38));	
		obj[8] = Math.sqrt((xreal[0] - 17)*(xreal[0] - 17) + (xreal[1] - 62)*(xreal[1] - 62));
		obj[9] = Math.sqrt((xreal[0] - 30)*(xreal[0] - 30) + (xreal[1] - 80)*(xreal[1] - 80));
	    
	    for(int i=0;i<numberOfObjectives_;i++){
	    	solution.setObjective(i, obj[i]);
	    }
	    	    
	    
	    
	    
	  } // evaluate

}
