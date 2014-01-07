package com.btaz.datautil.xml.diff;

import com.btaz.datautil.xml.model.Content;
import com.btaz.datautil.xml.model.Element;
import com.btaz.datautil.xml.model.Node;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This arbitrator sorts all child elements. This is useful when element order is not relevant for comparisons.
 * User: msundell
 */
public class SortedChildElementsArbitrator extends DefaultArbitrator {
    @Override
    public void preProcessor(List<Node> list) {
        Collections.sort(list, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(o1 instanceof Content && o2 instanceof Content) {
                    Content c1 = (Content) o1;
                    Content c2 = (Content) o2;
                    return c1.getText().compareTo(c2.getText());
                } else if(o1 instanceof Element && o2 instanceof Content) {
                    return -1;
                } else if(o1 instanceof Content && o2 instanceof Element) {
                    return 1;
                } else {
                    assert o1 instanceof Element;
                    assert o2 instanceof Element;
                    Element e1 = (Element) o1;
                    Element e2 = (Element) o2;
                    return e1.toString(true, true).compareTo(e2.toString(true, true));
                }
            }
        });
    }
}
