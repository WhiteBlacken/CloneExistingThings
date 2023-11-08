package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;
import com.jvm.classfile.ConstantPool;

import java.util.Map;

/**
 * @Author qxy
 * @Date 2023/11/8 21:23
 * @Version 1.0
 */
public class ConstantMemberRefInfo implements ConstantInfo {

    protected ConstantPool constantPool;
    protected int classIdx;
    private int nameAndTypeIdx;

    ConstantMemberRefInfo(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public void readInfo(ClassReader reader) {
        this.classIdx = reader.readUnit16();
        this.nameAndTypeIdx = reader.readUnit16();
    }

    @Override
    public int tag() {
        return 0;
    }

    public String className(){
        return this.constantPool.getClassName(this.classIdx);
    }

    public Map<String,String> nameAndDescriptor(){
        return this.constantPool.getNameANdType(this.nameAndTypeIdx);
    }
}
