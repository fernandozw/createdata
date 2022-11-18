package com.thinkingdata.server.service;

import com.thinkingdata.server.dao.MethodDao;
import com.thinkingdata.server.dao.ScriptDao;
import com.thinkingdata.server.model.MethodEntity;
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
import java.util.stream.Collectors;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/09/29 4:31 PM
 */
@Service
public class MethodService {
    @Autowired
    private Actuator actuator;
    @Autowired
    private ScriptDao scriptDao;
    @Autowired
    private MethodDao methodDao;

    /**
     * 新增方法信息
     *
     * @param methodList 方法列表
     * @return 新增结果
     */
    public ResponseData<Object> addMethods(List<MethodEntity> methodList) {

        ResponseData<Object> responseData;
        Integer addNum = methodDao.batchAddMethod(methodList);
        // 获取脚本id
        ScriptEntity scriptEntity = scriptDao.getScriptById(methodList.get(0).getScriptId());
        // 获取脚本所有方法的参数列表
        Map<String, Map<String, List<String>>> map = actuator.getParamMap(scriptEntity.getContent());
        // 删除空参数列表的方法
        map.entrySet().removeIf(entry -> entry.getValue().get("paramNameList").size() == 0);
        // 当脚本的全部方法没有参数时,更新脚本的是否完成标识
        if (map.size() < 1) {
            scriptDao.updateIsDone(scriptEntity.getId());
        }
        responseData = ResponseDataUtils.buildAddSuccess(addNum);
        return responseData;
    }


    /**
     * 修改方法信息
     *
     * @param methodList 方法列表
     * @return 修改结果
     */
    public ResponseData<Object> updateMethods(List<MethodEntity> methodList) {
        ResponseData<Object> responseData;
        List<MethodEntity> existList = methodList.stream().filter(entity -> entity.getId() != null).collect(Collectors.toList());
        List<MethodEntity> noExistList = methodList.stream().filter(entity -> entity.getId() == null).collect(Collectors.toList());
        // 已存在的方法列表
        if (existList.size() > 0) {
            methodDao.batchUpdateMethod(existList);

        }
        // 不存在的方法列表
        if (noExistList.size() > 0) {
            methodDao.batchAddMethod(noExistList);
            updateScriptMark(noExistList);

        }
        responseData = ResponseDataUtils.buildUpdateSuccess(existList.size() + noExistList.size());
        return responseData;
    }

    /**
     * 如果脚本未配置方法信息时，调用此方法
     *
     * @param scriptEntity 脚本实体
     * @return 方法下拉列表
     */
    public ResponseData<Object> preMethods(ScriptEntity scriptEntity) {

        List<String> methodsList = actuator.getMethods(scriptEntity.getContent());
        List<Map<String, Object>> methodObjList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (String method : methodsList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("methodLabel", method);
            map.put("methodName", method);
            map.put("scriptId", scriptEntity.getId());
            if (actuator.getParamMap(scriptEntity.getContent()).get(method).get("paramNameList").size() > 0) {
                map.put("existParam", 1);
            } else {
                map.put("existParam", 0);
            }
            methodObjList.add(map);
        }
        dataMap.put("action", "add");
        dataMap.put("methodList", methodObjList);
        return ResponseDataUtils.buildSuccess("当前脚本的所有方法未设置标签，请自行设置！", dataMap);
    }

    /**
     * 根据脚本是否配置方法信息，来判断获取的数据源
     *
     * @param scriptEntity 脚本实体
     * @return 方法的下拉列表
     */
    public ResponseData<Object> getMethods(ScriptEntity scriptEntity) {

        List<String> changeList = new ArrayList<>();
        ResponseData<Object> response;
        List<MethodEntity> methodList = methodDao.getMethodByScriptId(scriptEntity.getId());
        List<String> sourceMethodList = actuator.getMethods(scriptEntity.getContent());
        if (methodList.size() > 0) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            if (sourceMethodList.size() == methodList.size()) {
                dataMap.put("action", "edit");
                dataMap.put("methodList", methodList);
                response = ResponseDataUtils.buildSuccess("当前脚本的所有方法已设置标签，可修改设置！", dataMap);
            } else {
                dataMap.put("action", "partEdit");
                for (String method : sourceMethodList) {
                    boolean flag = false;
                    for (MethodEntity methodEntity : methodList) {
                        if (method.equals(methodEntity.getMethodName())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        changeList.add(method);
                        MethodEntity entity = new MethodEntity();
                        entity.setMethodLabel(method);
                        entity.setMethodName(method);
                        entity.setScriptId(scriptEntity.getId());
                        if (actuator.getParamMap(scriptEntity.getContent()).get(method).get("paramNameList").size() > 0) {
                            entity.setExistParam(1);
                        } else {
                            entity.setExistParam(0);
                            methodDao.updateParamSet(1,scriptEntity.getId());
                        }
                        methodList.add(entity);
                    }
                }

                dataMap.put("methodList", methodList);
                response = ResponseDataUtils.buildSuccess("当前脚本的" + changeList.toString() + "方法未设置标签，请设置！", dataMap);
            }

        } else {
            response = preMethods(scriptEntity);
        }
        return response;
    }

    /**更新脚本可执行标识位
     *
     * @param methodList 方法列表
     */
    public void updateScriptMark(List<MethodEntity> methodList) {
        List<MethodEntity> noExistParamList = methodList.stream().filter(entry -> entry.getExistParam() == 0).collect(Collectors.toList());
        List<MethodEntity> existParamList = methodList.stream().filter(entry -> entry.getExistParam() != 0).collect(Collectors.toList());
        // 无参方法列表一定要先执行
        if (noExistParamList.size() > 0) {
            scriptDao.updateIsDone(noExistParamList.get(0).getScriptId());
        }
        // 用于保证状态位处于正确状态
        if (existParamList.size() > 0) {
            scriptDao.updateNoDone(existParamList.get(0).getScriptId());

        }
    }
}
