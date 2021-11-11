package nz.ac.massey.se.miningSStuBs.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface MyLogger {
    Logger CLONEREPO= LogManager.getLogger("clone-repo");
    Logger SCANALYSIS=LogManager.getLogger("sourcecode-analysis");
    Logger RETRIEVESC=LogManager.getLogger("retrieve-sourcecode");
}
