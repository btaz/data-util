package com.btaz.datautil.xml.diff;

import com.btaz.datautil.xml.model.Node;

/**
 * User: msundell
 */
public class Difference {
    private String pathA;
    private String pathB;
    private String reason;

    public Difference(Node a, Node b, String reason) {
        pathA = getPath(a);
        pathB = getPath(b);
        this.reason = reason;
    }

    private String getPath(Node node) {
        StringBuilder path = new StringBuilder();
        while(node != null) {
            path.insert(0, node.toString());
            node = node.getParent();
        }
        return path.toString();
    }

    public String getPathA() {
        return pathA;
    }

    public String getPathB() {
        return pathB;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Reason: ").append(reason).append("\n")
                .append(" - ").append(pathA).append("\n")
                .append(" - ").append(pathB).append("\n")
                .toString();
    }
}
