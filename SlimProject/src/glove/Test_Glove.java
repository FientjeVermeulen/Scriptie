/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package glove;

import glove.objects.Cooccurrence;
import glove.objects.Vocabulary;
import glove.utils.Options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Thanos
 */
public class Test_Glove {
	
     
    public static void main(String[][] stringParams) throws Exception { 
    	int beginParams = Integer.parseInt(stringParams[31][0]);
    	int eindParams = Integer.parseInt(stringParams[31][1]);
    	
		int windowSize= (int) Double.parseDouble(stringParams[beginParams][4]);
		int iterations = (int) Double.parseDouble(stringParams[beginParams+1][4]);
		int vectors = (int) Double.parseDouble(stringParams[beginParams+2][4]);
		int xmax = (int) Double.parseDouble(stringParams[beginParams+3][4]);
		int learningRate = (int) Double.parseDouble(stringParams[beginParams+4][4]);
		int alphaGloVe = (int) Double.parseDouble(stringParams[beginParams+5][4]);
		int weightFCode = (int) Double.parseDouble(stringParams[beginParams+6][4]);
		String distanceFunction = getSourceCode21052018.Main.weightFCodeToString(weightFCode);	
		
        String file = "input\\allGoodSequencesSplitSpace.txt";   
		
		System.out.println("windowSize: " + windowSize + " iterations: " + iterations + " vector: " + vectors + " xmax: " + xmax + " learningRate: " + learningRate + " alphaGloVe: " + alphaGloVe + " Distance function: " + distanceFunction);
	    System.out.println("start GloVe");
	    
        Options options = new Options();       
        options.window_size = windowSize; //8
        options.iterations = iterations; //10
        options.vector_size = vectors; //8
        options.weightFCode = weightFCode; //1
        options.x_max = xmax;
        options.learning_rate = (double) learningRate/1000;
        System.out.println(options.learning_rate);
        options.alpha = (double) alphaGloVe/1000;
        //options.weight = weight;
        options.debug = true;
        
	    Vocabulary vocab = returnVocab(file, options);
	    writeVocabToFile(vocab, windowSize, iterations, vectors, xmax, learningRate, alphaGloVe, distanceFunction, true);
	    DoubleMatrix W = returnStuff(vocab, file, options);
        writeMatrixToFile(W, windowSize, iterations, vectors, xmax, learningRate, alphaGloVe, distanceFunction, true);
    }

    public static DoubleMatrix returnStuff(Vocabulary vocab, String file, Options options) throws Exception {        
        List<Cooccurrence> c =  GloVe.build_cooccurrence(vocab, file, options);
        DoubleMatrix W = GloVe.train(vocab, c, options);
        return W;
    }
    

    
    public static void writeVocabToFile(Vocabulary vocab, int scope, int iterations, int vectors, int xmax, int learningRate, int alphaGloVe, String distanceFunction, boolean weight) {
 		try {
 	        String objectName = "VocabularyS" + scope + "I" + iterations + "V" + vectors + "X" + xmax + "learningRate" + learningRate + "A" + alphaGloVe + distanceFunction + "weight" + weight + "-object.txt";
 	        File dir = new File(".\\input\\GloVeObjects\\");
 	        dir.mkdirs();
 	        File file = new File(dir, objectName);
 			FileOutputStream f = new FileOutputStream(file);
 			ObjectOutputStream o = new ObjectOutputStream(f);
 			
 			o.writeObject(vocab);
 			
 			o.close();
 			f.close();
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
    }
    
    public static void writeMatrixToFile(DoubleMatrix W, int scope, int iterations, int vectors, int xmax, int learningRate, int alphaGloVe, String distanceFunction, boolean weight) {
 		try {
 	        String objectName = "DoubleMatrixS" + scope + "I" + iterations + "V" + vectors + "X" + xmax + "learningRate" + learningRate + "A" + alphaGloVe + distanceFunction + "weight" + weight + "-object.txt";
 	        File dir = new File(".\\input\\GloVeObjects\\");
 	        dir.mkdirs();
 	        File file = new File(dir, objectName);
 			FileOutputStream f = new FileOutputStream(file);
 			ObjectOutputStream o = new ObjectOutputStream(f);
 			
 			o.writeObject(W);
 			
 			o.close();
 			f.close();
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			
 			e.printStackTrace();
 		}
    }
     
    public static Vocabulary returnVocab(String file, Options options) throws Exception {
        
    	Vocabulary vocab = GloVe.build_vocabulary(file, options);
    	return vocab;
    }
    
}
