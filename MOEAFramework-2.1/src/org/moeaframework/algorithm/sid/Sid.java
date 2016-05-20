package org.moeaframework.algorithm.sid;


import org.moeaframework.algorithm.AbstractEvolutionaryAlgorithm;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.EpsilonBoxEvolutionaryAlgorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.Population;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Selection;
import org.moeaframework.core.SidNonDominatedSorting;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;

public class Sid extends AbstractEvolutionaryAlgorithm implements
		EpsilonBoxEvolutionaryAlgorithm {

	/**
	 * The selection operator.
	 */
	private final Selection selection;

	/**
	 * The variation operator.
	 */
	private final Variation variation;

	/**
	 * Constructs the NSGA-II algorithm with the specified components.
	 * 
	 * @param problem
	 *            the problem being solved
	 * @param population
	 *            the population used to store solutions
	 * @param archive
	 *            the archive used to store the result; can be {@code null}
	 * @param selection
	 *            the selection operator
	 * @param variation
	 *            the variation operator
	 * @param initialization
	 *            the initialization method
	 */
	
	
	public Sid(Problem problem, SidNonDominatedSorting population,
			EpsilonBoxDominanceArchive archive, Selection selection,
			Variation variation, Initialization initialization) {
		super(problem, population, archive, initialization);
		this.variation = variation;
		this.selection = selection;
		
	}

	@Override
	public void iterate() {
		
		//System.out.println("Sid working");
		
		SidNonDominatedSorting population = getPopulation();
		EpsilonBoxDominanceArchive archive = getArchive();
		Population offspring = new Population();
		int populationSize = population.size();

		while (offspring.size() < populationSize) {
			Solution[] parents = selection.select(variation.getArity(),
					population);
			Solution[] children = variation.evolve(parents);

			offspring.addAll(children);
		}

		evaluateAll(offspring);

		if (archive != null) {
			archive.addAll(offspring);
		}

		population.addAll(offspring);
		
		population.prune(populationSize);
	}

	@Override
	public EpsilonBoxDominanceArchive getArchive() {
		return (EpsilonBoxDominanceArchive) super.getArchive();
	}

	@Override
	public SidNonDominatedSorting getPopulation() {
		return (SidNonDominatedSorting) super.getPopulation();
	}

}
