import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class NBCTester extends NBCBagger{

	String testNBC(File file) throws FileNotFoundException{
		String predictedclass = null;
		HashMap<String,Integer> bag = makeBag(file);
		
		return predictedclass;
	}
	
}