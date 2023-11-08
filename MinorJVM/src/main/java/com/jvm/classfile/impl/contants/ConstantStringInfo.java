package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;
import com.jvm.classfile.ConstantPool;

/**
 * @Author qxy
 * @Date 2023/11/8 20:11
 * @Version 1.0
 */
// 不存放字符串数据，只存放常量池索引，指向CONSTANT——UTF——info的常量
public class ConstantStringInfo implements ConstantInfo {

    private ConstantPool constantPool;
    private int strIdx;

    public ConstantStringInfo(ConstantPool constantPool){
        this.constantPool = constantPool;
    }

    @Override
    public void readInfo(ClassReader reader) {
        // 读取常量池索引
        this.strIdx = reader.readUnit16();
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_STRING;
    }

    public String string(){
        return this.constantPool.getUTF8(this.strIdx);
    }
}
