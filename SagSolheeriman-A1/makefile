#NOTE: You need to comment all package declarations from the java files for a successful comiplation
#Edit test cases in the DBAppTest file

all: compile-files run-DBAppTest

compile-files:
	javac src/sagSolheeriman/*.java -d bin/sagSolheeriman/

run-DBAppTest:
	java -classpath bin/sagSolheeriman/ DBAppTest

clean:
	rm -f bin/sagSolheeriman/*.class
	rm -r databases

