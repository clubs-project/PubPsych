/**
 * 
 */
package eu.pubpsych.query;

import java.io.File;
import java.util.Locale;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author cristinae
 *
 */
public class Preprocessor {
	
	
	
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
		Tok2Stemmer t2s = new Tok2Stemmer(new Locale(language));

	}

}
