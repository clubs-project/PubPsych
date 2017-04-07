package eu.pubpsych.query;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// TODO add the other languages
public class Tok2StemmerTest {
	
	private Tok2Stemmer t2s;
	private final String text = "Such an analysis can reveal features that are not easily "
								+ "visible from the variations in the individual genes and " 
                                + "can lead to a picture of expression that is more "
                                + "biologically transparent and accessible to interpretation";
                                
	private final String expectedText = "analysi can reveal featur easili visibl from "
								+ "variat individu gene can lead pictur express more biolog "
								+ "transpar access interpret";

	@Before
	public void setUp() throws Exception {
		t2s = new Tok2Stemmer(new Locale("en"));
	}
	
	@After
	public void tearDown() throws Exception {		
	}

	@Test
	public final void testProcessing() {
		Assert.assertEquals(expectedText, t2s.processing(text));
	}

}
