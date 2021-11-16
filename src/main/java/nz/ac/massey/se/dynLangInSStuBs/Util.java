package nz.ac.massey.se.dynLangInSStuBs;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * util class
 * @author Li Sui
 */
public class Util {

    public static Configuration configuration;
    public static  Map<String, String> cookie;
    //these two string arrays contain projects from SStuBs dataset
    public static  String[] topProjects;
    public static String[] topMavenProjects;
    public static List<ReflectiveAPI>  reflectiveAPIList;
    static{
        Gson gson =new Gson();
        String cookieInput= null;
        String configInput= null;
        String reflectAPIContent=null;
        try {
            cookieInput = FileUtils.readFileToString(new File("src/main/resources/cookie.json"), Charset.defaultCharset());
            configInput=FileUtils.readFileToString(new File("src/main/resources/config.json"), Charset.defaultCharset());
            topProjects=FileUtils.readFileToString(new File("src/main/resources/topProjects.csv"),Charset.defaultCharset()).split("\\n");
            topMavenProjects=FileUtils.readFileToString(new File("src/main/resources/topJavaMavenProjects.csv"),Charset.defaultCharset()).split("\\n");
            reflectAPIContent= FileUtils.readFileToString(new File("src/main/resources/ReflectiveAPIs.json"),Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cookie=gson.fromJson(cookieInput, new TypeToken<Map<String, String>>() {}.getType());
        configuration=gson.fromJson(configInput, Configuration.class);
        reflectiveAPIList=gson.fromJson( reflectAPIContent,new TypeToken<List<ReflectiveAPI>>(){}.getType());
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

    public static Multimap<String, List<String>> getAllCallsites(){
        Multimap<String, List<String>> multimap = ArrayListMultimap.create();
        Set<String> set=new HashSet<>();
        for(ReflectiveAPI ref: reflectiveAPIList){
            for(Keyword keyword: ref.getKeywords()){
                String callsite= keyword.getCallsite();
                multimap.put(callsite, keyword.getArguments());
            }
        }
        return multimap;
    }
}
