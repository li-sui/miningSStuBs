package nz.ac.massey.se.dynLangInSStuBs.sourceCodeAnalysis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nz.ac.massey.se.dynLangInSStuBs.ReflectiveAPI;
import nz.ac.massey.se.dynLangInSStuBs.Util;
import nz.ac.massey.se.dynLangInSStuBs.logger.MyLogger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MyAnalysis {

    public static boolean verify(String inputClass, int expectedLineNumber){
        boolean isDynLang=false;
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        CompilationUnit cu = StaticJavaParser.parse(inputClass);
        Multimap<String, List<String>> callsites= Util.getAllCallsites();

        for(MethodCallExpr mce : cu.findAll(MethodCallExpr.class)){
            int lineNumber= mce.getName().getBegin().get().line;
            String callsiteName= mce.getName().asString();
            if(expectedLineNumber==lineNumber && callsites.containsKey(callsiteName)){

                NodeList<Expression> argumentList= mce.getArguments();
                Collection<List<String>> expectedArguments=callsites.get(callsiteName);
                if(argumentList.size()==0 && checkZEROArgument(expectedArguments)){
                    MyLogger.SCANALYSIS.info("the callsite has no arguments and it matches the expected arguments");
                    return true;
                }
                List<String> convertedArgs= new ArrayList<>();
                for(Expression expression: argumentList){
                    convertedArgs.add(expression.calculateResolvedType().describe());
                }
                if(checkArgument(expectedArguments,convertedArgs)){
                    MyLogger.SCANALYSIS.info("found the callsite:"+callsiteName+" at expected line:" +lineNumber);
                    return true;
                }
            }
        }
        MyLogger.SCANALYSIS.info("no dynamic language feature");
        return isDynLang;
    }

    public static boolean checkZEROArgument(Collection<List<String>> expectedArguments){
        boolean noArgument=true;
        for(List<String> list: expectedArguments){
            if(list.size()!=0){
                noArgument=false;
            }
        }
        return noArgument;
    }

    public static boolean checkArgument(Collection<List<String>> expectedArguments, List<String> arguments){
        for(List<String> list: expectedArguments){
            //equal or contain?
           if(CollectionUtils.containsAny(arguments,list)){
               return true;
           }
        }
        return false;
    }
}
