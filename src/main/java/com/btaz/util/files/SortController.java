package com.btaz.util.files;

import com.btaz.util.comparator.Lexical;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import static com.btaz.util.files.FileMerger.*;
import static com.btaz.util.files.FileSorter.*;
import static com.btaz.util.files.FileSplitter.split;

/**
 * User: msundell
 */
public class SortController {
    private static final long DEFAULT_MAX_BYTES = 10 * 1024 * 1024; // 10 MB RAM, roughly 4.7 MB per file on disk
    private static final int MERGE_FACTOR = 8;

    /**
     * Sort a file, write sorted data to output file. Use the default max bytes size and the default merge factor.
     * @param inputFile input file
     * @param outputFile output file
     * @param comparator comparator used to sort rows
     */
    public static void sortFile(File sortDir, File inputFile, File outputFile, Comparator<String> comparator,
                                boolean skipHeader) {
        sortFile(sortDir, inputFile, outputFile, comparator, skipHeader, DEFAULT_MAX_BYTES, MERGE_FACTOR);
    }

    /**
     * Sort a file, write sorted data to output file.
     * @param inputFile input file
     * @param outputFile output file
     * @param comparator comparator used to sort rows
     */
    public static void sortFile(File sortDir, File inputFile, File outputFile, Comparator<String> comparator,
                                boolean skipHeader, long maxBytes, int mergeFactor) {
        // validation
        if(comparator == null) {
            comparator = Lexical.ascending();
        }

        // steps
        List<File> splitFiles = split(sortDir, inputFile, maxBytes, skipHeader);
        List<File> sortedFiles = sort(sortDir, splitFiles, comparator);
        merge(sortDir, sortedFiles, outputFile, comparator, mergeFactor);
    }
}
