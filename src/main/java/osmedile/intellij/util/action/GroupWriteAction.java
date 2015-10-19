package osmedile.intellij.util.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;

/**
 * @author Olivier Smedile
 * @version $Id: GroupWriteAction.java 73 2008-09-29 19:22:20Z osmedile $
 */
public abstract class GroupWriteAction extends WriteAction {


    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(true);
    }

    /**
     * @param project
     * @param dataContext
     */
    public void doAction(Project project, DataContext dataContext) {
        final PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        final PsiManager psiManager = PsiManager.getInstance(project);

        final VirtualFile[] vfs = DataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);

        listFiles(dataContext, project, psiManager, vfs);


        manager.commitAllDocuments();
    }

    public void listFiles(DataContext dataContext, Project project,
                          PsiManager psiManager, VirtualFile[] vfs) {
        for (VirtualFile virtualFile : vfs) {
            if (virtualFile.isDirectory()) {
                listFiles(dataContext, project, psiManager,
                        virtualFile.getChildren());
            } else {
                PsiFile psiFile = psiManager.findFile(virtualFile);
                actionOn(dataContext, project, psiManager, psiFile);
            }
        }
    }


    public void actionOn(DataContext dataContext, Project project,
                         PsiManager psiManager, PsiFile psiFile) {
        if (psiFile instanceof PsiJavaFile) {
            actionOn(dataContext, project, psiManager, (PsiJavaFile) psiFile);
        }
    }

    public void actionOn(DataContext dataContext, Project project,
                         PsiManager psiManager, PsiJavaFile psiJavaFile) {
        PsiClass[] psiClasses = psiJavaFile.getClasses();
        for (PsiClass psiClass : psiClasses) {
            actionOn(dataContext, project, psiManager, psiClass);
        }
    }

    public void actionOn(DataContext dataContext, Project project,
                         PsiManager psiManager, PsiClass psiClass) {
        PsiMethod[] methods = psiClass.getMethods();
        for (PsiMethod method : methods) {
            actionOn(dataContext, project, psiManager, method);
        }


        PsiClass[] inners = psiClass.getAllInnerClasses();
        if (inners != null) {
            for (PsiClass inner : inners) {
                actionOn(dataContext, project, psiManager, inner);
            }
        }
    }

    public void actionOn(DataContext dataContext, Project project,
                         PsiManager psiManager, PsiMethod method) {

    }
}