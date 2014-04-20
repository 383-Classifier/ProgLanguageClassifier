import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NBCMain{
	
	public static void main(String[] args) throws FileNotFoundException{
		/*
		 * Wrong argument format. We'll decide on the program's arguments and then we can set up the usage warning
		 */
		if(args.length<4){
			System.out.println("usage: [train/test] [documentDir] [loadFile] [saveFile]");
			System.exit(0);
		}
		
		/*
		 * init
		 */
		File docsdirectory = new File(args[1]);			//directory of class directories each containing files to test/train, 
		String loadfile = args[2];						//existing serialized
		String savefile = args[3];						//where to save new serialized 
		String classtype = null;
		Classifier classifier = null;
		
		try{
			FileInputStream filein = new FileInputStream(loadfile);
			ObjectInputStream objin = new ObjectInputStream(filein);
			classifier = (Classifier) objin.readObject();
			filein.close(); objin.close();
			System.out.println("Classifier read in from " + loadfile);
		}catch(FileNotFoundException e){
			System.out.println("New classifier created");
			classifier = new Classifier();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		/*
		 * Train over documents in docsdirectory for classtype
		 */
		if(args.length>3 && args[0].contains("train")){
			NBCTrainer trainer = new NBCTrainer();
			trainer.setClassifier(classifier);
			for(File docclassdir : docsdirectory.listFiles()){
				classtype = docclassdir.getName();
				System.out.println("Training class " + classtype);
				for(File doc : docclassdir.listFiles()) {
					trainer.trainNBC(doc, classtype);
				}
			}
			classifier = trainer.getClassifier();
			
			NBCTester tester = new NBCTester();
			tester.setClassifier(classifier);
			File testdirectory = new File("TestFiles");
			if (testdirectory.exists()) {
				for(File doc : testdirectory.listFiles()){
					System.out.println(doc.getName() + ": " + tester.testNBC(doc));
				}
			}
		} 
		
		/* 
		 * Detailed Test
		 */
		else if(args[0].contains("test-detailed")){
			NBCTester tester = new NBCTester();
			tester.setClassifier(classifier);
			for(File doc : docsdirectory.listFiles()){
				System.out.println(doc.getName() + ":\n" + tester.testDetailedNBC(doc));
			}
		}
		
		/*
		 * Test over document(s) in docsdirectory
		 * 		I was thinking of storing each document's result in a hashmap to print out later
		 * 		but for now I'll leave it to print the file name and its result when it makes a guess
		 */
		
		else if(args[0].contains("test")){
			NBCTester tester = new NBCTester();
			tester.setClassifier(classifier);
			for(File doc : docsdirectory.listFiles()){
				System.out.println(doc.getName() + ": " + tester.testNBC(doc));
			}
		}
		
		
		/*
		 * Wrong argument format, I guess
		 */
		else{
			System.out.println("usage: [train/test] [documentDir] [loadFile] [saveFile] [ifTrain:class]");
			System.exit(0);
		}
		
		try{
			FileOutputStream fileout = new FileOutputStream(savefile);
			ObjectOutputStream objout = new ObjectOutputStream(fileout);
			objout.writeObject(classifier);
			objout.close(); fileout.close();
			System.out.println("Classifer saved at " + savefile);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		


	}
	
	
}