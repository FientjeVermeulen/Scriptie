package parameterOptimization;

import java.util.ArrayList;
import java.util.Random;

public class Configuration {
	double score;
	ArrayList<Parameter> params;
	int size;
	int nextNeighbourIndex;
	String nextMove;
	Random rando;
	int counter;
	boolean upIsFirst;
	
	//from scratch
	public Configuration(ArrayList<Parameter> params, Random rando, int nextNeighbourIndex) {
		this.params = params;
		this.rando = rando;
		size = params.size();
		this.nextNeighbourIndex = nextNeighbourIndex;
	}
	
	//from old configuration FOUT
//	public Configuration(Configuration oldConfig, Parameter newP, Random rando) {
//		this.rando = rando;
//		size = params.size();
//		int indexNewP = newP.index;
//		
//		ArrayList<Parameter> oldParams = oldConfig.params;
//		ArrayList<Parameter> params = new ArrayList<Parameter>();
//		
//		for(int i=0; i<oldParams.size(); i++) {
//			if(i!=indexNewP) {
//				params.add(i,oldParams.get(i).copy());
//			} else {
//				params.add(i, newP);
//			}
//		}
//	}
	
	/*	
	 * Creates new neighbouring configuration by copying the List of parameters, 
	 * except for the parameter at the nextNeighbourIndex.
	 * This parameter becomes a new Parameter, that differs one increment from 
	 * the old parameter. The old parameter has the info whether or not this should 
	 * be up or down. If it is the first time the parameter was tried, this is decided
	 * randomly. If it is the second time, whichever tweak was not tried yet, is tried now. 
	 * 
	 * A new configuration is completely new/reset, as we would have to start
	 * a new firstImprovement run if this new configuration were to be chosen. 
	 * The only information that it takes from the old configuration is the nextNeighbourIndex, 
	 * because this way we will try changing this parameter again first, because apparently 
	 * this parameter is influential.
	*/
	
	
	public Configuration nextNeighbour(Random rando) {
		ArrayList<Parameter> newParams = new ArrayList<Parameter>();
		Parameter newP = null;
		Parameter oldP = params.get(nextNeighbourIndex);
		boolean shouldAdvance = !oldP.first;
		
		for(int i=0; i<params.size(); i++) {
			if(i!=nextNeighbourIndex) {
				newParams.add(i,params.get(i).copy());
			} else {	
				newP = oldP.tweak(); 	
				newParams.add(i, newP);
			}
		}
		
		if(shouldAdvance) {
			this.advanceNextNeighbour();
		}
			
		Configuration newConfiguration = new Configuration(newParams, rando, newP.index);
		
		return newConfiguration;
	}
	
	public Configuration perturb(Random rando) {
		ArrayList<Parameter> newParams = new ArrayList<Parameter>();
		int randomParam1 = rando.nextInt(size);
		int randomParam2 = rando.nextInt(size);
		
		while(randomParam2 == randomParam1) {
			randomParam2 = rando.nextInt(size);
		}
		Parameter p1 = params.get(randomParam1);
		Parameter p2 = params.get(randomParam2);
		
		for(int i=0; i<size; i++) {
			if(i==randomParam1) {
				Parameter newp;
				if(rando.nextBoolean()) {
					newp = p1;
					for(int j=0; j<3; j++) {
						newp = newp.up();
					}
				} else {
					newp = p1;
					for(int j=0; j<3; j++) {
						newp = newp.down();
					}
				}
				newParams.add(i,newp);
			} else if(i==randomParam2) {
				Parameter newp;
				if(rando.nextBoolean()) {
					newp = p2;
					for(int j=0; j<3; j++) {
						newp = newp.up();
					}
				} else {
					newp = p2;
					for(int j=0; j<3; j++) {
						newp = newp.down();
					}
				}
				newParams.add(i,newp);
			} else {
				newParams.add(i,params.get(i).copy());
			}
		}
			
		Configuration newConfiguration = new Configuration(newParams, rando, 0);
		
		return newConfiguration;
	}
	
	
	public void updateScore(double newScore) {
		score = newScore;
	}

	public void firstOnTheBlock() {
		nextNeighbourIndex = rando.nextInt(size);
	}
	
	
	public void advanceNextNeighbour() {
		nextNeighbourIndex++;
		
		//Go to beginning to complete circle
		if(nextNeighbourIndex == size) {
			nextNeighbourIndex = 0;
		}
	}
	
	
	@Override
	public String toString() {
		String s = "[";
		
		for(Parameter p: params) {
			s += p.toString() + " ";
		}
		
		s += "] mean score: " + Math.floorDiv((int) score, 60);
		return s;
	}
}
