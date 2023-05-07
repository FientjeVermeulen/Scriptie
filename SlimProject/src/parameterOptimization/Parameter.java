package parameterOptimization;

import java.util.Random;

public abstract class Parameter {
	final double value;
	final double increment;
	final int index;
	Random rando;
	boolean first;
	String previousTweak;
	String up;
	String down;
	
	public Parameter(double value, double increment, int index, Random rando, boolean first) {
		this.value = value;
		this.increment = increment;
		this.index = index;
		this.first = first;
		this.rando = rando;
		up = "up";
		down = "down";
	}
	
	public Parameter tweak() {
		if(first) {
			if(rando.nextBoolean()) {
				previousTweak = up;
				first = false;
				return up();
			} else {
				previousTweak = down;
				first = false;
				return down();
			}
		}
		if(previousTweak==up) {
			return down();
		} else {
			return up();
		}
	}
	
	public String toString() {
		return Double.toString(value);
	}

	public abstract Parameter up();
	public abstract Parameter down();
	public abstract Parameter copy();

}
