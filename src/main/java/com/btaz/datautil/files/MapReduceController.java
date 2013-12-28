package com.btaz.datautil.files;

import com.btaz.datautil.files.mapreduce.KeyComparator;
import com.btaz.datautil.files.mapreduce.Mappable;
import com.btaz.datautil.files.mapreduce.Reducable;

import java.io.File;
import java.util.List;

/**
 * User: msundell
 */
public class MapReduceController {
    public static void execute(File workDir, File inputFile, File outputFile, Mappable mappable,
                               KeyComparator keyComparator, Reducable reducable) {
        FileTracker tracker = new FileTracker();

        // map
        List<File> mappedFiles = FileMapper.map(workDir, inputFile, mappable);
        tracker.add(mappedFiles);

        // sort and merge
        List<File> sortedFiles = FileSorter.sort(workDir, mappedFiles, keyComparator);
        File mergeFile = new File(workDir, "merge-file.data");
        if(mergeFile.exists()) {
            mergeFile.delete();
        }
        tracker.createFile(mergeFile);
        FileMerger.merge(workDir, sortedFiles, mergeFile, keyComparator);

        // reduce
        FileReducer.reduce(mergeFile, outputFile, reducable, keyComparator);

        // clean-up
        tracker.deleteAll();
    }
}
