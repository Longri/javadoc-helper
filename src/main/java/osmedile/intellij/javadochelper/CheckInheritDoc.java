package osmedile.intellij.javadochelper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.IncorrectOperationException;
import osmedile.intellij.util.action.GroupWriteAction;
import osmedile.intellij.util.psi.PsiEquivalenceUti;
import osmedile.intellij.util.psi.PsiParameterComparator;

/**
 * @author Olivier Smedile
 * @version $Id: CheckInheritDoc.java 67 2008-09-27 16:27:15Z osmedile $
 */
public class CheckInheritDoc extends GroupWriteAction {
    public static final String CHECK_INHERIT_DOC_ID =
            "osmedile.intellij.javadochelper.CheckInheritDoc";

    public static final PsiParameterComparator ELTS_COMPARATOR =
            new PsiParameterComparator();


    public static final String INHERIT_DOC = "{@inheritDoc}";
    public static final String INHERIT_DOC_COMMENT =
            "/**\n* " + INHERIT_DOC + "\n*/";


    /**
     * If true INHERIT_DOC is added even if there is ONE comment
     */
    private boolean addIfNonEmpty;


    /**
     * If true javadoc is removed if it matches parent javadoc
     */
    private boolean removeIfDocMatch;

    /**
     * if true {@inheritDoc} is added if there is no javadoc
     */
    private boolean addInheritDocIfNoDoc;

// --------------------- GETTER / SETTER METHODS ---------------------

    public boolean getRemoveIfDocMatch() {
        return removeIfDocMatch;
    }

    public void setRemoveIfDocMatch(boolean removeIfDocMatch) {
        this.removeIfDocMatch = removeIfDocMatch;
    }

    public boolean getAddInheritDocIfNoDoc() {
        return addInheritDocIfNoDoc;
    }

    public void setAddInheritDocIfNoDoc(boolean addInheritDocIfNoDoc) {
        this.addInheritDocIfNoDoc = addInheritDocIfNoDoc;
    }

    public boolean getAddIfNonEmpty() {
        return addIfNonEmpty;
    }

    public void setAddIfNonEmpty(boolean addIfNonEmpty) {
        this.addIfNonEmpty = addIfNonEmpty;
    }

// --------------------- METHODS ---------------------

    @Override
    public void actionOn(DataContext dataContext, Project project,
                         PsiManager psiManager, PsiMethod method) {
        if (!method.isConstructor() && method.findSuperMethods().length > 0) {
            try {
                PsiDocComment currentComment = method.getDocComment();
                PsiElementFactory factory = psiManager.getElementFactory();


                PsiDocComment inheritComment =
                        factory.createDocCommentFromText(
                                INHERIT_DOC_COMMENT, currentComment);

                if (currentComment == null) {
                    if (getAddInheritDocIfNoDoc()) {
                        method.getContainingClass()
                                .addBefore(inheritComment, method);
                    }
                    //else don't do anything
                } else {

                    //only compare javadoc with direct parent method.
                    PsiMethod parentMeth = method.findSuperMethods()[0];

                    //First try to see if javadoc only contains {@inheritDoc}
                    if (getRemoveIfDocMatch() && !getAddInheritDocIfNoDoc() &&
                            PsiEquivalenceUti
                                    .areElementsEquivalent(currentComment,
                                            inheritComment, ELTS_COMPARATOR,
                                            false)) {
                        //remove current comment
                        currentComment.delete();
                    } else
                        //See if javadoc of parent and current comment are equals
                        if (parentMeth.getDocComment() != null &&
                                PsiEquivalenceUti
                                        .areElementsEquivalent(currentComment,
                                                parentMeth.getDocComment(),
                                                ELTS_COMPARATOR,
                                                false)) {
                            //at this point we are sure that javadocs are equals

                            if (getAddInheritDocIfNoDoc()) {
                                // replace current with inherit
                                currentComment.replace(inheritComment);
                            } else if (getRemoveIfDocMatch()) {
                                //remove current comment
                                currentComment.delete();
                            }
                        } else if (StringUtil.isEmpty(currentComment.getText())
                                || (getAddIfNonEmpty() &&
                                !currentComment.getText()
                                        .contains(INHERIT_DOC))) {


                            PsiElement[] elts = currentComment.getChildren();
                            PsiElement[] commentToAdds =
                                    inheritComment.getChildren();
                            if (elts.length >= 2) {
                                for (int i = 1; i < commentToAdds.length - 2;
                                     i++) {
                                    currentComment
                                            .addBefore(commentToAdds[i],
                                                    elts[1]);
                                }
                            } else {
                                for (int i = 1; i < commentToAdds.length - 1;
                                     i++) {
                                    currentComment.add(commentToAdds[i]);
                                }
                            }
                        }
                }
            } catch (IncorrectOperationException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}