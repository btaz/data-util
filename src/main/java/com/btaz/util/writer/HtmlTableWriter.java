package com.btaz.util.writer;

import com.btaz.util.DataUtilException;
import com.btaz.util.tf.HtmlEscape;
import freemarker.template.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple HTML file writer that allows you to write tabular data to an HTML file.
 * The output format is simple. The first row is the header row. All other calls are data rows.
 * User: msundell
 */
public class HtmlTableWriter {
    private BufferedWriter writer;
    private Configuration cfg;
    private int tableColumns;
    private String encoding;

    /**
     * This initializes a new HTML table writer instance
     * @param outputFile output file
     * @param pageTitle HTML page title
     * @param tableColumns how many columns that table will support
     */
    public HtmlTableWriter(File outputFile, String pageTitle, int tableColumns) {
        this.tableColumns = tableColumns;
        encoding = "UTF8";

        // validations
        if(outputFile == null) {
            throw new DataUtilException("The outputFile field can not be a null value");
        }
        if(pageTitle == null) {
            pageTitle = "";
        }
        if(tableColumns < 1) {
            throw new DataUtilException("You must have at least one column. Invalid column value: " + tableColumns);
        }

        try {
            // our output HTML file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile.getAbsoluteFile(), false), Charset.forName(encoding)));

            // setup freemarker
            cfg = new Configuration();
            cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setDefaultEncoding(encoding);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
            cfg.setIncompatibleImprovements(new Version(2, 3, 20));  // FreeMarker 2.3.20

            // render header
            Map<String,Object> root = new HashMap<String,Object>();
            root.put("pageTitle", pageTitle);
            Template template = cfg.getTemplate("html-table-header.ftl");
            template.process(root, writer);
        } catch (FileNotFoundException e) {
            throw new DataUtilException("Failed to open an HTML table file", e);
        } catch (IOException e) {
            throw new DataUtilException("Failed to open an HTML table file", e);
        } catch (TemplateException e) {
            throw new DataUtilException("Failed to write the HTML table file header", e);
        }
    }

    /**
     * Write a row to the table. The first row will be the header row
     * @param columns columns
     * @throws DataUtilException exception
     */
    public void write(String... columns) throws DataUtilException {
        if(columns.length != tableColumns) {
            throw new DataUtilException("Invalid column count. Expected " + tableColumns + " but found: "
                    + columns.length);
        }
        StringBuilder html = new StringBuilder();
        html.append("<tr>");
        for(String data : columns) {
            html.append("<td>").append(HtmlEscape.escape(data)).append("</td>");
        }
        html.append("</tr>\n");
        try {
            writer.write(html.toString());
        } catch (IOException e) {
            throw new DataUtilException("Failed to write the HTML table file", e);
        }
    }

    /**
     * Close the HTML table and resources
     * @throws DataUtilException exception
     */
    public void close() throws DataUtilException {
        // render footer
        try {
            if(writer != null) {
                Template template = cfg.getTemplate("html-table-footer.ftl");
                template.process(null, writer);
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            throw new DataUtilException("Failed to close the HTML table file", e);
        } catch (TemplateException e) {
            throw new DataUtilException("Failed to close the HTML table file", e);
        }
    }

    /**
     * In case the user forges
     * @throws Throwable exception
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
