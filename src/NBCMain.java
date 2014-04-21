import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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
		Classifier classifier = loadClassifier(loadfile);

		/*
		 * Train over documents in docsdirectory for classtype
		 */
		if(args.length>3 && args[0].contains("train")){
			classifier = train(classifier, docsdirectory);
		} 

		/* 
		 * Detailed Test
		 */
		else if(args[0].contains("rtt")){
			ArrayList<File> filesSkipped = new ArrayList<File>();
			ArrayList<String> classesSkipped = new ArrayList<String>();
			double probSkip = 0.5;
			classifier = trainSkipRandom(classifier, docsdirectory, filesSkipped, classesSkipped, probSkip);
			testSkippedFiles(classifier, filesSkipped, classesSkipped);
		}

		/*
		 * Test over document(s) in docsdirectory
		 * 		I was thinking of storing each document's result in a hashmap to print out later
		 * 		but for now I'll leave it to print the file name and its result when it makes a guess
		 */

		else if(args[0].contains("test")){
			test(classifier, docsdirectory);
		}


		/*
		 * Wrong argument format, I guess
		 */
		else{
			System.out.println("usage: [train/test] [documentDir] [loadFile] [saveFile] [ifTrain:class]");
			System.exit(0);
		}

		saveClassifier(classifier, savefile);



	}


	public static Classifier loadClassifier(String loadfile) {
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
		return classifier;
	}

	public static Classifier train(Classifier classifier, File docsdirectory) {
		NBCTrainer trainer = new NBCTrainer();
		trainer.setClassifier(classifier);
		for(File docclassdir : docsdirectory.listFiles()){
			String classtype = docclassdir.getName();
			if (!classtype.matches("\\..*")) {
				System.out.println("Training class " + classtype);
				for(File doc : docclassdir.listFiles()) {
					if (doc.getName().matches(".*\\.txt")) {
						try {
							trainer.trainNBC(doc, classtype);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		classifier = trainer.getClassifier();
		return classifier;
	}

	public static Classifier trainSkipRandom(Classifier classifier, File docsdirectory, ArrayList<File> filesSkipped, ArrayList<String> classesSkipped, double probSkip) {
		NBCTrainer trainer = new NBCTrainer();
		trainer.setClassifier(classifier);
		for(File docclassdir : docsdirectory.listFiles()){
			String classtype = docclassdir.getName();
			if (!classtype.matches("\\..*")) {
				System.out.println("Training class " + classtype);
				for(File doc : docclassdir.listFiles()) {
					if (doc.getName().matches(".*\\.txt")) {
						if (Math.random() < probSkip) {
							filesSkipped.add(doc);
							classesSkipped.add(classtype);
						} else {
							try {
								trainer.trainNBC(doc, classtype);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		classifier = trainer.getClassifier();
		return classifier;
	}


	public static void test(Classifier classifier, File docsdirectory) {
		NBCTester tester = new NBCTester();
		tester.setClassifier(classifier);
		for(File doc : docsdirectory.listFiles()){
			if (doc.getName().matches(".*\\.txt")) {
				try {
					System.out.println(doc.getName() + ": " + tester.testNBC(doc));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void testSkippedFiles(Classifier classifier, ArrayList<File> filesSkipped, ArrayList<String> classesSkipped) {
		NBCTester tester = new NBCTester();
		tester.setClassifier(classifier);
		int filesCorrect = 0;
		int filesTested = filesSkipped.size();
		for (int i=0; i<filesSkipped.size(); i++) {
			File doc = filesSkipped.get(i);
			String classtype = classesSkipped.get(i);
			try {
				if (classtype.equals(tester.testNBC(doc)))
					filesCorrect++;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		System.out.println(filesCorrect + "/" + filesTested + " correct");
	}


	public static void testDetailed(Classifier classifier, File docsdirectory) {
		NBCTester tester = new NBCTester();
		tester.setClassifier(classifier);
		for(File doc : docsdirectory.listFiles()){
			if (doc.getName().matches(".*\\.txt")) {
				try {
					System.out.println(doc.getName() + ":\n" + tester.testDetailedNBC(doc));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveClassifier(Classifier classifier, String savefile) {
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