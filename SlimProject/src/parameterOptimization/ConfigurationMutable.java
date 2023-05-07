package parameterOptimization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ConfigurationMutable {
	double score;
	ArrayList<ParameterMutable> params;
	int size;
	int pointer;
	Random rando;
	int counter;
	boolean upIsFirst;
	
	public ConfigurationMutable(ArrayList<ParameterMutable> params, Random rando) {
		this.params = params;
		this.rando = rando;
		size = params.size();
		
		
		
		
		counter=0;
	}
	
	public void foundBetter(double newScore) {
		score = newScore;
		counter = size*2;
	}

	public void randomPointer() {
		pointer = rando.nextInt(size);
		counter=0;
	}
	
	public boolean nextParam() {
		if(counter==size*2) {
			return false;
		} else {
			counter++;
			params.get(pointer).tweak(rando);
		}
		return true;
	}
	
	public void advancePointer() {
		pointer++;
		
		//Go to beginning to complete circle
		if(pointer == size) {
			pointer = 0;
		}
	}
	
	public void returnToPreviousValue() {
		params.get(pointer).returnToPreviousValue();
	}
	
	@Override
	public String toString() {
		String s = "[";
		
		for(ParameterMutable p: params) {
			s += p.toString() + " ";
		}
		
		s += "] mean score: " + score;
		return s;
	}
}
