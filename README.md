Distributed Graph Processing
===============

Algorithms to analyse and process graphs in a distributed way using Apache Hama or Apache Giraph.
		
Requirements
=========

* Git
* Java 7
* Maven 3

Installing necessary libs
=========================

### To install both libs Apache Hama Graph(PATCHED) and Apache Giraph 1.1.0:

sh lib/install_libs.sh

### If you only want to use one of the libs:

#### Apache Giraph 1.1.0

sh lib/install_giraph.sh

or,

git clone https://github.com/apache/giraph.git -b release-1.1

cd giraph

mvn install -DskipTests

#### Apache Hama Graph - Patched

sh lib/install_hama_patched.sh

or,

mvn install:install-file -Dfile=libs/hama-graph-0.6.4-PATCHED.jar -DgroupId=org.apache.hama -DartifactId=hama-graph -Dversion=0.6.4-PATCHED -Dpackaging=jar

Building
========

After the necessary libs were installed just run:

mvn package

Algorithm Example(Runner)
=========================

java -jar \<PATH>/algorithm-example.jar alg plat -i \<input file> -o \<output path> -w \<number of workers>

* **plat:** Defines if it will run in Giraph or Hama. Possible values: hama, giraph.
* **alg:** The algorithm that will run.
* **input file:** DFS path to the input file.
* **output path:** DFS path to the output file.
* **number of workers:** Number of workers in Hama and Number of the
* **l flag:** To run the algorithm locally, without needing to configure Hama or Giraph.

java -jar \<PATH>/algorithm-example.jar -h

* **h flag:** Prints possible options.

Input
=====

The input being used has the following format:

verticeId verticeValue targetVerticeId edgeValue target2VerticeId edge2Value ....
vertice2Id verticeValue targetVerticeId edgeValue target2VerticeId edge2Value ....
...
