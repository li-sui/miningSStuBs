package nz.ac.massey.se.dynLangInSStuBs.tasks;

import nz.ac.massey.se.dynLangInSStuBs.Util;
import nz.ac.massey.se.dynLangInSStuBs.logger.MyLogger;
import nz.ac.massey.se.dynLangInSStuBs.Configuration;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * mining projects from selected communities from www.github.com.
 * This will produce a csv file: githubCommunityProjects.csv
 * before running, configure: src/main/resources/config.json and src/main/resources/cookie.json
 *
 * @author Li Sui
 */
public class WebCrawlingGithub {

    public static void main(String[] args) throws Exception{
        List<Configuration.Community> communities= Util.configuration.getCommunities();
        String url =Util.configuration.getUrl();
        String language= Util.configuration.getLanguage();
        int requestInterval=Util.configuration.getRequestInterval();
        StringBuilder sb=new StringBuilder();
        for(Configuration.Community community: communities){
            int count=0;
            int total =0;
            MyLogger.CLONEREPO.info("mining for "+community.getName()+"....");
            for(int page=1;page<=community.getMaxPage();page++){
                Document doc = Jsoup.connect(url+"/"+community.getName()+"/repositories?language="+language+"&page="+page).cookies(Util.cookie).get();
                //warning:hard coded elements, need to update if github webpage changes
                List<Element> repos=doc.getElementsByAttributeValue("data-hovercard-type","repository");
                if(repos.size()==0){
                    MyLogger.CLONEREPO.error("no such element,please check the website to see if the page has been updated");
                    return;
                }
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
                MyLogger.CLONEREPO.info("-----page "+page+" done, pause for a while.....");
                Thread.sleep(requestInterval);
            }
            MyLogger.CLONEREPO.info("extracted: "+count+"/"+total);
        }
        File output =new File("githubCommunityProjects.csv");
        FileUtils.touch(output);
        FileUtils.writeStringToFile(output,sb.toString(), Charset.defaultCharset());
    }

    /**
     * cross check with projects in SStuBs study.
     * @param communityName
     * @param project
     * @return
     */
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