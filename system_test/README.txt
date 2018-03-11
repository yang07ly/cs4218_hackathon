AutoTester Alpha Instructions

Jar File: the program that AutoTester will run.
Test System: the folder that contain the test system files and folders to be tested on.
Query Files: file with commands that will be inputted to the program and the corresponding expected outputs that will be used to compare with the actual output.

Query File Format:
//Comment1
command1
expected output 1
//Comment2
command2
exepcted output 2

Auto-loading:
- the first jar file with name that does not contain "AutoTester" will be loaded.
- the first folder with a name that contain "system" will be loaded.
- text files that starts with the word "query" will be loaded as query files.

Assumption:
- cd an absolute path have to be working. Autotester use this to change the current directory of the program.
- program output cannot contain the right arrow ">". AutoTester uses that to know when the program has finish outputting the sctual output.
