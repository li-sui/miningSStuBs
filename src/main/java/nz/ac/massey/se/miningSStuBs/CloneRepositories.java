package nz.ac.massey.se.miningSStuBs;

import com.google.common.base.Preconditions;
import nz.ac.massey.se.miningSStuBs.logger.MyLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.nio.charset.Charset;

/**
 * clone repositories from list of projects in a csv file.
 * need specify output dir: -outputDir /home/{user}/downloads
 * @author Li Sui
 */
public class CloneRepositories {
    //list of projects from Github.
    private static final File JAVA_PROJECTS_LIST_FILE=new File("src/main/resources/allJavaProjects.csv");
    public static void main(String[] args) throws Exception{

        Preconditions.checkState(JAVA_PROJECTS_LIST_FILE.exists());
        Options cliOptions = new Options().addOption("outputDir",true,"directory that projects are cloned to");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( cliOptions, args);
        if(!cmd.hasOption("outputDir")){
            System.err.println("e.g. -outputDir /home/{user}/downloads");
            return;
        }
        String clonedDir=cmd.getOptionValue("outputDir");
        MyLogger.CLONEREPO.info("cloning to dir: "+clonedDir);
        String[] projects= FileUtils.readFileToString(JAVA_PROJECTS_LIST_FILE,
                Charset.defaultCharset()).split("\\n");
        int count=0;
        for (String line : projects) {
            String url =line.split(",")[0];
            String community=line.split(",")[1];
            String projectName=community+"."+line.split(",")[2];
            //skip already cloned repo
            File file = new File(clonedDir +"/"+ projectName);
            if (!file.exists()) {
                MyLogger.CLONEREPO.info("cloning: " + projectName);
                count++;
                try {
                    gitClone(url, clonedDir,projectName);
                }catch (Exception e){
                   MyLogger.CLONEREPO.error("something wrong with cloning:"+projectName);
                   MyLogger.CLONEREPO.error(e.getMessage());
                   MyLogger.CLONEREPO.error(count+" has been cloned");
                }
            }
        }
       MyLogger.CLONEREPO.info(count+ " cloned projects");
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
