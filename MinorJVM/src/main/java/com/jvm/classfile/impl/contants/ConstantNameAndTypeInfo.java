package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;

/**
 * @Author qxy
 * @Date 2023/11/8 21:18
 * @Version 1.0
 */
public class ConstantNameAndTypeInfo implements ConstantInfo {

    public int nameIdx;
    public int descIdx;

    @Override
    public void readInfo(ClassReader reader) {
        this.nameIdx = reader.readUnit16();
        this.descIdx = reader.readUnit16();
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_NAMEANDTYPE;
    }
}
