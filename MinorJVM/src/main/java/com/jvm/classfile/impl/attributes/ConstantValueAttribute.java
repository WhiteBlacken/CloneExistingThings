package com.jvm.classfile.impl.attributes;

import com.jvm.classfile.AttributeInfo;
import com.jvm.classfile.ClassReader;

/**
 * @Author qxy
 * @Date 2023/11/8 23:48
 * @Version 1.0
 */
public class ConstantValueAttribute implements AttributeInfo {

    private int constantValueIdx;
    @Override
    public void readInfo(ClassReader reader) {
        this.constantValueIdx = reader.readUnit16();
    }

    public int constantValueIdx() {
        return constantValueIdx;
    }
}
