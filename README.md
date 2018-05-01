# thefloow

Read a really huge file and parse it to count words used.

info.tecnocchio.theFloow.MainClass

usage
java -jar challenge-jar-with-dependencies.jar -source filename -mongo host:port -output numOfWordToShow

source and mongo are mandatory, params order don't matter

notes
  
The Chunk of multiple lines idea:
This example is based on the wikimedia dump source, so looking the source i could do a reasonable simple assumption that the file is parsable by line. This  assumption reduced the complexity in multiprocessing , because i could split the source by a reasonable big number of line without having to do with words split by my division. It would not be a problem, but i like to follow easy way when possible, leaving complexity where required. So this solution split the source file in chunks of many lines for multiple work.

Analyzing the file before process:
at moment i don't count before, the number of chunk in the file, but i could, the choice is on how many process are ran each time. For a large number of process would be a good approach, for few process is not so useful
I don't do an hash of the file, i assume that if multiple process are run, the filename (without path) is what link each processes, it would be time expensive.

Behaviour:
Running a finished process don't restart all, produces only output and statistics from mongo if "-output num" in arguments 
