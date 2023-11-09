package com.jvm.rtda;

/**
 * @Author qxy
 * @Date 2023/11/9 1:14
 * @Version 1.0
 */
public class JvmStack {

    private int maxSize;
    private int size; // 当前容量
    private Frame _top; // 栈顶指针

    public JvmStack(int maxSize){
        this.maxSize = maxSize;
    }

    public void push(Frame frame) {
        if (this.size > this.maxSize) {
            throw new StackOverflowError();
        }

        if (this._top != null) {
            frame.lower = this._top;
        }
        this._top = frame;
        this.size++;
    }

    public Frame pop() {
        if (this._top == null) {
            throw new RuntimeException("jvm stack is empty!");
        }

        Frame top = this._top;
        this._top = top.lower;
        top.lower = null;
        this.size--;
        return top;
    }

    public Frame top() {
        if (this._top == null) {
            throw new RuntimeException("jvm stack is empty!");
        }
        return this._top;
    }

}
