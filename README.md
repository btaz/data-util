# data-util - Java data manipulation

Java data manipulation utility for data structures and files. data-util provides functionality like:
- writing unit/integration tests that uses files
- external file sort
- splitting big files into many smaller ones
- merging of pre-sorted files
- simple map-reduce (intentionally non-distributed)
- reading and validating big XML files

Why?
- Make it easier to work with large files
- Make it easier to work with XML and JSON
- Make it easier to write integration and functional tests

Some of the goals of data-util are:
- make external sorting simple for large file
- provide Unix/Linux like file management capabilities
- provide map-reduce like functionality
- provide tools to validate big XML files
- provide tools to validate JSON  files
- provide tools for API access (REST/SOAP)

Upcoming
- adding project files to Maven repo
- custom java.util.Comparator builder
- more examples
- file shuffler for randomizing data
- data cutter to provide functionalities similar to Unix/Linux cut command
- REST API client
- SOAP API client

Current status:
- In full working condition
- Have been tested with big files
- Decent level of unit test coverage
- The API is still being worked out (e.g. clean-up and simplification) so expect future changes

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
// mapper        - the mapper class must implement the mappable interface
// keyComparator - com.btaz.datautil.files.mapreduce.KeyComparator which extends Comparable<String>, used to sort by key
// reducer       - the reducer class must implement the reducable interface
MapReduceController.execute(workDir, inputFile, outputFile, mapper, comparator, reducer);
```

Example code for a XML data extractor, XML Document and XML path queries (similar to XPath). This code extracts an XML
sub-tree as an XML document that provides methods for data processing, simple validations and other testing.

```
// inputStream              - an XML file as an input stream
// "/response/result/doc"   - this extracts an XML element from <response><result><doc>
XmlReader reader = new XmlReader(inputStream);
Document doc;
while((doc = reader.read("/response/result/doc") != null) {
    // process the XML document here
    List<Node> nodes = doc.pathQuery("/response/result/doc");
    Element elem = (Element) nodes.get(0);
    if("country".equals(elem.attributeValue("str")) {
        ...
    }
}
```

Example code for a XML Documents. These documents are useful to create XML documents. For data processing as well as
testing. Example code:

```
Document doc = new Document()
    .addElement("<fruits>")
    .addElement("<banana />")
    .addElement("<orange />");

System.out.println(doc.toString());

/*
  <fruits>
    <banana />
    <orange />
  </fruits>
/*
```

XML diffing. First we create two XML documents, then we use the DifferenceReporter to compare the documents. The
compare method supports different Arbitrators to give you full control over the comparison process. There's also
different Report implementations that either use memory or files to store the difference information. To use the
FileReport implementation is useful if you perform a diff operation on large XML documents.

Example code:
```
/*
    <fruits>
      <orange />
      <lemon />
      <pear />
    </fruits>
*/
Document a = new Document()
	.addElement("<fruits>")
	    .addElement("<orange/>")
	    .addElement("<lemon />")
	    .addElement("<pear />");

/*
    <fruits>
      <orange />
      <banana />
      <lemon />
    </fruits>
*/
Document b = new Document()
	.addElement("<fruits>")
	    .addElement("<orange/>")
	    .addElement("<banana/>")
	    .addElement("<lemon />");

Report report = differenceReporter.compare(a, b);
```
