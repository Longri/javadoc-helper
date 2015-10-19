package osmedile.intellij.util.psi;

import com.intellij.psi.PsiMethod;

/**
 * @author Olivier Smedile
 * @version $Id: MethodUtil.java 62 2008-07-31 06:31:35Z osmedile $
 */
public class MethodUtil {

    /**
     * Return true if the specified method override a method from a super class.
     *
     * @param method the method to test
     *
     * @return true if the method override another one, false otherwise
     */
    public static boolean override(PsiMethod method) {
        PsiMethod[] supMethods = method.findSuperMethods();
        boolean dontOverride = true;
        for (int i = 0; i < supMethods.length && dontOverride; i++) {
            PsiMethod psiMethod = supMethods[i];
            if (!psiMethod.getContainingClass().isInterface()) {
                dontOverride = false;
            }
        }
        return !dontOverride;
    }
}
