package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ConstantInfo;
import com.jvm.classfile.ConstantPool;

/**
 * @Author qxy
 * @Date 2023/11/8 21:30
 * @Version 1.0
 */
public class ConstantMethodRefInfo extends ConstantMemberRefInfo{
    public ConstantMethodRefInfo(ConstantPool constantPool) {
        super(constantPool);
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_METHODREF;
    }
}
