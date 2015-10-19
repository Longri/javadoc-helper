package osmedile.intellij.util.action;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;

/**
 * @author Olivier Smedile
 * @version $Id: CommandUtil.java 70 2008-09-28 18:32:38Z osmedile $
 */
public class CommandUtil {


    public static void doWriteAction(Project project, final Runnable action, String commandName) {
        CommandProcessor commandProcessor = CommandProcessor.getInstance();

        final Runnable command = new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(action);
            }
        };

        commandProcessor.executeCommand(project, command, commandName, null);
    }
}
