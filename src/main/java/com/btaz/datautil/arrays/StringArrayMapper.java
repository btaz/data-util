package com.btaz.datautil.arrays;

import com.btaz.datautil.DataUtilException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * User: msundell
 */
public class StringArrayMapper {
    /**
     * This method creates a field remap table. This is useful for CSV/TSV type files where you have to similar headers
     * and you need to map from one format to the other. Control fields is the desired format, remapped fields is the
     * format that you need to convert from. The remap table will contain -1 for unmappable fields
     *
     * The resulting remap table can then be used like this:
     *
     * int [] remap = createRemapTable(newHeaders, oldHeaders);
     * for(int i=0; newArray.length ; i++) {
     *     if(remap[i] != -1) {
     *         newArray[i] = oldArray[remap[i]]
     *     }
     * }
     *
     * @param controlFields string field array representing the new header format
     * @param remappedFields string field array representing the old header format
     * @return int array with a remap table
     */
    public static int [] createRemapTable(String [] controlFields, String [] remappedFields) {
        // create hash map with remapped fields
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        for (int index = 0; index < remappedFields.length; index++) {
            String field = remappedFields[index];
            map.put(field, index);
        }

        // create remap table
        int remap [] = new int[controlFields.length];

        for (int index = 0; index < controlFields.length; index++) {
            Integer remapIndex = map.get(controlFields[index]);
            if(remapIndex == null) {
                // no match
                remapIndex = -1;
            }
            remap[index] = remapIndex;
        }

        return remap;
    }

    // todo: implement a remap method

    //
    // Todo: Should return a complex object that contains combined fields, missing fields, allows for ignorable fields
    //
    public Set<String> getCombinedHeaders(String[] h1, String[] h2, Set ignoreSet) {
        Set<String> headerFields = new HashSet<String>();
        int idCounter = 0;
        for(String field : h1) {
            if("id".equals(field)) {
                idCounter += 1;
            }
            if(! ignoreSet.contains(field)) {
                headerFields.add(field);
            }
        }
        for(String field : h2) {
            if("id".equals(field)) {
                idCounter += 1;
            }
            if(! ignoreSet.contains(field)) {
                headerFields.add(field);
            }
        }
        if(idCounter != 2) {
            throw new DataUtilException("Invalid header format. The \"id\" field must be present in both files");
        }
        if(headerFields.size() < h1.length || headerFields.size() < h1.length) {
            throw new DataUtilException("Invalid header format. Repeated header fields with the same name");
        }
        return headerFields;
    }
}


