import java.util.Scanner;
import java.util.HashMap;
import java.io.File;

abstract class NBCBagger{

	HashMap<String, Integer> makeBag(File file) throws IOExcetion{
		Scanner sc = null;
		String line = null;
		sc = new Scanner(file);
		HashMap<String,Integer> bag = new HashMap<String,Integer>();
		/*
		*	This doesn't split correctly yet; only on space delims probably.
		*/
		while(sc.hasNext()){
			line = sc.next();
			if(bag.containsKey(line))	bag.put(line,bag.get(line)+1);
			else bag.put(line,1);
		}
		br.close();
		return bag;
	}

}