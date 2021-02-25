package nz.ac.massey.se.miningSStuBs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * extract projects from www.github.com
 * @author Li Sui
 */
public class ExtractProjectsFromGithub {
    private static final String cookiePath="cookie.json";
    private static final String url="https://github.com/";
    private static final String language ="java";
    //interval between each get request, in milliseconds
    private static final int requestInterval=5000;
    //target group. TODO: configure
    private static final String group="apache";
    //prior to the search, you need to find out how many pages are there. TODO: configure
    private static final int maxPage=35;
    private static final String outputPath=group+".projects.csv";
    public static void main(String[] args) throws Exception{
        Gson gson =new Gson();
        String cookieInput=FileUtils.readFileToString(new File(cookiePath), Charset.defaultCharset());
        Map<String, String> cookie=gson.fromJson(cookieInput, new TypeToken<Map<String, String>>() {}.getType());

        StringBuilder sb=new StringBuilder();
        for(int page=1;page<maxPage;page++){
            Document doc = Jsoup.connect(url+"?language="+language+"&page="+page).cookies(cookie).get();
            //hard coded elements, need to update if github webpage changes
            List<Element> repos=doc.getElementsByAttributeValue("data-hovercard-type","repository");
            for(Element element: repos){
                sb.append(element.text());
                sb.append("\n");
            }
            System.out.println("page "+page+" done, pause for a while.....");
            Thread.sleep(requestInterval);
        }

        File output =new File(outputPath);
        FileUtils.touch(output);
        FileUtils.writeStringToFile(output,sb.toString(), Charset.defaultCharset());
    }
}
