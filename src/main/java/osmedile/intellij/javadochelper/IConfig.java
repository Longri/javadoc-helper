package osmedile.intellij.javadochelper;

import java.util.List;

/**
 * @author Olivier Smedile
 * @version $Id: IConfig.java 52 2008-07-29 10:32:50Z osmedile $
 */
public interface IConfig {

    boolean getAddIfDocDiffers();


    List<ClassTag> getClassTags();

    void setAddIfDocDiffers(boolean addIfNonEmpty);

    void setClassTags(List<ClassTag> classTags);

    void resetToDefault();

    boolean getAddInheritDocIfNoDoc();

    void setAddInheritDocIfNoDoc(boolean addInheritDocIfNoDoc);

    boolean getRemoveIfDocMatch();

    void setRemoveIfDocMatch(boolean removeIfDocMatch);
}
