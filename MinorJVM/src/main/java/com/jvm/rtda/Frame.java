package com.jvm.rtda;

/**
 * @Author qxy
 * @Date 2023/11/9 1:19
 * @Version 1.0
 */
public class Frame {

    Frame lower; // 指向栈的下一层

    // 局部变量表
    private LocalVars localVars;

    // 操作数栈
    private OperandStack operandStack;

    public Frame(int maxLocals, int maxStack) {
        this.localVars = new LocalVars(maxLocals);
        this.operandStack = new OperandStack(maxStack);
    }

    public LocalVars getLocalVars() {
        return localVars;
    }

    public OperandStack getOperandStack() {
        return operandStack;
    }
}
