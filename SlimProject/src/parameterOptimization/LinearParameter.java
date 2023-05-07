package parameterOptimization;

import java.util.Random;

public class LinearParameter extends Parameter {

	public LinearParameter(double value, double increment, int index, Random rando, boolean first) {
		super(value, increment, index, rando, first);
	}

	public Parameter up() {
		double newValue = value;		
		newValue += increment;

		return new LinearParameter(newValue, increment, index, rando, true);	
	}
	
	
	public Parameter down() {
		double newValue = value;		
		newValue -= increment;
		
		return new LinearParameter(newValue, increment, index, rando, true);
	}
	
	public Parameter copy() {
		LinearParameter newP = new LinearParameter(this.value, this.increment, this.index, rando, true);
		
		return newP;
	}
	
}
