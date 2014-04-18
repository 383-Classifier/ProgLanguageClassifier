import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

public class Classifier implements Serializable{
	
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
		double maximumValue = 0;
		
		for (String nbcClass : wordCountsByClass.keySet()) {
			double value = getClassPosterior(nbcClass, bag);
			if (value > maximumValue) {
				maximumValue = value;
				maximumClass = nbcClass;
			}
		}
		
		return maximumClass;
	}
	
	private double getWordLikelihood(String nbcClass, String word) {
		double nci = wordCountsByClass.get(nbcClass).get(word);
		double nc = wordCountsTotal.get(nbcClass);
		double alphaSum = wordCountsByClass.get(nbcClass).size() * alpha;
		
		return (nci + alpha) / (nc + alphaSum);
	}
	
	private double getClassPrior(String nbcClass) {
		int numberOfClasses = wordCountsByClass.size();
		
		return 1.0 / numberOfClasses;
	}
	
	private double getClassPosterior(String nbcClass, HashMap<String, Integer> bag) {
		double sumOfLogLikelihoods = 0;
		
		for (String word : bag.keySet()) {
			double wordLogLikelihood = bag.get(word) * Math.log(getWordLikelihood(nbcClass, word));
			sumOfLogLikelihoods += wordLogLikelihood;
		}
		
		return Math.log(getClassPrior(nbcClass)) + sumOfLogLikelihoods;
	}
}
