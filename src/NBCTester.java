import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class NBCTester extends NBCBagger{

	String testNBC(File file, Classifier cla) throws FileNotFoundException{
		Classifier classifier = cla;
		HashMap<String,Integer> bag = makeBag(file);
		return classifier.test(bag);
	}
	
}