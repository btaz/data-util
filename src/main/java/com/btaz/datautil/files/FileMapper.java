package com.btaz.datautil.files;

import com.btaz.datautil.DataUtilDefaults;
import com.btaz.datautil.DataUtilException;
import com.btaz.datautil.files.mapreduce.MapReduceException;
import com.btaz.datautil.files.mapreduce.Mapper;
import com.btaz.datautil.files.mapreduce.OutputCollector;

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
     * @param mapper callback method, a class that implements the Mappable interface
     * @throws DataUtilException data util exception
     */
    public static List<File> map(File workDir, File inputFile, Mapper mapper)
            throws DataUtilException {
        List<File> files = new ArrayList<File>();
        files.add(inputFile);
        return map(workDir, files, mapper);
    }

    /**
     * This method functions provide map (map-reduce) like functionality. For every row in the input data there is a
     * call to the mapper callback method;
     * @param workDir work directory where the mapped files will be created
     * @param inputFiles input files
     * @param mapper callback method, a class that implements the Mappable interface
     * @throws DataUtilException data util exception
     */
    public static List<File> map(File workDir, List<File> inputFiles, Mapper mapper)
            throws DataUtilException {
        // validations
        if(workDir == null) {
            throw new DataUtilException("The workDir parameter can not be a null value");
        }
        if(inputFiles == null) {
            throw new DataUtilException("The inputFiles parameter can not be a null value");
        }
        if(mapper == null) {
            throw new DataUtilException("The mappable parameter can not be a null value");
        }

        // setup output collector
        OutputCollector collector = new OutputCollector(workDir, "map");

        // map all data
        try {
            for(File file : inputFiles) {
                FileInputStream inputStream;

                try {
                    inputStream = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,
                            DataUtilDefaults.charSet));

                    // map data
                    String row;
                    while((row = br.readLine()) != null) {
                        mapper.map(row, collector);
                    }

                    inputStream.close();
                } catch (IOException e) {
                    throw new DataUtilException(e);
                } catch (MapReduceException e) {
                    throw new DataUtilException("Irrecoverable map operation", e);
                }
            }
        } finally {
            collector.close();
        }

        // return output files
        return collector.getFiles();
    }
}
