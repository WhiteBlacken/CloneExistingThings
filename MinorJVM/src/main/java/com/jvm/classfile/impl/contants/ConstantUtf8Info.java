package com.jvm.classfile.impl.contants;

import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantInfo;

/**
 * @Author qxy
 * @Date 2023/11/8 19:02
 * @Version 1.0
 */
public class ConstantUtf8Info implements ConstantInfo {

    private String str;

    @Override
    public void readInfo(ClassReader reader) {
        int length = reader.readUnit16();
        byte[] bytes = reader.readByte(length);
        this.str = new String(bytes);
    }

    @Override
    public int tag() {
        return ConstantInfo.CONSTANT_TAG_UTF8;
    }

    public String str(){
        return this.str;
    }
}
