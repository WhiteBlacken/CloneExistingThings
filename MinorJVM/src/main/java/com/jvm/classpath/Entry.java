package com.jvm.classpath;

import com.jvm.classpath.impl.CompositeEntry;
import com.jvm.classpath.impl.DirEntry;
import com.jvm.classpath.impl.WildcardEntry;
import com.jvm.classpath.impl.ZipEntry;

import java.io.File;
import java.io.IOException;

/**
 * @Author qxy
 * @Date 2023/11/3 14:51
 * @Version 1.0
 */
public interface Entry {

    /*负责寻找和加载class文件
    参数是class的相对路径，java.lang.Object -> java/lang/Object
     */
    byte[] readClass(String className) throws IOException;

    //根据输入的路径，判断具体创建那种实现类
    static Entry create(String path) {
        if (path.contains(File.pathSeparator)) {
            return new CompositeEntry(path);
        }
        if (path.endsWith("*")) {
            return new WildcardEntry(path);
        }
        if (isZipOrJarFile(path)){
            return new ZipEntry(path);
        }
        return new DirEntry(path);
    }

    static boolean isZipOrJarFile(String path) {
        return path.endsWith(".jar") || path.endsWith(".JAR") || path.endsWith(".zip") || path.endsWith(".ZIP");
    }
}
