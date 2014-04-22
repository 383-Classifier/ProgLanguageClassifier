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

		else if(args[0].contains("rtt-loop")){
			ArrayList<File> filesSkipped = new ArrayList<File>();
			ArrayList<String> classesSkipped = new ArrayList<String>();
			ArrayList<Double> correct = new ArrayList<Double>();
			double probSkip = 0.25;
			for (int i=1; i<=100; i++) {
				filesSkipped = new ArrayList<File>();
				classesSkipped = new ArrayList<String>();
				classifier = trainSkipRandom(new Classifier(), docsdirectory, filesSkipped, classesSkipped, probSkip);
				correct.add(testSkippedFiles(classifier, filesSkipped, classesSkipped));

				double sum = 0;
				double sumOfSqErr = 0;
				int count = 0;
				for (double v : correct) {
					if (!Double.isNaN(v)) {
						sum += v;
						count++;
					}
				}
				double mean = sum / count;
				for (double v : correct) {
					if (!Double.isNaN(v)) {
						sumOfSqErr += (v - mean) * (v - mean);
					}
				}
				double variance = sumOfSqErr / count;
				System.out.println("Mean: " + mean + "\tVariance: " + variance);

			}
		}

		else if(args[0].contains("rtt")){
			ArrayList<File> filesSkipped = new ArrayList<File>();
			ArrayList<String> classesSkipped = new ArrayList<String>();
			double probSkip = 0.5;
			classifier = trainSkipRandom(classifier, docsdirectory, filesSkipped, classesSkipped, probSkip);
			testSkippedFiles(classifier, filesSkipped, classesSkipped);
		}

		else if(args[0].contains("tebt")) {
			ArrayList<File> files = new ArrayList<File>();
			ArrayList<String> classes = new ArrayList<String>();
			ArrayList<String> results = new ArrayList<String>();
			getAllFiles(docsdirectory, files, classes);
			int correct = 0;
			for (int i=0; i<files.size(); i++) {
				File file = files.get(i);
				String classtype = classes.get(i);
				classifier = trainSkipFile(new Classifier(), files, classes, file);
				String result = testFile(classifier, file);
				if (result.equals(classtype))
					correct++;
				System.out.println(file.getName() + " \tClass: " + classtype + " \tResult: " + result);
			}
			System.out.println(correct + "/" + files.size() + ", " + ((double)correct)/files.size() + " correct");
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
				//System.out.println("Training class " + classtype);
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

	public static void getAllFiles(File docsdirectory, ArrayList<File> files, ArrayList<String> classes) {
		for(File docclassdir : docsdirectory.listFiles()){
			String classtype = docclassdir.getName();
			if (!classtype.matches("\\..*")) {
				//System.out.println("Training class " + classtype);
				for(File doc : docclassdir.listFiles()) {
					if (doc.getName().matches(".*\\.txt")) {
						files.add(doc);
						classes.add(classtype);
					}
				}
			}
		}
	}

	public static Classifier trainSkipFile(Classifier classifier, ArrayList<File> files, ArrayList<String> classes, File skip) {
		NBCTrainer trainer = new NBCTrainer();
		trainer.setClassifier(classifier);
		for (int i=0; i<files.size(); i++) {
			File doc = files.get(i);
			String classtype = classes.get(i);
			if (!doc.equals(skip)) {
				try {
					trainer.trainNBC(doc, classtype);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		classifier = trainer.getClassifier();
		return classifier;
	}

	public static String testFile(Classifier classifier, File file) {
		NBCTester tester = new NBCTester();
		tester.setClassifier(classifier);
		String classtype = null;
		try {
			classtype = tester.testNBC(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return classtype;
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

	public static double testSkippedFiles(Classifier classifier, ArrayList<File> filesSkipped, ArrayList<String> classesSkipped) {
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
		return ((double)filesCorrect)/filesTested;
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