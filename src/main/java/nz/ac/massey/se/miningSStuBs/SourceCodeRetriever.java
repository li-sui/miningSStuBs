package nz.ac.massey.se.miningSStuBs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

public class SourceCodeRetriever {
    static final String projectsDir="/home/lsui/projects/allJavaProjects";
    static final String outputDir="/home/lsui/projects/allSourceCode";

    public static String retrieveSource(String projectName, String commit,String bugFilePath) throws Exception{
        String sourceCode="";
        File repoDir= new File(projectsDir+"/"+projectName);
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist( true );
        repositoryBuilder.setGitDir(new File(projectsDir+"/"+projectName+"/.git"));
        Repository repository= repositoryBuilder.build();
        Git git =new Git(repository);
        try {
            git.checkout().setName(commit).call();
        }catch (Exception e){
           return null;
        }
        Thread.sleep(1000);

        RevCommit latestCommit = new Git(repository).log().setMaxCount(1).call().iterator().next();
        if(latestCommit.getName().equals(commit)) {
            sourceCode = FileUtils.readFileToString(new File(projectsDir+"/"+projectName+"/" + bugFilePath), Charset.defaultCharset());
        }else {
            System.err.println("error, the HEAD does not match the commit");
            //System.exit(0);
        }
        return sourceCode;
    }

    public static void createLocalCopy(String projectName, String commit,String soureCode, String bugFilePath) throws Exception{
        File dir= new File(outputDir+"/"+projectName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String fileName=bugFilePath.replace("/",".");
        String name=commit+"."+fileName;
        File file=new File(outputDir+"/"+projectName+"/"+name);
        try {
            FileUtils.writeStringToFile(file, soureCode, Charset.defaultCharset());
        }catch (FileNotFoundException e){
            file =new File(outputDir+"/"+projectName+"/"+commit+".fileNameTooLong.java");
            FileUtils.writeStringToFile(file, soureCode, Charset.defaultCharset());
        }
    }
}
