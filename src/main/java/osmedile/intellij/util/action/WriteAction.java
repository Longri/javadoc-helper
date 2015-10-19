package osmedile.intellij.util.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;

/**
 * @author Olivier Smedile
 * @version $Id: WriteAction.java 70 2008-09-28 18:32:38Z osmedile $
 */
public abstract class WriteAction extends AnAction {


    public void actionPerformed(AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        final Project project = DataKeys.PROJECT.getData(dataContext);

        String commandName = getCommandName();
        if (commandName == null) {
            commandName = "";
        }
        CommandUtil.doWriteAction(project, new Runnable() {
            public void run() {
                doAction(project, dataContext);
            }
        }, commandName);
    }

    public String getCommandName() {
        return getTemplatePresentation().getText();
    }

    public abstract void doAction(Project project, DataContext dataContext);
}