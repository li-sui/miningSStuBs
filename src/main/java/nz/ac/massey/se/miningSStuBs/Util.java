package nz.ac.massey.se.miningSStuBs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

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
    static{
        String configFile="config.json";
        String cookieFile="cookie.json";
        Gson gson =new Gson();
        String cookieInput= null;
        String configInput= null;
        try {
            cookieInput = FileUtils.readFileToString(new File(cookieFile), Charset.defaultCharset());
            configInput=FileUtils.readFileToString(new File(configFile), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cookie=gson.fromJson(cookieInput, new TypeToken<Map<String, String>>() {}.getType());
        Map<String,String> config=gson.fromJson(configInput, new TypeToken<Map<String, String>>() {}.getType());
        group=config.get("group");
        requestInterval=Integer.getInteger(config.get("requestInterval"));
        maxPage=Integer.getInteger(config.get("maxPage"));
        url=config.get("url");
        language=config.get("language");

    }
}
