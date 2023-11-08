package com.jvm.classfile.impl.attributes;

import com.jvm.classfile.AttributeInfo;
import com.jvm.classfile.ClassReader;
import com.jvm.classfile.ConstantPool;

/**
 * @Author qxy
 * @Date 2023/11/8 23:50
 * @Version 1.0
 */
public class CodeAttribute implements AttributeInfo {

    private ConstantPool constantPool;
    private int maxStack;
    private int maxLocals;
    private byte[] data;
    private ExceptionTableEntry[] exceptionTable;
    private AttributeInfo[] attributes;

    public CodeAttribute(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public void readInfo(ClassReader reader) {
        this.maxStack = reader.readUnit16();
        this.maxLocals = reader.readUnit16();
        int dataLength = (int) reader.readUnit32();
        this.data = reader.readByte(dataLength);
        this.exceptionTable = ExceptionTableEntry.readExceptionTable(reader);
        this.attributes = AttributeInfo.readAttributes(reader, this.constantPool);
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public byte[] getData() {
        return data;
    }

    public ExceptionTableEntry[] getExceptionTable() {
        return exceptionTable;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    static class ExceptionTableEntry {
        private int startPC;
        private int endPC;
        private int handlerPC;
        private int catchType;

        public ExceptionTableEntry(int startPC, int endPC, int handlerPC, int catchType) {
            this.startPC = startPC;
            this.endPC = endPC;
            this.handlerPC = handlerPC;
            this.catchType = catchType;
        }

        static ExceptionTableEntry[] readExceptionTable(ClassReader reader) {
            int exceptionTableLength = reader.readUnit16();
            ExceptionTableEntry[] exceptionTableEntries = new ExceptionTableEntry[exceptionTableLength];
            for (int i = 0; i < exceptionTableLength; i++) {
                exceptionTableEntries[i] = new ExceptionTableEntry(reader.readUnit16(), reader.readUnit16(), reader.readUnit16(), reader.readUnit16());
            }
            return exceptionTableEntries;
        }

        public int getStartPC() {
            return startPC;
        }

        public int getEndPC() {
            return endPC;
        }

        public int getHandlerPC() {
            return handlerPC;
        }

        public int getCatchType() {
            return catchType;
        }
    }
}
