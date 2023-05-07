package parameterOptimization;

public class PolynomialParameterMutable extends ParameterMutable implements Tweakable<Double>{

	public PolynomialParameterMutable(double value, double increment) {
		super(value, increment);
	}
	public Double up() {
		value *= increment;
		return value;
	}
	public Double down() {
		value /= decrement;
		return value;	
	}
	
}