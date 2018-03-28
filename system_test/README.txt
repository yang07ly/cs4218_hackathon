AutoTester Alpha Instructions
- AutoTester Alpha can be run by double clicking on the jar file and a UI will appear.
- AutoTester Alpha is used to automatically run test cases, compare the results and display the result in an output file. It would run the specified jar file on the specified test system folder, input the test cases and compare with the results in the query files specified and output the result in a html file.
- Make sure Windows can find the Java compiler and interpreter by setting the System variable path on your system and be able to be run java on cmd.

Jar File: the program that AutoTester will run.
Test System: the folder that contain the test system files and folders to be tested on.
Query Files: file with commands that will be inputted to the program and the corresponding expected outputs that will be used to compare with the actual output.

Query File Format:
//Comment1
command1
expected output 1 line 1
expected output 1 line 2
expected output 1 line ...
//Comment2
command2
expected output 2 line 1
expected output 2 line 2
expected output 1 line ...

Auto-loading:
- the first jar file with name that does not contain "AutoTester" will be loaded.
- the first folder with a name that contain "system" will be loaded.
- text files that starts with the word "query" will be loaded as query files.

Assumption:
- cd an absolute path have to be working. Autotester use this to change the current directory of the program.
- program output cannot contain the right arrow ">". AutoTester uses that to know when the program has finish outputting the sctual output.
- program will always print "current/directory>" on a new line without part of the actual output on the same line. AutoTester will remove the last line of the output after every query.
