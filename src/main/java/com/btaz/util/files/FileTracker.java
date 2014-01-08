package com.btaz.util.files;

import com.btaz.util.DataUtilException;
import com.btaz.util.comparator.FilePathComparator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * This class is used to add files for deletion. This is useful when you create file structures that you later want to
 * delete all at once. You first add the files and directories you want to add. Once the delete all method is called
 * all the tracked files will be deleted in reverse order, bottom to top. Note that this class assumes that the delete
 * all method will be called. There's not automatic call on a JVM termination. Note that if you put untracked files and
 * directories inside a tracked directory structure, then the deleteAll method will fail.
 */
public class FileTracker {
    private Set<File> trackedFiles;
    private FilePathComparator filePathComparator;


    /**
     * Default constructor
     */
    public FileTracker() {
        trackedFiles = new HashSet<File>();
        filePathComparator = new FilePathComparator();
    }

    /**
     * Track a new file
     * @param file tracked file
     */
    public void add(File file) {
        if(file != null) {
            trackedFiles.add(file);
        }
    }

    /**
     * Track a new list of new files and directories
     * @param files list of files and directories
     */
    public void add(List<File> files) {
        if(files != null) {
            trackedFiles.addAll(files);
        }
    }

    /**
     * Track a new array of new files and directories
     * @param files array of files and directories
     */
    public void add(File[] files) {
        if(files != null) {
            Collections.addAll(trackedFiles, files);
        }
    }

    /**
     * Create a new tracked file
     * @param dir file directory
     * @param filename filename
     * @return file new file
     * @throws DataUtilException data util exception
     */
    public File createFile(File dir, String filename) throws DataUtilException {
        if(dir == null) {
            throw new DataUtilException("The directory parameter can't be a null value");
        }
        try {
            File file = new File(dir, filename);
            return createFile(file);
        } catch (Exception e) {
            throw new DataUtilException("Invalid file: " + filename);
        }
    }

    /**
     * Create a new tracked file
     * @param file new file
     * @return file new file
     */
    public File createFile(File file) throws DataUtilException {
        if(file == null) {
            throw new DataUtilException("File parameter can not be a null value");
        }

        try {
            if(! file.createNewFile()) {
                throw new DataUtilException("Failed to create file. Result of File.createNewFile() was false.");
            }
            trackedFiles.add(file);
        } catch (IOException e) {
            throw new DataUtilException("Failed to create file: " + file.getAbsolutePath(), e);
        }
        return file;
    }

    /**
     * Create and add a new directory
     * @param dir new directory
     * @return file new directory
     */
    public File createDir(File dir) throws DataUtilException {
        if(dir == null) {
            throw new DataUtilException("Dir parameter can not be a null value");
        }
        if(dir.exists()) {
            throw new DataUtilException("Directory already exists: " + dir.getAbsolutePath());
        }
        if(! dir.mkdir()) {
            throw new DataUtilException("The result of File.mkDir() was false. Failed to create directory. : "
                    + dir.getAbsolutePath());
        }
        trackedFiles.add(dir);
        return dir;
    }

    /**
     * This is a convenience method to get a test resource by name. Test resources are not tracked since that could lead
     * to their deletion.
     * @param resourceName resource name
     */
    public File getTestResource(String resourceName) {
        URL url = this.getClass().getClassLoader().getResource(resourceName);

        assert url != null : "Couldn't load : " + resourceName;
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new DataUtilException("Couldn't load test resource");
        }
    }

    /**
     * Delete all tracked files
     */
    public void deleteAll() {
        if(trackedFiles.size() == 0) {
            return;
        }

        ArrayList<File> files = new ArrayList<File>(trackedFiles);
        Collections.sort(files, filePathComparator);

        for(File file : files) {
            if(file.exists()) {
                if(!file.delete()) {
                    throw new DataUtilException("Couldn't delete a tracked "
                            + (file.isFile()? "file":"directory" + ": ") + file.getAbsolutePath());
                }
            }
        }

        trackedFiles.clear();
    }

    /**
     * Tracked file count
     * @return int number of tracked files
     */
    public int size() {
        return trackedFiles.size();
    }
}
