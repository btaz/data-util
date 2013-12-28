package com.btaz.datautil.files;

import com.btaz.datautil.DataUtilDefaults;
import com.btaz.datautil.DataUtilException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: msundell
 */
public class FileSorter {
    /**
     * This method sorts every file in a list of input files
     * @param sortDir directory to provide the sorting in. If null, then system temp directory is used.
     * @param files files to sort
     * @param comparator comparator to use for sorting
     */
    public static List<File> sort(File sortDir, List<File> files, Comparator<String> comparator) {
        // validations
        if(sortDir == null) {
            throw new DataUtilException("The sort directory parameter can't be a null value");
        } else if(!sortDir.exists()) {
            throw new DataUtilException("The sort directory doesn't exist");
        }
        if(files == null) {
            throw new DataUtilException("The files parameter can't be a null value");
        }

        // sort files
        List<File> sortedFiles = new ArrayList<File>();
        for(File file : files) {
            FileInputStream inputStream = null;
            BufferedWriter writer = null;

            try {
                // readLine part into memory
                ArrayList<String> list = new ArrayList<String>();
                inputStream = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, DataUtilDefaults.charSet));
                String line;
                while((line = br.readLine()) != null) {
                    list.add(line);
                }
                inputStream.close();
                inputStream = null;

                // sort
                Collections.sort(list, comparator);

                // write sorted partial
                File sortedFile = File.createTempFile("sorted-", ".part", sortDir);
                sortedFiles.add(sortedFile);

                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        sortedFile.getAbsoluteFile(), false),DataUtilDefaults.charSet));

                for(String item : list) {
                    writer.write(item + DataUtilDefaults.lineTerminator);
                }
                writer.flush();
                writer.close();
                writer = null;
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
                        // intentionally, do nothing
                    }
                }
                if(writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        // intentionally, do nothing
                    }
                }
            }
        }
        return sortedFiles;
    }
}
