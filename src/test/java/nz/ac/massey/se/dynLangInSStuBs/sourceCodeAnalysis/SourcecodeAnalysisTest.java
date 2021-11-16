package nz.ac.massey.se.dynLangInSStuBs.sourceCodeAnalysis;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

public class SourcecodeAnalysisTest {
    @Test
    public void testTP() throws Exception{
       String inputClass= FileUtils.readFileToString(new File("src/test/resources/4cadfe2704ad309a86137698c1842956d87afc7a.java.org.apache.catalina.loader.WebappClassLoader.java"), Charset.defaultCharset());
        Assert.assertTrue(MyAnalysis.verify(inputClass,1672));
    }

    @Test
    public void testFP() throws Exception{
        Gson gson =new Gson();
        String inputClass= FileUtils.readFileToString(new File("src/test/resources/27c3e32abf48c50f369b98fac9c7b73ab0d9aade.ReactAndroid.src.main.java.com.facebook.react.modules.location.LocationModule.java"), Charset.defaultCharset());
        Assert.assertFalse(MyAnalysis.verify(inputClass,126));
    }

}