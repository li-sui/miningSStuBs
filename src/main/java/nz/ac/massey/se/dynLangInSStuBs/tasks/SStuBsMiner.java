package nz.ac.massey.se.dynLangInSStuBs.tasks;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.File;

/**
 * required nz.ac.massey.se.dynLangInSStuBs.tasks.CloneRepositories to be run first.
 * @author Li Sui
 */
public class SStuBsMiner {
    //dir to output
    static final String outputDir= "results";
    public static void main(String[] args) throws Exception{

        Options cliOptions = new Options()
                .addOption("inputDir",true,"the directory that contains all cloned repositories.");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( cliOptions, args);
        if(!cmd.hasOption("inputDir")){
            System.err.println("e.g. -inputDir /home/{user}/downloads");
            return;
        }
        String inputDir=cmd.getOptionValue("inputDir");

        File output= new File(outputDir);
        if(!output.exists() && output.isDirectory()){
            output.mkdirs();
        }
        String[] minerInput={inputDir,outputDir};
        org.eclipse.jdt.internal.jarinjarloader.JarRsrcLoader.main(minerInput);
    }
}
