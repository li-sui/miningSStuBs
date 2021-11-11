package nz.ac.massey.se.dynLangInSStuBs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

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

    static{
        Gson gson =new Gson();
        String cookieInput= null;
        String configInput= null;
        try {
            cookieInput = FileUtils.readFileToString(new File("src/main/resources/cookie.json"), Charset.defaultCharset());
            configInput=FileUtils.readFileToString(new File("src/main/resources/config.json"), Charset.defaultCharset());
            topProjects=FileUtils.readFileToString(new File("src/main/resources/topProjects.csv"),Charset.defaultCharset()).split("\\n");
            topMavenProjects=FileUtils.readFileToString(new File("src/main/resources/topJavaMavenProjects.csv"),Charset.defaultCharset()).split("\\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        cookie=gson.fromJson(cookieInput, new TypeToken<Map<String, String>>() {}.getType());
        configuration=gson.fromJson(configInput, Configuration.class);
        ;
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
