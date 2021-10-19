package nz.ac.massey.se.miningSStuBs;

public class BugData {
    private String fixType;
    private String fixCommitSHA1;
    private String fixCommitParentSHA1;
    private String bugFilePath;
    private String projectName;
    private String sourceBeforeFix;
    private String sourceAfterFix;

    public String getFixType() {
        return fixType;
    }

    public String getFixCommitSHA1() {
        return fixCommitSHA1;
    }

    public String getFixCommitParentSHA1() {
        return fixCommitParentSHA1;
    }

    public String getBugFilePath() {
        return bugFilePath;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getSourceBeforeFix() {
        return sourceBeforeFix;
    }

    public String getSourceAfterFix() {
        return sourceAfterFix;
    }

    @Override
    public String toString() {
        return "BugData{" +
                "fixType='" + fixType + '\'' +
                ", fixCommitSHA1='" + fixCommitSHA1 + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
