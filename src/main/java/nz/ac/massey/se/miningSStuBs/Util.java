package nz.ac.massey.se.miningSStuBs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * util class
 * @author Li Sui
 */
public class Util {

    static Configuration configuration;
    static  Map<String, String> cookie;
    //these two string arrays contain projects from SStuBs dataset
    static  String[] topProjects;
    static String[] topMavenProjects;

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
}
