package com.jvm;


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

        try {
            byte[] classData = cp.readClass(className);
            System.out.println(Arrays.toString(classData));
        } catch (IOException e) {
            System.out.println("Could not find or load main class" + cmd.getMainClass());
            e.printStackTrace();
        }
    }
}
