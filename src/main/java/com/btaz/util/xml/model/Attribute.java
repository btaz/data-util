package com.btaz.util.xml.model;

/**
 * User: msundell
 */
public class Attribute implements Cloneable {
    private String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute1 = (Attribute) o;

        if (!value.equals(attribute1.value)) return false;
        if (!name.equals(attribute1.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    /**
     * Creates a deep clone of the Attribute object.
     * @return [@code Object] cloned object
     * @throws CloneNotSupportedException exception
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new Attribute(name, value);
    }
}
