README.TXT
----------
Before running the following command please download stanford-core-nlp-3.6.0-models 
from following site: http://nlp.stanford.edu/software/stanford-english-corenlp-2016-01-10-models.jar


you can cd to the dist/lib, run curl -O http://nlp.stanford.edu/software/
stanford-english-corenlp-2016-01-10-models.jar to download. It is a big file it may take some time.

To run please issue the following command:
——————————————————————————————————————————
1) java -classpath "dist/QueryingJena.jar:dist/lib/*:" queryingjena.QueryingJena 
<modelfile> <question number>

example: java -classpath "dist/QueryingJena.jar:dist/lib/*:" queryingjena.QueryingJena 
cmput690_a3.ttl 3


2) if you need to build:

ant -f build.xml under main project folder.