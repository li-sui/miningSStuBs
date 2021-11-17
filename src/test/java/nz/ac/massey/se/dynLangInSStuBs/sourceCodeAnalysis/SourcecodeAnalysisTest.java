package nz.ac.massey.se.dynLangInSStuBs.sourceCodeAnalysis;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

public class SourcecodeAnalysisTest {

    @Test
    public void testCallsiteWithoutArgument() throws Exception{

        String sourceCode="public class Foo {\n" +
                "    String foo;\n"+
                "    private String getBar(){\n" +
                "        Foo.class.getDeclaredFields();\n" +
                "        return \"\";\n" +
                "    }\n" +
                "}";

        Assert.assertTrue(MyAnalysis.verify(sourceCode,4));
    }

    @Test
    public void testClassLoader() {
        String sourceCode="public class Foo {\n" +
                "    private void getBar() throws Exception{\n" +
                "        Class.forName(\"Foo\",false,Foo.class.getClassLoader());\n" +
                "    }\n" +
                "}";
        Assert.assertTrue(MyAnalysis.verify(sourceCode,3));
    }

    @Test
    public void testClassLoaderOverloading() {
        String sourceCode="public class Foo {\n" +
                "    private void getBar() throws Exception{\n" +
                "        Class.forName(\"Foo\");\n" +
                "    }\n" +
                "}";
        Assert.assertTrue(MyAnalysis.verify(sourceCode,3));
    }

    @Test
    public void testdefineClassInClassLoader() {
        String sourceCode="public class CustomClassLoader extends ClassLoader {\n" +
                "\n" +
                "    @Override\n" +
                "    public Class findClass(String name) throws ClassNotFoundException {\n" +
                "        byte[] b = loadClassFromFile(name);\n" +
                "        return defineClass(name, b, 0, b.length);\n" +
                "    }\n" +
                "\n" +
                "    private byte[] loadClassFromFile(String fileName)  {\n" +
                "        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(\n" +
                "                fileName.replace('.', File.separatorChar) + \".class\");\n" +
                "        byte[] buffer;\n" +
                "        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();\n" +
                "        int nextValue = 0;\n" +
                "        try {\n" +
                "            while ( (nextValue = inputStream.read()) != -1 ) {\n" +
                "                byteStream.write(nextValue);\n" +
                "            }\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        buffer = byteStream.toByteArray();\n" +
                "        return buffer;\n" +
                "    }\n" +
                "}";
        Assert.assertTrue(MyAnalysis.verify(sourceCode,6));
    }

    @Test
    public void testdefineClassInClassLoaderOverloading() {
        String sourceCode="import java.security.ProtectionDomain;\n "+
                "public class CustomClassLoader extends ClassLoader {\n" +
                "\n" +
                "    @Override\n" +
                "    public Class findClass(String name) throws ClassNotFoundException {\n" +
                "        byte[] b = loadClassFromFile(name);\n" +
                "        ProtectionDomain domain= CustomClassLoader.class.getProtectionDomain();\n"+
                "        return defineClass(name, b, 0, b.length, domain);\n" +
                "    }\n" +
                "\n" +
                "    private byte[] loadClassFromFile(String fileName)  {\n" +
                "        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(\n" +
                "                fileName.replace('.', File.separatorChar) + \".class\");\n" +
                "        byte[] buffer;\n" +
                "        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();\n" +
                "        int nextValue = 0;\n" +
                "        try {\n" +
                "            while ( (nextValue = inputStream.read()) != -1 ) {\n" +
                "                byteStream.write(nextValue);\n" +
                "            }\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        buffer = byteStream.toByteArray();\n" +
                "        return buffer;\n" +
                "    }\n" +
                "}";
        Assert.assertTrue(MyAnalysis.verify(sourceCode,8));
    }

    @Test
    public void testServiceLoader() throws Exception{

        String sourceCode="import java.util.ServiceLoader;\n" +
                "public class Foo {\n" +
                "    private void getBar(){\n" +
                "        ServiceLoader<Foo> loader =ServiceLoader.load(Foo.class);\n" +
                "    }\n" +
                "}";

        Assert.assertTrue(MyAnalysis.verify(sourceCode,4));
    }

    @Test
    public void testReflectiveMethodInvocation() throws Exception{

        String sourceCode="import java.lang.reflect.Method;\n" +
                "public class Foo {\n" +
                "    private void getBar() throws Exception{\n" +
                "        Foo f=new Foo();\n" +
                "        Method method = f.getClass().getDeclaredMethod(\"bar\");\n" +
                "        method.invoke(f);\n" +
                "    }\n" +
                "}";

        Assert.assertTrue(MyAnalysis.verify(sourceCode,6));
    }


    @Test
    public void testRealTP() throws Exception{
       String inputClass= FileUtils.readFileToString(new File("src/test/resources/4cadfe2704ad309a86137698c1842956d87afc7a.java.org.apache.catalina.loader.WebappClassLoader.java"), Charset.defaultCharset());
        Assert.assertTrue(MyAnalysis.verify(inputClass,1672));
    }

    @Test
    public void testRealFP() throws Exception{
        String inputClass= FileUtils.readFileToString(new File("src/test/resources/27c3e32abf48c50f369b98fac9c7b73ab0d9aade.ReactAndroid.src.main.java.com.facebook.react.modules.location.LocationModule.java"), Charset.defaultCharset());
        Assert.assertFalse(MyAnalysis.verify(inputClass,126));
    }

}