package com.btaz.util.writer;

import com.btaz.util.DataUtilException;
import com.btaz.util.tf.Template;

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
    private final int tableColumns;
    private final String encoding;
    private final File outputDirectory;
    private final String filenamePrefix;
    private final String pageTitle;
    private final String pageHeader;
    private final String pageDescription;
    private int currentRow;
    private int currentPageNumber;
    private final int maxRowsPerPage;
    private String[] headerRowColumns;

    /**
     * This initializes a new HTML table writer instance. No pagination is when using this constructor
     * @param outputDirectory output directory for the HTML files
     * @param filenamePrefix prefix used for output file ${prefix}.html or ${prefix}-${page}.html
     * @param pageTitle HTML page title
     * @param pageHeader page header text above the table
     * @param pageDescription description displayed under the header
     * @param tableColumns how many columns that table will support
     */
    public HtmlTableWriter(File outputDirectory, String filenamePrefix, String pageTitle, String pageHeader,
                           String pageDescription, int tableColumns) {
        this(outputDirectory, filenamePrefix, pageTitle, pageHeader, pageDescription, tableColumns, 0);
    }

    /**
     * This initializes a new HTML table writer instance. For Paginated results set maxRows. By default pagination is
     * disabled.
     * @param outputDirectory output directory for the HTML files
     * @param filenamePrefix prefix used for output file ${prefix}.html or ${prefix}-${page}.html
     * @param pageTitle HTML page title
     * @param pageHeader page header text above the table
     * @param pageDescription description displayed under the header
     * @param tableColumns how many columns that table will support
     * @param maxRowsPerPage maximum of rows per document if < 1 then pagination is disabled
     */
public HtmlTableWriter(File outputDirectory, String filenamePrefix, String pageTitle, String pageHeader,
                           String pageDescription, int tableColumns, int maxRowsPerPage) {
        encoding = "UTF8";

        // validations
        if(outputDirectory == null) {
            throw new DataUtilException("The outputDirectory field can not be a null value");
        }
        if(!outputDirectory.exists()) {
            throw new DataUtilException("The output directory must exist: " + outputDirectory.getAbsolutePath());
        }
        if(filenamePrefix == null) {
            throw new DataUtilException("The filenamePrefix field can not be a null value");
        }
        if(filenamePrefix.length() < 1) {
            throw new DataUtilException("The filenamePrefix field must contain at least one character");
        }
        if(pageTitle == null) {
            pageTitle = "";
        }
        if(tableColumns < 1) {
            throw new DataUtilException("You must have at least one column. Invalid column value: " + tableColumns);
        }

        this.tableColumns = tableColumns;
        this.outputDirectory = outputDirectory;
        this.filenamePrefix = filenamePrefix;
        this.pageTitle = pageTitle;
        this.pageHeader = pageHeader;
        this.pageDescription = pageDescription;
        this.maxRowsPerPage = maxRowsPerPage + 1;
        this.currentPageNumber = 1;
        this.currentRow = 0;

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
        try {
            if(currentRow == 0) {
                // capture header row
                this.headerRowColumns = columns;
                writeTop();
                writeRow(headerRowColumns);
            } else if(maxRowsPerPage > 1 && currentRow % maxRowsPerPage == 0) {
                writeBottom(true);
                currentPageNumber = (currentRow/maxRowsPerPage) + 1;
                writeTop();
                writeRow(headerRowColumns);
                currentRow += 1;
                writeRow(columns);
            } else {
                writeRow(columns);
            }
            currentRow += 1;
        } catch (Exception e) {
            throw new DataUtilException(e);
        }
    }

    /**
     * Write data to file
     * @param columns column data
     * @throws IOException exception
     */
    private void writeRow(String... columns) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<tr>");
        for(String data : columns) {
            html.append("<td>").append(data).append("</td>");
        }
        html.append("</tr>\n");
        writer.write(html.toString());
    }

    /**
     * Write the top part of the HTML document
     * @throws IOException exception
     */
    private void writeTop() throws IOException {
        // setup output HTML file
        File outputFile = new File(outputDirectory, createFilename(currentPageNumber));
        writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFile, false), Charset.forName(encoding)));

        // write header
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageTitle", pageTitle);
        if(pageHeader != null && pageHeader.length() > 0) {
            map.put("header", "<h1>" + pageHeader + "</h1>");
        } else {
            map.put("header","");
        }
        if(pageDescription != null && pageDescription.length() > 0) {
            map.put("description", "<p>" + pageDescription + "</p>");
        } else {
            map.put("description", "");
        }

        String template = Template.readResourceAsStream("com/btaz/util/templates/html-table-header.ftl");
        String output = Template.transform(template, map);
        writer.write(output);
    }

    /**
     * Write bottom part of the HTML page
     * @param hasNext has next, true if there's supposed to be another page following this one
     */
    private void writeBottom(boolean hasNext) throws IOException {
        String template = Template.readResourceAsStream("com/btaz/util/templates/html-table-footer.ftl");
        Map<String,Object> map = new HashMap<String,Object>();
        String prev = " ";
        String next = "";
        if(currentPageNumber > 1) {
            prev = "<a href=\"" + createFilename(1) + "\">First</a> &nbsp; "
            + "<a href=\"" + createFilename(currentPageNumber-1) + "\">Previous</a> &nbsp; ";
        }
        if(hasNext) {
            next = "<a href=\"" + createFilename(currentPageNumber+1) + "\">Next</a>";
        }
        map.put("pageNavigation", prev + next);
        template = Template.transform(template, map);
        writer.write(template);
        writer.close();
        writer = null;
    }

    /**
     * Create a new file name using page number, only page 2 and higher have a page number in the file name
     * @param pageNumber page number
     * @return {@code String} new filename
     */
    public String createFilename(int pageNumber) {
       if(pageNumber < 2) {
           // no pagination
           return filenamePrefix + ".html";
       } else {
           // paginated name
           return filenamePrefix + "-" + pageNumber + ".html";
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
                writeBottom(false);
            }
        } catch (IOException e) {
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
