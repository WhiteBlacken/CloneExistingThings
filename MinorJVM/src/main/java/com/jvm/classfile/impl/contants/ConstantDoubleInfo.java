package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;

/**
 * @Author qxy
 * @Date 2023/11/8 18:58
 * @Version 1.0
 */
public class ConstantDoubleInfo implements ConstantInfo {

    private double val;

    @Override
    public void readInfo(ClassReader reader) {
        this.val = reader.readUnit64Double();
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_DOUBLE;
    }

    public double value(){
        return this.val;
    }
}
