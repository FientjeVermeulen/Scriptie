package glove.utils;

/**
 *
 * @author thanos
 */
public class Options {
    
    public double x_max = 100.0;
    public double learning_rate = 0.05;
    public double alpha = 0.9;
    public int vector_size = 10;
    public int iterations = 25;
    public int window_size = 10;
    public int min_count = 0;
    public int weightFCode = 0;
    public boolean weight = true;
    public boolean debug = false;
    
    public void print() {
    	System.out.println(x_max);
    	System.out.println(learning_rate);
    	System.out.println(alpha);
    	System.out.println(vector_size);
    	System.out.println(iterations);
    	System.out.println(window_size);
    	System.out.println(weightFCode);
    	System.out.println(weight);
    	
    }
}
