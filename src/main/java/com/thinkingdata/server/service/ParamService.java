package com.thinkingdata.server.service;

import com.thinkingdata.server.dao.MethodDao;
import com.thinkingdata.server.dao.ParamDao;
import com.thinkingdata.server.dao.ScriptDao;
import com.thinkingdata.server.model.MethodEntity;
import com.thinkingdata.server.model.ParamEntity;
import com.thinkingdata.server.model.ScriptEntity;
import com.thinkingdata.tools.execution.Actuator;
import com.thinkingdata.tools.response.ResponseData;
import com.thinkingdata.tools.response.ResponseDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/08 3:22 PM
 */
@Service
public class ParamService {
    @Autowired
    private Actuator actuator;
    @Autowired
    private ScriptDao scriptDao;
    @Autowired
    private ParamDao paramDao;
    @Autowired
    private MethodDao methodDao;

    /**
     * 新增参数配置
     *
     * @param paramList 参数配置列表
     * @return 返回结果
     */
    public ResponseData<Object> addParams(List<ParamEntity> paramList) {
        ResponseData<Object> responseData;
        Integer addNum = paramDao.batchAddParam(paramList);
        MethodEntity methodEntity = paramDao.getMethodById(paramList.get(0).getMethodId());
        methodDao.updateParamSet(1,methodEntity.getId());
        ScriptEntity scriptEntity = scriptDao.getScriptById(methodEntity.getScriptId());
        Map<String, Map<String, List<String>>> map = actuator.getParamMap(scriptEntity.getContent());
        // 删除class中的无参方法
        map.entrySet().removeIf(entry -> entry.getValue().get("paramNameList").size() == 0);
        List<ParamEntity> list = paramDao.getParamByScriptId(scriptEntity.getId());
        // 当方法id在参数表中全部出现时,更新脚本表的标识位
        if (map.size() == list.size()) {
            scriptDao.updateIsDone(scriptEntity.getId());
        }
        responseData = ResponseDataUtils.buildAddSuccess(addNum);
        return responseData;
    }

    /**
     * 更新参数配置
     *
     * @param paramList 参数配置列表
     * @return 返回结果
     */
    public ResponseData<Object> updateParams(List<ParamEntity> paramList) {
        ResponseData<Object> responseData;
        MethodEntity methodEntity = paramDao.getMethodById(paramList.get(0).getMethodId());
        methodDao.updateParamSet(1,methodEntity.getId());
        responseData = ResponseDataUtils.buildUpdateSuccess(paramDao.batchUpdateParam(paramList));
        return responseData;
    }

    /**
     * 如果方法未配置参数信息时，调用此方法
     *
     * @param methodEntity 方法实体
     * @return 返回参数配置列表
     */
    public ResponseData<Object> preParam(MethodEntity methodEntity) {
        ResponseData<Object> responseData;
        ScriptEntity scriptEntity = scriptDao.getScriptById(methodEntity.getScriptId());
        String content = scriptEntity.getContent();
        Map<String, List<String>> allParamMap = actuator.getParamMap(content).get(methodEntity.getMethodName());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String msg = "";
        if (allParamMap.get("paramNameList").size() > 0) {
            for (int i = 0; i < allParamMap.get("paramNameList").size(); i++) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("methodId", methodEntity.getId());
                paramMap.put("paramName", allParamMap.get("paramNameList").get(i));
                paramMap.put("paramType", allParamMap.get("paramTypeList").get(i));
                paramMap.put("paramMark", allParamMap.get("paramNameList").get(i));
                list.add(paramMap);
            }
            msg = "当前方法的参数未设置名称、默认值、控件类型等信息，请自行设置！";
        } else {
            msg = "当前方法没有参数，无需设置参数信息！";
        }
        dataMap.put("action", "add");
        dataMap.put("paramList", list);
        dataMap.put("methodName", methodEntity.getMethodName());
        responseData = ResponseDataUtils.buildSuccess(msg, dataMap);
        return responseData;
    }

    /**
     * 根据方法是否配置参数信息，来判断获取的数据源
     *
     * @param methodEntity 方法实体
     * @return 返回参数列表
     */
    public ResponseData<Object> getParams(MethodEntity methodEntity) {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<ParamEntity> paramList = paramDao.getParamsByMethodId(methodEntity.getId());

        if (methodEntity.getExistParam() == 1) {
            if (paramList.size() > 0) {
                dataMap.put("action", "edit");
                dataMap.put("paramList", paramList);
                dataMap.put("methodName", methodEntity.getMethodName());
                responseData = ResponseDataUtils.buildSuccess("当前方法的参数已设置名称、默认值、控件类型等信息，可修改设置！", dataMap);
            } else {
                responseData = preParam(methodEntity);
            }
        } else {
            dataMap.put("action", "None");
            dataMap.put("paramList", paramList);
            dataMap.put("methodName", methodEntity.getMethodName());
            responseData = ResponseDataUtils.buildSuccess("当前方法没有参数，可直接执行！", dataMap);
        }

        return responseData;
    }
}
