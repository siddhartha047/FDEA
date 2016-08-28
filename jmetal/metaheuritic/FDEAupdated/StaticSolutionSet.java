package jmetal.metaheuritic.FDEAupdated;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;



public class StaticSolutionSet {
	public static SolutionSet MakeSolutionSet(SolutionSet solutionSet){
		Solution s1=new Solution(5); 
		Solution s2=new Solution(5);
		Solution s3=new Solution(5);
		Solution s4=new Solution(5);
		
		double []objectives1=s1.get_objectives();
		double []objectives2=s2.get_objectives();
		double []objectives3=s3.get_objectives();
		double []objectives4=s4.get_objectives();
		
		
		
		objectives1[0]=1; objectives1[1]=2; objectives1[2]=3; objectives1[3]=4; objectives1[4]=5;
		objectives2[0]=1; objectives2[1]=1; objectives2[2]=4; objectives2[3]=6; objectives2[4]=9;
		objectives3[0]=9; objectives3[1]=8; objectives3[2]=7; objectives3[3]=1; objectives3[4]=5;
		objectives4[0]=1; objectives4[1]=1; objectives4[2]=2; objectives4[3]=5; objectives4[4]=9;
		
		
		
		solutionSet.add(s1);
		solutionSet.add(s2);
		solutionSet.add(s3);
		//solutionSet.add(s4);
		
		return solutionSet;
	}

	
	
	public static SolutionSet MakeTwoObjectiveSolution(SolutionSet solutionSet){		
		
		int size=solutionSet.getMaxSize();		
		int obj=2;
		
		for(int i=0;i<size;i++){
			solutionSet.add(new Solution(2));
		}
		
		double objectives[][]=new double[size][2];
		
		for(int i=0;i<size;i++){
			objectives[i]=solutionSet.get(i).get_objectives();
		}
		
		//random solutions
		/*
		objectives[0][0]=0;
		objectives[0][1]=1;
		objectives[1][0]=0.14286;
		objectives[1][1]=0.85714;
		objectives[2][0]=0.28571;
		objectives[2][1]=0.71429;
		objectives[3][0]=0.42857;
		objectives[3][1]=0.57143;
		objectives[4][0]=0.57143;
		objectives[4][1]=0.42857;
		objectives[5][0]=0.71429;
		objectives[5][1]=0.28571;
		objectives[6][0]=0.85714;
		objectives[6][1]=0.14286;
		objectives[7][0]=1;
		objectives[7][1]=0;
		
		objectives[8][0]=0.74264;
		objectives[8][1]=0.40166;
		objectives[9][0]=0.54023;
		objectives[9][1]=0.47627;
		objectives[10][0]=0.74696;
		objectives[10][1]=0.28875;
		objectives[11][0]=0.93663;
		objectives[11][1]=0.73481;
		objectives[12][0]=0.96116;
		objectives[12][1]=0.95898;
		objectives[13][0]=0.52988;
		objectives[13][1]=0.49372;
		objectives[14][0]=0.91278;
		objectives[14][1]=0.47613;
		objectives[15][0]=0.82768;
		objectives[15][1]=0.62824;
		*/
		
		
		//line
		/*
		objectives[0][0]=0.2;
		objectives[0][1]=0.8;
		objectives[1][0]=0.3;
		objectives[1][1]=0.7;
		objectives[2][0]=0.4;
		objectives[2][1]=0.6;
		objectives[3][0]=0.5;
		objectives[3][1]=0.5;
		objectives[4][0]=0.55;
		objectives[4][1]=0.95;
	//	objectives[4][0]=0.15;
	//	objectives[4][1]=0.45;
		objectives[5][0]=0.6;
		objectives[5][1]=0.4;
		objectives[6][0]=0.7;
		objectives[6][1]=0.3;
		objectives[7][0]=0.8;
		objectives[7][1]=0.2;
		*/
		//four line cases
		
		/*
		objectives[0][0]=.2;
		objectives[0][1]=.8;
		objectives[1][0]=.5;
		objectives[1][1]=.5;
		objectives[2][0]=.8;
		objectives[2][1]=.2;		
		objectives[3][0]=.7;
		objectives[3][1]=.9;
		*/
		
		objectives[0][0]=.1;
		objectives[0][1]=.3;
		objectives[1][0]=.2;
		objectives[1][1]=.2;
		objectives[2][0]=.3;
		objectives[2][1]=.1;		
		objectives[3][0]=.4;
		objectives[3][1]=0.0;
		objectives[4][0]= 0.22;
		objectives[4][1]=0.12;
		objectives[5][0]=.3;
		objectives[5][1]=1.5;

		///concave
		/*
		objectives[0][0]=.2;
		objectives[0][1]=0.96;
		objectives[1][0]= .4;
		objectives[1][1]=0.84;
		objectives[2][0]=.6;
		objectives[2][1]=0.64;
		objectives[3][0]=0.8;
		objectives[3][1]=.36;
		*/
		
		//line diff scale
		/*
		objectives[0][0]=0.1;
		objectives[0][1]=0.95;
		objectives[1][0]=0.6;
		objectives[1][1]=0.7;
		objectives[2][0]=1;
		objectives[2][1]=0.5;
		objectives[3][0]=1.5;
		objectives[3][1]=0.25;
		*/
		
/*
		
		objectives[0][0]=0.2;
		objectives[0][1]=0.8;
		objectives[1][0]=0.3;
		objectives[1][1]=0.7;
		objectives[2][0]=0.4;
		objectives[2][1]=0.6;
		objectives[3][0]=0.3;
		objectives[3][1]=0.3;
		objectives[4][0]=0.4;
		objectives[4][1]=0.95;
		objectives[5][0]=0.6;
		objectives[5][1]=0.4;
		objectives[6][0]=0.3;
		objectives[6][1]=0.9;
		objectives[7][0]=0.8;
		objectives[7][1]=0.2;
		*/
		//two layer random line
		/*
		objectives[0][0]=0.48448;
		objectives[0][1]=0.51552;
		objectives[1][0]=0.15185;
		objectives[1][1]=0.84815;
		objectives[2][0]=0.78193;
		objectives[2][1]=0.21807;
		objectives[3][0]=0.10061;
		objectives[3][1]=0.89939;
		objectives[4][0]=0.29407;
		objectives[4][1]=0.70593;
		objectives[5][0]=0.23737;
		objectives[5][1]=0.76263;
		objectives[6][0]=0.53087;
		objectives[6][1]=0.46913;
		objectives[7][0]=0.091499;
		objectives[7][1]=0.9085;
		
		objectives[8][0]=0.24723;
		objectives[8][1]=0.95277;
		objectives[9][0]=0.104;
		objectives[9][1]=1.096;
		objectives[10][0]=0.92632;
		objectives[10][1]=0.27368;
		objectives[11][0]=0.24681;
		objectives[11][1]=0.95319;
		objectives[12][0]=0.46593;
		objectives[12][1]=0.73407;
		objectives[13][0]=0.66213;
		objectives[13][1]=0.53787;
		objectives[14][0]=0.27474;
		objectives[14][1]=0.92526;
		objectives[15][0]=0.77033;
		objectives[15][1]=0.42967;
		*/
		
		//concave
		/*
		objectives[0][0]=1;
		objectives[0][1]=0;
		objectives[1][0]=0.97493;
		objectives[1][1]=0.22252;
		objectives[2][0]=0.90097;
		objectives[2][1]=0.43388;
		objectives[3][0]=0.78183;
		objectives[3][1]=0.62349;
		objectives[4][0]=0.62349;
		objectives[4][1]=0.78183;
		objectives[5][0]=0.43388;
		objectives[5][1]=0.90097;
		objectives[6][0]=0.22252;
		objectives[6][1]=0.97493;
		objectives[7][0]=0;
		objectives[7][1]=1;
		*/
		
		//convex
		/*
		objectives[0][0]=0;
		objectives[0][1]=1;
		objectives[1][0]=0.025072;
		objectives[1][1]=0.77748;
		objectives[2][0]=0.099031;
		objectives[2][1]=0.56612;
		objectives[3][0]=0.21817;
		objectives[3][1]=0.37651;
		objectives[4][0]=0.37651;
		objectives[4][1]=0.21817;
		objectives[5][0]=0.56612;
		objectives[5][1]=0.099031;
		objectives[6][0]=0.77748;
		objectives[6][1]=0.025072;
		objectives[7][0]=1;
		objectives[7][1]=0;
		*/
		
		//Line
		/*
		objectives[0][0]=0;
		objectives[0][1]=1;
		objectives[1][0]=0.083333;
		objectives[1][1]=0.91667;
		objectives[2][0]=0.16667;
		objectives[2][1]=0.83333;
		objectives[3][0]=0.25;
		objectives[3][1]=0.75;
		objectives[4][0]=0.33333;
		objectives[4][1]=0.66667;
		objectives[5][0]=0.41667;
		objectives[5][1]=0.58333;
		objectives[6][0]=0.5;
		objectives[6][1]=0.5;
		objectives[7][0]=0.58333;
		objectives[7][1]=0.41667;
		objectives[8][0]=0.66667;
		objectives[8][1]=0.33333;
		objectives[9][0]=0.75;
		objectives[9][1]=0.25;
		objectives[10][0]=0.83333;
		objectives[10][1]=0.16667;
		objectives[11][0]=0.91667;
		objectives[11][1]=0.083333;
		*/
		
		//two level concave
		/*
		objectives[0][0]=1;
		objectives[0][1]=0;
		objectives[1][0]=0.98982;
		objectives[1][1]=0.14231;
		objectives[2][0]=0.95949;
		objectives[2][1]=0.28173;
		objectives[3][0]=0.90963;
		objectives[3][1]=0.41542;
		objectives[4][0]=0.84125;
		objectives[4][1]=0.54064;
		objectives[5][0]=0.75575;
		objectives[5][1]=0.65486;
		objectives[6][0]=0.65486;
		objectives[6][1]=0.75575;
		objectives[7][0]=0.54064;
		objectives[7][1]=0.84125;
		objectives[8][0]=0.41542;
		objectives[8][1]=0.90963;
		objectives[9][0]=0.28173;
		objectives[9][1]=0.95949;
		objectives[10][0]=0.14231;
		objectives[10][1]=0.98982;
		objectives[11][0]=0;
		objectives[11][1]=1;
		objectives[12][0]=1.2;
		objectives[12][1]=0;
		objectives[13][0]=1.1878;
		objectives[13][1]=0.17078;
		objectives[14][0]=1.1514;
		objectives[14][1]=0.33808;
		objectives[15][0]=1.0916;
		objectives[15][1]=0.4985;
		objectives[16][0]=1.0095;
		objectives[16][1]=0.64877;
		objectives[17][0]=0.9069;
		objectives[17][1]=0.78583;
		objectives[18][0]=0.78583;
		objectives[18][1]=0.9069;
		objectives[19][0]=0.64877;
		objectives[19][1]=1.0095;
		objectives[20][0]=0.4985;
		objectives[20][1]=1.0916;
		objectives[21][0]=0.33808;
		objectives[21][1]=1.1514;
		objectives[22][0]=0.17078;
		objectives[22][1]=1.1878;
		objectives[23][0]=0;
		objectives[23][1]=1.2;
		*/
		//two level line
		/*
		objectives[0][0]=0;
		objectives[0][1]=1;
		objectives[1][0]=0.090909;
		objectives[1][1]=0.90909;
		objectives[2][0]=0.18182;
		objectives[2][1]=0.81818;
		objectives[3][0]=0.27273;
		objectives[3][1]=0.72727;
		objectives[4][0]=0.36364;
		objectives[4][1]=0.63636;
		objectives[5][0]=0.45455;
		objectives[5][1]=0.54545;
		objectives[6][0]=0.54545;
		objectives[6][1]=0.45455;
		objectives[7][0]=0.63636;
		objectives[7][1]=0.36364;
		objectives[8][0]=0.72727;
		objectives[8][1]=0.27273;
		objectives[9][0]=0.81818;
		objectives[9][1]=0.18182;
		objectives[10][0]=0.90909;
		objectives[10][1]=0.090909;
		objectives[11][0]=1;
		objectives[11][1]=0;
		objectives[12][0]=0;
		objectives[12][1]=1.2;
		objectives[13][0]=0.10909;
		objectives[13][1]=1.0909;
		objectives[14][0]=0.21818;
		objectives[14][1]=0.98182;
		objectives[15][0]=0.32727;
		objectives[15][1]=0.87273;
		objectives[16][0]=0.43636;
		objectives[16][1]=0.76364;
		objectives[17][0]=0.54545;
		objectives[17][1]=0.65455;
		objectives[18][0]=0.65455;
		objectives[18][1]=0.54545;
		objectives[19][0]=0.76364;
		objectives[19][1]=0.43636;
		objectives[20][0]=0.87273;
		objectives[20][1]=0.32727;
		objectives[21][0]=0.98182;
		objectives[21][1]=0.21818;
		objectives[22][0]=1.0909;
		objectives[22][1]=0.10909;
		objectives[23][0]=1.2;
		objectives[23][1]=-0;
		*/
		
		return solutionSet;
	}

	public static SolutionSet MakeTwoObjectiveSolutionChild(SolutionSet solutionSet){
		Solution s1=new Solution(2); 
		Solution s2=new Solution(2);
		Solution s3=new Solution(2);
		Solution s4=new Solution(2);
		Solution s5=new Solution(2);
		Solution s6=new Solution(2);
		
		double []objectives1=s1.get_objectives();
		double []objectives2=s2.get_objectives();
		double []objectives3=s3.get_objectives();
		double []objectives4=s4.get_objectives();
		double []objectives5=s5.get_objectives();
		double []objectives6=s6.get_objectives();
		
		objectives1[0]=0.5; objectives1[1]=0.5;
		objectives2[0]=0.6; objectives2[1]=0.4; 
		objectives3[0]=0.7; objectives3[1]=0.3; 
		objectives4[0]=0.8; objectives4[1]=0.2;
		objectives5[0]=0.9; objectives5[1]=0.1;
		objectives6[0]=0.85; objectives6[1]=0.15;
		
		
		
		solutionSet.add(s1);
		solutionSet.add(s2);
		solutionSet.add(s3);
		solutionSet.add(s4);
		solutionSet.add(s5);
		solutionSet.add(s6);
		
		return solutionSet;
	}

	
	public static SolutionSet MakeThreeObjectiveSolution(SolutionSet solutionSet){
		Solution s1=new Solution(3); 
		Solution s2=new Solution(3);
		Solution s3=new Solution(3);
		Solution s4=new Solution(3);
		Solution s5=new Solution(3);
		Solution s6=new Solution(3);
		
		double []objectives1=s1.get_objectives();
		double []objectives2=s2.get_objectives();
		double []objectives3=s3.get_objectives();
		double []objectives4=s4.get_objectives();
		double []objectives5=s5.get_objectives();
		double []objectives6=s6.get_objectives();
		
		/*
		objectives1[0]=1; objectives1[1]=6;
		objectives2[0]=2; objectives2[1]=4; 
		objectives3[0]=3.5; objectives3[1]=2.5; 
		objectives4[0]=4; objectives4[1]=2;
		objectives5[0]=5; objectives5[1]=1;
		*/
		objectives1[0]=1; objectives1[1]=1;
		objectives2[0]=2.5; objectives2[1]=2; 
		objectives3[0]=3; objectives3[1]=3; 
		objectives4[0]=4.3; objectives4[1]=4;
		objectives5[0]=5.9; objectives5[1]=5;
		
		
		
		solutionSet.add(s1);
		solutionSet.add(s2);
		solutionSet.add(s3);
		solutionSet.add(s4);
		solutionSet.add(s5);
		
		return solutionSet;
	}

}
