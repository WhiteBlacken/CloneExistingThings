package com.jvm.classfile.impl.attributes;

import com.jvm.classfile.AttributeInfo;
import com.jvm.classfile.ClassReader;

/**
 * @Author qxy
 * @Date 2023/11/9 0:11
 * @Version 1.0
 */
public class ExceptionsAttribute implements AttributeInfo {

    private int[] exceptionIndexTable;

    @Override
    public void readInfo(ClassReader reader) {
        this.exceptionIndexTable = reader.readUnit16s();
    }

    public int[] getExceptionIndexTable(){
        return this.exceptionIndexTable;
    }
}
