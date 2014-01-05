package com.btaz.datautil.xml.diff;

import com.btaz.datautil.xml.model.Content;
import com.btaz.datautil.xml.model.Element;
import com.btaz.datautil.xml.model.Node;

import java.util.List;

/**
 * Default arbitrator. Rules:
 * - the elements must be identical
 * - the content character data must be identical
 * - elements, attributes and content are case sensitive
 * - empty element tag and an element without children is different
 * - element attribute order matters
 * - element order matters
 * User: msundell
 */
public class DefaultArbitrator implements Arbitrator {
    /**
     * {@inheritDoc}
     */
    @Override
    public Difference compare(Node a, Node b) {
        if(a instanceof Content && b instanceof Content) {
            // - content character data
            String textA = ((Content)a).getText();
            String textB = ((Content)b).getText();
            if(! textA.equals(textB)) {
                return new Difference(a, b, "Content is different");
            }
        } else if (a instanceof Element && b instanceof Element) {
            Element ea = (Element)a;
            Element eb = (Element)b;

            // - element name
            if(! ea.getName().equals(eb.getName())) {
                return new Difference(a, b, "Element names are different");
            }

            // - attributes names
            if(! ea.getAttributeNames().equals(eb.getAttributeNames())) {
                return new Difference(a, b, "Element attribute names are different");
            }

            // - attributes values
            for(int i=0; i<ea.getAttributeNames().size(); i++) {
                if(! ea.attributeValue(ea.getAttributeNames().get(i))
                        .equals(eb.attributeValue(eb.getAttributeNames().get(i)))) {
                    return new Difference(a, b, "Element attribute values are different");
                }
            }

            // - element type
            if(ea.isEmptyElementTag() != eb.isEmptyElementTag()) {
                return new Difference(a, b, "Empty element tags are different");
            }
        } else {
            return new Difference(a, b, "The nodes are different");
        }

        // passed all tests
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preProcessor(List<Node> list) {
        // intentionally doing nothing
    }
}
