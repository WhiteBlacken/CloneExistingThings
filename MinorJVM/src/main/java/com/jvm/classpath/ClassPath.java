package com.jvm.classpath;

import com.jvm.classpath.impl.WildcardEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author qxy
 * @Date 2023/11/3 14:50
 * @Version 1.0
 */
public class ClassPath {

    // JVM启动时必须要加载的三类类路径
    private Entry bootstrapClassPath; // 启动类路径
    private Entry extensionClassPath; // 扩展类路径
    private Entry userClassPath; // 用户类路径

    public ClassPath(String jreOption, String cpOption) {
        parseBootAndExtensionClassPath(jreOption);
        parseUserClassPath(cpOption);
    }

    private void parseBootAndExtensionClassPath(String jreOption) {
        String jreDir = getJreDir(jreOption);
        // 启动类在 jre/lib/*
        String jreLibPath = Paths.get(jreDir, "lib") + File.separator + "*";
        bootstrapClassPath = new WildcardEntry(jreLibPath);

        // 扩展类在 jre/lib/ext/*
        String jreExtPath = Paths.get(jreDir, "lib", "ext") + File.separator + "*";
        extensionClassPath = new WildcardEntry(jreExtPath);
    }

    private String getJreDir(String jreOption) {
        // 先找指定路径
        if (jreOption != null && Files.exists(Paths.get(jreOption))) {
            return jreOption;
        }
        // 找当前目录
        if (Files.exists(Paths.get("./jre"))) {
            return "./jre";
        }
        // 找系统环境
        String jh = System.getenv("JAVA_HOME");
        if (jh != null) {
            return Paths.get(jh, "jre").toString();
        }
        throw new RuntimeException("找不到jre路径！");

    }

    private void parseUserClassPath(String cpOption) {
        if (cpOption == null) {
            cpOption = ".";
        }
        userClassPath = Entry.create(cpOption);
    }

    public byte[] readClass(String className) throws IOException {
        // 根据类名获取一个类的字节码
        // 根据双亲委派机制，按照bootstrap->application->userClass的顺序读取
        // 这里的调用实现仅体现顺序，非jvm实际的调用方式
        className = className + ".class";

        try {
            return bootstrapClassPath.readClass(className);
        } catch (Exception e) {

        }

        try{
            return extensionClassPath.readClass(className);
        } catch (Exception e){

        }

        return userClassPath.readClass(className);
    }


}
