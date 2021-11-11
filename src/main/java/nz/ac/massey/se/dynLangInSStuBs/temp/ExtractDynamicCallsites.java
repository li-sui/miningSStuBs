package nz.ac.massey.se.dynLangInSStuBs.temp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nz.ac.massey.se.dynLangInSStuBs.BugData;
import nz.ac.massey.se.dynLangInSStuBs.Keyword;
import nz.ac.massey.se.dynLangInSStuBs.ReflectiveAPI;
import nz.ac.massey.se.dynLangInSStuBs.tasks.SourceCodeRetriever;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * required SStuBsMiner to be run first
 * @author Li Sui
 */
public class ExtractDynamicCallsites {
    //path to bug file. TODO:configurable
    static final File sstubsFile=  new File("/home/lsui/projects/miningSStuBs/results/bugs.json");
    static final File reflectiveAPIFile= new File("src/main/resources/ReflectiveAPIs.json");


    public static void main(String[] args) throws Exception{
        Gson gson =new Gson();
        String bugContent = FileUtils.readFileToString(sstubsFile, Charset.defaultCharset());
        String reflectContent= FileUtils.readFileToString(reflectiveAPIFile, Charset.defaultCharset());
        List<BugData> reportList= gson.fromJson(bugContent ,new TypeToken<List<BugData>>(){}.getType());
        List<ReflectiveAPI> reflectiveAPIList=gson.fromJson(reflectContent,new TypeToken<List<ReflectiveAPI>>(){}.getType());
        int total=0;
        Set<String> projects=new HashSet<>();
        Set<String> commits=new HashSet<>();
        List<String> listKeywords= new ArrayList<>();

        for(ReflectiveAPI api: reflectiveAPIList){
            for(Keyword keyword:api.getKeywords()){
                listKeywords.add(keyword.getCallsite());
            }
        }
        int count=0;
        for(BugData bugReport: reportList){

            String beforeFix= bugReport.getSourceBeforeFix();
            String afterFix=bugReport.getSourceAfterFix();
//            String projectName=bugReport.getProjectName();
//            String commit=bugReport.getFixCommitSHA1();
            String parentCommit=bugReport.getFixCommitParentSHA1();
            String bugFilePath=bugReport.getBugFilePath();
//            String projectUrl="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/");
//            String fixUrl="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/")+"/commit/"
//                    +bugReport.getFixCommitSHA1();
//            String bugSourceCode="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/")+"/commit/"
//                    +bugReport.getFixCommitParentSHA1()+"/"+bugReport.getBugFilePath();
            count++;
            System.out.println("processing bug "+count+"/249,089");

            innerloop:
            for (String keyword : listKeywords) {
                if (beforeFix.contains(keyword) || afterFix.contains(keyword)) {
//                    total=total+1;
//                    projects.add(projectName);
//                    commits.add(commit);
                    String source = SourceCodeRetriever.retrieveSource(bugReport.getProjectName(), parentCommit, bugFilePath);
                    if (source != null) {
                        SourceCodeRetriever.createLocalCopy(bugReport.getProjectName(), parentCommit, source, bugFilePath);
                    }
                    break innerloop;
                }
            }




//                        total=total+1;
//                        projects.add(projectName);
//

        }
//        System.out.println(total);
//        System.out.println(commits.size());
//        System.out.println(projects.size());
//
//        int totalBugs=0;
//        for(BugData bugReport: reportList){
//
//            if(projects.contains(bugReport.getProjectName())){
//                totalBugs++;
//            }
//        }
//        System.out.println(totalBugs);
//        System.out.println("invoke: " +invokeList.size());
//        System.out.println("getDeclaredMethod: " +getDeclaredMethodList.size());
//        System.out.println("newInstance: " +newInstanceList.size());
//        System.out.println("forName: " +forNameList.size());
//        System.out.println("getDeclaredConstructor: " +getDeclaredConstructorList.size());
//        System.out.println("getDeclaredField: " +getDeclaredFieldList.size());
//        System.out.println("getClassLoader: " +getClassLoaderList.size());
//        System.out.println("findClass: " +findClassList.size());
//        System.out.println("defineClass: " +defineClassList.size());
//        System.out.println("loadClass: " +loadClassList.size());
//        System.out.println("readObject: " +readObjectList.size());
//        System.out.println("allocateInstance: " +allocateInstanceList.size());
//        System.out.println("getInvocationHandler: " +getInvocationHandlerList.size());
//        System.out.println("newProxyInstance: " +newProxyInstanceList.size());
//        System.out.println("ServiceLoader: " +serviceLoaderList.size());
//        System.out.println("--------------------------");
//        System.out.println("number of projects: "+projectSet.size());

//        System.out.println("--------------------------");
//        for(String line: totalFixedCommitSet){
//            System.out.println(line);
//        }
    }


}
