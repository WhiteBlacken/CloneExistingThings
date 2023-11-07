package com.jvm.classpath.impl;

import com.jvm.classpath.Entry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author qxy
 * @Date 2023/11/3 15:08
 * @Version 1.0
 */
public class CompositeEntry implements Entry {
    // 需要拆分成不同的entry去处理

    private final List<Entry> entryList = new ArrayList<>();

    public CompositeEntry(String pathList) {
        String[] paths = pathList.split(File.pathSeparator);
        for (String path : paths) {
            // 每个小的entry又可以交给entry的create去判断，交给适合的类处理
            entryList.add(Entry.create(path));
        }
    }

    @Override
    public byte[] readClass(String className) throws IOException {
        for (Entry entry : entryList) {
            try {
                // 根据entry的类型调用相应的方法
                return entry.readClass(className);
            } catch (Exception ignored) {

            }
        }
        throw new IOException("class not found " + className);
    }

    @Override
    public String toString() {
        String[] strs = new String[entryList.size()];
        for (int i = 0; i < entryList.size(); i++) {
            strs[i] = entryList.get(i).toString();
        }
        return String.join(File.pathSeparator, strs);
    }
}
