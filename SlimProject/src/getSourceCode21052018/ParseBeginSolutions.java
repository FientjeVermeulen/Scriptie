package getSourceCode21052018;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ParseBeginSolutions {
	
	Path batchPath;

	public ParseBeginSolutions() {
	}
	
	public SimulatedAnnealing nextSolution(int solutionCounter) {
		SimulatedAnnealing solution = null;
		
		try {	
			FileInputStream fi = new FileInputStream(new File(batchPath.toFile(), "BeginSolution" + String.format("%03d", solutionCounter) + "-object.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			solution = (SimulatedAnnealing) oi.readObject();
			
			oi.close();
			fi.close();
		} catch (IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return solution;
	}

   public void loadBeginSolutions() {

		try {			
			parseBatches();
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void parseBatches() throws IOException {
		Path inputDir = Paths.get("input/");
		System.out.println();
		ArrayList<Path> allBatches = listSourceFiles(inputDir);		

		for(Path batchPath : allBatches) {
			String string = batchPath.toString();
			
			if(string.startsWith("input\\BeginOplossingen")) {
				this.batchPath = batchPath;
			}
		}
		
	}
	
  ArrayList<Path> listSourceFiles(Path inputDir) throws IOException {
      ArrayList<Path> result = new ArrayList<>();
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir)) {
          for (Path entry: stream) {
              result.add(entry);
          }
      } catch (DirectoryIteratorException ex) {
          // I/O error encounted during the iteration, the cause is an IOException
          throw ex.getCause();
      }
      return result;
  }
	
	
	public void parseSolution(Path solution, int number) {		

	}

}
