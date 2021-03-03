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

    static String url;
    static String language;
    //interval between each get request, default set to 5000 milliseconds
    static int requestInterval;
    //target group. e.g. google, apache, eclipse
    static String group;
    //prior to the search, you need to find out how many pages are there.
    static int maxPage;
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
        Map<String,String> config=gson.fromJson(configInput, new TypeToken<Map<String, String>>() {}.getType());
        group=config.get("group");
        requestInterval=Integer.parseInt(config.get("requestInterval"));
        maxPage=Integer.parseInt(config.get("maxPage"));
        url=config.get("url");
        language=config.get("language");
    }
}
