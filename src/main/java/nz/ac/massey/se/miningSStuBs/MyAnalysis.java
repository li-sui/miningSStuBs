package nz.ac.massey.se.miningSStuBs;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

public class MyAnalysis {
    public static void main(String[] args) throws Exception{
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        String inputClass= FileUtils.readFileToString(new File("src/main/java/nz/ac/massey/se/miningSStuBs/CountNoOfBugs.java"), Charset.defaultCharset());
        CompilationUnit cu = StaticJavaParser.parse(inputClass);

        cu.findAll(MethodCallExpr.class).forEach(be -> {
            // Find out what type it has:
            System.out.println(be.getName()+"-"+be.getArguments().size());

        });


    }
}
