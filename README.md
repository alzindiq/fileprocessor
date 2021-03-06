# Simple File Stat Gen

A very simple set of classes to read a file (one line at a time) and aggregate statistics about its contents.

## Prerequisites

1. Maven 3.
2. Java 8.

## Installation

As any Mavenised project you can simply install it by:

`mvn clean install`

Note this will not automatically pull all the dependencies for you.  

If you want a single, self contained jar including all the dependencies, then run:

`mvn clean compile assembly:single`

NOTE: javac provides the ``-encoding <encoding>` option for source files that contain unicode files, as this project does in some of its tests.
The pom.xml file we provided already gives the right encoding (UTF-8) to cope with this, but make sure you add the `-encoding UTF-8` option if
 you decide to compile outside of maven or with a different pom.xml. This is used for test purposes only, as for when the code reals from a file,
 it asks for the right Charset and will only resort to the OS default encoding if this is not provided.

## Usage

You can import the provided jar file into your project dependencies and start using the 
FileProcessor to get stats from your files. 

The test classes under the ./test folder show simple examples on how to use it programmatically. 

If you want to use the command line, from the root folder of the project, execute:

`java -cp ./target/FileProcessor-0.1-SNAPSHOT-jar-with-dependencies.jar org.fileprocessor.FileProcessor <fileName>`

## Extensibility

If not LineProcessor is given to the FileProcessor, it will use the default one (provided). 

It is very simple to create a new LineProcessor (see DoNothingLineProcessor under ./test dir).

You can provide the FileProcessor with a List of LineProcessors and it will invoke each of them after reading each line.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## JavaDocs

[Generated documentation](docs/index.html)

