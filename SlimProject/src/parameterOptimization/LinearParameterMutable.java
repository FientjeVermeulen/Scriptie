package parameterOptimization;

public class LinearParameterMutable extends ParameterMutable implements Tweakable<Double>{

	public LinearParameterMutable(double value, double increment) {
		super(value, increment);
	}

	public Double up() {
		value += increment;
		previousAction = "up";
		return value;
	}
	
	public Double down() {
		value -= decrement;
		previousAction = "down";
		return value;	
	}
}
