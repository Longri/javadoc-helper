package osmedile.intellij.javadochelper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsModifierListImpl;
import com.intellij.util.IncorrectOperationException;
import osmedile.intellij.util.action.GroupWriteAction;
import osmedile.intellij.util.psi.MethodUtil;

/**
 * @author Olivier Smedile
 * @version $Id: AddOverride.java 15 2008-06-24 13:00:22Z osmedile $
 */
public class AddOverride extends GroupWriteAction {


    @Override
    public void actionOn(DataContext dataContext, Project project,
                         PsiManager psiManager, PsiMethod method) {
        if (!method.isConstructor() && MethodUtil.override(method)) {
            try {

                final PsiModifierList modifiers = method.getModifierList();

                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                PsiAnnotation annot =
                        factory.createAnnotationFromText("@Override", null);

                boolean contains = false;
                for (PsiAnnotation an : modifiers.getAnnotations()) {
                    if (annot.getText().equals(an.getText())) {
                        contains = true;
                    }

                }

                if (contains == false) {
                    try {
                        if (modifiers.getChildren().length > 0) {
                            modifiers.addBefore(annot, modifiers.getChildren()[0]);
                        } else {
                            modifiers.add(annot);
                        }
                    } catch (IncorrectOperationException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IncorrectOperationException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}