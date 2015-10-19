package osmedile.intellij.javadochelper;

import java.io.Serializable;

/**
 * @author Olivier Smedile
 * @version $Id: ClassTag.java 43 2008-07-11 14:02:05Z osmedile $
 */
public class ClassTag implements Serializable {
    private String name;
    private String value;


    private REPLACE_MODE replaceMode;

    /**
     * True if class tag is currently active.
     */
    private boolean active;
    private boolean ignoreCase;

// --------------------------- CONSTRUCTORS ---------------------------

    public ClassTag() {
        this.active = true;
        this.ignoreCase = true;
    }

    public ClassTag(String name, String value, REPLACE_MODE replace,
                    boolean active, final boolean ignoreCase) {
        this.name = name;
        this.value = value;
        this.replaceMode = replace;
        this.active = active;
        this.ignoreCase = ignoreCase;
        checkName();
    }

    private void checkName() {
        //if name don't begin with '@', add it
        name = "@" + name.replaceAll("@", "");
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * @return true if multiple ClassTag can have the same name of this tag
     */
    public boolean getActive() {
        return active;
    }

    /**
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    /**
     * @return name of the tag
     */
    public String getName() {
        return name;
    }


    public REPLACE_MODE getReplaceMode() {
        return replaceMode;
    }

    public void setReplaceMode(REPLACE_MODE replaceMode) {
        this.replaceMode = replaceMode;
    }

    /**
     * @return value of the tag
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

// ------------------------ CANONICAL METHODS ------------------------

// --------------------- METHODS ---------------------

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassTag)) return false;

        ClassTag classTag = (ClassTag) o;

        if (active != classTag.active) return false;
        if (ignoreCase != classTag.ignoreCase) return false;
        if (!name.equals(classTag.name)) return false;
        if (replaceMode != classTag.replaceMode) return false;
        if (!value.equals(classTag.value)) return false;

        return true;
    }

    public int hashCode() {
        return name.hashCode();
    }

    // -------------------------- OTHER METHODS --------------------------

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name can't be null");
        }
        this.name = name;
        checkName();
    }

// -------------------------- ENUMERATIONS --------------------------

    public enum REPLACE_MODE {
        ADD("Add"),
        DONT_ADD("Don't add"),

        /**
         * All tags with same are replaced
         */
        REPLACE_ALL("Replace all");

        String desc;

        REPLACE_MODE(String desc) {
            this.desc = desc;
        }


        public String toString() {
            return desc;
        }
    }
}
