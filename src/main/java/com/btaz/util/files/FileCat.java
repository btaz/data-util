package com.btaz.util.files;

import com.btaz.util.DataUtilDefaults;
import com.btaz.util.DataUtilException;

import java.io.*;
import java.util.List;

/**
 * User: msundell
 */
public class FileCat {
    /**
     * This method will concatenate one or more files
     * @param files files to concatenate
     * @param concatenatedFile concatenated output file
     */
    public static void concatenate(List<File> files, File concatenatedFile) {

        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(concatenatedFile.getAbsoluteFile(),
                    false), DataUtilDefaults.charSet));

            FileInputStream inputStream;
            for(File input : files) {
                inputStream = new FileInputStream(input);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while((line = reader.readLine()) != null) {
                    writer.write(line + DataUtilDefaults.lineTerminator);
                }
                inputStream.close();
            }
            writer.flush();
            writer.close();
        } catch (UnsupportedEncodingException e) {
            throw new DataUtilException(e);
        } catch (FileNotFoundException e) {
            throw new DataUtilException(e);
        } catch (IOException e) {
            throw new DataUtilException(e);
        }
    }
}
