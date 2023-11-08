package com.jvm.classfile;


import com.jvm.classfile.impl.attributes.CodeAttribute;
import com.jvm.classfile.impl.attributes.ConstantValueAttribute;

/**
 * @Author qxy
 * @Date 2023/11/8 12:02
 * @Version 1.0
 */
// 字段和方法信息
public class MemberInfo {

    private ConstantPool constantPool;
    private int accessFlags;
    private int nameIdx;
    private int descriptionIdx;
    private AttributeInfo[] attributes;

    private MemberInfo(ClassReader reader, ConstantPool constantPool){
        this.constantPool = constantPool;
        this.accessFlags = reader.readUnit16();
        this.nameIdx = reader.readUnit16();
        this.descriptionIdx = reader.readUnit16();
        this.attributes = AttributeInfo.readAttributes(reader, constantPool);
    }

    static MemberInfo[] readMembers(ClassReader reader, ConstantPool constantPool){
        int fieldCount = reader.readUnit16();
        MemberInfo[] fields = new MemberInfo[fieldCount];
        for(int i=0;i<fieldCount;i++){
            fields[i] = new MemberInfo(reader, constantPool);
        }
        return fields;
    }

    public int accessFlags() {
        return this.accessFlags;
    }

    public String name() {
        return this.constantPool.getUTF8(this.nameIdx);
    }

    public String descriptor() {
        return this.constantPool.getUTF8(this.descriptionIdx);
    }

    public CodeAttribute codeAttribute() {
        for (AttributeInfo attrInfo : attributes) {
            if (attrInfo instanceof CodeAttribute) return (CodeAttribute) attrInfo;
        }
        return null;
    }

    public ConstantValueAttribute ConstantValueAttribute() {
        for (AttributeInfo attrInfo : attributes) {
            if (attrInfo instanceof ConstantValueAttribute) return (ConstantValueAttribute) attrInfo;
        }
        return null;
    }
}
