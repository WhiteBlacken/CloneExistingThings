package com.jvm.rtda;

/**
 * @Author qxy
 * @Date 2023/11/9 1:13
 * @Version 1.0
 */
public class Thread {

    // 程序计数器
    private int pc;

    // 虚拟机栈
    private JvmStack stack;

    public Thread(){
        stack = new JvmStack(1024);
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    // 压入栈帧
    public void pushFrame(Frame frame){
        this.stack.push(frame);
    }

    // 弹出栈帧
    public Frame popFrame(){
        return this.stack.pop();
    }

    public Frame currentFrame(){
        return this.stack.top();
    }
}
