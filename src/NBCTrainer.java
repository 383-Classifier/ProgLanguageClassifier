import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NBCTrainer extends NBCBagger{

	public void trainNBC(File file, String lclass) throws FileNotFoundException{
		HashMap<String, Integer> bag = null;
		try {
			bag = makeBag(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		nbc.train(lclass, bag);
	}
	
}