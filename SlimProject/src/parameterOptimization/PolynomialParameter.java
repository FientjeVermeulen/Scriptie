package parameterOptimization;

import java.util.Random;

public class PolynomialParameter extends Parameter {

	public PolynomialParameter(double value, double increment, int index, Random rando, boolean first) {
		super(value, increment, index, rando, first);
	}

	public Parameter up() {
		double newValue = value;		
		newValue *= increment;

		return new PolynomialParameter(newValue, increment, index, rando, true);	
	}
	
	
	public Parameter down() {
		double newValue = value;		
		newValue /= increment;
		
		return new PolynomialParameter(newValue, increment, index, rando, true);
	}
	
	public Parameter copy() {
		PolynomialParameter newP = new PolynomialParameter(this.value, this.increment, this.index, rando, true);
		
		return newP;
	}
	
}
