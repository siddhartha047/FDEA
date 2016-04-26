package jmetal.metaheuritic.abc;

import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the crowding distance, as in NSGA-II.
 */
public class SidCrowdingComparator implements Comparator {

	/**
	 * stores a comparator for check the rank of solutions
	 */

	/**
	 * Compare two solutions.
	 * 
	 * @param o1
	 *            Object representing the first <code>Solution</code>.
	 * @param o2
	 *            Object representing the second <code>Solution</code>.
	 * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
	 *         respectively.
	 */
	public int compare(Object o1, Object o2) {
		if (o1 == null)
			return 1;
		else if (o2 == null)
			return -1;

		/* His rank is equal, then distance crowding comparator */
		double distance1 = ((ReferencePoint) o1).getCrowdingDistance();
		double distance2 = ((ReferencePoint) o2).getCrowdingDistance();
		if (distance1 > distance2)
			return -1;

		if (distance1 < distance2)
			return 1;

		return 0;
	} // compare
} // CrowdingComparator

