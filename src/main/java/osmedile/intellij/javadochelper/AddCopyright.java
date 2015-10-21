package osmedile.intellij.javadochelper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.util.IncorrectOperationException;
import osmedile.intellij.util.action.GroupWriteAction;
import osmedile.intellij.util.psi.PsiDocTagUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static osmedile.intellij.javadochelper.ClassTag.REPLACE_MODE.*;

/**
 * <p/>
 * tag: version, value Id
 * <p/>
 * if one tag with same name,                       add | don't add | replace all
 * if one tag with same name and value,             don't add
 *
 * @author Olivier Smedile
 * @author Longri 2015
 * @version $Id: AddClassTag.java 67 2008-09-27 16:27:15Z osmedile $
 */
public class AddCopyright extends GroupWriteAction {
    public static final String ADD_COPYRIGHT_TAG_ID =
            "osmedile.intellij.javadochelper.AddCopyrightTag";

    private List<ClassTag> tags;

    public AddCopyright() {
        tags = new ArrayList<ClassTag>();
        tags.add(new ClassTag("author", "Longri", ClassTag.REPLACE_MODE.ADD, true, false));

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

        //find first element, skip WhiteSpace
        while (firstChild instanceof PsiWhiteSpace) {
            firstChild = firstChild.getNextSibling();
        }

        if (firstChild instanceof PsiComment) {
            System.out.print(firstChild.getText());
//TODO has Cpoyright, maybe modify year

        }

        if (firstChild instanceof PsiPackageStatement) {
            //create and add Copyright header
            PsiComment psiComment = factory.createCommentFromText(COPYRIGHT, firstChild.getParent());
            firstChild.getParent().addBefore(psiComment, firstChild);
        }

        addAuthorTag(dataContext, project, psiManager, psiJavaFile);

    }


    private void addAuthorTag(DataContext dataContext, Project project,
                              PsiManager psiManager, PsiJavaFile psiJavaFile) {


        //blame file with GIT command
        // git shortlog -s -p "filename"

        String failePath = psiJavaFile.getOriginalFile().getVirtualFile().getPath();
        String projectPath = new File(failePath).getParent();
        try {
            //runCommand(new File(projectPath), "git", "shortlog", "-s", "-p", failePath );
          //  runCommand(new File(projectPath), "cmd", "/c start dir");


            //runCommand(new File(projectPath), "git", " shortlog -s");

           // runCommand(new File(projectPath), "git", "--help", "-a");

         //   runCommand(new File(projectPath), "git", "--help", "-g");

            runCommand(new File(projectPath), "git", "shortlog", "-s");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        PsiClass[] psiClasses = psiJavaFile.getClasses();
        for (PsiClass psiClass : psiClasses) {
            try {
                PsiDocComment docComment = psiClass.getDocComment();

                boolean addDocComment = false;
                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                if (docComment == null) {
                    docComment =
                            factory.createDocCommentFromText(FinalStrings.COMMENT_TEMPLATE);
                    addDocComment = true;
                }

                /**
                 * Ref to an existantTag with same name.
                 */
                PsiDocTag existantTag;

                /**
                 * true if one tag with same name and same value already exists
                 */
                boolean existantTagEquals;


                for (ClassTag tag : tags) {
                    //skip inactive tag
                    if (!tag.getActive()) {
                        continue;
                    }
                    //remove first char "@"
                    String tagName = tag.getName().substring(1);
                    String tagValueTrim = tag.getValue().trim();

                    //existant tag with same name and perhaps same value
                    existantTag = null;
                    existantTagEquals = false;

                    //If not a new javadoc comment and do not replace tag value
                    if (!addDocComment) {
                        PsiDocTag[] docTags =
                                docComment.findTagsByName(tagName);
                        if (docTags.length > 0) {
                            existantTag = docTags[0];
                        }
                        for (int i = 0;
                             !existantTagEquals && i < docTags.length; i++) {

                            final String val =
                                    PsiDocTagUtil.extractTagValue(docTags[i])
                                            .trim();
                            if (tag.getIgnoreCase()) {
                                existantTagEquals =
                                        val.toLowerCase().contains(tagValueTrim.toLowerCase());
                            } else {
                                existantTagEquals =
                                        val.contains(tagValueTrim);
                            }
                            if (existantTagEquals) {
                                existantTag = docTags[i];
                            }
                        }
                    }

//                    DONT_ADD: existantTag != null
//                    ADD: !existantTagEquals
//                    REPLACE_ALL: !existantTagEquals

                    if (addDocComment
                            || REPLACE_ALL.equals(tag.getReplaceMode())
                            || (ADD.equals(tag.getReplaceMode()) &&
                            !existantTagEquals)
                            || (DONT_ADD.equals(tag.getReplaceMode()) &&
                            existantTag == null)) {

                        PsiDocTag newTag = factory.createDocTagFromText(
                                "@" + tagName.replaceAll("@", "") + " " +
                                        tag.getValue());


                        if (REPLACE_ALL.equals(tag.getReplaceMode()) &&
                                existantTag != null) {

                            //need to remove all existant tags
                            //first search tags
                            PsiDocTag[] docTags =
                                    docComment.findTagsByName(tagName);

                            //then add the newTag (this tag won't be
                            // included in previous search)
                            docComment.addBefore(newTag, existantTag);

                            //Finaly remove all previous tags
                            for (PsiDocTag docTag : docTags) {
                                docComment.deleteChildRange(docTag, docTag);
                            }
                        } else {
                            docComment.add(newTag);
                        }
                    }
                }

                if (addDocComment) {
                    psiJavaFile.addBefore(docComment, psiClass);
                }
            } catch (IncorrectOperationException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    public static void runCommand(File directory, String... command) throws IOException, InterruptedException {

        try {
            ProcessBuilder pb = new ProcessBuilder(command)
                    .redirectErrorStream(true).directory(directory);


//            Map<String, String> envs = pb.environment();
//            envs.put("Path", "C:/Program Files (x86)/Git/cmd");

            Process p = pb.start();

            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String in;

            boolean timeOut = false;

            if (!p.waitFor(6, TimeUnit.SECONDS)) {
                //timeout - kill the process.
                p.destroy(); // consider using destroyForcibly instead
                timeOut = true;
            }
            if (!timeOut) {
                while ((in = br.readLine()) != null) {
                    System.out.println(in);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


//        ProcessBuilder pb = new ProcessBuilder()
//                .command(command)
//                .directory(directory);
//        Process p = pb.start();
//        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
//        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
//        outputGobbler.start();
//        errorGobbler.start();
//        int exit = p.waitFor();
//        errorGobbler.join();
//        outputGobbler.join();
//        if (exit != 0) {
//            throw new AssertionError(String.format("runCommand returned %d", exit));
//        }
    }

    private static class StreamGobbler extends Thread {

        InputStream is;
        String type;

        private StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(type + "> " + line);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


}
