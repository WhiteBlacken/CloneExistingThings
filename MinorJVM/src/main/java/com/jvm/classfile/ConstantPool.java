package com.jvm.classfile;

import com.jvm.classfile.impl.contants.ConstantClassInfo;
import com.jvm.classfile.impl.contants.ConstantNameAndTypeInfo;
import com.jvm.classfile.impl.contants.ConstantUtf8Info;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author qxy
 * @Date 2023/11/8 17:50
 * @Version 1.0
 */
// 存放各类常量信息，如数字和字符串常量、类和接口名、字段和方法名等
public class ConstantPool {

    private ConstantInfo[] constantInfos;
    private final int size;


    public ConstantPool(ClassReader reader) {
        // 表头是常量池的大小
        size = reader.readUnit16();
        constantInfos = new ConstantInfo[size];
        for (int i = 1; i < size; i++) {
            constantInfos[i] = ConstantInfo.readConstantInfo(reader, this);
            switch (constantInfos[i].tag()){
                // 有两种类型需要占用两个位置
                case ConstantInfo.CONSTANT_TAG_DOUBLE:
                case ConstantInfo.CONSTANT_TAG_LONG:
                    i++;
                    break;
            }
        }
    }

    public Map<String,String> getNameANdType(int idx){
        ConstantNameAndTypeInfo constantInfo = (ConstantNameAndTypeInfo) this.constantInfos[idx];
        Map<String,String> map = new HashMap<>();
        map.put("name", this.getUTF8(constantInfo.nameIdx));
        map.put("_type", this.getUTF8(constantInfo.descIdx));
        return map;
    }

    public String getUTF8(int idx){
        ConstantUtf8Info utf8Info = (ConstantUtf8Info) this.constantInfos[idx];
        return utf8Info == null ? "":utf8Info.str();
    }

    public ConstantInfo[] getConstantInfos(){
        return constantInfos;
    }

    public void setConstantInfos(ConstantInfo[] constantInfos){
        this.constantInfos = constantInfos;
    }

    public int getSize(){
        return size;
    }

    // 通过下标从常量里查找类名，使用到getUTF8，用于从常量池里查找字符串
    public String getClassName(int idx){
        ConstantClassInfo classInfo = (ConstantClassInfo) this.constantInfos[idx];
        return this.getUTF8(classInfo.nameIdx);
    }

}
