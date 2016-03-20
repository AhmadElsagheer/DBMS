#NOTE: You need to comment all package declarations from the java files for a successful comiplation
#Edit test cases in the DBAppTest file

all: compile-files run-DBAppTest

compile-files:
	javac src/sagSolheeriman/*.java src/BPTree/*.java -d bin/

run-DBAppTest:
	java -classpath bin/ sagSolheeriman.DBAppTest

clean:
	rm -r bin/
	rm -r databases

