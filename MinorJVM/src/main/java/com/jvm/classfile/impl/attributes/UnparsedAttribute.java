package com.jvm.classfile.impl.attributes;

import com.jvm.classfile.AttributeInfo;
import com.jvm.classfile.ClassReader;

/**
 * @Author qxy
 * @Date 2023/11/8 22:06
 * @Version 1.0
 */
public class UnparsedAttribute implements AttributeInfo {

    private String name;
    private int length;
    private byte[] info;

    public UnparsedAttribute(String name, int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public void readInfo(ClassReader reader) {
        this.info = reader.readByte(this.length);
    }

    public byte[] info(){
        return this.info;
    }

}
