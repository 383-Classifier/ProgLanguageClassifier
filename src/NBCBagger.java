import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NBCBagger{

	protected Classifier nbc;
	
	public NBCBagger() {
		setClassifier(new Classifier());
	}
	
	public Classifier getClassifier() {
		return nbc;
	}

	public void setClassifier(Classifier nbc) {
		this.nbc = nbc;
	}
	
	private enum State {
	    EMPTY, ALPH, SYMBOL
	}
	
	private void put(HashMap<String,Integer> bag, String token) {
		int count;
		if (bag.containsKey(token))
			count = bag.get(token);
		else
			count = 0;
		bag.put(token, count + 1);
	}

	public HashMap<String, Integer> makeBag(File file) throws IOException{
		FileReader reader = null;
		HashMap<String,Integer> bag = new HashMap<String,Integer>();
	
		try {
			reader = new FileReader(file);
			int c;
			String token = "";
			State state = State.EMPTY;
			while ((c = reader.read()) != -1) {
				switch(state) {
				case EMPTY:
					token = Character.toString((char)c);
					if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
						state = State.ALPH;
					} else {
						state = State.SYMBOL;
						put(bag,token);
						token = "";
						state = State.EMPTY;
					}	
					break;
				case ALPH:
					if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
						token += (char)c;
					} else {
						put(bag,token);
						token = Character.toString((char)c);
						state = State.SYMBOL;
						put(bag,token);
						token = "";
						state = State.EMPTY;
					}
					break;
				case SYMBOL:
					//We should never be in this state at the beginning of loop
					break;
				}
			}
			if (state != State.EMPTY) {
				put(bag,token);
				state = State.EMPTY;
			}
		} finally {
			if (reader != null) {
                reader.close();
            }
		}
		
		return bag;
	}
}