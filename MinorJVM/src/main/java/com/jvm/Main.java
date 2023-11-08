package com.jvm;


import com.jvm.classfile.ClassFile;
import com.jvm.classfile.MemberInfo;
import com.jvm.classpath.ClassPath;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author qxy
 * @Date 2023/11/3 14:32
 * @Version 1.0
 */
public class Main {
    public static void main(String[] args) {
        Cmd cmd = Cmd.parse(args);
        if(!cmd.ok || cmd.helpFlag){
            System.out.println("Usage: <main class> [-options] class [args...]");
            return;
        }
        if(cmd.versionFlag){
            System.out.println("java version \"1.8.0\"");
            return;
        }

        startJVM(cmd);

    }

    private static void startJVM(Cmd cmd){
        ClassPath cp = new ClassPath(cmd.jre, cmd.classpath);
        System.out.printf("classpath:%s class:%s args:%s \n", cmd.classpath, cmd.getMainClass(), cmd.getAppArgs());
        String className = cmd.getMainClass().replace(".", "/");

        ClassFile classFile = loadClass(className, cp);
        assert classFile != null;
        printClassInfo(classFile);
//        try {
//            byte[] classData = cp.readClass(className);
//            System.out.println(Arrays.toString(classData));
//        } catch (IOException e) {
//            System.out.println("Could not find or load main class" + cmd.getMainClass());
//            e.printStackTrace();
//        }
    }

    private static void printClassInfo(ClassFile cf) {
        System.out.println("version: " + cf.majorVersion() + "." + cf.minorVersion());
        System.out.println("constants count：" + cf.constantPool().getSize());
        System.out.format("access flags：0x%x\n", cf.accessFlags());
        System.out.println("this class：" + cf.className());
        System.out.println("super class：" + cf.superClassName());
        System.out.println("interfaces：" + Arrays.toString(cf.interfaceNames()));
        System.out.println("fields count：" + cf.fields().length);

        for(MemberInfo memberInfo: cf.fields()){
            System.out.format("%s \t\t %s\n", memberInfo.name(), memberInfo.descriptor());
        }

        System.out.println("methods count: " + cf.methods().length);
        for (MemberInfo memberInfo : cf.methods()) {
            System.out.format("%s \t\t %s\n", memberInfo.name(), memberInfo.descriptor());
        }

    }

    private static ClassFile loadClass(String className, ClassPath cp) {
        try {
            byte[] classData = cp.readClass(className);
            return new ClassFile(classData);
        } catch (IOException e) {
            System.out.println("Could not find or load main class " + className);
            e.printStackTrace();
            return null;
        }
    }

}
