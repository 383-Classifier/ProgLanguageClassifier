import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;

public class Classifier implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private HashMap<String, HashMap<String, Integer>> wordCountsByClass;
	private HashMap<String, Integer> wordCountsTotal;
	private double alpha = 1.0;
	
	public Classifier() {
		wordCountsByClass = new HashMap<String, HashMap<String, Integer>>();
		wordCountsTotal = new HashMap<String, Integer>();
	}
	
	public void train(String nbcClass, HashMap<String, Integer> bag) {
		if (!wordCountsByClass.containsKey(nbcClass))
			wordCountsByClass.put(nbcClass, new HashMap<String, Integer>());
		
		if (!wordCountsTotal.containsKey(nbcClass))
			wordCountsTotal.put(nbcClass, 0);
		
		for (String word : bag.keySet()) {
			if (wordCountsByClass.get(nbcClass).containsKey(word)) {
				int oldCount = wordCountsByClass.get(nbcClass).get(word);
				wordCountsByClass.get(nbcClass).put(word, oldCount + bag.get(word));
			} else {
				wordCountsByClass.get(nbcClass).put(word, bag.get(word));
			}		
			
			int oldCount = wordCountsTotal.get(nbcClass);
			wordCountsTotal.put(nbcClass, oldCount + bag.get(word));
		}
	}
	
	public String test(HashMap<String, Integer> bag) {
		String maximumClass = null;
		double maximumValue = Double.NEGATIVE_INFINITY;
		
		for (String nbcClass : wordCountsByClass.keySet()) {
			double value = getClassPosterior(nbcClass, bag);
			if (value > maximumValue) {
				maximumValue = value;
				maximumClass = nbcClass;
			}
		}
		
		return maximumClass;
	}
	
	public String testDetailed(HashMap<String, Integer> bag) {
		String maximumClass = null;
		double maximumValue = Double.NEGATIVE_INFINITY;
		HashMap<String, Double> results = new HashMap<String, Double>() ;
		
		for (String nbcClass : wordCountsByClass.keySet()) {
			double value = getClassPosterior(nbcClass, bag);
			if (value > maximumValue) {
				maximumValue = value;
				maximumClass = nbcClass;
			}
			results.put(nbcClass, value);
		}
		
		return results.toString() + "\nBest fit: " + maximumClass + "\n";
	}
	
	/**
	 * Calculates the word likelihood using the formula in paper:
	 * (nci + alpha ) / (nc + alphaSum)
	 * where: nci = number of times word i appears in the documents in class c
	 * nc =  total number of word occurrences in class c
	 * alpha = the smoothing constant, imagined occurrences 
	 * alphaSum = the constant, over all words in class c (alpha * number of keys in class)
	 * @param nbcClass	The class we are computing likelihood over
	 * @param word		The word we are calculating likelihood of
	 * @return			The Pr( word | class )
	 */
	private double getWordLikelihood(String nbcClass, String word) {	
		double alphaSum = wordCountsByClass.get(nbcClass).size() * alpha;
		double nc = wordCountsTotal.get(nbcClass);
		double nci;
		if (wordCountsByClass.get(nbcClass).containsKey(word))
			nci = wordCountsByClass.get(nbcClass).get(word);
		else
			nci = 0;
		return (nci + alpha) / (nc + alphaSum);
	}
	
	private double getClassPrior(String nbcClass) {
		int numberOfClasses = wordCountsByClass.size();
		
		return 1.0 / numberOfClasses;
	}
	
	private double getClassPosterior(String nbcClass, HashMap<String, Integer> bag) {
		double sumOfLogLikelihoods = 0;
		
		for (String word : bag.keySet()) {
			int wordFrequency = bag.get(word);
			double wordLogLikelihood = wordFrequency * Math.log(getWordLikelihood(nbcClass, word));
			sumOfLogLikelihoods += wordLogLikelihood;
		}

		return Math.log(getClassPrior(nbcClass)) + sumOfLogLikelihoods;
	}
}
