import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

public class NBCTrainer extends NBCBagger{

	public void trainNBC(File file, String lclass) throws FileNotFoundException{
		HashMap<String,Integer> bag = makeBag(file);

	}
	
}