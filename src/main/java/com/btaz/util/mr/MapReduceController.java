package com.btaz.util.mr;

import com.btaz.util.files.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Todo: add identity/entity mappers and reducers if a null value is provided
 * User: msundell
 */
public class MapReduceController {
    public static void execute(File workDir, File inputFile, File outputFile, Mapper mapper,
                               KeyComparator keyComparator, Reducer reducer) {
        List<File> inputFiles = new ArrayList<File>();
        inputFiles.add(inputFile);
        execute(workDir, inputFiles, outputFile, mapper, keyComparator, reducer);
    }

    public static void execute(File workDir, List<File> inputFiles, File outputFile, Mapper mapper,
                               KeyComparator keyComparator, Reducer reducer) {
        FileTracker tracker = new FileTracker();

        // map
        List<File> mapFiles = FileMapper.map(workDir, inputFiles, mapper, null);
        tracker.add(mapFiles);

        // sort and merge
        List<File> sortedFiles = FileSorter.sort(workDir, mapFiles, keyComparator);
        File mergeFile = new File(workDir, "merge-file.data");
        if(mergeFile.exists()) {
            mergeFile.delete();
        }
        tracker.createFile(mergeFile);
        FileMerger.merge(workDir, sortedFiles, mergeFile, keyComparator);

        // reduce
        FileReducer.reduce(mergeFile, outputFile, reducer, keyComparator);

        // clean-up
        tracker.deleteAll();
    }
}
