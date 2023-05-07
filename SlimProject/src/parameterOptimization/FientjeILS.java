package parameterOptimization;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class FientjeILS {
	int maxRuns;
	double bestScore;
	double localBest;
	boolean improvement; //true = best neighbor, false = first improvement
	double minDifference;
	Random rando;
	int noConfigs;
	ArrayList<Double> standardDevs;

	public FientjeILS(int maxRuns, boolean improvement, double minDifference, Random r) {
		this.maxRuns = maxRuns;
		this.improvement = improvement;
		this.minDifference = minDifference;
		this.rando = r;
		noConfigs = 0;
		standardDevs = new ArrayList<Double>();
	}
	
	public double[] somethingGenius(Configuration configuration) throws FileNotFoundException {
		Configuration currentBest = localSearch(configuration);
		Configuration optimizedPerturbation = null;
		bestScore = localBest;
		
		for(int run=1; run<= 5; run++) {
			System.out.println("PERTURB");
			System.out.println("perturbation no: " + run);
			Configuration perturbedConfiguration = currentBest.perturb(rando);
			optimizedPerturbation = localSearch(perturbedConfiguration);
			if(bestScore - localBest > minDifference *60) {
				currentBest = optimizedPerturbation;
				bestScore = localBest;
			}
		}				
		
		System.out.println("ANSWER: " + currentBest);
	
		return null;
	}
	
	public Configuration perturb(Configuration configuration) throws FileNotFoundException {
		Configuration newConfiguration = configuration.perturb(rando);
		localBest = testConfiguration(newConfiguration).score;
		newConfiguration.updateScore(localBest);
		Configuration newerConfiguration = localSearch(newConfiguration);
		return newerConfiguration;
	}
	
	public Configuration localSearch(Configuration configuration) throws FileNotFoundException {
		localBest = testConfiguration(configuration).score;		
		
		while(true) {		
			Configuration newConfiguration = firstImprovement(configuration);
			if(newConfiguration==null) {
				return configuration;
			} else {
				configuration = newConfiguration;
			}
		}
	}
	
	public Configuration firstImprovement(Configuration configuration) throws FileNotFoundException {
		int neighbourhoodSize = configuration.params.size()*2;
		//configuration.firstOnTheBlock();
		System.out.println("");
		System.out.println("");
		System.out.println("doe firstImprovement");
				
		for(int i=0; i<neighbourhoodSize; i++) {
			System.out.println("---------------------------------");
			Configuration newConfiguration = configuration.nextNeighbour(rando);
			newConfiguration = testConfiguration(newConfiguration);
			double newScore = newConfiguration.score;
			System.out.println("Neighbour number: " + i);
			System.out.println("Neighbour index:  " + newConfiguration.nextNeighbourIndex);		

			if(localBest - newScore > minDifference * 60) {
				System.out.println("BETTER FOUND!");
				localBest = newScore;
				System.out.println("---------------------------------");
				return newConfiguration;
			} else {
				System.out.println("Not better");
				System.out.println("---------------------------------");
			}
		}
		
		return null;	
	}
	
	
	public Configuration testConfiguration(Configuration configuration) throws FileNotFoundException {
		noConfigs++;
		String[] stringParams = prepareParams(configuration.params);	
		double[] scores = null;// getSourceCode21052018.Main.returnStuff(stringParams);
		double meanScore = calculateMean(scores);
		configuration.updateScore(meanScore);
		System.out.println(configuration);
		System.out.println("Local best Score: " + Math.floorDiv((int) localBest, 60));
		double standardDev = getSourceCode21052018.Main.calculateStandardDeviation(scores, meanScore);
		System.out.println("Average SD: " + Math.floorDiv((int)getSourceCode21052018.SimulatedAnnealing.calculateMean(standardDevs),60));
		standardDevs.add(standardDev);
		
		System.out.println("number of tests: " + noConfigs);
			
		return configuration;
	}
	
	public String[] prepareParams(ArrayList<Parameter> params) {
		String[] stringParams = new String[30];
		
//		stringParams[10] = params.get(0).toString(); //2-OPT polynomiaal
//		stringParams[13] = params.get(1).toString(); // order toevoegen random
//		stringParams[14] = params.get(2).toString();//34; //order verwijderen
//		stringParams[15] = params.get(3).toString();//7.2; //order verplaatsen zelfde dag random
//		stringParams[18] = params.get(4).toString();//28.8; // order verplaatsen andere dag random
//		stringParams[23] = params.get(5).toString(); //weight_AReplace
//		stringParams[24] = params.get(6).toString(); //alpha in the big run
//		stringParams[25] = params.get(7).toString(); //initial T
//		stringParams[27] = params.get(8).toString(); //max fisfactor distance
		
		stringParams[23] = params.get(0).toString(); //weight_AReplace
		stringParams[27] = params.get(1).toString(); //max fisfactor distance
		stringParams[28] = params.get(2).toString(); //cap

		return stringParams;
	}
	
	
	public static double calculateMean(double[] list) {
		double total=0.0;
		
		for(int i=0; i<list.length; i++) {
			total += list[i];
		}
		
		//System.out.println(total);
		
		if(list.length==0) {
			return 0;
		} else {

			return total/list.length;
		}
	}

}
