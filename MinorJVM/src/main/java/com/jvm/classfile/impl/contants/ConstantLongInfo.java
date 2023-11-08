package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;

/**
 * @Author qxy
 * @Date 2023/11/8 18:52
 * @Version 1.0
 */
public class ConstantLongInfo implements ConstantInfo {

    private long val;

    @Override
    public void readInfo(ClassReader reader) {
        this.val = reader.readUnit64Long();
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_LONG;
    }

    public long value(){
        return this.val;
    }
}
