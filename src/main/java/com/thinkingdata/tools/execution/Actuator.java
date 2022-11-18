package com.thinkingdata.tools.execution;

import com.thinkingdata.tools.response.HandleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/08/20 2:09 PM
 */
@Component
public class Actuator {

    private Logger log = LoggerFactory.getLogger(Actuator.class);
    //java脚本后缀
    private final String JAVA_SUFFIX = ".java";


    /**
     * 通过字符串脚本执行特定的方法
     *
     * @param methodName 指定的方法名称
     * @param paramList  指定的方法参数列表
     * @return
     */
    public Object JavaInvoke(String source, String methodName, List<Object> paramList) {
        Object result;
        try {
            Class<?> clazz = this.clazz(source);
            // 目标类实例化
            Object object = clazz.getDeclaredConstructor().newInstance();
            // 获取目标方法
            Method targetMethod = clazz.getMethod(methodName, this.getParamTypes(source, methodName));
            // 参数列表转换成Object类型数组,固定格式
            Object[] paramArray = paramList.toArray();
            // 指定方法的返回结果
            //result = targetMethod.invoke(object, paramArray);
            result = targetMethod.invoke(object, paramArray);

        } catch (Exception e) {
            result= HandleUtils.handleErrInfo(e).replace("\n","").replace("\tat","");

        }
        return result;
    }

    /**
     * 从字符串脚本中获取当前类的所有方法
     *
     * @return 返回当前类的所有方法
     */
    public List<String> getMethods(String source) {
        // 获取全部方法名称
        List<String> methodsName = new ArrayList<>();
        Arrays.asList(this.clazz(source).getDeclaredMethods()).stream().forEach
                (method -> {
                    if (method.getModifiers() == 1) {
                        methodsName.add(method.getName());
                    }
                });
        return methodsName;
    }

    /**
     * 从字符串脚本中获取当前类所有方法及参数类型列表
     *
     * @return 返回当前类所有公共方法及参数类型列表
     * <p>
     * 修饰符关键字对照表:
     * PUBLIC: 1 （二进制 0000 0001）
     * PRIVATE: 2 （二进制 0000 0010）
     * PROTECTED: 4 （二进制 0000 0100）
     * STATIC: 8 （二进制 0000 1000）
     * FINAL: 16 （二进制 0001 0000）
     * SYNCHRONIZED: 32 （二进制 0010 0000）
     * VOLATILE: 64 （二进制 0100 0000）
     * TRANSIENT: 128 （二进制 1000 0000）
     * NATIVE: 256 （二进制 0001 0000 0000）
     * INTERFACE: 512 （二进制 0010 0000 0000）
     * ABSTRACT: 1024 （二进制 0100 0000 0000）
     * STRICT: 2048 （二进制 1000 0000 0000）
     */
    public Map<String, Map<String, List<String>>> getParamMap(String source) {
        Map<String, Map<String, List<String>>> methodsMap = new HashMap<>();

        for (Method method : this.clazz(source).getDeclaredMethods()) {
            List<String> returnTypeList = new ArrayList<>();
            returnTypeList.add(method.getAnnotatedReturnType().getType().toString());
            // 获取当前方法的所有参数
            Parameter[] params = method.getParameters();
            // 参数类型列表
            List<String> paramTypeList = new ArrayList<>();
            // 参数名称列表
            List<String> paramNameList = new ArrayList<>();
            // 参数名称与参数类型Map
            Map<String, List<String>> paramMap = new HashMap<>();
            for (Parameter parameter : params) {
                paramTypeList.add(parameter.getType().toString());
                paramNameList.add(parameter.getName());
            }
            // 只保留public关键字修饰的方法（public:1,private:2）
            if (method.getModifiers() == 1) {
                paramMap.put("paramTypeList", paramTypeList);
                paramMap.put("paramNameList", paramNameList);
                paramMap.put("returnTypeList", returnTypeList);
                methodsMap.put(method.getName(), paramMap);
            }

        }
        return methodsMap;
    }

    /**
     * 获取指定方法的参数类型列表
     *
     * @param methodName 指定方法名称
     * @return 指定方法的参数类型列表
     * @throws ClassNotFoundException
     */
    private Class[] getParamTypes(String source, String methodName) throws ClassNotFoundException {
        // 指定方法的参数类型列表
        Class[] paramTypes = null;
        Method[] methods = this.clazz(source).getDeclaredMethods();
        // 获取目标方法的参数类型列表
        for (int i = 0; i < methods.length; i++) {
            if (methodName.equals(methods[i].getName())) {
                Class[] params = methods[i].getParameterTypes();
                paramTypes = new Class[params.length];
                for (int j = 0; j < params.length; j++) {
                    paramTypes[j] = Class.forName(params[j].getName());
                }
                break;
            }
        }

        return paramTypes;
    }

    /**
     * 根据脚本文件加载目标类
     *
     * @param source 脚本文件
     * @return 目标类
     */
    private Class<?> clazz(String source) {
        Class<?> clazz = null;
        try {
            String className = this.getClassName(source);
            Map<String, byte[]> bytecode = DynamicLoader.compile(className + JAVA_SUFFIX, source);
            DynamicLoader.MemoryClassLoader classLoader = new DynamicLoader.MemoryClassLoader(bytecode);
            clazz = classLoader.loadClass(className);
        } catch (Exception e) {
            log.error("加载目标类异常:", e);
        }
        return clazz;
    }

    /**
     * 从字符串脚本中获取类名称
     *
     * @param source 字符串脚本
     * @return 返回类名称
     */
    public String getClassName(String source) {
        String result = "";
        String reg = "class(.*)\\{";
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(source);
        if (m.find()) {
            result = m.group(1);
        }
        return result.trim();
    }

}



