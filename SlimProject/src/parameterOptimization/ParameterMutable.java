package parameterOptimization;

import java.util.Random;

public abstract class ParameterMutable {
	String previousAction;
	boolean first;
	String up;
	String down;
	double previousValue;
	double value;
	double increment;
	double decrement;
	
	public ParameterMutable(double value, double increment) {
		this.value = value;
		this.increment = increment;
		this.decrement = increment;
		first = true;
		up = "up";
		down = "down";
	}
	
	
	public void tweak(Random rando) {
		if(first) {
			if(rando.nextBoolean()) {
				up();
				previousAction = up;
			} else {
				down();
				previousAction = down;
			}
			first = false;
		} else {
			if(previousAction==up) {
				down();
				previousAction = down;
			} else {
				up();
				previousAction = up;
			}
			first = true;
		}

	}
	
	public void returnToPreviousValue() {
		value = previousValue;
	}
	
	public void updatePrevious() {
		previousValue = value;
	}
	
	public String toString() {
		return Double.toString(value);
	}

	public abstract Double up();
	public abstract Double down();

}
