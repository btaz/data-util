package com.btaz.util.tf;

/**
 * This class is used for escaping HTML
 * Ref: https://www.owasp.org/index.php/XSS_%28Cross_Site_Scripting%29_Prevention_Cheat_Sheet#RULE_.231_-_HTML_Escape_Before_Inserting_Untrusted_Data_into_HTML_Element_Content
 * User: msundell
 */
public class HtmlEscape {
    public static String escape(String html) {
        char [] input = html.toCharArray();
        StringBuilder output = new StringBuilder();
        for(char ch : input) {
            switch (ch) {
                case '&':
                    output.append("&amp;");
                    break;
                case '<':
                    output.append("&lt;");
                    break;
                case '>':
                    output.append("&gt;");
                    break;
                case '"':
                    output.append("&quot;");
                    break;
                case '/':
                    output.append("&#x2F;");
                    break;
                default:
                    output.append(ch);
            }
        }
        return output.toString();
    }
}
