package osmedile.intellij.javadochelper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import osmedile.intellij.util.action.GroupWriteAction;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * tag: version, value Id
 * <p/>
 * if one tag with same name,                       add | don't add | replace all
 * if one tag with same name and value,             don't add
 *
 * @author Olivier Smedile
 * @version $Id: AddClassTag.java 67 2008-09-27 16:27:15Z osmedile $
 */
public class AddCopyright extends GroupWriteAction {
    public static final String ADD_COPYRIGHT_TAG_ID =
            "osmedile.intellij.javadochelper.AddCopyrightTag";

    private List<ClassTag> tags;

    public AddCopyright() {
        tags = new ArrayList<ClassTag>();
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public List<ClassTag> getTags() {
        return tags;
    }

    public void setTags(List<ClassTag> tags) {
        this.tags = tags;
    }

// --------------------- METHODS ---------------------


    private final String COPYRIGHT = "/* \n" +
            " * Copyright (C) 2015 team-cachebox.de\n" +
            " *\n" +
            " * Licensed under the : GNU General Public License (GPL);\n" +
            " * you may not use this file except in compliance with the License.\n" +
            " * You may obtain a copy of the License at\n" +
            " *\n" +
            " *      http://www.gnu.org/licenses/gpl.html\n" +
            " *\n" +
            " * Unless required by applicable law or agreed to in writing, software\n" +
            " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " * See the License for the specific language governing permissions and\n" +
            " * limitations under the License.\n" +
            " */";


    @Override
    public void actionOn(DataContext dataContext, Project project,
                         PsiManager psiManager, PsiJavaFile psiJavaFile) {

        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiElement firstChild = psiJavaFile.getFirstChild();

        if (firstChild instanceof PsiComment) {
            System.out.print(firstChild.getText());
//TODO has Cpoyright, maybe modify year

        }

        if (firstChild instanceof PsiPackageStatement) {
            //create and add Copyright header
            PsiComment psiComment = factory.createCommentFromText(COPYRIGHT, firstChild.getParent());
            firstChild.getParent().addBefore(psiComment, firstChild);
        }
    }
}
