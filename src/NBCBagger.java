import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

public class NBCBagger{

	private Classifier nbc;
	
	public NBCBagger() {
		setClassifier(new Classifier());
	}
	
	public Classifier getClassifier() {
		return nbc;
	}

	public void setClassifier(Classifier nbc) {
		this.nbc = nbc;
	}

	// Doesn't split properly yet
	public HashMap<String, Integer> makeBag(File file) throws IOException{
		Scanner sc = null;
		String line = null;
		sc = new Scanner(file);
		HashMap<String,Integer> bag = new HashMap<String,Integer>();
		/*
		*	This doesn't split correctly yet; only on space delims probably.
		*/
		while(sc.hasNext()){
			line = sc.next();
			if(bag.containsKey(line))	
				bag.put(line, bag.get(line)+1);
			else 
				bag.put(line, 1);
		}
		sc.close();
		return bag;
	}

}