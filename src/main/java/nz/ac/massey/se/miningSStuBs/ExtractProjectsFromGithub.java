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
        List<Configuration.Community> communities= Util.configuration.getCommunities();
        String url =Util.configuration.getUrl();
        String language= Util.configuration.getLanguage();
        int requestInterval=Util.configuration.getRequestInterval();
        StringBuilder sb=new StringBuilder();
        for(Configuration.Community community: communities){
            int count=0;
            int total =0;
            System.out.println("mining for "+community.getName()+"....");
            for(int page=1;page<=community.getMaxPage();page++){

                Document doc = Jsoup.connect(url+"/"+community.getName()+"/repositories?language="+language+"&page="+page).cookies(Util.cookie).get();
                //TODO:hard coded elements, need to update if github webpage changes
                List<Element> repos=doc.getElementsByAttributeValue("data-hovercard-type","repository");
                for(Element element: repos){
                    String repoName = element.text();
                    String repoUrl= element.attr("href");
                    total++;
                    //checking the mined project that has been included in SStuBs dataset
                    if(!isDup(community.getName(),repoName)) {
                        String fullUrl = "https://github.com"+repoUrl;
                        //format: projectUrl,communityName,projectName
                        sb.append(fullUrl);sb.append(",");sb.append(community.getName());sb.append(",");sb.append(repoName);
                        sb.append("\n");
                        count++;
                    }
                }
//                System.out.println("-----page "+page+" done, pause for a while.....");
                Thread.sleep(requestInterval);
            }
            System.out.println("extracted: "+count+"/"+total);
        }
        File output =new File("githubCommunityProjects.csv");
        FileUtils.touch(output);
        FileUtils.writeStringToFile(output,sb.toString(), Charset.defaultCharset());
    }

    public static boolean isDup(String communityName, String project){
        boolean dup=false;
        String fullName= communityName+"/"+project;
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