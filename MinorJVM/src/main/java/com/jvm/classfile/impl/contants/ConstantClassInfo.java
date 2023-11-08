package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;
import com.jvm.classfile.ConstantPool;

/**
 * @Author qxy
 * @Date 2023/11/8 20:25
 * @Version 1.0
 */
public class ConstantClassInfo implements ConstantInfo {

    public ConstantPool constantPool;
    public int nameIdx;

    public ConstantClassInfo(ConstantPool constantPool){
        this.constantPool = constantPool;
    }

    @Override
    public void readInfo(ClassReader reader) {
        this.nameIdx = reader.readUnit16();
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_CLASS;
    }

    public String name(){
        return this.constantPool.getUTF8(this.nameIdx);
    }
}
