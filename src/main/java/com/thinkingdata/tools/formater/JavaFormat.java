package com.thinkingdata.tools.formater;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/20 2:51 PM
 */

import com.thinkingdata.tools.execution.MemoryJavaFileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JavaFormat {
    // ext 目录路径
    @Value(value = "${javaPath.extDir}")
    private String extJars;
    // 忽略的错误类型list
    private List<String> IGNORE_LIST = new ArrayList(Arrays.asList(new String[]{"NOTE"}));

    /***
     * 格式化java代码
     * @param data 源代码
     * @return 返回格式化后的代码
     */
    public String formJava(String data) {
        String dataTmp = replaceStrToUUid(data, "\"");
        dataTmp = replaceStrToUUid(dataTmp, "'");
        dataTmp = repalceHHF(dataTmp, "\n", "");
        dataTmp = repalceHHF(dataTmp, "{", "{\n");
        dataTmp = repalceHHF(dataTmp, "}", "}\n");
        dataTmp = repalceHHF(dataTmp, "/*", "\n/*\n");
        dataTmp = repalceHHF(dataTmp, "* @", "\n* @");
        dataTmp = repalceHHF(dataTmp, "*/", "\n*/\n");
        dataTmp = repalceHHF(dataTmp, ";", ";\n");
        dataTmp = repalceHHF(dataTmp, "//", "\n//");
        dataTmp = repalceHHFX(dataTmp, "\n");
        for (Map.Entry<String, String> r : mapZY.entrySet()) {
            dataTmp = dataTmp.replace(r.getKey(), r.getValue());
        }
        if (dataTmp == null)
            return data;
        return dataTmp;
    }

    public Map<String, String> mapZY = new HashMap<String, String>();


    /***
     * 循环替换指定字符为随机uuid  并将uuid存入全局map:mapZY
     * @param string 源字符串
     * @param type 类型
     * @return
     */
    public String replaceStrToUUid(String string, String type) {
        Matcher slashMatcher = Pattern.compile(type).matcher(string);
        boolean bool = false;
        StringBuilder sb = new StringBuilder();
        int indexHome = -1; //开始截取下标
        while (slashMatcher.find()) {
            int indexEnd = slashMatcher.start();
            String tmp = string.substring(indexHome + 1, indexEnd); //获取"号前面的数据
            if (indexHome == -1 || bool == false) {
                sb.append(tmp);
                bool = true;
                indexHome = indexEnd;
            } else {
                if (bool) {
                    String tem2 = "";
                    for (int i = indexEnd - 1; i > -1; i--) {
                        char c = string.charAt(i);
                        if (c == '\\') {
                            tem2 += c;
                        } else {
                            break;
                        }
                    }
                    int tem2Len = tem2.length();
                    if (tem2Len > -1) {
                        //结束符前有斜杠转义符 需要判断转义个数奇偶   奇数是转义了  偶数才算是结束符号
                        if (tem2Len % 2 == 1) {
                            //奇数 非结束符
                        } else {
                            //偶数才算是结束符号
                            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                            uuid = type + uuid + type;
                            mapZY.put(uuid, type + tmp + type);
                            sb.append(uuid);
                            bool = false;
                            indexHome = indexEnd;
                        }
                    }
                }
            }
        }
        sb.append(string.substring(indexHome + 1, string.length()));
        return sb.toString();
    }

    /***
     * 处理换行
     * @param data 源代码
     * @param a 标识
     * @param b 标识
     * @return 目标代码
     */

    public String repalceHHF(String data, String a, String b) {
        try {
            data = data.replace(a, "$<<yunwangA>>$<<yunwangB>>");
            String arr[] = data.split("$<<yunwangA>>");
            StringBuilder result = new StringBuilder();
            if (arr != null) {
                for (int i = 0; i < arr.length; i++) {
                    String t = arr[i];
                    result.append(t.trim());
                    if (t.indexOf("//") != -1 && "\n".equals(a)) {
                        result.append("\n");
                    }
                }
            }
            String res = result.toString();
            res = res.replace("$<<yunwangB>>", b);
            res = res.replace("$<<yunwangA>>", "");
            return res;
        } catch (Exception e) {
        }
        return null;
    }

    /***
     * 处理缩进
     * @param data 源代码
     * @param a 标识
     * @return 目标代码
     */

    public String repalceHHFX(String data, String a) {

        try {
            String arr[] = data.split(a);
            StringBuilder result = new StringBuilder();
            if (arr != null) {
                String zbf = "    ";
                Stack<String> stack = new Stack<String>();
                for (int i = 0; i < arr.length; i++) {
                    String tem = arr[i].trim();
                    if (tem.indexOf("{") != -1) {
                        String kg = getStack(stack, false);
                        if (kg == null) {
                            result.append((tem + "\n"));
                            kg = "";
                        } else {
                            kg = kg + zbf;
                            result.append(kg + tem + "\n");
                        }
                        stack.push(kg);
                    } else if (tem.indexOf("}") != -1) {
                        String kg = getStack(stack, true);
                        if (kg == null) {
                            result.append(tem + "\n");
                        } else {
                            result.append(kg + tem + "\n");
                        }
                    } else {
                        String kg = getStack(stack, false);
                        if (kg == null) {
                            result.append(tem + "\n");
                        } else {
                            result.append(kg + zbf + tem + "\n");
                        }
                    }
                }
            }
            String res = result.toString();
            return res;
        } catch (Exception e) {
        }
        return null;
    }

    /***
     * 获得栈数据
     * @param stack 栈信息
     * @param bool 是否弹出
     * @return 栈数据
     */
    public String getStack(Stack<String> stack, boolean bool) {
        String result = null;
        try {
            if (bool) {
                return stack.pop();
            }
            return stack.peek();
        } catch (EmptyStackException e) {
        }
        return result;
    }

    /***
     * 检查class的语法错误
     * @param javaName class名称
     * @param javaSrc 脚本内容
     * @return 返回错误列表
     * @throws IOException
     */
    public List<String> check(String javaName, String javaSrc) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);

        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            Iterable<String> options = Arrays.asList("-encoding", "UTF-8", "-classpath", this.getJarList());
            @SuppressWarnings("static-access")
            JavaFileObject javaFileObject = manager.makeStringSource(javaName, javaSrc);
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            compiler.getTask(null, manager, diagnostics, options, null, Arrays.asList(javaFileObject)).call();

            List<String> messages = new ArrayList<>();
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                if (!IGNORE_LIST.contains(diagnostic.getKind().toString())) {
                    messages.add("错误类型[" + diagnostic.getKind() + "] 行号[" + diagnostic.getLineNumber() + "] 位置[" + diagnostic.getPosition() + "] 详细信息[" + diagnostic.getMessage(Locale.ROOT) + "]");
                }
            }
            return messages;
        }

    }

    /**
     * 获取第三方jar列表
     *
     * @return
     */
    public String getJarList() {
        String classPath = "";
        File dir = new File(extJars);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdir();
        }
        File[] array = dir.listFiles();
        for (File file : array) {
            classPath += extJars + file.getName() + ":";
        }
        return classPath;
    }
}
