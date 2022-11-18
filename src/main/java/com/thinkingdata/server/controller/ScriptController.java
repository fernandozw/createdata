package com.thinkingdata.server.controller;

import com.thinkingdata.server.model.ScriptEntity;
import com.thinkingdata.server.service.ScriptService;
import com.thinkingdata.tools.aop.WebLog;
import com.thinkingdata.tools.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/09/10 3:00 PM
 */

@RestController
@RequestMapping("/script")

public class ScriptController {
    @Autowired
    private ScriptService scriptService;

    @RequestMapping(value = "/addScript", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了新增脚本接口")
    public ResponseData<Object> addScript(@RequestBody ScriptEntity scriptEntity) {
        return scriptService.addScript(scriptEntity);
    }

    @RequestMapping(value = "/updateScript", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了编辑脚本接口")
    public ResponseData<Object> updateScript(@RequestBody ScriptEntity scriptEntity) {
        return scriptService.updateScript(scriptEntity);
    }

    @RequestMapping(value = "/deleteScript", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了删除脚本接口")
    public ResponseData<Object> deleteScript(@RequestBody Map<String, Integer> map) {
        return scriptService.deleteScript(map.get("id"));
    }

    @RequestMapping(value = "/scriptList", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了脚本列表接口")
    public ResponseData<Object> scriptList(@RequestBody Map<String, Object> map) {
        return scriptService.scriptList(map);
    }


    @RequestMapping(value = "/getScriptById", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取脚本接口")
    public ResponseData<Object> getScriptById(@RequestBody Map<String, Integer> map) {
        return scriptService.getScriptById(map.get("id"));
    }

    @RequestMapping(value = "/formatScript", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了格式化脚本接口")
    public ResponseData<Object> formatScript(@RequestBody Map<String, Object> map) throws IOException {
        return scriptService.formatScript(map);
    }

    @RequestMapping(value = "/selectList", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取下拉列表接口")
    public ResponseData<Object> selectList() {
        return scriptService.selectList();
    }
}

