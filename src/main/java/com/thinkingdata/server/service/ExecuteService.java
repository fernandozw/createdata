package com.thinkingdata.server.service;

import com.alibaba.fastjson.JSON;
import com.thinkingdata.server.dao.ScriptDao;
import com.thinkingdata.tools.execution.Actuator;
import com.thinkingdata.tools.response.ResponseData;
import com.thinkingdata.tools.response.ResponseDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/13 5:20 PM
 */
@Service
public class ExecuteService {
    @Autowired
    private Actuator actuator;
    @Autowired
    private ScriptDao scriptDao;

    /**
     * 通过脚本字符串反射执行目标方法
     *
     * @param map 请求参数
     * @return 返回目标方法的返回结果
     */
    public ResponseData<Object> executeMethodBySource(Map<String, Object> map) {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<>();
        Object methodResult = actuator.JavaInvoke((String) map.get("script"), (String) map.get("method"), (java.util.List<Object>) map.get("param"));
        dataMap.put("methodResult", methodResult);
        responseData = ResponseDataUtils.buildSuccess("执行成功", dataMap);

        return responseData;
    }

    /**
     * 通过脚本id反射执行目标方法
     *
     * @param map 请求参数
     * @return 返回目标方法的返回结果
     */
    public ResponseData<Object> executeMethod(Map<String, Object> map) {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<>();
        String script = scriptDao.getScriptById((Integer) map.get("scriptId")).getContent();
        Object methodResult = actuator.JavaInvoke(script, (String) map.get("method"), (java.util.List<Object>) map.get("param"));
        if (isJSON(methodResult.toString())) {
            methodResult = JSON.parse(methodResult.toString());
        }
        dataMap.put("methodResult", methodResult);
        responseData = ResponseDataUtils.buildSuccess("执行成功", dataMap);

        return responseData;
    }

    public static boolean isJSON(String str) {
        boolean result;
        try {
            Object obj = JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;

    }
}
