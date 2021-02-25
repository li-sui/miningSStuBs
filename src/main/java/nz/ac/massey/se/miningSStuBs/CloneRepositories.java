package nz.ac.massey.se.miningSStuBs;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.nio.charset.Charset;

/**
 * clone repositories for mining, list of projects needs to be extract first.
 * @author Li Sui
 */
public class CloneRepositories {
    public static void main(String[] args) throws Exception{
        String[] apacheProjects= FileUtils.readFileToString(new File("apache.projects.noDup_15_02_2020.csv"),
                Charset.defaultCharset()).split("\\n");
        int count=0;
        for (String apacheProject : apacheProjects) {
            //skip already cloned repo
            File file = new File("/home/lsui/projects/PilotExperiments/SStuBs/Apache-projects/" + apacheProject);
            if (!file.exists()) {
                System.out.println("cloning: " + apacheProject);
                gitClone(apacheProject);
            }
        }

    }

    public static void gitClone(String name) throws GitAPIException {
        File outputDir= new File("/home/lsui/projects/PilotExperiments/SStuBs/Apache-projects/"+name);
        if(!outputDir.exists() && outputDir.isDirectory()){
            outputDir.mkdirs();
        }
        Git.cloneRepository()
                .setURI("https://github.com/apache/"+name)
                .setDirectory(outputDir)
                .call();
    }
}
