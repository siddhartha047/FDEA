package org.moeaframework.algorithm.sid;

public class SigMoid {
	// x -a c-mean
	public static double SigMoidValue(double x, double a, double c) {
		double value = 1 + Math.pow(Math.E, -a * (x - c));
		return 1 / value;
	}

	public static double CalculateA(double x, double p, double c) {
		double value = Math.log(p) - Math.log(1 - p);
		return value / (x - c);
	}
}
