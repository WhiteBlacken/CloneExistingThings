package com.jvm.classfile;

import com.jvm.classfile.impl.attributes.*;

/**
 * @Author qxy
 * @Date 2023/11/8 21:36
 * @Version 1.0
 */
public interface AttributeInfo {

    void readInfo(ClassReader reader);

    static AttributeInfo[] readAttributes(ClassReader reader, ConstantPool constantPool) {
        int attributesCount = reader.readUnit16();
        AttributeInfo[] attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++){
            attributes[i] = readAttribute(reader, constantPool);
        }
        return attributes;
    }

    static AttributeInfo readAttribute(ClassReader reader, ConstantPool constantPool) {
        int attrNameIdx = reader.readUnit16();
        String attrName = constantPool.getUTF8(attrNameIdx);
        int attrLen = reader.readUnit32Integer();
        AttributeInfo attributeInfo = newAttributeInfo(attrName, attrLen, constantPool);
        attributeInfo.readInfo(reader);
        return attributeInfo;
    }

    static AttributeInfo newAttributeInfo(String attrName, int attrLen, ConstantPool constantPool) {
        switch (attrName) {
//            case "BootstrapMethods":
//                return new BootstrapMethodsAttribute();
            case "Code":
                return new CodeAttribute(constantPool);
            case "ConstantValue":
                return new ConstantValueAttribute();
            case "Deprecated":
                return new DeprecatedAttribute();
//            case "EnclosingMethod":
//                return new EnclosingMethodAttribute(constantPool);
            case "Exceptions":
                return new ExceptionsAttribute();
//            case "InnerClasses":
//                return new InnerClassesAttribute();
//            case "LineNumberTable":
//                return new LineNumberTableAttribute();
//            case "LocalVariableTable":
//                return new LocalVariableTableAttribute();
//            case "LocalVariableTypeTable":
//                return new LocalVariableTypeTableAttribute();
            // case "MethodParameters":
            // case "RuntimeInvisibleAnnotations":
            // case "RuntimeInvisibleParameterAnnotations":
            // case "RuntimeInvisibleTypeAnnotations":
            // case "RuntimeVisibleAnnotations":
            // case "RuntimeVisibleParameterAnnotations":
            // case "RuntimeVisibleTypeAnnotations":
//            case "Signature":
//                return new SignatureAttribute(constantPool);
            case "SourceFile":
                return new SourceFileAttribute(constantPool);
            // case "SourceDebugExtension":
            // case "StackMapTable":
            case "Synthetic":
                return new SyntheticAttribute();
            default:
                return new UnparsedAttribute(attrName, attrLen);
        }
    }
}
