package nz.ac.massey.se.dynLangInSStuBs.tasks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nz.ac.massey.se.dynLangInSStuBs.BugData;
import nz.ac.massey.se.dynLangInSStuBs.Keyword;
import nz.ac.massey.se.dynLangInSStuBs.ReflectiveAPI;
import nz.ac.massey.se.dynLangInSStuBs.logger.MyLogger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * checkout each cloned repository, and retrieve bug related source code.
 * Note that this will run for days
 *
 * obtain bugs happen before the fix commit
 * @author Li Sui
 */
public class SourceCodeRetriever {
    static final String projectsDir="/home/lsui/projects/allJavaProjects";
    static final String outputDir="/home/lsui/projects/allsroucecode2";
    static final File sstubsFile=  new File("results/sstubs.json");
    static final File reflectiveAPIFile= new File("src/main/resources/ReflectiveAPIs.json");
    //sleep for 1 second between each git checkout
    private static final int sleep=1000;

    public static void main(String[] args) throws Exception{
        Gson gson =new Gson();
        String bugContent = FileUtils.readFileToString(sstubsFile, Charset.defaultCharset());
        String reflectContent= FileUtils.readFileToString(reflectiveAPIFile, Charset.defaultCharset());
        List<BugData> reportList= gson.fromJson(bugContent ,new TypeToken<List<BugData>>(){}.getType());
        List<ReflectiveAPI> reflectiveAPIList=gson.fromJson(reflectContent,new TypeToken<List<ReflectiveAPI>>(){}.getType());
        Set<String> keywordSet= new HashSet<>();
        for(ReflectiveAPI api: reflectiveAPIList){
            for(Keyword keyword:api.getKeywords()){
                keywordSet.add(keyword.getCallsite());
            }
        }


        int count=0;
        for(BugData bugReport: reportList) {
            String beforeFix = bugReport.getSourceBeforeFix();
            String afterFix = bugReport.getSourceAfterFix();
            String parentCommit = bugReport.getFixCommitParentSHA1();
            String bugFilePath = bugReport.getBugFilePath();
            count++;
            MyLogger.RETRIEVESC.info("processing "+count+ " bug instance/104337");
            innerloop:
            for (String keyword : keywordSet) {
                if (beforeFix.contains(keyword)) {
                    MyLogger.RETRIEVESC.info("found a dynamic language ("+keyword+") related bugs at "+bugFilePath);
                     retrieveSource(bugReport.getProjectName(), parentCommit, bugFilePath);
                    break innerloop;
                }
            }
        }

    }

    public static void retrieveSource(String projectName, String commit,String bugFilePath) throws Exception{
        String sourceCode=null;
        String fileName=null;
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist( true );
        repositoryBuilder.setGitDir(new File(projectsDir+"/"+projectName+"/.git"));
        Repository repository= repositoryBuilder.build();
        Git git =new Git(repository);
        try {
            git.checkout().setName(commit).call();
        }catch (Exception e){
            MyLogger.RETRIEVESC.error("error in "+bugFilePath +" at commit:"+commit +" :"+e.getMessage());
            MyLogger.RETRIEVESC.error(e);
        }
        Thread.sleep(sleep);

        RevCommit latestCommit = new Git(repository).log().setMaxCount(1).call().iterator().next();
        if(latestCommit.getName().equals(commit)) {
            File file =new File(projectsDir+"/"+projectName+"/" + bugFilePath);
            fileName=file.getName();
            sourceCode = FileUtils.readFileToString(file, Charset.defaultCharset());
        }else {
            MyLogger.RETRIEVESC.error("the current commit does not match:"+commit +" for"+bugFilePath);
        }

        if(sourceCode!=null){
            File dir= new File(outputDir+"/"+projectName);
            if(!dir.exists()){
                dir.mkdirs();
            }
            String name=commit+"."+fileName;
            File file=new File(outputDir+"/"+projectName+"/"+name);
            try {
                FileUtils.writeStringToFile(file, sourceCode, Charset.defaultCharset());
            }catch (FileNotFoundException e){
                MyLogger.RETRIEVESC.error("error in create a local copy:"+e.getMessage());
            }
        }

    }
}
