package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;

/**
 * @Author qxy
 * @Date 2023/11/8 18:43
 * @Version 1.0
 */
public class ConstantFloatInfo implements ConstantInfo {

    private float val;

    @Override
    public void readInfo(ClassReader reader) {
        this.val = reader.readUnit64Float();
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_FLOAT;
    }

    public float value(){
        return this.val;
    }
}
