package com.jvm.classfile;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * @Author qxy
 * @Date 2023/11/8 11:00
 * @Version 1.0
 */
public class ClassReader {

    private byte[] data;

    public ClassReader(byte[] data) {
        this.data = data;
    }

    public int readUnit8() {
        byte[] val = readByte(1);
        return byte2int(val);
    }

    public int readUnit16() {
        byte[] val = readByte(2);
        return byte2int(val);
    }

    public long readUnit32() {
        byte[] val = readByte(4);
        String str_hex = new BigInteger(1, val).toString(16);
        return Long.parseLong(str_hex, 16);
    }

    public int readUnit32Integer() {
        byte[] val = readByte(4);
        return new BigInteger(1, val).intValue();
    }

    public float readUnit64Float() {
        byte[] val = readByte(8);
        return new BigInteger(1, val).floatValue();
    }

    public long readUnit64Long() {
        byte[] val = readByte(8);
        return new BigInteger(1, val).longValue();
    }

    public double readUnit64Double() {
        byte[] val = readByte(8);
        return new BigInteger(1, val).doubleValue();
    }

    public int[] readUnit16s() {
        int n = this.readUnit16();
        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = this.readUnit16();
        }
        return s;
    }

    public byte[] readByte(int length) {
        byte[] copy = new byte[length];
        System.arraycopy(data, 0, copy, 0, length);
        // 保证每次都只用从头读取
        System.arraycopy(data, length, data, 0, data.length - length);
        return copy;
    }

    public int byte2int(byte[] val) {
        String str_hex = new BigInteger(1, val).toString(16);
        return Integer.parseInt(str_hex, 16);
    }
}
