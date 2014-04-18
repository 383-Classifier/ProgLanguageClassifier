import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

public class NBCTrainer extends NBCBagger{

	public void trainNBC(File file, Classifier cla, String lclass) throws FileNotFoundException{
		Classifier classifier = cla;
		HashMap<String,Integer> bag = makeBag(file);
		classifier.train(lclass, bag);
	}
	
}