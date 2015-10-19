package osmedile.intellij.util.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;

import java.util.Comparator;

/**
 * @author Olivier Smedile
 * @version $Id: File Header.java 3 2008-03-11 08:52:55Z osmedile $
 */
public class PsiParameterComparator implements Comparator<PsiElement> {

    public int compare(PsiElement o1, PsiElement o2) {
        if (o1 instanceof PsiParameter && o2 instanceof PsiParameter) {
            PsiParameter p1 = (PsiParameter) o1;
            PsiParameter p2 = (PsiParameter) o2;
            return p1.getText().compareTo(p2.getText());
        }
        return -1;
    }
}
