package com.btaz.datautil.files;

import com.btaz.datautil.DataUtilDefaults;
import com.btaz.datautil.DataUtilException;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * File merge utility class
 * User: msundell
 */
public class FileMerger {
    private static final int MERGE_FACTOR = 8;

    /**
     * Merge multiple files into a single file. Multi-pass approach where the merge factor controls how many passes that
     * are required. This method uses the default merge factor for merging.
     * @param mergeDir merge directory. If null, then it defaults to system temp.
     * @param sortedFiles sorted files that are to be merged
     * @param mergedFile final output file
     * @param comparator comparator used to merge files (sorting)
     */
    public static void merge(File mergeDir, List<File> sortedFiles, File mergedFile, Comparator<String> comparator) {
        merge(mergeDir, sortedFiles, mergedFile, comparator, MERGE_FACTOR);
    }

    /**
     * Merge multiple files into a single file. Multi-pass approach where the merge factor controls how many passes that
     * are required. If a merge factor of X is specified, then X files will be opened concurrently for merging.
     * @param mergeDir merge directory. If null, then it defaults to system temp.
     * @param sortedFiles sorted files that are to be merged
     * @param mergedFile final output file
     * @param comparator comparator used to merge files (sorting)
     * @param mergeFactor merge factor, a higher number typically merges faster but uses more file handles.
     */
    public static void merge(File mergeDir, List<File> sortedFiles, File mergedFile, Comparator<String> comparator,
                             int mergeFactor) {
        LinkedList<File> mergeFiles = new LinkedList<File>(sortedFiles);

        // merge all files
        LinkedList<BatchFile> batch = new LinkedList<BatchFile>();
        try {
            while(mergeFiles.size() > 0) {
                // create batch
                batch.clear();
                for(int i = 0; i < mergeFactor && mergeFiles.size() > 0; i++) {
                    batch.add(new BatchFile(mergeFiles.remove()));
                }

                // create aggregation file
                File aggFile;
                if(mergeFiles.size() > 0) {
                    // create new aggregate file
                    aggFile = File.createTempFile("merge-", ".part", mergeDir);
                    mergeFiles.addLast(aggFile);
                } else {
                    // create final file
                    aggFile = mergedFile;
                }

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(aggFile.getAbsoluteFile(), false),DataUtilDefaults.charSet));

                // process batch
                String [] buffer = new String[batch.size()];
                Arrays.fill(buffer, null);
                boolean [] inUse = new boolean[batch.size()];
                Arrays.fill(inUse, true);
                boolean inUseFlag = true;
                while(inUseFlag) {
                    // load comparison buffer
                    int index = -1;
                    String selected = null;
                    for (int i = 0; i < batch.size(); i++) {
                        if(inUse[i]) {
                            if(buffer[i] == null) {
                                // need more data
                                buffer[i] = batch.get(i).getRow();
                                if(buffer[i] == null) {
                                    inUse[i] = false;
                                }
                            }
                            if(buffer[i] != null) {
                                if(index == -1) {
                                    // set item
                                    index = i;
                                    selected = buffer[i];
                                } else if(comparator.compare(buffer[i], selected) < 0) {
                                    // replace item
                                    index = i;
                                    selected = buffer[i];
                                }
                            }
                        }
                    }

                    if(index >= 0) {
                        // select item and write to new aggregate file
                        writer.write(buffer[index] + DataUtilDefaults.lineTerminator);
                        buffer[index] = null;
                        inUseFlag = true;
                    } else {
                        inUseFlag = false;
                    }
                }
                // no more data
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            throw new DataUtilException(e);
        }
    }

    /**
     * Represents an open file used for file aggregation
     */
    private static class BatchFile {
        private File file;
        private FileInputStream inputStream;
        private BufferedReader reader;
        private String line;
        private boolean hasMoreData;

        /**
         * Private constructor
         * @param file file
         */
        private BatchFile(File file) {
            this.file = file;
            try {
                inputStream = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(inputStream, DataUtilDefaults.charSet));
                hasMoreData = true;
            } catch (FileNotFoundException e) {
                throw new DataUtilException(e);
            } catch (UnsupportedEncodingException e) {
                throw new DataUtilException(e);
            }
        }

        /**
         * Read a row from the encapsulated file. Automatically closes the file upon end of file
         * @return <code>String</code> a single row or null if there is no more data
         */
        private String getRow() {
            if(! hasMoreData) {
                return null;
            }
            try {
                line = reader.readLine();
                if(line == null) {
                    hasMoreData = false;
                    close();
                }
                return line;
            } catch (IOException e) {
                hasMoreData = false;
                throw new DataUtilException(e);
            }
        }

        /**
         * Get the encapsulated File object
         * @return <code>File</code> object
         */
        public File getFile() {
            return file;
        }

        /**
         * Close the encapsulated file
         */
        private void close() {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new DataUtilException(e);
            }
        }
    }
}
