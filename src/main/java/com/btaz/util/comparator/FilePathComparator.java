package com.btaz.util.comparator;

import com.btaz.util.DataUtilException;

import java.io.File;
import java.util.Comparator;

/**
 * User: msundell
 */
public class FilePathComparator implements Comparator<File> {
    @Override
    public int compare(File o1, File o2) {
        if(o1 == null || o2 == null) {
            throw new DataUtilException("None of the file parameters can be a null value");
        }
        String [] f1 = o1.getAbsolutePath().split(File.separator);
        String [] f2 = o2.getAbsolutePath().split(File.separator);

        for(int i=0; i<Math.min(f1.length, f2.length); i++) {
            if(! f1[i].equals(f2[i])) {
                return -1 * f1[i].compareTo(f2[i]);
            }
        }
        Integer fl1 = f1.length;
        Integer fl2 = f2.length;
        return -1 * fl1.compareTo(fl2);
    }
}
