package com.thinkingdata.tools.execution;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/08/20 11:02 AM
 */

import org.apache.logging.log4j.util.PropertiesUtil;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class DynamicLoader {

    /**
     * 通过类名和其代码（Java代码字符串），编译得到字节码，返回类名及其对应类的字节码，封装于Map中，值得注意的是，
     * 平常类中就编译出来的字节码只有一个类，但是考虑到内部类的情况， 会出现很多个类名及其字节码，所以用Map封装方便。
     *
     * @param javaName 类名
     * @param javaSrc  Java源码
     * @return map
     */
    public static Map<String, byte[]> compile(String javaName, String javaSrc) {
        Map<String, byte[]> result;
        // 保存形参名的重要配置,Java8以上支持

        Iterable<String> options = Arrays.asList("-encoding", "UTF-8", "-parameters", "-classpath", DynamicLoader.getJarList());
        // optionSet.add("-parameters");
        // 调用java编译器接口
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler
                .getStandardFileManager(null, null, null);

        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(
                stdManager)) {

            @SuppressWarnings("static-access")
            JavaFileObject javaFileObject = manager.makeStringSource(javaName,
                    javaSrc);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager,
                    null, options, null, Arrays.asList(javaFileObject));
            if (task.call()) {
                result = manager.getClassBytes();
            } else {
                result = null;
            }

        } catch (IOException e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 先根据类名在内存中查找是否已存在该类，若不存在则调用 URLClassLoader的 defineClass方法加载该类
     * URLClassLoader的具体作用就是将class文件加载到jvm虚拟机中去
     */
    public static class MemoryClassLoader extends URLClassLoader {
        Map<String, byte[]> classBytes = new HashMap<>();

        public MemoryClassLoader(Map<String, byte[]> classBytes) {
            super(new URL[0], MemoryClassLoader.class.getClassLoader());
            this.classBytes.putAll(classBytes);
        }

        @Override
        protected Class<?> findClass(String name)
                throws ClassNotFoundException {
            byte[] buf = classBytes.get(name);
            if (buf == null) {
                return super.findClass(name);
            }
           classBytes.remove(name);
            return defineClass(name, buf, 0, buf.length);
        }
    }

    public static String getJarList() {
        String classPath = "";
        File dir = new File(DynamicLoader.getProjectConfig());
        File[] array = dir.listFiles();
        for (File file : array) {
            classPath += DynamicLoader.getProjectConfig() + file.getName() + ":";
        }
        return classPath;
    }

    public static String getProjectConfig() {
        Properties pros = new Properties();
        String value = "";
        try {

            pros.load(new InputStreamReader(PropertiesUtil.class.getResourceAsStream("/application.yml"), "UTF-8"));
            value = pros.getProperty("extDir");
            File file = new File(value);
            if (!file.exists()) {
                file.mkdirs();
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        return value;
    }
}


