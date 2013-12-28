package com.btaz.datautil.files;

import com.btaz.datautil.DataUtilDefaults;
import com.btaz.datautil.DataUtilException;
import com.btaz.datautil.files.mapreduce.MapException;
import com.btaz.datautil.files.mapreduce.Mappable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: msundell
 */
public class FileMapper {
    /**
     * This method functions provide map (map-reduce) like functionality. For every row in the input data there is a
     * call to the mapper callback method;
     * @param workDir work directory where the mapped files will be created
     * @param inputFile input file
     * @param mappable callback method, a class that implements the Mappable interface
     * @return <code>List</code> of <code>File</code> items
     * @throws DataUtilException data util exception
     */
    public static List<File> map(File workDir, File inputFile, Mappable mappable) throws DataUtilException {
        List<File> files = new ArrayList<File>();
        files.add(inputFile);
        return map(workDir, files, mappable);
    }

    /**
     * This method functions provide map (map-reduce) like functionality. For every row in the input data there is a
     * call to the mapper callback method;
     * @param workDir work directory where the mapped files will be created
     * @param inputFiles input files
     * @param mappable callback method, a class that implements the Mappable interface
     * @return <code>List</code> of <code>File</code> items
     * @throws DataUtilException data util exception
     */
    public static List<File> map(File workDir, List<File> inputFiles, Mappable mappable) throws DataUtilException {
        // validations
        if(workDir == null) {
            throw new DataUtilException("The workDir parameter can not be a null value");
        }
        if(inputFiles == null) {
            throw new DataUtilException("The inputFiles parameter can not be a null value");
        }
        if(mappable == null) {
            throw new DataUtilException("The mappable parameter can not be a null value");
        }

        // map all data
        List<File> mappedFiles = new ArrayList<File>();
        for(File file : inputFiles) {
            FileInputStream inputStream;

            BufferedWriter writer;
            File mapFile;

            try {
                inputStream = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,
                        DataUtilDefaults.charSet));

                mapFile = File.createTempFile("map-", ".part", workDir);

                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapFile.getAbsoluteFile(),
                        false), DataUtilDefaults.charSet));

                // map data
                String row;
                while((row = br.readLine()) != null) {
                    String output = mappable.map(row);
                    if(output == null) {
                        // null value means that there's nothing to write
                        continue;
                    } else if(output.contains(DataUtilDefaults.lineTerminator)) {
                        throw new DataUtilException("Mapper output data can not contain an end-of-line terminator");
                    }
                    writer.write(output + DataUtilDefaults.lineTerminator);
                }

                writer.flush();
                writer.close();
                mappedFiles.add(mapFile);

                inputStream.close();
            } catch (IOException e) {
                throw new DataUtilException(e);
            } catch (MapException e) {
                throw new DataUtilException("Irrecoverable map operation", e);
            }
        }

        return mappedFiles;
    }
}
