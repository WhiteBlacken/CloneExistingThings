package com.jvm.classpath.impl;

import com.jvm.classpath.Entry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @Author qxy
 * @Date 2023/11/3 15:05
 * @Version 1.0
 */
public class DirEntry implements Entry {

    private Path absolutePath;

    public DirEntry(String path) {
        this.absolutePath = Paths.get(path).toAbsolutePath();
    }

    @Override
    public byte[] readClass(String className) throws IOException {
        return Files.readAllBytes(absolutePath.resolve(className));
    }

    @Override
    public String toString() {
        return this.absolutePath.toString();
    }
}
