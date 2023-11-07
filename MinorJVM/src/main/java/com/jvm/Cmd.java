package com.jvm;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.List;

/**
 * @Author qxy
 * @Date 2023/11/3 14:25
 * @Version 1.0
 * 用于解析命令的选项和参数
 */
public class Cmd {

    @Parameter(names = {"-?", "-help"}, description = "print help message", order = 3, help = true)
    boolean helpFlag = false;

    @Parameter(names = "-v", description = "print version and exit", order = 2)
    boolean versionFlag = false;

    @Parameter(names = {"-cp", "-classpath"}, description = "classpath", order = 1)
    String classpath;

    @Parameter(description = "main class and args")
    List<String> mainClassAndArgs;

    @Parameter(names = "-Xjre", description = "path to jre", order = 4)
    String jre;

    boolean ok;

    static Cmd parse(String[] argv) {
        Cmd args = new Cmd();
        JCommander cmd = JCommander.newBuilder().addObject(args).build();
        cmd.parse(argv);
        args.ok = true;
        return args;
    }

    String getMainClass() {
        return (mainClassAndArgs != null && !mainClassAndArgs.isEmpty()) ? mainClassAndArgs.get(0) : null;
    }

    List<String> getAppArgs() {
        return (mainClassAndArgs != null && mainClassAndArgs.size() > 1) ? mainClassAndArgs.subList(1, mainClassAndArgs.size()) : null;
    }

}
