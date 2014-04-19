import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
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

	public HashMap<String, Integer> makeBag(File file) throws IOException{
		FileReader reader = null;
		HashMap<String,Integer> bag = new HashMap<String,Integer>();
	
		try {
			reader = new FileReader(file);
			int c, lastc;
			lastc = 0;
			String token = "";
			while ((c = reader.read()) != -1) {
				if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
					if (('a' <= lastc && lastc <= 'z') || ('A' <= lastc && lastc <= 'Z')) {
						token += String.valueOf((char) c);
					} else {
						if(bag.containsKey(token)) {
							bag.put(token, bag.get(token)+1);
						} else {
							bag.put(token, 1);
						}
						token = "";
						token += String.valueOf((char) c);
					}
				} else {
					if(bag.containsKey(token)) {
						bag.put(token, bag.get(token)+1);
					} else {
						bag.put(token, 1);
					}
					token = "";
					token += String.valueOf((char) c);
				}
				lastc = c;
			}
			if (token != "") {
				if(bag.containsKey(token)) {
					bag.put(token, bag.get(token)+1);
				} else {
					bag.put(token, 1);
				}	
			}
			
		} finally {
			if (reader != null) {
                reader.close();
            }
		}
		
		return bag;
	}

}