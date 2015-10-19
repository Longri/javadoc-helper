package osmedile.intellij.util.psi;

import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocToken;

/**
 * @author Olivier Smedile
 * @version $Id: PsiDocTagUtil.java 61 2008-07-31 06:23:03Z osmedile $
 */
public class PsiDocTagUtil {

    public static String extractTagValue(PsiDocTag docTag) {
        StringBuilder resp = new StringBuilder();
        PsiElement[] childs = docTag.getChildren();

        boolean endAsteriskReached = false;
        //+1 to skip name of the docTag
        //+1 to skip whitespace
        for (int i = 2; !endAsteriskReached && i < childs.length; i++) {
            PsiElement child = childs[i];
            if ((child instanceof PsiDocToken && ((PsiDocToken) child)
                    .getTokenType() == JavaDocTokenType
                    .DOC_COMMENT_LEADING_ASTERISKS)
                    || (child instanceof PsiWhiteSpace &&
                    child.getText().startsWith("\n"))) {
                endAsteriskReached = true;
//            } else if(child instanceof com.intellij.psi.PsiWhiteSpace){
//                resp.append(child.getText())
            } else {
                resp.append(child.getText());

            }
        }

        return resp.toString();
    }
}
