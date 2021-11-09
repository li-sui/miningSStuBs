package nz.ac.massey.se.miningSStuBs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * count number of single line bugs in results/bugs.json
 * @author Li Sui
 */
public class CountNoOfBugs {
    //TODO: make it configurable.
    static String  jsonFile = "/home/lsui/projects/miningSStuBs/results/bugs.json";
    public static void main(String[] args) throws Exception {

        Gson gson = new Gson();
        String input = FileUtils.readFileToString(new File(jsonFile), Charset.defaultCharset());
        List<BugData> reportList= gson.fromJson(input,new TypeToken<List<BugData>>(){}.getType());
        System.out.println(reportList.size());
    }

}
