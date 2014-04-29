                                                                     
                                                                     
                                                                     
                                             
COMPILING:
Either run 'make' in the src directory,
or 'javac NBCMain.java' in the src directory

RUNNING:
The program takes four arguments: [train|test|one-vs-all-but-one|random-skip-loop|likelihood-details] [documentDir] [loadFile] [saveFile]

* Not all arguments may be used in any given operation, but they must all be specified. Simply using a string will suffice (e.g. “none”).

* The first argument is the type of operation to be done. Each operation is explained further below.

* The second argument is the directory training data is to be read from. The format should look like:

documentDir
 |-> Class1
      |-> file1.txt
      |-> file2.txt
 |-> Class2
 |-> Class3
 |-> etc.

Where each subfolder is named for the class it contains, and contains training files in *.txt format.

For our training data, use “ExampleFiles”

If testing, this should simply be a one-level directory of *.txt files. For example “ExampleFiles/Python”.

* The third argument is the location of a serialized classifier to be loaded.  Again, use a non-file string to initialize a new classifier (“none”).

* The fourth and last argument is the location of where the trained classifier should be serialized to. (e.g. “classifier.ser”).

When running the program from command line, it seems like it needs to be run while the user is sitting in the directory containing the binary files. I was having trouble running "elnux3> java ./src/NBCMain"
-- Error: Could not find or load main class ..src.NBCMain
so I had to navigate into ./src/ and running from there is fine.


*** Either run 
*** 'make runProg'
	
	* This will run with the following defaults:
		- it will run 'one-vs-all-but-one'
		- save/load to src's parent directory as: ProgLanguageClassifier/serial.files
		- document dir is ../ExampleFiles

	* Default document dir is our "ExampleFiles" directory, which should be kept as the default for all running methods, EXCEPT for test. For test, use a class directory within ExampleFiles, such as 'ExampleFiles/JavaScript' . The other running methods train and test on all class directories anyhow, so you might not even end up using test

	* To change arguments, run:
		make runProg method="random-skip-loop" load="../serialfile.file" save="../serialfile.file" directory="../ExampleFiles" 

*** Or run
*** 'java NBCMain train ../ExampleFiles ../serialfile.file ../serialfile.file'
	with the arg choices listed above


OPERATIONS:
* train: train a new or existing classifier over a two-level directory, each sub-directory named for it’s class and containing training documents. Classifier is serialized to disk.

* test: test an existing classifier (unserialized from disk) over a one-level directory containing test documents, and displays results.

* one-vs-all-but-one: for each document d, trains a new classifier over each document excluding d, and tests on d, displays results.

* random-skip-loop: for probability 5%,10%,...,95%, trains on random portion of the documents using the probability, and tests on the remainining documents. Performs this 100 times per probability. Each probability is saved to a csv file, the mean and variance for each probability is displayed after running all 1900 tests.

* likelihood-details: for each class in documentDir, trains the classifier, and prints the top 100 features for that class, according to their relative likelihood and in descending order.