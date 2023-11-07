package com.jvm.classpath.impl;

import com.jvm.classpath.Entry;

import java.io.IOException;
import java.nio.file.*;

/**
 * @Author qxy
 * @Date 2023/11/3 15:06
 * @Version 1.0
 */
public class ZipEntry implements Entry {
    private Path absolutePath;

    public ZipEntry(String path){
        this.absolutePath = Paths.get(path).toAbsolutePath();
    }

    // 读取压缩包内的路径
    @Override
    public byte[] readClass(String className) throws IOException {
        try(FileSystem zipFs = FileSystems.newFileSystem(absolutePath, null)){
            return Files.readAllBytes(zipFs.getPath(className));
        }
    }

    @Override
    public String toString() {
        return this.absolutePath.toString();
    }
}
