package osmedile.intellij.javadochelper;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Olivier Smedile
 * @version $Id: JavadocHelperAppComp.java 68 2008-09-28 18:26:14Z osmedile $
 */
@State(
        name = "JavadocHelper",
        storages = {@Storage(id = "JavadocHelper",
                file = "$OPTIONS$/JavadocHelper.xml")}
)
public class JavadocHelperAppComp implements ApplicationComponent, Configurable,
        PersistentStateComponent<ConfigImpl> {


    private ConfigImpl config = new ConfigImpl();


    private JHConfig form;


    @Nls
    public String getDisplayName() {
        return "JavadocHelper";
    }


    @Nullable
    public Icon getIcon() {
        Icon ico = IconLoader.findIcon("cb_48x48_icon.png", this.getClass());;
        return ico;
    }


    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }


    public JComponent createComponent() {
        if (form == null) {
            form = new JHConfig();
        }
        return form.getRootComponent();
    }


    public boolean isModified() {
        return form != null && form.isModified(this.config);
    }

    public void apply() throws ConfigurationException {
        if (form != null) {
            form.getData(config);
            configure();
        }
    }

    public void reset() {
        if (form != null) {
            form.setData(this.config);
        }
    }

    public void disposeUIResources() {
        form = null;
    }

    protected void configure() {
        AddClassTag addClassTag = (AddClassTag) ActionManager.getInstance()
                .getAction(AddClassTag.ADD_CLASS_TAG_ID);
        addClassTag.setTags(config.getClassTags());

        CheckInheritDoc checkInheritDoc =
                (CheckInheritDoc) ActionManager.getInstance()
                        .getAction(CheckInheritDoc.CHECK_INHERIT_DOC_ID);
        checkInheritDoc
                .setAddInheritDocIfNoDoc(config.getAddInheritDocIfNoDoc());
        checkInheritDoc.setAddIfNonEmpty(config.getAddIfDocDiffers());
        checkInheritDoc.setRemoveIfDocMatch(
                config.getRemoveIfDocMatch());
    }


    public void initComponent() {
        configure();
    }


    public void disposeComponent() {
    }


    @NotNull
    public String getComponentName() {
        return "JavadocHelperAppComp";
    }


    public ConfigImpl getState() {
        return config;
    }


    public void loadState(ConfigImpl state) {
       // XmlSerializerUtil.copyBean(state, config);
    }
}
