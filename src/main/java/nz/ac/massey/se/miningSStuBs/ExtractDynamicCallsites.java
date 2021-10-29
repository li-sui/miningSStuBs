package nz.ac.massey.se.miningSStuBs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

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

        for(BugData bugReport: reportList){
            String beforeFix= bugReport.getSourceBeforeFix();
            String afterFix=bugReport.getSourceAfterFix();
            String projectUrl="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/");
            String fixUrl="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/")+"/commit/"
                    +bugReport.getFixCommitSHA1();
            String bugSourceCode="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/")+"/commit/"
                    +bugReport.getFixCommitParentSHA1()+"/"+bugReport.getBugFilePath();
            outerloop:
            for(ReflectiveAPI api: reflectiveAPIList){
                for(Keyword keyword:api.getKeywords()){
                    if(beforeFix.contains(keyword.getCallsite()) || afterFix.contains(keyword.getCallsite())){
                        total=total+1;
                        break outerloop;
                    }
                }
            }
        }
        System.out.println(total);
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
