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

    public static void main(String[] args) throws Exception{
        String outputPath=Util.group+".projects.csv";

        StringBuilder sb=new StringBuilder();
        for(int page=1;page<Util.maxPage;page++){
            Document doc = Jsoup.connect(Util.url+"?language="+Util.language+"&page="+page).cookies(Util.cookie).get();
            //TODO:hard coded elements, need to update if github webpage changes
            List<Element> repos=doc.getElementsByAttributeValue("data-hovercard-type","repository");
            for(Element element: repos){
                String project = element.text();
                //checking the mined project that has been included in SStuBs dataset
                if(!isDup(project)) {
                    sb.append(project);
                    sb.append("\n");
                }
            }
            System.out.println("page "+page+" done, pause for a while.....");
            Thread.sleep(Util.requestInterval);
        }
        File output =new File(outputPath);
        FileUtils.touch(output);
        FileUtils.writeStringToFile(output,sb.toString(), Charset.defaultCharset());
    }



    public static boolean isDup(String project){
        String[] topProjects=ExtractProjectsFromGithub.class.getResource("topProjects.csv").getFile().split("\\n");
        String[] topMavenProjects=ExtractProjectsFromGithub.class.getResource("topJavaMavenProjects.csv").getFile().split("\\n");

        boolean dup=false;
        String fullName= Util.group+"/"+project;
        for(int j =0; j< topProjects.length;j++){
            if(topProjects[j].contains(fullName)){
                dup=true;
                break;
            }
        }
        for(int k =0; k< topMavenProjects.length;k++){
            if(topMavenProjects[k].contains(fullName)){
                dup=true;
                break;
            }
        }
     return dup;
    }
}
