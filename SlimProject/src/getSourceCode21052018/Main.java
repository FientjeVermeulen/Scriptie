package getSourceCode21052018;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

import org.jblas.DoubleMatrix;

import glove.objects.Vocabulary;

public class Main {
	
	public static void main(String[] args) throws Exception {
		int eindSettings=12;
		int beginConfig = 2; int eindConfig = 11;
		int beginGloveParams = 40; int eindGloveParams = 46;
		int beginSACosParams = 50; int eindSACosParams = 54;
		int beginSAParams = 60; int eindSAParams = 67;
		int beginResults=20; int eindResults = 28;
		int beginSettings = 30; eindSettings = 32;
		int comment = 70;
		String[][] stringParams = readParamInput();
		
		stringParams[30][0] = Integer.toString(beginConfig); stringParams[30][1] = Integer.toString(eindConfig);
		stringParams[31][0] = Integer.toString(beginGloveParams); stringParams[31][1] = Integer.toString(eindGloveParams);
		stringParams[32][0] = Integer.toString(beginSACosParams); stringParams[32][1] = Integer.toString(eindSACosParams);
		stringParams[33][0] = Integer.toString(beginSAParams); stringParams[33][1] = Integer.toString(eindSAParams);
		stringParams[34][0] = Integer.toString(beginResults); stringParams[34][1] = Integer.toString(eindResults);
		stringParams[35][0] = Integer.toString(beginSettings); stringParams[35][1] = Integer.toString(eindSettings);
		stringParams[36][0] = Integer.toString(comment);
		
		
		//write2DArray(stringParams);
		
		//[i][0] = name
		//[i][1] = min
		//[i][2] = max
		//[i][3] = prime
		//[i][4] = paramValue
		
		int kvan = Integer.parseInt(stringParams[7][1]);
		int ktot = Integer.parseInt(stringParams[8][1]);
		
		int bestScore=1000000;
		
		for(int k=kvan; k<=ktot; k++) {
			System.out.println(bestScore);
			System.out.println("k: " + k + " (k*29: " + k*29 + ")");
			stringParams[20][1] = Integer.toString(k);
			System.out.println();
			
			stringParams = haltonFien(k*29, stringParams);
			stringParams = returnStuff(stringParams);
			int configScore = Integer.parseInt(stringParams[23][1]);
			if(configScore < bestScore) {
				bestScore = configScore;
			}

			System.out.println("avgScore: " + configScore);
			System.out.println("-----------------------------------------");
			System.out.println();
			//writeK(igroot-20, stringParams[4][1]);
			stringParams[28][1] = Integer.toString(bestScore);
			writeConfigurationLine(stringParams);
		}
		writeMachineSummary(stringParams);
	}
	
	public static void writeK(int k, String name) throws IOException {
		String fileName = name + "Ks";
		File dir = new File(".\\output\\");
		dir.mkdirs();
		File file = new File(dir, fileName);
		PrintWriter writer = new PrintWriter(new FileWriter(file, true));
		
		writer.println("K: "+ k + " " + name);
		writer.close();
		
	}
	
	public static void write1DArray(String[] array) {
		try {

				System.out.print("[");
				for (int j = 0; j < array.length; j++) {
					System.out.print(",\t" + array[j]);
				}
				System.out.println("]");

		} catch (Exception e) {

		}

	}
	
	public static void print1DArray(double[] array) {
		System.out.print("[");
			for (int j = 0; j < array.length; j++) {
				System.out.print(",\t" + array[j]);
			}
		System.out.println("]");
	}
	
	public static void print2DArray(String[][] array) {
	
		try {
			for(int i=0; i<array.length; i++) {
				System.out.print("[");
				for (int j = 0; j < array[i].length; j++) {
					System.out.print(",\t" + array[i][j]);
				}
				System.out.println("]");
			}
		} catch (Exception e) {

		}
	}
	
	private static String[][] readParamInput() {
		File file = new File("input\\paramInput\\paramInput.txt");
		String[][] params = new String[71][5];
		
		try {
			Scanner input = new Scanner(file);
			input.useLocale(Locale.ENGLISH);
			input.useDelimiter(":|\r|,");
			
			int rowNumber =1;
			
			while(input.hasNext()) {
				String line = input.nextLine();

				if(line.startsWith("%")) {
				} else {
					Scanner lineScanner = new Scanner(line);
					lineScanner.useLocale(Locale.ENGLISH);
					lineScanner.useDelimiter(", min:|, max:|, prime:|:|\r| %");
					
					int colNumber = 0;
					
					while(lineScanner.hasNext()) {
						String next = lineScanner.next();
					
						if(next.startsWith("%")) {
							break;
						}
						params[rowNumber][colNumber] = next;
						colNumber++;
					}
					lineScanner.close();
				}
				rowNumber++;
			}
					
			input.close();
		} catch (IOException e) {
			System.out.println("WHY YOU NO GIVE FILE?!");
			return new String[1][1];
		}		
		try {
		    Files.write(Paths.get("input\\paramInput\\paramInput.txt"), "_".getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    System.out.println("noooo");
		}
		
		return params;
	}
	
	/*
	 * private static String[][] readParamInput2(int eindSettings, int beginParams,
	 * int eindParams) { File file = new File("input\\paramInput\\paramInput.txt");
	 * String[][] params = new String[51][5];
	 * 
	 * try { Scanner input = new Scanner(file); input.useLocale(Locale.ENGLISH);
	 * input.useDelimiter(":|\r|,");
	 * 
	 * input.nextLine();
	 * params[1][0]="eindSettings";params[1][1]=Integer.toString(eindSettings);
	 * input.nextLine();
	 * params[2][0]="beginParams";params[2][1]=Integer.toString(beginParams);
	 * input.nextLine(); params[3][0]=
	 * "eindParams";params[3][1]=Integer.toString(eindParams); input.nextLine();
	 * 
	 * int counter = 4;
	 * 
	 * while(counter<=eindSettings) { params[counter][0] = input.next();
	 * params[counter][1] = input.next(); input.nextLine(); counter++; }
	 * 
	 * input.nextLine(); input.nextLine(); input.nextLine(); input.nextLine();
	 * input.nextLine(); counter=beginParams; while(counter<=eindParams) {
	 * params[counter][0] = input.next(); input.next();//min params[counter][1] =
	 * input.next(); input.next();//max params[counter][2] = input.next();
	 * input.next();//prime params[counter][3] = input.next(); input.nextLine();
	 * counter++; } input.nextLine(); input.nextLine(); //mean input.nextLine();
	 * //meantime
	 * 
	 * params[49][0] = "timePerSolution"; counter=50; //input.nextLine();
	 * params[counter][0] = input.next(); params[counter][1] = input.next();
	 * 
	 * input.close(); } catch (IOException e) {
	 * System.out.println("WHY YOU NO GIVE FILE?!"); return new String[1][1]; }
	 * 
	 * try { Files.write(Paths.get("input\\paramInput\\paramInput.txt"),
	 * "_".getBytes(), StandardOpenOption.APPEND); }catch (IOException e) {
	 * System.out.println("noooo"); }
	 * 
	 * return params; }
	 */
	
	private static void writeSolutionLine(String[][] stringParams) {
		String batchName = stringParams[3][1];
		String machineName = stringParams[2][1];
		int beginConfig = Integer.parseInt(stringParams[30][0]); int eindConfig = Integer.parseInt(stringParams[30][1]);
		int beginSACos = Integer.parseInt(stringParams[32][0]); int eindSACos = Integer.parseInt(stringParams[32][1]);
		int beginGloVe = Integer.parseInt(stringParams[31][0]); int eindGloVe = Integer.parseInt(stringParams[31][1]);
		int beginSA = Integer.parseInt(stringParams[33][0]); int eindSA = Integer.parseInt(stringParams[33][1]);
		int beginResults = Integer.parseInt(stringParams[34][0]); int eindResults = Integer.parseInt(stringParams[34][1]);
		int comment = Integer.parseInt(stringParams[36][0]);
		
		try {
			String fileName = machineName + "perSolution";
			String fileNameNrs = machineName + "perSolutionNumbers";
			File dir = new File(".\\output\\" + batchName + "\\perSolution\\");
			dir.mkdirs();
			File file = new File(dir, fileName);
			File fileNrs = new File(dir, fileNameNrs);
			PrintWriter writer = new PrintWriter(new FileWriter(file, true));
			PrintWriter writerNrs = new PrintWriter(new FileWriter(fileNrs, true));
			
			writer.print("[Comment: " + stringParams[comment][1] + ", ");
			writerNrs.print("[Comment: " + stringParams[comment][1] + ", ");
			writer.print("SCORE: " + stringParams[22][1] + ", ");
			writerNrs.print("SCORE: " + stringParams[22][1]+ ", ");
			for (int i = beginConfig; i <=eindConfig; i++) {
				writer.print(stringParams[i][0] + ":" + stringParams[i][1]);
				writerNrs.print(stringParams[i][1]);
				writer.print(", ");
				writerNrs.print(", ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			
			for (int i = beginResults; i <=eindResults; i++) {
				writer.print(stringParams[i][0] + ":" + stringParams[i][1]);
				writerNrs.print(stringParams[i][1]);
				writer.print(", ");
				writerNrs.print(", ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			
			for (int i = beginGloVe; i <=eindGloVe; i++) {
				writer.print(stringParams[i][0] + ":" + stringParams[i][4]);
				writerNrs.print(stringParams[i][4]);
				writer.print(", ");
				writerNrs.print(", ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			
			for (int i = beginSACos; i <=eindSACos; i++) {
				writer.print(stringParams[i][0] + ":" + stringParams[i][4]);
				writerNrs.print(stringParams[i][4]);
				writer.print(", ");
				writerNrs.print(", ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			
			for (int i = beginSA; i <=eindSA; i++) {
				writer.print(stringParams[i][0] + ":" + stringParams[i][4]);
				writerNrs.print(stringParams[i][4]);
				writer.print(", ");
				writerNrs.print(", ");
			}
						
			writer.println("]");
			writerNrs.println("]");
			
			writer.close();
			writerNrs.close();
		} catch (Exception e) {

		}
	}
	
	private static void writeConfigurationLine(String[][] stringParams) {
		String name = stringParams[3][1];
		int beginConfig = Integer.parseInt(stringParams[30][0]); int eindConfig = Integer.parseInt(stringParams[30][1]);
		int beginSACos = Integer.parseInt(stringParams[32][0]); int eindSACos = Integer.parseInt(stringParams[32][1]);
		int beginGloVe = Integer.parseInt(stringParams[31][0]); int eindGloVe = Integer.parseInt(stringParams[31][1]);
		int beginSA = Integer.parseInt(stringParams[33][0]); int eindSA = Integer.parseInt(stringParams[33][1]);
		int beginResults = Integer.parseInt(stringParams[34][0]); int eindResults = Integer.parseInt(stringParams[34][1]);
		int comment = Integer.parseInt(stringParams[36][0]);
		
		try {
			String fileName = name + "configuration";
			String fileNameNrs = name + "configurationNumbers";
			File dir = new File(".\\output\\" + name + "\\parameterConfigurations\\");
			dir.mkdirs();
			File file = new File(dir, fileName);
			File fileNrs = new File(dir, fileNameNrs);
			PrintWriter writer = new PrintWriter(new FileWriter(file, true));
			PrintWriter writerNrs = new PrintWriter(new FileWriter(fileNrs, true));
			
			writer.print("[Comment: " + stringParams[comment][1] + ", ");
			writerNrs.print("[Comment: " + stringParams[comment][1] + ", ");
			writer.print("AVG_SCORE: " + stringParams[23][1] + ", ");
			writerNrs.print("AVG_SCORE: " + stringParams[23][1]+ ", ");
			for (int i = beginConfig; i <=eindConfig; i++) {
				writer.print(stringParams[i][1]);
				writerNrs.print(stringParams[i][1]);
				writer.print(", ");
				writerNrs.print(", ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			writer.print("AVG_SCORE: " + stringParams[23][1] + ", ");
			writerNrs.print("AVG_SCORE: " + stringParams[23][1]+ ", ");
			
			for (int i = beginResults; i <=eindResults; i++) {
				writer.print(stringParams[i][0] + ":" + stringParams[i][1]);
				writerNrs.print(stringParams[i][1]);
				writer.print(", ");
				writerNrs.print(", ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			writer.print("AVG_SCORE: " + stringParams[23][1] + ", ");
			writerNrs.print("AVG_SCORE: " + stringParams[23][1]+ ", ");
			
			for (int i = beginGloVe; i <=eindGloVe; i++) {
				writer.print(stringParams[i][0] + "(min:" + stringParams[i][1]  + ",max:" + stringParams[i][2] + ",prime:" + stringParams[i][3] + ",value:" + stringParams[i][4]);
				writerNrs.print(stringParams[i][0] + "(:" + stringParams[i][1]  + ":" + stringParams[i][2] + ":" + stringParams[i][3] + ":" + stringParams[i][4]);
				writer.print("), ");
				writerNrs.print("), ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			writer.print("AVG_SCORE: " + stringParams[23][1] + ", ");
			writerNrs.print("AVG_SCORE: " + stringParams[23][1]+ ", ");
			
			for (int i = beginSACos; i <=eindSACos; i++) {
				writer.print(stringParams[i][0] + "(min:" + stringParams[i][1]  + ",max:" + stringParams[i][2] + ",prime:" + stringParams[i][3] + ",value:" + stringParams[i][4]);
				writerNrs.print(stringParams[i][0] + "(:" + stringParams[i][1]  + ":" + stringParams[i][2] + ":" + stringParams[i][3] + ":" + stringParams[i][4]);
				writer.print("), ");
				writerNrs.print("), ");
			}
			
			writer.print("]\t[");
			writerNrs.print("]\t[");
			writer.print("AVG_SCORE: " + stringParams[23][1] + ", ");
			writerNrs.print("AVG_SCORE: " + stringParams[23][1]+ ", ");
			
			for (int i = beginSA; i <=eindSA; i++) {
				writer.print(stringParams[i][0] + ", min:" + stringParams[i][1]  + ",max:" + stringParams[i][2] + ",prime:" + stringParams[i][3] + ",value:" + stringParams[i][4]);
				writerNrs.print(stringParams[i][0] + ",:" + stringParams[i][1]  + ":" + stringParams[i][2] + ":" + stringParams[i][3] + ":" + stringParams[i][4]);
				writer.print("), ");
				writerNrs.print("), ");
			}
						
			writer.println("]");
			writerNrs.println("]");
			
			writer.close();
			writerNrs.close();
		} catch (Exception e) {

		}
	}
	
	private static void writeMachineSummary(String[][] stringParams) {
		String machineName = stringParams[2][1];
		String batchName = stringParams[3][1];
		int beginSettings = Integer.parseInt(stringParams[30][0]);
		int eindSettings = Integer.parseInt(stringParams[30][1]);
		int beginParams = Integer.parseInt(stringParams[31][0]);
		int eindParams = Integer.parseInt(stringParams[33][1]);
		
		try {
			String fileName = machineName + "Summary";
			File dir = new File(".\\output\\" + batchName + "\\parameterConfigurations\\");
			dir.mkdirs();
			File file = new File(dir, fileName);
			PrintWriter writer = new PrintWriter(new FileWriter(file, true));
			
			writer.println("Best AVG score: " + stringParams[28][1] + "\n");

			for (int i = beginSettings; i <=eindSettings; i++) {
				writer.println(stringParams[i][0] + ":" + stringParams[i][1]);
			}
			
			writer.println();
			
			for (int i = beginParams; i <=eindParams; i++) {
				writer.println(stringParams[i][0] + ", min:" + stringParams[i][1]  + ", max:" + stringParams[i][2] + ", prime:" + stringParams[i][3] + ", value:" + stringParams[i][4]);
			}
			
			writer.println();
			//writer.println(stringParams[50][0] + ":" + stringParams[50][1]);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[][] haltonFien(int k, String[][] inputParams) {
		int beginGloVeParams = Integer.parseInt(inputParams[31][0]); int eindGloVeParams = Integer.parseInt(inputParams[31][1]);
		int beginSACosParams = Integer.parseInt(inputParams[32][0]); int eindSACosParams = Integer.parseInt(inputParams[32][1]);
		int beginSAParams = Integer.parseInt(inputParams[33][0]); int eindSAParams = Integer.parseInt(inputParams[33][1]);

		for(int i=beginGloVeParams; i<=eindGloVeParams; i++) {
			double min = Double.parseDouble(inputParams[i][1]);
			double max = Double.parseDouble(inputParams[i][2]);
			int prime = Integer.parseInt(inputParams[i][3]);
			
			double diff = max-min; 
			double phi = 0;
			double a;
			int pprime = prime;
			
			int kk = k;
			 while(kk > 0) {
				 a = Math.floorMod(kk, prime);
				 phi = phi + (a/pprime);
				 kk = Math.floorDiv(kk, prime);
				 pprime = prime*pprime;
			 }

			 String value = String.format(Locale.ENGLISH,"%.3f",phi*diff+min);
			 inputParams[i][4] = value;
			 
			 System.out.println(inputParams[i][0] + " : " + value);
		}	
		
		for(int i=beginSACosParams; i<=eindSACosParams; i++) {
			double min = Double.parseDouble(inputParams[i][1]);
			double max = Double.parseDouble(inputParams[i][2]);
			int prime = Integer.parseInt(inputParams[i][3]);
			
			double diff = max-min; 
			double phi = 0;
			double a;
			int pprime = prime;
			
			int kk = k;
			 while(kk > 0) {
				 a = Math.floorMod(kk, prime);
				 phi = phi + (a/pprime);
				 kk = Math.floorDiv(kk, prime);
				 pprime = prime*pprime;
			 }

			 String value = String.format(Locale.ENGLISH,"%.3f",phi*diff+min);
			 inputParams[i][4] = value;
			 
			 System.out.println(inputParams[i][0] + " : " + value);
		}		
		
		for(int i=beginSAParams; i<=eindSAParams; i++) {
			double min = Double.parseDouble(inputParams[i][1]);
			double max = Double.parseDouble(inputParams[i][2]);
			int prime = Integer.parseInt(inputParams[i][3]);
			
			double diff = max-min; 
			double phi = 0;
			double a;
			int pprime = prime;
			
			int kk = k;
			 while(kk > 0) {
				 a = Math.floorMod(kk, prime);
				 phi = phi + (a/pprime);
				 kk = Math.floorDiv(kk, prime);
				 pprime = prime*pprime;
			 }

			 String value = String.format(Locale.ENGLISH,"%.3f",phi*diff+min);
			 inputParams[i][4] = value;
			 
			 System.out.println(inputParams[i][0] + " : " + value);
		}
		
		return inputParams;
	}
	
	public static String weightFCodeToString(int code) {
		switch(code) {
		case 1:
			return "oneDistance";
		case 2:
			return "normalDistance";
		case 3:
			return "squaredDistance";
		}
		
		return "ERROR";
	}
	
	public static int calculateMean(ArrayList<Integer> list) {
		int total=0;
		
		for(int i: list) {
			total += i;
		}
		
		if(list.size()==0) {
			return 1;
		} else {
			return total/list.size();
		}
	}
	
	public static String[][] returnStuff(String[][] stringParams) throws Exception {
		LinkedList<Order> allOrders = loadOrders();
		int[] dictionary = createDictionary(allOrders);
		int[] reverseDictionary = createReverseDictionary(allOrders);

		double[][] d_matrix = loadMatrix();
		ParseBeginSolutions beginSolutions = new ParseBeginSolutions();
		beginSolutions.loadBeginSolutions();
		
		LinkedList<Solution> solutions = new LinkedList<Solution>();
		
		double[][] supports = convertDoubleMatrix(stringParams, d_matrix, reverseDictionary, dictionary);

		int vanSol = Integer.parseInt(stringParams[9][1]);
		int totSol = Integer.parseInt(stringParams[10][1]);
		int runs = totSol - vanSol + 1;
		long solItsM = Integer.parseInt(stringParams[11][1]);
		long solIts = solItsM * 1000000L;
		double totalScore = 0.0D;
		int totalTime = 0;
		double bestScore = 100000000;
		String batchName = stringParams[3][1];
		boolean saveSolutions = Boolean.parseBoolean(stringParams[6][1]);
		boolean saveScores = false;

		double[] scores = new double[runs];
		int run=0;
		stringParams[23][1] = "0";
		stringParams[24][1] = "0";
		stringParams[25][1] = "0";
		stringParams[27][1] = "0";
		
		
		for (int s = vanSol; s <= totSol; s++) {
			System.out.println("solution number " + s);
		    Date date = new Date();
			System.out.println(date.toString());
			
			stringParams[21][1] = Integer.toString(s);
			long startTimeR = System.currentTimeMillis();

			SimulatedAnnealing simA = beginSolutions.nextSolution(s);
			//print2DArray(stringParams);
			Solution endSolution = simA.run(solIts, stringParams, supports, dictionary);
			
			long endTimeR = System.currentTimeMillis();
			int runTimedeciSec = (int) ((endTimeR-startTimeR)/100);
			//System.out.println(runTimedeciSec);
			totalTime += runTimedeciSec;
			stringParams[26][1] = Integer.toString(runTimedeciSec);
			
			stringParams[22][1] = Integer.toString((int)(simA.score/60));
			endSolution.score = simA.score;

			scores[run] = endSolution.score;
			if (bestScore > endSolution.score) {
				bestScore = endSolution.score;
			}
			totalScore += endSolution.score;
			solutions.add(endSolution);
			
			writeSolutionLine(stringParams);

			if (saveSolutions) {
				writeToFileSingle(batchName, endSolution, run, s, startTimeR);
			} else if (saveScores){
				//writeScores(simA.score, i, stringParams);
			}
			run++;
		}

		int averageScore = (int) (totalScore / runs);		
		int averageTime = totalTime/runs;
		double standardDev = calculateStandardDeviation(scores, averageScore);
		String sDev = String.format("%.2f", standardDev/60);
		
		stringParams[21][1] = "0";
		stringParams[22][1] = "0";
		stringParams[23][1] = Integer.toString((int) averageScore/60);
		stringParams[24][1] = Integer.toString((int) bestScore/60);
		stringParams[25][1] = sDev;
		stringParams[26][1] = "0";
		stringParams[27][1] = Integer.toString(averageTime);
		
		System.out.println();
		System.out.println("Average score: " + (int) averageScore/60);
		System.out.println("Best score: " + (int) bestScore/60);
		System.out.println("Standard Deviation: " + sDev);
		System.out.println(
				Math.floorDiv(averageTime, 600) + " minuten en " + Math.floorMod(averageTime, 600) + " deciseconden");
		System.out.println();
		
		return stringParams;
	}


	private static int[] createReverseDictionary(LinkedList<Order> allOrders) {
		int[] reverseDictionary = new int[1177];
		int i = 0;

		for (Order o : allOrders) {
			reverseDictionary[i] = o.orderNr;
			i++;
		}
		return reverseDictionary;
	}


	private static double[][] convertDoubleMatrix(String[][] stringParams, double[][] d_matrix, int[] reverseDictionary,
		int[] dictionary) throws Exception {
		Vocabulary vocab = readVocab(stringParams);
		DoubleMatrix W = readDoubleMatrix(stringParams);
		//System.out.println(W);

		double cap = Double.parseDouble(stringParams[54][4]);
		double[][] supports = new double[1177][1177];

		for (int i = 0; i < 1177; i++) {
			for (int j = 0; j < 1177; j++) {
				String orderNr1 = String.valueOf(reverseDictionary[i]);
				String orderNr2 = String.valueOf(reverseDictionary[j]);	
				//System.out.println(orderNr1);
				try {
					DoubleMatrix vector1 = W.getColumn(vocab.getWordId(orderNr1));
					//System.out.println(vector1);
					DoubleMatrix vector2 = W.getColumn(vocab.getWordId(orderNr2));
					double cos = cosineSimilarity(vector1,vector2);
					//System.out.println(cos);
					supports[i][j] = Math.pow((cos+1),(1+cap))/Math.pow(2, cap);
					//System.out.println(supports[i][j]);

					if(supports[i][j]<0) {
						supports[i][j]= 0.0000001;
						System.out.println("zero");
					}
					
					if(supports[i][j]>2) {
						supports[i][j]= 1.9999999;
						//System.out.println("1");
					}
				} catch (NullPointerException e){
					supports[i][j] = 0;
				}

			}
		}

		return supports;
	}
	
	public static void write2DArray(double[][] array, String name) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter("output\\array2D" + name + ".txt", true));
			
			for (int i = 0; i < array.length; i++) {
				writer.print("[");
				for (int j = 0; j < array[0].length; j++) {
					writer.format("%.3f", array[i][j]);
					writer.print("\t");
				}
				writer.println("]");
			}

			writer.close();
		} catch (Exception e) {
		}
	}

	
	//Takes 2 vectors a, b and returns the cosine similarity according to the definition of the dot product
	private static double cosineSimilarity(DoubleMatrix vector1, DoubleMatrix vector2) {
			double dot_product = vector1.dot(vector2);
			
			double norm_a = vector1.norm2();
			double norm_b = vector2.norm2();
			
			return dot_product / (norm_a * norm_b);
	}


	private static DoubleMatrix readDoubleMatrix(String[][] stringParams) throws Exception {
		DoubleMatrix W = null;
		
		int windowSize= (int) Double.parseDouble(stringParams[40][4]);
		int iterations = (int) Double.parseDouble(stringParams[41][4]);
		int vectors = (int) Double.parseDouble(stringParams[42][4]);
		int xmax = (int) Double.parseDouble(stringParams[43][4]);
		int learningRate = (int) Double.parseDouble(stringParams[44][4]);
		int alphaGloVe = (int) Double.parseDouble(stringParams[45][4]);
		int weightFCode = (int) Double.parseDouble(stringParams[46][4]);
		String weight = "true";//stringParams[8][1];
		
		try {
			FileInputStream fi = new FileInputStream(new File("input/GloVeObjects/DoubleMatrixS" + windowSize + "I" + iterations + "V" + vectors + "X" + xmax + "learningRate" + learningRate + "A" + alphaGloVe + weightFCodeToString(weightFCode) + "weight" + weight + "-object.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			W = (DoubleMatrix) oi.readObject();
			//System.out.println(W);
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			glove.Test_Glove.main(stringParams);
			try {
				FileInputStream fi = new FileInputStream(
						new File("input/GloVeObjects/DoubleMatrixS" + windowSize + "I" + iterations + "V" + vectors + "X" + xmax + "learningRate" + learningRate + "A" + alphaGloVe + weightFCodeToString(weightFCode) + "weight" + weight +  "-object.txt"));
				ObjectInputStream oi2 = new ObjectInputStream(fi);

				// Read objects
				W = (DoubleMatrix) oi2.readObject();

				oi2.close();
				fi.close();
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
				throw e2;
			} catch (IOException e2) {
				e2.printStackTrace();
				throw e2;
			} catch (ClassNotFoundException e2) {	
				e2.printStackTrace();
				throw e2;
			}		
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw e;
		}

		return W;
	}
	
	private static Vocabulary readVocab(String[][] stringParams) throws Exception {
		Vocabulary vocab = null;
		
		int windowSize= (int) Double.parseDouble(stringParams[40][4]);
		int iterations = (int) Double.parseDouble(stringParams[41][4]);
		int vectors = (int) Double.parseDouble(stringParams[42][4]);
		int xmax = (int) Double.parseDouble(stringParams[43][4]);
		int learningRate = (int) Double.parseDouble(stringParams[44][4]);
		int alphaGloVe = (int) Double.parseDouble(stringParams[45][4]);
		int weightFCode = (int) Double.parseDouble(stringParams[46][4]);
		String weight = "true";//stringParams[8][1];		
		
		try {
			FileInputStream fi = new FileInputStream(
					new File("input/GloVeObjects/VocabularyS" + windowSize + "I" + iterations + "V" + vectors + "X" + xmax + "learningRate" + learningRate + "A" + alphaGloVe + weightFCodeToString(weightFCode) + "weight" + weight  + "-object.txt"));
			
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			vocab = (Vocabulary) oi.readObject();
			System.out.println(vocab);
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			glove.Test_Glove.main(stringParams);
			try {
				FileInputStream fi = new FileInputStream(
						new File("input/GloVeObjects/VocabularyS" + windowSize + "I" + iterations + "V" + vectors + "X" + xmax + "learningRate" + learningRate + "A" + alphaGloVe + weightFCodeToString(weightFCode) + "weight" + weight + "-object.txt"));
				ObjectInputStream oi = new ObjectInputStream(fi);

				// Read objects
				vocab = (Vocabulary) oi.readObject();
				System.out.println(vocab);
				oi.close();
				fi.close();
			} catch (FileNotFoundException e2){
				throw e2;
			}catch (IOException e2) {
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return vocab;
	}

	
	public static void write1DArray(int[] array, String name) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter("parameterArrays\\array2D" + name + ".txt", true));

				writer.print("[");
				for (int j = 0; j < array.length; j++) {
					writer.print(",\t" + array[j]);
				}
				writer.println("]");

			writer.close();
		} catch (Exception e) {
		}
	}
	
	

	private static int[] createDictionary(LinkedList<Order> allOrders) {
		int[] dictionary = new int[34911];
		int i = 0;

		for (Order o : allOrders) {
			dictionary[o.orderNr] = i;
			i++;
		}

		return dictionary;
	}

	public static double[][] loadMatrix() {
		File file = new File("input\\MatrixBestand.txt");
		double[][] matrix = new double[1099][1099];
		try {
			Scanner input = new Scanner(file);
			input.useDelimiter(";|\r");
			int counter = 0;
			input.nextLine();
			input.nextLine();
			while (input.hasNext()) {
				counter++;
				int matrixID1 = input.nextInt();
				int matrixID2 = input.nextInt();
				double tijd = input.nextDouble();
				input.nextLine();

				matrix[matrixID1][matrixID2] = tijd;
			}
			input.close();
			return matrix;
		} catch (IOException e) {
			System.out.println("WHY YOU NO GIVE FILE?!");
			return new double[1][1];
		}
	}

	public static LinkedList<Order> loadOrders() {
		File file = new File("input\\RequestBestand.txt");
		LinkedList<Order> allOrders = new LinkedList<Order>();
		try {
			Scanner input = new Scanner(file);
			input.useDelimiter(";");
			input.nextLine();

			while (input.hasNext()) {
				int orderNr = input.nextInt();
				input.next();
				int freq = Character.getNumericValue(input.next().charAt(0));
				int containers = input.nextInt();
				int volume = input.nextInt();
				double duurM = Double.parseDouble(input.next());
				double duur = duurM * 60.0D;
				int matrixID = input.nextInt();
				input.nextLine();

				Order order = new Order(orderNr, freq, volume * containers, duur, matrixID);
				allOrders.add(order);
			}
			input.close();
			return allOrders;
		} catch (IOException e) {
			System.out.println("WHY YOU NO GIVE FILE?!");
		}
		return new LinkedList<Order>();
	}

	public static void writeToFileSingle(String batchname, Solution solution, int number, int j, long startTime) {
		try {
			String solutionName = "solution" + String.format("%03d", number) + "-" + j + ".txt";
			// File dir = new File(".\\input\\BeginOplossingen\\");
			File dir = new File(".\\output\\" + batchname + "\\");
			dir.mkdirs();
			File file = new File(dir, solutionName);
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			solution.writeToFile(writer);

			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void writeObjectToFile(SimulatedAnnealing solution, int number) {
		try {
			String objectName = "BeginSolution" + String.format("%03d", number) + "-object.txt";
			File dir = new File(".\\input\\BeginOplossingen\\");
			dir.mkdirs();
			File file = new File(dir, objectName);
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(solution);

			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void writeScores(double score, int number, String[][]
	 * stringParams) { String batchName = stringParams[3][1];
	 * 
	 * int windowSize= (int) Double.parseDouble(stringParams[18][4]); int iterations
	 * = (int) Double.parseDouble(stringParams[19][4]); int vectors = (int)
	 * Double.parseDouble(stringParams[20][4]); int xmax = (int)
	 * Double.parseDouble(stringParams[21][4]); int learningRate = (int)
	 * Double.parseDouble(stringParams[22][4]); int alphaGloVe = (int)
	 * Double.parseDouble(stringParams[23][4]); int weightFCode = (int)
	 * Double.parseDouble(stringParams[24][4]); String weight = "true";
	 * //stringParams[8][1];
	 * 
	 * try { String fileName = "ScoresS" + windowSize + "I" + iterations + "V" +
	 * vectors + "X" + xmax + "learningRate" + learningRate + "A" + alphaGloVe +
	 * weightFCodeToString(weightFCode) + "weight" + weight + "-object.txt"; File
	 * dir = new File(".\\output\\" + batchName + "\\solutions\\"); dir.mkdirs();
	 * File file = new File(dir, fileName); FileWriter fileW = new FileWriter(file,
	 * true); PrintWriter writer = new PrintWriter(fileW);
	 * 
	 * writer.println(number + "; " + Math.floorDiv((int) score, 60));
	 * writer.close();
	 * 
	 * } catch (FileNotFoundException | UnsupportedEncodingException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } }
	 */


	public static double calculateStandardDeviation(double[] scores, double averageScore) {
		double totalDeviation = 0;

		for (int i = 0; i < scores.length; i++) {
			double deviation = Math.pow(averageScore - scores[i], 2);
			totalDeviation += deviation;
		}

		return Math.sqrt(totalDeviation / (scores.length - 1));
	}
	
	public static double calculateVariance(double[] scores, double averageScore) {
		double totalDeviation = 0;

		for (int i = 0; i < scores.length; i++) {
			double deviation = Math.pow(averageScore - scores[i], 2);
			totalDeviation += deviation;
		}

		return totalDeviation / (scores.length - 1);
	}

}