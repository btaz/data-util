package com.btaz.util.tf;

import com.btaz.util.DataUtilException;
import com.btaz.util.unit.ResourceUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Micro templating engine
 * User: msundell
 */
public class Template {
    /**
     * Template containing ${name} tags that will be replaced with the values from the map. For more advanced templating
     * use something like Velocity or Freemarker. The "name" can not contain a '}' character
     * @param template template string
     * @param map object containing name value pairs
     * @return {@code String} transformed data
     */
    public static String transform(String template, Map<String,Object> map) {
        // validations
        if(template == null) {
            return null;
        }
        if(map == null) {
            return template;
        }

        // transform
        List<String> nameList = new ArrayList<String>();
        nameList.addAll(map.keySet());
        for(String name : nameList) {
            template = template.replaceAll("\\$\\{" + name + "}", map.get(name).toString());
        }
        return template;
    }

    /**
     * This method looks for a file on the provided path, attempts to load it as a stream and returns it as a string
     * @param path path
     * @return {@code String} text
     */
    public static String readResourceAsStream(String path) {
        try {
            InputStream inputStream = ResourceUtil.getResourceAsStream(path);
            return ResourceUtil.readFromInputStreamIntoString(inputStream);
        } catch (Exception e) {
            throw new DataUtilException("Failed to load resource", e);
        }
    }

    /**
     * This method looks for a file on the provided path and returns it as a string
     * @param path path
     * @return {@code String} text
     */
    public static String readResource(String path) {
        try {
            File file = ResourceUtil.getResourceFile(path);
            return ResourceUtil.readFromFileIntoString(file);
        } catch (Exception e) {
            throw new DataUtilException("Failed to load resource", e);
        }
    }
}
