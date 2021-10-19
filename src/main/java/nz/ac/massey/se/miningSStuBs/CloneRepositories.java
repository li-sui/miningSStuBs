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
    static final String projectsFilePath="allJavaProjects.csv";
    //directory that projects are cloned to. TODO:configurable
    static final String clonedDir="/home/lsui/projects/allJavaProjects";

    public static void main(String[] args) throws Exception{
        String[] projects= FileUtils.readFileToString(new File(projectsFilePath),
                Charset.defaultCharset()).split("\\n");
        int count=0;
        for (String line : projects) {
            String url =line.split(",")[0];
            String community=line.split(",")[1];
            String projectName=community+"."+line.split(",")[2];
            //skip already cloned repo
            File file = new File(clonedDir +"/"+ projectName);
            if (!file.exists()) {
                System.out.println("cloning: " + projectName);
                count++;
                try {
                    gitClone(url, clonedDir,projectName);
                }catch (Exception e){
                    e.printStackTrace();
                    System.err.println(count+" has been cloned");
                }
            }
        }
        System.out.println("numbers of cloned projects:"+count);
    }

    public static void gitClone(String url, String outputDir, String name) throws GitAPIException {
        File repoDir= new File(outputDir+"/"+name);
        if(!repoDir.exists() && repoDir.isDirectory()){
            repoDir.mkdirs();
        }
        Git.cloneRepository()
                .setURI(url)
                .setDirectory(repoDir)
                .call();
    }
}
