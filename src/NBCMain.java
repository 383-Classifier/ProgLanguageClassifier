import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.DirectoryStream;

public class NBCMain{
	
	public static void main(String[] args) throws FileNotFoundException{
		if(args.length<4){
			System.out.println("usage: [train/test] [documentDir] [loadFile] [saveFile] [ifTrain:class]");
			System.exit(0);
		}
		File docsdirectory = new File(args[1]);			//directory of files to test/train
		String loadFile = args[2];						//existing serialized
		String saveFile = args[3];						//where to save new serialized 
		String langclass = null;
		NBCTrainer trainer = null;
		
		/*
		 * Train [languagetype:langclass] over docs in [docsdirectory] 
		 */
		if(args.length>4 && args[0].contains("train")){
			langclass = args[4];
			Classifier classifier = new Classifier();	
			for(File doc : docsdirectory.listFiles()){
				trainer.trainNBC(doc, langclass);
			}
		} 
		
		else if(args[0].contains("test")){

		}
		else{
			System.out.println("usage: [train/test] [documentDir] [loadFile] [saveFile] [ifTrain:class]");
			System.exit(0);
		}
		


	}
}