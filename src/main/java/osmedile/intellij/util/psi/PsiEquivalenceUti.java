package osmedile.intellij.util.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiWhiteSpace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Olivier Smedile
 * @version $Id: PsiEquivalenceUti.java 15 2008-06-24 13:00:22Z osmedile $
 */
public class PsiEquivalenceUti {

    public static boolean areElementsEquivalent(@NotNull PsiElement element1,
                                                @NotNull PsiElement element2,
                                                @Nullable Comparator<PsiElement> resolvedElementsComparator,
                                                boolean areCommentsSignificant) {
        if (element1 == element2) {
            return true;
        }
        ASTNode node1 = element1.getNode();
        ASTNode node2 = element2.getNode();
        if (node1 == null || node2 == null) {
            return false;
        }
        if (node1.getElementType() != node2.getElementType()) {
            return false;
        }

        PsiElement[] children1 =
                getFilteredChildren(element1, areCommentsSignificant);
        PsiElement[] children2 =
                getFilteredChildren(element2, areCommentsSignificant);

        //Modif from IDEA
        //remove "*" text
        List<PsiElement> childs = new ArrayList<PsiElement>();
        for (PsiElement child1 : children1) {
            if (!"*".equals(child1.getText().trim())) {
                childs.add(child1);
            }
        }
        children1 = childs.toArray(new PsiElement[childs.size()]);

        childs.clear();
        for (PsiElement child2 : children2) {
            if (!"*".equals(child2.getText().trim())) {
                childs.add(child2);
            }
        }
        children2 = childs.toArray(new PsiElement[childs.size()]);

        if (children1.length != children2.length) {
            return false;
        }


        for (int i = 0; i < children1.length; i++) {
            PsiElement child1 = children1[i];
            PsiElement child2 = children2[i];
            if (!areElementsEquivalent(child1, child2,
                    resolvedElementsComparator, areCommentsSignificant)) {
                return false;
            }
        }

        if (children1.length == 0) {
            //Modif from original IDEA code,
            //Trim and compare without case
            if (!element1.getText().trim()
                    .equalsIgnoreCase(element2.getText().trim())) {
                return false;
            }
        }

        PsiReference ref1 = element1.getReference();
        if (ref1 != null) {
            PsiReference ref2 = element2.getReference();
            if (ref2 == null) {
                return false;
            }
            PsiElement resolved1 = ref1.resolve();
            PsiElement resolved2 = ref2.resolve();
            if (!Comparing.equal(resolved1, resolved2)
                    && (resolvedElementsComparator == null ||
                    resolvedElementsComparator.compare(resolved1, resolved2) !=
                            0)) {
                return false;
            }
        }
        return true;

    }

    public static boolean areElementsEquivalent(@NotNull PsiElement element1,
                                                @NotNull PsiElement element2) {
        return areElementsEquivalent(element1, element2, null, false);
    }


    public static PsiElement[] getFilteredChildren(PsiElement element1,
                                                   boolean areCommentsSignificant) {
        ASTNode[] children1 = element1.getNode().getChildren(null);
        ArrayList<PsiElement> array = new ArrayList<PsiElement>();
        for (ASTNode node : children1) {
            final PsiElement child = node.getPsi();
            if (!(child instanceof PsiWhiteSpace) && (areCommentsSignificant ||
                    !(child instanceof PsiComment))) {
                array.add(child);
            }
        }
        return array.toArray(new PsiElement[array.size()]);
    }
}
