import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class NBCTester extends NBCBagger{

	String testNBC(File file, Classifier cla) throws FileNotFoundException{
		Classifier classifier = cla;
		HashMap<String, Integer> bag = null;
		try {
			bag = makeBag(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classifier.test(bag);
	}
	
}