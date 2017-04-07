package eu.pubpsych.query;

import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;


/**
 * Factory for the analyzers PubPsych needs.
 * 
 * @author cristinae
 * @since 07.04.2017
 */
public class AnalyzerFactory {
	
	protected static Analyzer analyzer; 


	/** Analyser from Lucene for different languages.
	 * 
	 * @param language
	 * @return The Lucene analyser for the required language; error if not available.
	 */
	public static Analyzer loadAnalyzer(Locale language){
		
		String lang = null;
		try {
			lang = language.getLanguage();
		} catch (NullPointerException e){
			System.out.println("A language must be choosen for your analyser");
		}
		
		
		switch(lang){
		case "de":
			analyzer = new GermanAnalyzer();
			break;
		case "en":
			analyzer = new EnglishAnalyzer();
			break;
		case "es":
			analyzer = new SpanishAnalyzer();
			break;
		case "fr":
			analyzer = new FrenchAnalyzer();
			break;
		default:
			return null;
		}
		return analyzer;
	}

	


}
