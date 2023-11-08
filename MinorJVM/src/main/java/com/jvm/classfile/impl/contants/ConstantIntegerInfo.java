package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;

/**
 * @Author qxy
 * @Date 2023/11/8 18:37
 * @Version 1.0
 */
public class ConstantIntegerInfo implements ConstantInfo {

    private int val;

    @Override
    public void readInfo(ClassReader reader) {
        this.val = reader.readUnit32Integer();
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_INTEGER;
    }

    public int value(){
        return this.val;
    }
}
