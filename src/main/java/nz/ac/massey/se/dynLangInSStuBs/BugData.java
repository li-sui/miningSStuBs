package nz.ac.massey.se.dynLangInSStuBs;

import java.util.Objects;

public class BugData {
    private String fixType;
    private String fixCommitSHA1;
    private String fixCommitParentSHA1;
    private String bugFilePath;
    private String projectName;
    private String sourceBeforeFix;
    private String sourceAfterFix;
    private int bugLineNum;

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

    public int getBugLineNum(){return bugLineNum;}

    @Override
    public String toString() {
        return "BugData{" +
                "fixType='" + fixType + '\'' +
                ", fixCommitSHA1='" + fixCommitSHA1 + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BugData bugData = (BugData) o;
        return bugLineNum == bugData.bugLineNum && Objects.equals(fixType, bugData.fixType) && Objects.equals(fixCommitSHA1, bugData.fixCommitSHA1) && Objects.equals(fixCommitParentSHA1, bugData.fixCommitParentSHA1) && Objects.equals(bugFilePath, bugData.bugFilePath) && Objects.equals(projectName, bugData.projectName) && Objects.equals(sourceBeforeFix, bugData.sourceBeforeFix) && Objects.equals(sourceAfterFix, bugData.sourceAfterFix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixType, fixCommitSHA1, fixCommitParentSHA1, bugFilePath, projectName, sourceBeforeFix, sourceAfterFix, bugLineNum);
    }
}
