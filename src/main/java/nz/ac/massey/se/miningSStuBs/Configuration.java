package nz.ac.massey.se.miningSStuBs;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;


public class Configuration {
    String url;
    String language;
    //interval between each get request, default set to 5000 milliseconds
    int requestInterval;
    //target group. e.g. google, apache, eclipse. prior to the search, you need to find out how many pages are there.
    List<Community> communities;
    //indicate the date of the last update to pages from above communities
    String lastUpdate;

    public class Community{
        String name;
        int maxPage;

        public String getName() {
            return name;
        }

        public int getMaxPage() {
            return maxPage;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getLanguage() {
        return language;
    }

    public int getRequestInterval() {
        return requestInterval;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
