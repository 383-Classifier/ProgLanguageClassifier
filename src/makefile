#makefile
#
#For use in CMPSCI 383 (Artificial Intelligence)
#
#Written by: Jon Saj, Jeremy Hummel, and Tim Simon
#Use for Source Code Classifier
#
		
compileProg:    
		echo "Compiling"
		javac NBCMain.java 
#This will run your code via command line with standard input and output handled
#(You will use the variables specified below for stdin and stdout)
method =  one-vs-all-but-one
directory = ../ExampleFiles/
load = ../serial.file
save = ../serial.file
#List files needed for running at the end of the next line
runProg:        
#List all command line entries (in order) that will run the code
#Remember to press tab twice at the start of each command
		echo "Running"
		java NBCMain $(method) $(directory) $(load) $(save)
#This will remove the compiled files to allow a fresh compile (sometimes make thinks everything is up-to-date when it's not)
clean:  
#List each rm statement to remove executable and object files; be careful
#Remember to press tab twice at the start of each command