import java.util.ArrayList;
import java.util.HashMap;

public class Classifier {
	private HashMap<String, ArrayList<HashMap<String, Integer>>> wordHash;
	
	public Classifier() {}
	
	public void train(String nbcClass, HashMap<String, Integer> bag) {}
	
	public String test(HashMap<String, Integer> bag) {return "";}
	
	private double getPofClass(String nbcClass) {return 1;}
	
	private double getPofFeature(String feature) {return 1;}
	
	private double getPofFeatureGivenClass(String nbcClass, String feature) {return 1;}
}
