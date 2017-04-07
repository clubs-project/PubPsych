/**
 * 
 */
package eu.pubpsych.query;

import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


/**
 * Class for preprocessing an input string with tokenisation, stop word removal and stemmming
 * 
 * @author cristinae
 * @since 07.04.2017
 */
public class Tok2Stemmer {
	
	private Analyzer analyzer; 
	
	// Constructor
	public Tok2Stemmer(Locale language) {
	    this.analyzer = AnalyzerFactory.loadAnalyzer(language);
	}

    /**
     * Runs a standard Lucene preprocessing (tokenisation, stop word removal
     * and stemmming)
     * 
     * @param input
     * @return
     */
	public String processing(String input){
		String stemmedString = "" ;
		
		StringReader reader = new StringReader(input);
		TokenStream ts = analyzer.tokenStream(null, reader);
		CharTermAttribute cattr = ts.addAttribute(CharTermAttribute.class);
		
		try {
			ts.reset();
			while (ts.incrementToken()) {
				//System.out.println("::new " + cattr.toString());
				stemmedString = stemmedString + cattr.toString() + " ";
			}
			ts.end();
			ts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			            

		return stemmedString;
	}

	
	/**
	 * Main function to run the class, serves as example
	 * 
	 * @param args  TOIMPLEMENT
	 * 		-l Language of the input text 
	 *      -i Input text/file
	 */
	public static void main(String[] args) {

		Tok2Stemmer t2s = new Tok2Stemmer(new Locale("en"));
		String frase = t2s.processing("The houses are big and red we want to grow several trees consigned yesterday");
		//t2s.processing("A ver como prerocesa los textos en castellano");
		System.out.println(frase);
	}

}

