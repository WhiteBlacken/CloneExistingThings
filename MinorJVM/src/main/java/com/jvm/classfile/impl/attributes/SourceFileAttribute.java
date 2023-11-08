package com.jvm.classfile.impl.attributes;

import com.jvm.classfile.AttributeInfo;
import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantPool;

/**
 * @Author qxy
 * @Date 2023/11/8 22:33
 * @Version 1.0
 */
public class SourceFileAttribute implements AttributeInfo {

    private ConstantPool constantPool;
    private int sourceFileIdx;

    public SourceFileAttribute(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public void readInfo(ClassReader reader) {
        this.sourceFileIdx = reader.readUnit16();
    }

    public String fileName(){
        return this.constantPool.getUTF8(this.sourceFileIdx);
    }
}
