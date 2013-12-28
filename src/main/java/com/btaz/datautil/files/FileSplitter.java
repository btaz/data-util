package com.btaz.datautil.files;

import com.btaz.datautil.DataUtilDefaults;
import com.btaz.datautil.DataUtilException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: msundell
 */
public class FileSplitter {
    private static final long DEFAULT_MAX_BYTES = 10 * 1024 * 1024; // 10 MB RAM, roughly 4.7 MB per file on disk
    private static final long DEFAULT_MIN_BYTES = 10 * 1024;        // 10 KB RAM, roughly 4.7 KB per file on disk
    private static final double LIST_CAPACITY_MULTIPLIER = 1.5D;    // compensate for how ArrayLists allocates capacity
    private static final int ARRAY_LIST_ROW_OVERHEAD = 5;           // compensate for ArrayLists item memory overhead

    /**
     * This method reads an input file and creates file splits if the file size in memory exceeds a certain size.
     * If the split directory is not specified the system specific temp directory will be used. This means that the
     * files will be deleted automatically for you when the JVM terminates. It is however a good idea to delete files
     * when no longer needed.
     * @param splitDir directory to create split files in.
     * @param inputFile the file we want to split into multiple files
     * @param skipHeader skip header, if true then the first row in the input file will be ignored
     * @return <code>List</code> of <code>FilePart</code> items
     */
    public static List<File> split(File splitDir, File inputFile, boolean skipHeader) {
        return split(splitDir, inputFile, DEFAULT_MAX_BYTES, skipHeader);
    }

    /**
     * This method reads an input file and creates file splits if the file size in memory exceeds a certain size.
     * If the split directory is not specified the system specific temp directory will be used. This means that the
     * files will be deleted automatically for you when the JVM terminates. It is however a good idea to delete files
     * when no longer needed.
     * @param splitDir directory to create split files in.
     * @param inputFile the file we want to split into multiple files
     * @param maxBytes the maximum in memory size a file is allowed to occupy (guesstimate). 10MB = 10 x 1024 x 1024
     * @param skipHeader skip header, if true then the first row in the input file will be ignored
     * @return <code>List</code> of <code>FilePart</code> items
     */
    public static List<File> split(File splitDir, File inputFile, long maxBytes, boolean skipHeader) {
        // validations
        if(splitDir == null) {
            throw new DataUtilException("The split directory parameter can not be a null value");
        } else if(!splitDir.exists()) {
            throw new DataUtilException("The split directory is not a valid path");
        }
        if(inputFile == null) {
            throw new DataUtilException("The input file parameter can not be a null value");
        } else if(!inputFile.exists()) {
            throw new DataUtilException("The input file doesn't exist");
        }
        if(maxBytes < DEFAULT_MIN_BYTES) {
            throw new DataUtilException("Invalid max bytes specification: " + maxBytes + ", minimum is: "
                    + DEFAULT_MIN_BYTES);
        }

        ArrayList<File> splitFiles = new ArrayList<File>();

        // open file
        FileInputStream inputStream = null;
        LinkedList<String> rows = new LinkedList<String>();
        try {
            // create FilePart
            inputStream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            long charsTotal = 0;
            String line;

            int maxChunk = 60;
            int rowNumber = 0;
            while((line=br.readLine()) != null) {
                rowNumber += 1;
                if(skipHeader && rowNumber == 1) {
                    continue;
                }
                if(storageCalculation(rows.size(), line.length(), charsTotal) > maxBytes) {
                    if(maxChunk-- <= 0) {
                        break;
                    }
                    // we have to stop or we may exceed max part size
                    File splitFile = writePartToFile(splitDir, rows);
                    splitFiles.add(splitFile);
                    rows.clear();
                    charsTotal = 0;
                }
                rows.add(line);
                charsTotal += line.length() + DataUtilDefaults.lineTerminator.length();
            }
            // no more data, finish up the final part
            if(charsTotal > 0) {
                File splitFile = writePartToFile(splitDir, rows);
                splitFiles.add(splitFile);
            }

            inputStream.close();
            inputStream = null;

        } catch (FileNotFoundException e) {
            throw new DataUtilException(e);
        } catch (UnsupportedEncodingException e) {
            throw new DataUtilException(e);
        } catch (IOException e) {
            throw new DataUtilException(e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // intentionally do nothing
                }
            }
        }

        return splitFiles;
    }

    /**
     * This method writes a partial input file to a split file
     * @param splitDir split file directory
     * @param data data
     * @return <code>File</code> new split file
     */
    private static File writePartToFile(File splitDir, List<String> data) {
        BufferedWriter writer = null;
        File splitFile;
        try {
            splitFile = File.createTempFile("split-", ".part", splitDir);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(splitFile.getAbsoluteFile(), false),
                    DataUtilDefaults.charSet));
            for(String item : data) {
                writer.write(item + DataUtilDefaults.lineTerminator);
            }
            writer.flush();
            writer.close();
            writer = null;
        } catch (UnsupportedEncodingException e) {
            throw new DataUtilException(e);
        } catch (FileNotFoundException e) {
            throw new DataUtilException(e);
        } catch (IOException e) {
            throw new DataUtilException(e);
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // Intentionally we're doing nothing here
                }
            }
        }
        return splitFile;
    }

    /**
     * This calculation attempts to calculate how much storage x number of rows may use in an ArrayList assuming that
     * capacity may be up to 1.5 times the actual space used.
     * @param rows rows
     * @param lineChars the amount of new character data we want to add
     * @param totalCharacters total amount of characters already stored
     * @return long a guesstimate for storage required
     */
    private static long storageCalculation(int rows, int lineChars, long totalCharacters) {
        long size = (long) Math.ceil((rows* ARRAY_LIST_ROW_OVERHEAD + lineChars + totalCharacters) * LIST_CAPACITY_MULTIPLIER);
        return size;
    }
}
