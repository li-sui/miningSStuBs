package nz.ac.massey.se.dynLangInSStuBs.sourceCodeAnalysis;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;
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
        CompilationUnit cu=null;
        try {
            cu = StaticJavaParser.parse(inputClass);
        }catch (ParseProblemException e){
            MyLogger.SCANALYSIS.error("error for parsing source code");
            return false;
        }
        Multimap<String, List<String>> callsites= Util.getAllCallsites();

        for(MethodCallExpr mce : cu.findAll(MethodCallExpr.class)){

            int lineNumber= mce.getName().getBegin().get().line;
            String callsiteName= mce.getName().asString();
            if(expectedLineNumber==lineNumber && callsites.containsKey(callsiteName)){

                NodeList<Expression> argumentList= mce.getArguments();
                Collection<List<String>> expectedArguments=callsites.get(callsiteName);
                if(argumentList.size()==0 && checkZEROArgument(expectedArguments)){
                   // MyLogger.SCANALYSIS.info("found the callsite:"+callsiteName+" at expected line:" +lineNumber +". (no argument)");
                    return true;
                }
                List<String> convertedArgs= new ArrayList<>();
                try {
                    for (Expression expression : argumentList) {
                        String type = expression.calculateResolvedType().describe();
                        // System.out.println(type);
                        if (type.equals("java.lang.ClassLoader")) {
                            convertedArgs.add("java.lang.ClassLoader");
                            continue;
                        }
                        if (type.equals("java.lang.String")) {
                            convertedArgs.add(type);
                            continue;
                        }
                        if (type.equals("java.security.ProtectionDomain")) {
                            convertedArgs.add("java.security.ProtectionDomain");
                            continue;
                        }
                        if (type.startsWith("java.lang.Class")) {
                            convertedArgs.add("java.lang.Class");
                            continue;
                        }
                        if (expression.calculateResolvedType().isReferenceType()) {
                            convertedArgs.add("java.lang.Object");
                            continue;
                        }
                        if (expression.calculateResolvedType().isPrimitive() || expression.calculateResolvedType().isArray()) {
                            convertedArgs.add(type);
                        }
                    }
                }catch (Throwable e){
                   // MyLogger.SCANALYSIS.error("error for resolve types, this callsite:"+callsiteName+" has been skipped");
                    return false;
                }
               // System.out.println(convertedArgs);
                if(checkArgument(expectedArguments,convertedArgs)){
                  //  MyLogger.SCANALYSIS.info("found the callsite:"+callsiteName+" at expected line:" +lineNumber+" with arguments:"+convertedArgs);
                    return true;
                }
            }
        }
       // MyLogger.SCANALYSIS.info("no dynamic language feature");
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
