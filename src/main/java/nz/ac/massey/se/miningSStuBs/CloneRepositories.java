package nz.ac.massey.se.miningSStuBs;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.nio.charset.Charset;

/**
 * clone repositories from list of projects in a csv file.
 * @author Li Sui
 */
public class CloneRepositories {
    //list of projects from Github.
    static final String projectsFilePath="apache.projects.noDup_15_02_2020.csv";
    //directory that projects are cloned to.
    static final String clonedDir="/home/lsui/projects/PilotExperiments/SStuBs/Apache-projects";

    static final String repoPrefix="https://github.com/apache";

    public static void main(String[] args) throws Exception{
        String[] projects= FileUtils.readFileToString(new File(projectsFilePath),
                Charset.defaultCharset()).split("\\n");
        int count=0;
        for (String project : projects) {
            //skip already cloned repo
            File file = new File(clonedDir +"/"+ project);
            if (!file.exists()) {
                System.out.println("cloning: " + project);
                count++;
                gitClone(clonedDir,project);
            }
        }
        System.out.println("numbers of cloned projects:"+count);
    }

    public static void gitClone(String outputDir, String name) throws GitAPIException {
        File repoDir= new File(outputDir+"/"+name);
        if(!repoDir.exists() && repoDir.isDirectory()){
            repoDir.mkdirs();
        }
        Git.cloneRepository()
                .setURI(repoPrefix+"/"+name)
                .setDirectory(repoDir)
                .call();
    }
}
