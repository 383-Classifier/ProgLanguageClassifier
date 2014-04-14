import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ClassifierTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTrain() {
		Classifier nbc = new Classifier();
		HashMap<String, Integer> bag1 = new HashMap<String, Integer>();
		bag1.put("a",1);
		bag1.put("b",2);
		bag1.put("c",3);
		String nbcClass = "alph";
		nbc.train(nbcClass, bag1);
		
		HashMap<String, HashMap<String, Integer>> wordCountsByClass = nbc.getWordCountsByClass();
		HashMap<String, Integer> wordCountsTotal = nbc.getWordCountsTotal();
		
		assertSame("a",1, wordCountsByClass.get(nbcClass).get("a"));
		assertSame("b",2, wordCountsByClass.get(nbcClass).get("b"));
		assertSame("c",3, wordCountsByClass.get(nbcClass).get("c"));
		assertSame("total", 6, wordCountsTotal.get(nbcClass));
		
		HashMap<String, Integer> bag2 = new HashMap<String, Integer>();
		bag2.put("b",1);
		bag2.put("c",2);
		bag2.put("d",3);
		nbc.train(nbcClass, bag2);
		
		wordCountsByClass = nbc.getWordCountsByClass();
		wordCountsTotal = nbc.getWordCountsTotal();
		
		assertSame("a",1, wordCountsByClass.get(nbcClass).get("a"));
		assertSame("b",3, wordCountsByClass.get(nbcClass).get("b"));
		assertSame("c",5, wordCountsByClass.get(nbcClass).get("c"));
		assertSame("d",3, wordCountsByClass.get(nbcClass).get("d"));
		assertSame("total", 12, wordCountsTotal.get(nbcClass));
		
		HashMap<String, Integer> bag3 = new HashMap<String, Integer>();
		bag3.put("a",1);
		bag3.put("b",2);
		bag3.put("c",3);
		String nbcClass2 = "alph2";
		nbc.train(nbcClass2, bag3);
		
		wordCountsByClass = nbc.getWordCountsByClass();
		wordCountsTotal = nbc.getWordCountsTotal();
		
		assertSame("a",1, wordCountsByClass.get(nbcClass).get("a"));
		assertSame("b",3, wordCountsByClass.get(nbcClass).get("b"));
		assertSame("c",5, wordCountsByClass.get(nbcClass).get("c"));
		assertSame("d",3, wordCountsByClass.get(nbcClass).get("d"));
		assertSame("total", 12, wordCountsTotal.get(nbcClass));
		assertSame("a",1, wordCountsByClass.get(nbcClass2).get("a"));
		assertSame("b",2, wordCountsByClass.get(nbcClass2).get("b"));
		assertSame("c",3, wordCountsByClass.get(nbcClass2).get("c"));
		assertSame("total", 6, wordCountsTotal.get(nbcClass2));
		
	}

	@Test
	public void testTest() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWordLikelihood() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClassPrior() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClassPosterior() {
		fail("Not yet implemented");
	}

}
