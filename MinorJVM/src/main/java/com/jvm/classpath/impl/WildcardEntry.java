package com.jvm.classpath.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @Author qxy
 * @Date 2023/11/3 15:06
 * @Version 1.0
 */
public class WildcardEntry extends CompositeEntry{
    public WildcardEntry(String pathList) {
        super(pathList);
    }

    private static String toPathList(String wildcardPath){
        // 实际是将通配符转换成多个有效路径
        String baseDir = wildcardPath.replace("*", "");
        try {
            return Files.walk(Paths.get(baseDir))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(p->p.endsWith(".jar") || p.endsWith(".JAR"))
                    .collect(Collectors.joining(File.pathSeparator));
        }catch(Exception e){
            return "";
        }
    }


}
