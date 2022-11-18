package com.thinkingdata.server.controller;

import com.thinkingdata.server.model.MethodEntity;
import com.thinkingdata.server.model.ParamEntity;
import com.thinkingdata.server.service.ParamService;
import com.thinkingdata.tools.aop.WebLog;
import com.thinkingdata.tools.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/11 11:52 AM
 */

@RestController
@RequestMapping("/param")
public class ParamController {
    @Autowired
    private ParamService paramService;

    @RequestMapping(value = "/getParams", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取参数配置接口")
    public ResponseData<Object> getParams(@RequestBody MethodEntity methodEntity) {
        return paramService.getParams(methodEntity);
    }

    @RequestMapping(value = "/addParams", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了新增参数配置接口")
    public ResponseData<Object> addParams(@RequestBody List<ParamEntity> paramList) {
        return paramService.addParams(paramList);
    }
    @RequestMapping(value = "/updateParams", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了更新参数配置接口")
    public ResponseData<Object> updateParams(@RequestBody List<ParamEntity> paramList) {
        return paramService.updateParams(paramList);
    }
}
