package com.thinkingdata.server.controller;

import com.thinkingdata.server.model.MethodEntity;
import com.thinkingdata.server.model.ScriptEntity;
import com.thinkingdata.server.service.MethodService;
import com.thinkingdata.tools.aop.WebLog;
import com.thinkingdata.tools.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/09/29 5:18 PM
 */
@RestController
@RequestMapping("/method")
public class MethodController {
    @Autowired
    private MethodService methodService;

    @RequestMapping(value = "/updateMethods", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了修改方法配置接口")
    public ResponseData<Object> updateMethods(@RequestBody List<MethodEntity> methodList) {
        return methodService.updateMethods(methodList);
    }


    @RequestMapping(value = "/addMethods", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了新增方法配置接口")
    public ResponseData<Object> addMethods(@RequestBody List<MethodEntity> methodList) {
        return methodService.addMethods(methodList);
    }

    @RequestMapping(value = "/getMethods", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取方法配置接口")
    public ResponseData<Object> getMethod(@RequestBody ScriptEntity scriptEntity) {
        return methodService.getMethods(scriptEntity);
    }
}
