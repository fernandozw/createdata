package com.thinkingdata.tools.execution;

import org.python.core.PyFunction;
import org.python.core.PyList;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/11/05 3:23 PM
 */
@Component
public class PythonActuator {

    //获取反射模块
    @Value("${python_config.refelect_tool}")
    private String refelectTool;
    // 获取执行器
    @Value("${python_config.exec_method}")
    private String execMethod;

    public Object pythonInvoke(String module_name, String method_name, List<Object> paramList) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(refelectTool);
        PyFunction pyFunction = interpreter.get(execMethod, PyFunction.class);
        Object result = pyFunction.__call__(new PyString(module_name), new PyString(method_name), new PyList(paramList));
        return result;
    }

    public Object check(List<Object> paramList) {

        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(refelectTool);
        PyFunction pyFunction = interpreter.get("check", PyFunction.class);
        Object result = pyFunction.__call__(new PyList(paramList));
        return result;
    }

}
