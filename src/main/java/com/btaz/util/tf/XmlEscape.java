package com.btaz.util.tf;

/**
 * This class is used for escaping XML
 * User: msundell
 */
public class XmlEscape {
    public static String escape(String text) {
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("\"", "&quot;");
        text = text.replaceAll("'", "&apos;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
    }
}
