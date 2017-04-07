package eu.pubpsych.query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * Main class for pre-processing a file and convert it into a lowercased, tokenised,
 * stop word-free and lemmatised version.
 * 
 * @author cristinae
 * @since 07.04.2017
 */
public class Preprocessor {
	
	/**
	 * Lemmatises an input file line by line
	 * 
	 * @param fIn
	 * @param fOut
	 * @param t2s
	 */
	public static void preprocessFile(File fIn, File fOut, Tok2Stemmer t2s){

		// Initilise the writer
		//FileIO.deleteFile(fOut);
	    FileWriter fw = null;
	    BufferedWriter bw = null;
		try {
			fw = new FileWriter(fOut, true);
			bw = new BufferedWriter(fw);
			bw.write("");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Read the input
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
		    inputStream = new FileInputStream(fIn);
		    sc = new Scanner(inputStream, "UTF-8");
		    int i = 0;
		    while (sc.hasNext()) {
		        String line = sc.nextLine();
		        String stemmedLine = t2s.processing(line);
		        bw.append(stemmedLine);
		        // Write every 10000 lines
		        if (i%10000==0){
		        	bw.flush();
		        }
		        i++;
		    }
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close everything
		    if (inputStream != null) {
		        try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    if (sc != null) {
		        sc.close();
		        try {
		        	bw.newLine();
					bw.close();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }}
		}	
	
	
	/**
	 * Parses the command line arguments
	 * 	
	 * @param args
	 * 			Command line arguments 
	 * @return
	 */
	private static CommandLine parseArguments(String[] args)
	{	
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cLine = null;
		Options options= new Options();
		CommandLineParser parser = new BasicParser();

		options.addOption("l", "language", true, 
					"Language of the input text (de/en/es/fr)");		
		options.addOption("i", "input", true, 
					"Input file to annotate -one sentence per line-");		
		options.addOption("h", "help", false, "This help");

		try {			
		    cLine = parser.parse( options, args );
		} catch( ParseException exp ) {
			System.out.println("Unexpected exception :" + exp.getMessage() );			
		}	
		
		if (cLine.hasOption("h")) {
			formatter.printHelp(Preprocessor.class.getSimpleName(),options );
			System.exit(0);
		}

		if (cLine == null || !(cLine.hasOption("l")) ) {
			System.out.println("Please, set the language\n");
			formatter.printHelp(Preprocessor.class.getSimpleName(),options );
			System.exit(1);
		}		

		return cLine;		
	}


	/**
	 * Main function to run the class, serves as example
	 * 
	 * @param args 
	 * 		-l Language of the input text 
	 *      -i Input text/file
	 */
	public static void main(String[] args) {
		CommandLine cLine = parseArguments(args);
		
		// Language
		String language = cLine.getOptionValue("l");
		// Input file
		File input = new File(cLine.getOptionValue("i"));
		File output = new File(cLine.getOptionValue("i").concat(".lem"));
		//run
		Tok2Stemmer t2s = new Tok2Stemmer(new Locale(language));
		preprocessFile(input, output, t2s);

	}

}
