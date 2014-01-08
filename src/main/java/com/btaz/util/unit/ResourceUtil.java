package com.btaz.util.unit;

import com.btaz.util.DataUtilDefaults;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

/**
 * User: msundell
 */
public class ResourceUtil {
    /**
     * This method will find test resources as a file, test/resources are mapped to target/test-classes e.g. file1.csv
     * @param resourceName resource name
     * @return <code>File</code> resource file
     */
    public static File getTestResourceFile(String resourceName) throws URISyntaxException, ClassNotFoundException {
        URL url = ResourceUtil.class.getClassLoader().getResource(resourceName);
        assert url != null : resourceName;
        return new File(url.toURI());
    }

    /**
     * This method reads all data from a file into a String object
     * @param file file to readLine data from
     * @return <code>String</code> containing all file data
     * @throws IOException IO exception
     */
    public static String readFromFileIntoString(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder text = new StringBuilder();

        String line;
        while( (line = reader.readLine()) != null) {
            text.append(line).append("\n");
        }
        inputStream.close();

        return text.toString();
    }

    /**
     * This method reads all data from a test resource into a String object
     * @param filename file name
     * @return <code>String</code> test resource data
     */
    public static String readTestResourceIntoString(String filename) throws URISyntaxException, ClassNotFoundException,
            IOException {
        return readFromFileIntoString(getTestResourceFile(filename));
    }

    /**
     * This method writes all data from a string to a temp file. The file will be automatically deleted on JVM shutdown.
     * @param prefix file prefix i.e. "abc" in abc.txt
     * @param suffix file suffix i.e. ".txt" in abc.txt
     * @param data string containing the data to write to the file
     * @return <code>File</code> object
     * @throws IOException IO exception
     */
    public static File writeStringToTempFile(String prefix, String suffix, String data) throws IOException {
        File testFile = File.createTempFile(prefix, suffix);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                testFile.getAbsoluteFile(), false),DataUtilDefaults.charSet));
        writer.write(data);
        writer.flush();
        writer.close();
        return testFile;
    }

    /**
     * This method creates test data containing letters between a-z
     * @param rows how many rows of data to create
     * @param rowLength how many characters per row
     * @param skipTrailingNewline if true then a trailing newline is always added
     * @return <code>String</code> contain all random data
     */
    public static String createRandomData(int rows, int rowLength, boolean skipTrailingNewline) {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder strb = new StringBuilder();
        for(int i=0; i<rows; i++) {
            for(int j=0; j<rowLength; j++) {
                strb.append((char)(((int)'a') + random.nextFloat() * 25));
            }
            if(skipTrailingNewline || i<rows) {
                strb.append("\n");
            }
        }
        return strb.toString();
    }

    /**
     * This method will create a new directory in the system temp folder
     * @param directoryName directory name
     */
    public static File createTmpDir(String directoryName) {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File testDir = new File(tmpDir, directoryName);
        if(!testDir.mkdir()) {
            throw new ResourceUtilException("Can't create directory: " + testDir.getAbsolutePath());
        }
        testDir.deleteOnExit();
        return testDir;
    }
}
