package nz.ac.massey.se.dynLangInSStuBs.tasks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nz.ac.massey.se.dynLangInSStuBs.BugData;
import nz.ac.massey.se.dynLangInSStuBs.sourceCodeAnalysis.MyAnalysis;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 *filter fps by matching argument size(type).
 */
public class CheckFP {
    static final File sstubsFile=  new File("/home/lsui/projects/miningSStuBs/results/sstubs.json");
    static final String inputDir="/home/lsui/projects/allsroucecode2";
    public static void main(String[] args) throws Exception{
        Gson gson =new Gson();
        String bugContent = FileUtils.readFileToString(sstubsFile, Charset.defaultCharset());
        List<BugData> reportList= gson.fromJson(bugContent ,new TypeToken<List<BugData>>(){}.getType());
        List<BugData> tpList=new ArrayList<>();
        Map<String, Integer> map =new HashMap<>();

        List<File> files = (List<File>) FileUtils.listFiles(new File(inputDir), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        int total=0;
        int count=0;
        StringBuilder sb =new StringBuilder();
        for(File file: files){
            String inputClass =FileUtils.readFileToString(file,Charset.defaultCharset());
            total++;
            for(BugData bugData:reportList){
                    String key =bugData.getFixCommitParentSHA1()+"."+bugData.getBugFilePath().replace("/",".");
                    String commit = file.getName().split("\\.")[0];
                    String fileName = file.getName().split("\\.")[1] + ".java";
                    int lineNumber=bugData.getBugLineNum();
                    if (key.contains(commit) && key.contains(fileName)) {
                        if (MyAnalysis.verify(inputClass, lineNumber)) {
                            map.put(file.getName(),lineNumber);
                            String fixURI="https://github.com/"+bugData.getProjectName().replace(".","/")+"/commit/"+bugData.getFixCommitSHA1();
                            sb.append(file.getName()+","+lineNumber+","+fixURI+"\n");
                            count++;
                            break;
                        }
                    }

            }
        }

        System.out.println("total: "+total);
        System.out.println("TP: "+count);
        FileUtils.writeStringToFile(new File("sample/sample-12-Dec.csv"),sb.toString(),Charset.defaultCharset());
/// sample 300
//        Map<String, Integer> sampled=sample(map,300);
//
//        for(String key: sampled.keySet()){
//            System.out.println(key+","+sampled.get(key));
//        }
    }

    public static Map<String, Integer> sample( Map<String, Integer> orgin,int size){
        Map<String, Integer> randomData=new HashMap<>();
        List<String> key=new ArrayList<>(orgin.keySet());
        for(int i=0;i<size;i++){
            Random rand = new Random();
            int randomIndex = rand.nextInt(orgin.size());
            String fileName= key.get(randomIndex);
            randomData.put(fileName,orgin.get(fileName));
            key.remove(randomIndex);
        }
        return randomData;
    }
}
