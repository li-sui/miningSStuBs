package nz.ac.massey.se.miningSStuBs;

import java.io.File;

/**
 * required CloneRepositories to be run first.
 * @author Li Sui
 */
public class SStuBsMiner {
    //TODO:configurable
    static final String inputDir="/home/lsui/projects/githubProjects";
    static final String outputDir= "results";
    public static void main(String[] args) throws Exception{
        File output= new File(outputDir);
        if(!output.exists() && output.isDirectory()){
            output.mkdirs();
        }
        String[] minerInput={inputDir,outputDir};
        org.eclipse.jdt.internal.jarinjarloader.JarRsrcLoader.main(minerInput);
    }
}