package getSourceCode21052018;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ParseScores {
	
	Path batchPath;

	public ParseScores() {
	}
	

	
	public ArrayList<Integer> parseBatches(String batchname) throws IOException {
		Path inputDir = Paths.get("output/");
		System.out.println();
		ArrayList<Path> allBatches = listSourceFiles(inputDir);		
		ArrayList<Integer> scores = null;
		
		
		for(Path batchPath : allBatches) {
			String string = batchPath.toString();
			
			if(string.startsWith("output\\" + batchname)) {
				scores = parseBatch(batchPath);
			}
		}
		
		return scores;
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
	
	
	public ArrayList<Integer> parseBatch(Path batchPath) throws IOException {
		ArrayList<Path> allSolutions = listSourceFiles(batchPath);
		Path metadata;
		int i=0;
		
		ArrayList<Integer> scores = new ArrayList<Integer>();

		
		while(i < allSolutions.size()-1) {
			Path path1 = allSolutions.get(i);
			String path1String = path1.toString();

			if(path1String.contains("summary") || path1String.contains("samenvatting")) {
				i++;
			} else {
				Path path2 = allSolutions.get(i+1);
				
				if(path1String.contains("metaData")) {
					metadata = path1;
				} else {
					metadata = path2;
				}
				scores.add(parseScores(metadata));
				i = i+2;
			}
			
		}
		
		return scores;
	}
	
	public int parseScores(Path metadata) {	
		int score =0;
		
		try {
			Scanner metadataInput = new Scanner(metadata);
			metadataInput.useLocale(Locale.ENGLISH);
			metadataInput.useDelimiter("Minutes: |\r?\n|\r");
			
			metadataInput.nextLine();
			metadataInput.nextLine();
			metadataInput.nextLine();
			metadataInput.nextLine();
			
			score = metadataInput.nextInt();
			
			
			metadataInput.close();
		} catch (IOException e){
			
		}
		
		return score;
	}
	

}
