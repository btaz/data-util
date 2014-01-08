package com.btaz.util.files;

import com.btaz.util.DataUtilException;

import java.io.File;
import java.io.FilenameFilter;

/**
 * User: msundell
 */
public class FileDeleter {
    /**
     * Delete files in a directory matching a file extension
     * @param dir directory
     * @param extension filename extension
     */
    public static void deleteFilesByExtension(File dir, String extension) {
        if(extension == null) {
            throw new DataUtilException("Filename extension can not be a null value");
        }
        FilenameFilter filter = new FileExtensionFilenameFilter(extension);
        FileDeleter.deleteFiles(dir, filter);
    }

    /**
     * Delete files in a directory matching a regular expression
     * @param dir directory
     * @param regex regular expression
     */
    public static void deleteFilesByRegex(File dir, String regex) {
        if(regex == null) {
            throw new DataUtilException("Filename regex can not be null");
        }
        FilenameFilter filter = new RegexFilenameFilter(regex);
        FileDeleter.deleteFiles(dir, filter);
    }

    /**
     * Delete files in a directory matching a filename filter
     * @param dir directory
     * @param filter filename filter, if null then delete all files
     */
    public static void deleteFiles(File dir, FilenameFilter filter) {
        // validations
        if(dir == null) {
            throw new DataUtilException("The delete directory parameter can not be a null value");
        } else if(!dir.exists() || !dir.isDirectory()) {
            throw new DataUtilException("The delete directory does not exist: " + dir.getAbsolutePath());
        }

        // delete files
        File [] files;
        if(filter == null) {
            files = dir.listFiles();
        } else {
            files = dir.listFiles(filter);
        }
        if (files != null) {
            for(File file : files) {
                if(file.isFile()) {
                    if(!file.delete()) {
                        throw new DataUtilException("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * FilenameFilter implementation that matches file extensions
     */
    private static class FileExtensionFilenameFilter implements FilenameFilter {
        private String fileExtension;

        public FileExtensionFilenameFilter(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        @Override
        public boolean accept(File dir, String filename) {
            return filename.endsWith(fileExtension);
        }
    }

    /**
     * FilenameFilter implementation that supports regex to match filenames
     */
    private static class RegexFilenameFilter implements FilenameFilter {
        private String regex;

        public RegexFilenameFilter(String regex) {
            this.regex = regex;
        }

        @Override
        public boolean accept(File dir, String filename) {
            return filename.matches(regex);
        }
    }
}
