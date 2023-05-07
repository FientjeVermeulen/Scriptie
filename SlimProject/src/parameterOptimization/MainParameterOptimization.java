package parameterOptimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainParameterOptimization {

	
	
	public static void main2(String[] args) throws FileNotFoundException {
		double[] test = new double[5];
		test[0] = 10;
		test[1] = 20; 
		test[2] = 15;
		test[3] = 25;
		test[4] = 35;
		
		double mean = FientjeILS.calculateMean(test);
		System.out.println(mean);
		System.out.println(getSourceCode21052018.Main.calculateStandardDeviation(test, mean));
		System.out.println(getSourceCode21052018.Main.calculateVariance(test, mean));
	}

	public static void printStringArray(String[] array) {
		for(int i=0; i<array.length; i++) {
			System.out.println(array[i]);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
        // Creating a File object that represents the disk file.
        PrintStream o = new PrintStream(new FileOutputStream("ConsoleOutput.txt", true));
 
        // Store current System.out before assigning a new value
        PrintStream console = System.out;
 
        // Assign o to output stream
        System.setOut(o);
        System.out.println("This will be written to the text file");
 
		Random rando = new Random(27);
		Configuration beginConfiguration = beginConfiguration(rando);

		boolean bestNeighbour = false; // best neighbour versus first improvement. 
		int maxRuns = 20;
		double minDifference = 1;
		
		FientjeILS ils = new FientjeILS(maxRuns, bestNeighbour, minDifference, rando);
		
		double endScores[] = ils.somethingGenius(beginConfiguration);
	}
	
//	private static double calculateMean(List<Double> list, int totalWeight) {
//		double total=0.0;
//		
//		for(Double d: list) {
//			total += d;
//		}
//		
//		//System.out.println(total);
//		
//		if(list.size()==0) {
//			return 1;
//		} else {
//			//System.out.println("is niet nul");
//			//System.out.println(total/(double)list.size());
//			return total/totalWeight;
//		}
//	}
//	
	public static Configuration beginConfiguration(Random rando) {
		ArrayList<Parameter> params = new ArrayList<Parameter>();

//		params.add(new LinearParameter(4,1,0, rando, true)); //2-OPT polynomiaal
//		params.add(new LinearParameter(120,5, 1, rando, true)); // order toevoegen random
//		params.add(new LinearParameter(9,1, 2, rando, true));//34); //order verwijderen
//		params.add(new LinearParameter(30, 5, 3, rando, true));//7.2); //order verplaatsen zelfde dag random
//		params.add(new LinearParameter(80, 5, 4, rando, true));//28.8); // order verplaatsen andere dag random
//		params.add(new PolynomialParameter(5, 1.5, 5, rando, true)); //weight_AReplace
//		params.add(new LinearParameter(0.993, 0.0005, 6, rando, true)); //alpha in the big run
//		params.add(new PolynomialParameter(165, 1.06, 7, rando, true)); //initial T
//		params.add(new LinearParameter(8, 1, 8, rando, true)); //max fisfactor distance
		
		params.add(new PolynomialParameter(1.02, 1.01, 0, rando, true)); //weight_AReplace
		params.add(new LinearParameter(8, 1, 1, rando, true)); //max fisfactor distance
		params.add(new LinearParameter(1, 0.03, 2, rando, true)); //cap
		
		Configuration configuration = new Configuration(params, rando, 0);
		
		return configuration;
	}
	

}
