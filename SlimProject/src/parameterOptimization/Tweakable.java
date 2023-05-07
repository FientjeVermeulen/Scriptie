package parameterOptimization;

public interface Tweakable<RESULT> {
	RESULT up();
	RESULT down();
	String toString();
	void returnToPreviousValue();
}
