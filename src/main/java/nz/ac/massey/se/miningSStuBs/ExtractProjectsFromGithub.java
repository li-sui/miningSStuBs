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

/**
 * extract projects from www.github.com
 * @author Li Sui
 */
public class ExtractProjectsFromGithub {

    public static void main(String[] args) throws Exception{
        String outputPath=Util.group+".projects.csv";
        StringBuilder sb=new StringBuilder();
        int count=0;
        int total =0;
        for(int page=1;page<=Util.maxPage;page++){
            Document doc = Jsoup.connect(Util.url+"/"+Util.group+"?language="+Util.language+"&page="+page).cookies(Util.cookie).get();
            //TODO:hard coded elements, need to update if github webpage changes
            List<Element> repos=doc.getElementsByAttributeValue("data-hovercard-type","repository");

            for(Element element: repos){
                String project = element.text();
                total++;
                //checking the mined project that has been included in SStuBs dataset
                if(!isDup(project)) {
                    String projectUrl= Util.url+"/"+Util.group+"/"+project;
                    sb.append(projectUrl);
                    sb.append("\n");
                    count++;
                }
            }
            System.out.println("page "+page+" done, pause for a while.....");
            Thread.sleep(Util.requestInterval);
        }
        System.out.println("extracted: "+count+"/"+total);
        File output =new File(outputPath);
        FileUtils.touch(output);
        FileUtils.writeStringToFile(output,sb.toString(), Charset.defaultCharset());
    }

    public static boolean isDup(String project){
        boolean dup=false;
        String fullName= Util.group+"/"+project;
        for(int j =0; j< Util.topProjects.length;j++){
            if(Util.topProjects[j].contains(fullName)){
                dup=true;
                break;
            }
        }
        for(int k =0; k< Util.topMavenProjects.length;k++){
            if(Util.topMavenProjects[k].contains(fullName)){
                dup=true;
                break;
            }
        }
     return dup;
    }
}