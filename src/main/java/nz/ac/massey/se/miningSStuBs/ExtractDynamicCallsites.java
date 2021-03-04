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
    static final String sstubsFile= "/home/lsui/projects/PilotExperiments/SStuBs/sstubs-testing/Apache-results/sstubs.json";

    public static void main(String[] args) throws Exception{
        Gson gson =new Gson();
        String input = FileUtils.readFileToString(new File(sstubsFile), Charset.defaultCharset());
        List<BugData> reportList= gson.fromJson(input,new TypeToken<List<BugData>>(){}.getType());
        //java.lang.reflect.Method
        List<BugData> invokeList=new ArrayList<>();
        List<BugData> getDeclaredMethodList=new ArrayList<>();
        //java.lang.Class
        List<BugData> newInstanceList=new ArrayList<>();
        List<BugData> forNameList=new ArrayList<>();
        List<BugData> getDeclaredConstructorList=new ArrayList<>();
        List<BugData> getDeclaredFieldList=new ArrayList<>();
        List<BugData> getClassLoaderList=new ArrayList<>();
        //java.lang.ClassLoader
        List<BugData> findClassList=new ArrayList<>();
        List<BugData> defineClassList=new ArrayList<>();
        List<BugData> loadClassList=new ArrayList<>();
        //java.lang.reflect.Proxy
        List<BugData> newProxyInstanceList=new ArrayList<>();
        List<BugData> getInvocationHandlerList=new ArrayList<>();
        //java.io.ObjectInputStream
        List<BugData> readObjectList=new ArrayList<>();
        //sun.misc.unsafe
        List<BugData> allocateInstanceList= new ArrayList<>();
        //java.util.ServiceLoader.load()
        List<BugData> serviceLoaderList= new ArrayList<>();

        Set<String> projectSet=new HashSet<>();
        Set<String> totalFixedCommitSet=new HashSet<>();
        Set<String> bugFileSet= new HashSet<>();

        int total=0;
        for(BugData bugReport: reportList){
            String beforeFix= bugReport.getSourceBeforeFix();
            String afterFix=bugReport.getSourceAfterFix();
            String projectUrl="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/");
            String fixUrl="https://github.com/apache/"+bugReport.getProjectName().replaceFirst("\\.","/")+"/commit/"
                    +bugReport.getFixCommitSHA1();
            String bugSourceCode="https://github.com/"+bugReport.getProjectName().replaceFirst("\\.","/")+"/commit/"
                    +bugReport.getFixCommitParentSHA1()+"/"+bugReport.getBugFilePath();

            if(StringUtils.countMatches(beforeFix,".invoke(") !=0){
                invokeList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".getDeclaredMethod(")!=0){
                getDeclaredMethodList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".newInstance(")!=0){
                newInstanceList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".forName(")!=0){
                forNameList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".getDeclaredConstructor(")!=0){
                getDeclaredConstructorList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".getDeclaredField(")!=0){
                getDeclaredFieldList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".getClassLoader(")!=0){
                getClassLoaderList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".findClass(")!=0){
                findClassList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".defineClass(")!=0){
                defineClassList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".loadClass(")!=0){
                loadClassList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".readObject(")!=0){
                readObjectList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".allocateInstance(")!=0){
                allocateInstanceList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }

            if(StringUtils.countMatches(beforeFix,".getInvocationHandler(")!=0){
                getInvocationHandlerList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
            if(StringUtils.countMatches(beforeFix,".newProxyInstance(")!=0){
                newProxyInstanceList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }

            if(StringUtils.countMatches(beforeFix,".load(")!=0){
                serviceLoaderList.add(bugReport);
                totalFixedCommitSet.add(fixUrl);
                projectSet.add(bugReport.getProjectName());
            }
        }
        System.out.println("invoke: " +invokeList.size());
        System.out.println("getDeclaredMethod: " +getDeclaredMethodList.size());
        System.out.println("newInstance: " +newInstanceList.size());
        System.out.println("forName: " +forNameList.size());
        System.out.println("getDeclaredConstructor: " +getDeclaredConstructorList.size());
        System.out.println("getDeclaredField: " +getDeclaredFieldList.size());
        System.out.println("getClassLoader: " +getClassLoaderList.size());
        System.out.println("findClass: " +findClassList.size());
        System.out.println("defineClass: " +defineClassList.size());
        System.out.println("loadClass: " +loadClassList.size());
        System.out.println("readObject: " +readObjectList.size());
        System.out.println("allocateInstance: " +allocateInstanceList.size());
        System.out.println("getInvocationHandler: " +getInvocationHandlerList.size());
        System.out.println("newProxyInstance: " +newProxyInstanceList.size());
        System.out.println("ServiceLoader: " +serviceLoaderList.size());
        System.out.println("total: "+totalFixedCommitSet.size());
        System.out.println("--------------------------");
        System.out.println("number of projects: "+projectSet.size());

        System.out.println("--------------------------");
        for(String line: totalFixedCommitSet){
            System.out.println(line);
        }
    }
}
