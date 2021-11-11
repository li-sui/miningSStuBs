package nz.ac.massey.se.dynLangInSStuBs.temp;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * combine SStubs to 1000 project with community projects
 * requires
 * src/main/resources/topProject.cvs&topJavaProjects.csv
 * githubCommunityProjects.csv
 * @author Li Sui
 */
public class CombineDataset {
     public static final File f1 =new File("src/main/resources/topJavaMavenProjects.csv");
    public static final File f2 =new File("/home/lsui/Downloads/topProjects.csv");
    public static final File f3 =new File("githubCommunityProjects.csv");
    public static void main(String[] args) throws Exception{
        Set<String> allProjects= new HashSet<>();
        getUrl(f1,allProjects);
        String content= FileUtils.readFileToString(f2, Charset.defaultCharset());
        int counter=0;
        for(String line: content.split("\n")){
            if(!line.startsWith("repository_url")){
                if(counter==1000){
                    break;
                }else {
                    String out = line.split(",")[0].replace("api.", "").replace("repos/", "");
                    counter++;
                    allProjects.add(out);
                }
            }
        }

        getUrl(f3,allProjects);
        StringBuilder sb =new StringBuilder();
        for(String url: allProjects){
            String community=url.split("/")[3];
            String project=url.split("/")[4];
            sb.append(url+","+community+","+project+"\n");
        }
        FileUtils.writeStringToFile(new File("allJavaProjects.csv"),sb.toString(),Charset.defaultCharset());
        System.out.println(allProjects.size());


    }

    private static void getUrl(File f, Set<String> allProjects) throws Exception{
        String content= FileUtils.readFileToString(f, Charset.defaultCharset());
        for(String line: content.split("\n")){
            if(!line.startsWith("repository_url")){
                allProjects.add(line.split(",")[0]);
            }
        }
    }
}
