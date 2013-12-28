# data-util - Java data manipulation

Java data manipulation utilities for data. For example: external sorting, splitting, and merging files. Simple
map-reduce like functionality.

Why?
- Make it easier to work with large files in Java
- Make it easier to write integration and functional tests

Some of the goals of data-util are:
- make external sorting simple for large file
- provide Unix/Linux like file management capabilities
- provide map-reduce like functionality

Upcoming
- adding project files to Maven repo
- custom java.util.Comparator builder
- examples
- file shuffler for randomizing data
- data cutter to provide functionalities similar to Unix/Linux cut command

Current status:
- In full working condition
- Have been tested with big files
- Decent level of unit test coverage
- The API is still being worked out (e.g. clean-up and simplification) so expect some changes between major versions

## Usage Example

Build the project using:
```
mvn clean package
```

Add the JAR file from the target directory to your project: datautil-*.jar

Example code for sorting a big file. There are several overrides e.g. split file size and merge factor.

```
// sortDir    - quite a few files will be created here if your input file is huge
// inputFile  - the file you want to sort
// outputFile - the name of your output file
// comparator - java.util.Comparator, you can use Lexical.ascending() to get a simple text sort comparator
// skipHeader - whether or not to skip a leading row in your input file e.g. a header row
SortController.sortFile(sortDir, inputFile, outputFile, comparator, skipHeader);
```

Example code for a simple map reduce.

```
// workDir       - intermediary files are written and read here
// inputFile     - mapper input file
// outputFile    - reducer output file
// mappable      - the mapper class must implement the mappable interface
// keyComparator - com.btaz.datautil.files.mapreduce.KeyComparator which extends Comparable<String>, used to sort by key
// reducable     - the reducer class must implement the reducable interface
MapReduceController.execute(workDir, inputFile, outputFile, mappable, comparator, reducer);
```

Example code for a XML data extractor. This code extracts an XML sub-tree as an XML String on a single row.

```
// inputStream              - an XML file as an input stream
// "/response/result/doc"   - this extracts an XML element from <response><result><doc>
XmlReader reader = new XmlReader(inputStream);
String xmlString;
while((String xmlString = reader.read("/response/result/doc") != null) {
    // process the xmlString here
}
```
