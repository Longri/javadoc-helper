package osmedile.intellij.javadochelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Olivier Smedile
 * @version $Id: ConfigImpl.java 52 2008-07-29 10:32:50Z osmedile $
 */
public class ConfigImpl implements IConfig, Serializable {
    private List<ClassTag> classTags;

    private boolean addIfDocDiffers;

    private boolean removeIfDocMatch;
    private boolean addInheritDocIfNoDoc;

    public ConfigImpl() {
        this.classTags = new ArrayList<ClassTag>();

        resetToDefault();
    }

    public void resetToDefault() {
        this.classTags
                .add(new ClassTag("version", "$" + "Id" + "$",
                        ClassTag.REPLACE_MODE.DONT_ADD, true, true));
        addIfDocDiffers = false;

        removeIfDocMatch = true;
        addInheritDocIfNoDoc = false;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public boolean getAddIfDocDiffers() {
        return addIfDocDiffers;
    }

    public void setAddIfDocDiffers(boolean addIfDocDiffers) {
        this.addIfDocDiffers = addIfDocDiffers;
    }


    public List<ClassTag> getClassTags() {
        return classTags;
    }

    public void setClassTags(List<ClassTag> classTags) {
        this.classTags = classTags;
    }

    public boolean getAddInheritDocIfNoDoc() {
        return addInheritDocIfNoDoc;
    }

    public void setAddInheritDocIfNoDoc(boolean addInheritDocIfNoDoc) {
        this.addInheritDocIfNoDoc = addInheritDocIfNoDoc;
    }

    public boolean getRemoveIfDocMatch() {
        return removeIfDocMatch;
    }

    public void setRemoveIfDocMatch(boolean removeIfDocMatch) {
        this.removeIfDocMatch = removeIfDocMatch;
    }
}
