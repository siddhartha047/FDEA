package jmetal.metaheuritic.abc;

import java.util.ArrayList;
import java.util.Collections;

import jmetal.core.*;
import jmetal.util.comparators.ObjectiveComparator;

/**
 * This class implements some utilities for calculating distances
 */
public class SidRefDistance {

	/**
	 * Constructor.
	 */
	public SidRefDistance() {
		// do nothing.
	} // Distance

	/**
	 * Assigns crowding distances to all solutions in a <code>SolutionSet</code>
	 * .
	 * 
	 * @param solutionSet
	 *            The <code>SolutionSet</code>.
	 * @param nObjs
	 *            Number of objectives.
	 */
	public void crowdingDistanceAssignment(
			ArrayList<ReferencePoint> solutionSet, int nObjs) {
		int size = solutionSet.size();

		if (size == 0)
			return;

		if (size == 1) {
			solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			return;
		} // if

		if (size == 2) {
			solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			solutionSet.get(1).setCrowdingDistance(Double.POSITIVE_INFINITY);
			return;
		} // if

		// Use a new SolutionSet to evite alter original solutionSet
		ArrayList<ReferencePoint> front = new ArrayList<ReferencePoint>();
		front.addAll(solutionSet);

		for (int i = 0; i < size; i++)
			front.get(i).setCrowdingDistance(0.0);

		double objetiveMaxn;
		double objetiveMinn;
		double distance;

		for (int i = 0; i < nObjs; i++) {
			// Sort the population by Obj n
			Collections.sort(front, new SidRefPointComparator(i));

			objetiveMinn = front.get(0).getObjective(i);
			objetiveMaxn = front.get(front.size() - 1).getObjective(i);

			// System.out.println(i+"->("+objetiveMaxn+","+objetiveMinn+")");

			// Set de crowding distance
			front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			front.get(size - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

			for (int j = 1; j < size - 1; j++) {

				distance = front.get(j + 1).getObjective(i)
						- front.get(j - 1).getObjective(i);

				distance = distance / (objetiveMaxn - objetiveMinn);
				distance += front.get(j).getCrowdingDistance();

				front.get(j).setCrowdingDistance(distance);

			} // for
		} // for
	} // crowdingDistanceAssing
} // Distance

